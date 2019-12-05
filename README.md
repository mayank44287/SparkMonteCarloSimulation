
## Overview
The aim of this project  is to create a Spark program for parallel processing of the predictive engine for stock portfolio losses using Monte Carlo simulation in Spark. You can use financial services from google and yahoo to obtain the histotical data for different stock profiles.

The functionality of your Monte Carlo simulator includes the following. The input to your simulator is a randomly selected list of stocks, your total fund amount in USD, and a time period for which prices are recorded for these stocks at Google Finance or some other financial engine that contains this information. You will allocate your fund using some distribution criteria to purchase stocks at the beginning of the time period. As the simulation starts, your simulator records losses and gains as the simulation time goes by. The simulator makes decisions at some time points to sell stocks (e.g., to stop losses or because the gain  plateaued). If more fund money becomes available because of the gain from a stock sell or because of the new contributions to the fund, the simulator will select new stock ticker randomly and invest the money in it. As the simulation comes to the end, you will record the gains and losses. You can repeat the simulation as many times as you want.

## Steps to run on VM:

- Setup sbt on your system
- Add the assembly plugin to it(you can find all the steps if you google sbt assembly)
- Run `sbt clean assembly` , which will run the test cases and  generate the fat JAR
- Copy the jar on the virtual machine which should have Java 1.8 .
    - to send the jar from your host to VM, you can use 
        - ``scp mayank_raj_hw2-assembly-0.1.jar username@IPAddressofVM:DestinationDirectory``
        - for Cloudera QuickStart VM , the default username and password is cloudera
- Copy all the historical data .csv files onto the file system on VM

- Then run the following command:
    - `spark-submit --class mayankraj.hw3.mainDriver --master local  --num-executors 3 --driver-memory 512m --executor-memory 512m --executor-cores 1 --queue default mayank_raj_hw3-assembly-0.1.jar /path/to input files directory/`
    - where stockData folder has all the csv files for stock portfolios
    - I have added all these files in resources , you can simply copy these files onto the VM filesystem
    

## Steps to run on EMR and Dataproc:
- Please follow the link https://youtu.be/gcqpXtKrGLc for youtube video for this.

##Analysis

The simulation generates a pattern based on which you can determine the nature of movement of stocks in the market. However the analysis done is not a certainity that would happen, but it rather draws some 
important results regarding the nature of investment path one might follow. I have choses 10 very well known companies from the stock exchange
and choose four of them randomly. Then an initial amount is used to invest in these stocks.
Then a random selection of dates is made and the simulation starts for that time period. During this period, we make informed decision
based on the performance of current stocks to either recollect our initial investment or invest more into the market. This
procedure is repeated for several simulations and then a judgement can be made about the particular set of stocks and their performance.  


