package mayankraj.hw3

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.{SparkConf, SparkContext, sql}
import org.apache.spark.sql.{DataFrame, Row, RowFactory, SparkSession, functions}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.DataTypes
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import collection.JavaConverters._
import java.io.File

import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.Random
import org.apache.spark.sql.Row
import org.apache.commons.math3.distribution.NormalDistribution

import Numeric.Implicits._

object mainDriver extends LazyLogging {

  def main(args: Array[String]): Unit = {
    logger.info(s"main(args: ${args.mkString})")

    val sparkConf = new SparkConf().setAppName("MonteCarlo Simulation").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val conf = ConfigFactory.load()
    val companySymbols = conf.getStringList("ALL_STOCKS").asScala
    val randomList = Random.shuffle(companySymbols)
    val instrumentList = new ArrayBuffer[String]
    var i = 0
    while(i <4){
      instrumentList += randomList(i)
      i += 1
    }
    instrumentList.foreach(x=>print(x + "\n"))


    //list of stocks being comsidered
    val stockLists = conf.getStringList("STOCKS").asScala

    val relativePath = "src/main/resources/"
    val vmRelativePath =  args(0).toString
    val weights : List[Double] = conf.getStringList("WEIGHTS").asScala.map(i=>i.toDouble).toList
    val numberOfTrials = conf.getString("NUMBER_OF_TRIALS")


    val spark = SparkSession.builder
      .master("local")
      .appName("CSV Reader")
      .getOrCreate()
    logger.info("Reading historical stock data")
    val dfList : List[DataFrame] = stockLists.map{ x =>
        spark.read.format("csv").option("header","true")
          .option("mode","DROPMALFORMED").load(vmRelativePath + x + ".csv")
          .withColumn("Symbol",functions.lit(x))
          .drop("High","Low","Adj Close","Volume","Open")
          .withColumnRenamed("Close",x)

    }.toList

   val pureList = dfList.map{x=>
     x.drop("Symbol")
   }


    var in = 0
    val schema = dfList(0).schema

    val mainDataFrame = pureList match {
      case head :: tail => tail.foldLeft(head){(df1,df2) => df1.join(df2,"Date")}
      case Nil => spark.emptyDataFrame
    }

    mainDataFrame.show(100)


    val stockListTests : List[String] = List("MSFT","AAPL")

    //total number of daays in the considered dataframe
    val days = 2

    //the simulation

    val numberOfUniverses : Int = conf.getString("NumberOfUniverse").toInt

    val timePeriod = conf.getString("TimePeriod").toInt

    val startDate = 1

    var initialInvestment = conf.getString("INVESTMENT").toInt


    (startDate to days).foreach{x =>
      var passedDays = 0
      while((passedDays < days) && (initialInvestment > 0)){
        val randNumber = scala.util.Random.nextInt(stockLists.length)
        val chosenStock: String = stockLists(randNumber)

        //taking outprices for the particular stock
        val priceList = mainDataFrame.select(chosenStock).rdd.map(x => java.lang.Double.parseDouble(x(0).toString)).collect().toList

        //run the simulation and get average results from simulating the change in prices
        val resultSim: Array[Double]= (sc.parallelize(1 to numberOfUniverses)).map(i => runSimulation(priceList, startDate, startDate+timePeriod )).collect()
        val maximumMean = resultSim.max
        val maxThreshold: Double = maximumMean/priceList(passedDays)

        if(maxThreshold*100 > 110) {
          //if threshold condition is met buy more of that stock
          initialInvestment = initialInvestment - (initialInvestment/4)
        }
        else if ( maxThreshold * 100 < 50 )
        {
          //sell
          initialInvestment = initialInvestment + initialInvestment/10
        }
        val investmentList = new ListBuffer[Int]()
        investmentList += initialInvestment
        passedDays = passedDays + 1

      }
    }
    logger.info("This completes the simulation")

  }

  def runSimulation(priceList: List[Double], timePeriod: Int, start: Int): Double = {
    val smpl: List[Double]= samplePrice(priceList)
    val slicedSmpl = smpl.slice(start, start+ timePeriod)

    //get mean of list and return, for decision process
    val average  = mean(slicedSmpl)
    average
  }

  def samplePrice(priceList: List[Double]): List[Double] = {
    //extrapolate the prices and randomly get the next price according to formula
    //standard deviation, mean for the List to generate new price
    //get new prices from the distribution
    val standardDeviation = stdDev(priceList)
    val variance1 = variance(priceList)
    val mean1 = mean(priceList)

    val distribution = new NormalDistribution(mean1, standardDeviation)
    priceList.map(x => distribution.sample())
    priceList
  }

  def mean[T: Numeric](m: Iterable[T]): Double = m.sum.toDouble / m.size
  def variance[T: Numeric](v: Iterable[T]): Double = {
    val avg = mean(v)
    v.map(_.toDouble).map(a => math.pow(a - avg, 2)).sum / v.size
  }

  def stdDev[T: Numeric](s: Iterable[T]): Double = {math.sqrt(variance(s))}


}


