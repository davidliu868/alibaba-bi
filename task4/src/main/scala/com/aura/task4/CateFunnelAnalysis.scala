package com.aura.task4

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.expr

object CateFunnelAnalysis {

  def main(args: Array[String]): Unit = {
    var inputFile = "hdfs://hadoop:9000/bi/behavior_log"
    var outputFile = "hdfs://hadoop:9000/bi/behavior_out/funnel/"

    if(args.length > 0) {
      inputFile = args(0)
    }

    val window = 604800L

    val eventIds = Array("pv", "cart", "fav", "buy")

    //2017-04-22
    val start = 1492819200
    //2017-05-14
    val end = 1494720000
    val ss = SparkSession.builder().appName("CateFunnelAnalysis").master("local[2]").getOrCreate()
    val events = ss.read
      .option("sep", ",")
      .option("inferSchema", true)
      .csv(inputFile)
      .toDF("user", "time_stamp","btag", "cate", "brand")

    val funnelEvents = events
      .filter("time_stamp between %s and %s".format(start, end))
      .select("user", "time_stamp", "btag")

    funnelEvents.explain()

    //定义处理函数
    ss.udf.register("funnel_count", new FunnelCountUDAF)
    ss.udf.register("funnel_sum", new FunnelSumUDAF)

    val funnelCounts = funnelEvents.groupBy("user")
      .agg(expr("funnel_count(btag, time_stamp, %d,'%s') as cnt".format(window, eventIds.mkString(","))))

    val result = funnelCounts.agg(expr("funnel_sum(%d, cnt)".format( eventIds.length)))
      .first()

    val resultList = result.getList(0)
    for(i <- 0 until resultList.size()){
      println(resultList.get(i).toString)
    }
  }
}
