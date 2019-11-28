import com.typesafe.config.ConfigFactory
import org.apache.commons.configuration.ConfigurationFactory
import org.apache.hadoop.conf.Configuration
import org.scalatest.FlatSpec


class TestConfig extends FlatSpec {

  val config = new Configuration
  val conf = ConfigFactory.load()

  "Loading the stocks" should "return list if stocks" in {
    val ans = conf.getStringList("ALL_STOCKS")
    assert(ans.size() == 10)

  }

  "weights" should "load the weight list" in {
    val ans = conf.getStringList("WEIGHTS")
    assert(ans.size() == 1)
  }

  "Investment" should "load investment values" in {
    val ans = conf.getStringList("INV")
    assert(ans.size() == 1)
  }

  "Config loader" should "load the number list" in {
    val ans = conf.getStringList("NUMBER")
    assert(ans.size() == 3)
  }

  "Config loader" should "load the number of universe list" in {
    val ans = conf.getStringList("UNIVERSE")
    assert(ans.size() == 4)
  }


  "Config loader" should "load the number of trials" in {
    val ans = conf.getStringList("TRIALS")
    assert(ans.size() == 2)
  }
}


