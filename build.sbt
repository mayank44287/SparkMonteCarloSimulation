name := "mayank_raj_hw3"

version := "0.1"

scalaVersion := "2.11.12"

//libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
//  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
//   "com.typesafe" % "config" % "1.3.2", "junit" % "junit" % "4.12", "org.apache.hadoop" % "hadoop-client" % "2.4.0", "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
//  "org.scalactic" %% "scalactic" % "3.0.5", "org.scalatest" %% "scalatest" % "3.0.5" % "test", "sax" % "sax" % "2.0.1","org.apache.spark" %% "spark-core" % "2.4.4", "org.apache.spark" %% "spark-sql" % "2.4.4")


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

libraryDependencies ++= Seq("com.typesafe" % "config" % "1.3.4",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.1",
  "org.apache.hadoop" % "hadoop-common" % "2.7.1",
  "org.apache.spark" %% "spark-core" % "2.4.4",
  "org.apache.spark" %% "spark-sql" % "2.4.4",
  "org.scala-lang.modules" %% "scala-xml" % "1.2.0",
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)