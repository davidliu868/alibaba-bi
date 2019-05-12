package com.aura.task4

import org.apache.spark.{SparkConf, SparkContext}
import com.aura.task4.util.DateTimeUtil.{getCurrentTime, long2string, string2long}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hdfs.HdfsConfiguration


/**
  * 留存分析
  * 2017-04-22开始有数据，22天数据
  * 1. 目标： 分析每个商品类目浏览行为22天的留存率
  * 2. 过滤数据，过滤出行为数据中为浏览的
  * 3. 根据日期-类目-用户ID    key:2018-03-04,1234  value:12,14,5,6,7
  * 4. 存hdfs 每天的总人数
  * 5. key: 类目ID   日期
  */

object RetentionAnalysis {

  def main(args: Array[String]): Unit = {

    var inputFile = "hdfs://hadoop:9000/bi/behavior_log_test"
    var outputFile = "hdfs://hadoop:9000/bi/behavior_out/retenrion/"

    val conf = new SparkConf().setAppName("CateRanking")

    var inputDate = "2017-04-22"

    if(args.length > 0) {
      inputFile = args(0)
    }else{
      conf.setMaster("local[2]")
    }

    val sc = new SparkContext(conf)
    //设置日志级别
    sc.setLogLevel("error")

    val behaviorRDD = sc.textFile(inputFile)

    val inputDateLong = string2long(inputDate, "yyyy-MM-dd")

    val endDate = string2long("2017-05-14", "yyyy-MM-dd")

    //1. 过滤数据，行为是浏览的，日期是给定日期之后的, 5月14之前的
    val results = behaviorRDD
      .filter(_.contains("pv"))
      .filter(s => {
        val splits = s.split(",")
        splits(1).toInt > (inputDateLong/1000) &&  splits(1).toInt< (endDate/1000)
      })
      .map(s => {
        val splits = s.split(",")
        (splits(3), (long2string(splits(1).toLong*1000), splits(0)))
      })
      .groupByKey()
      .foreachPartition( partition=> {
        while (partition.hasNext){
          val cate = partition.next()
          val cateId = cate._1// 类目ID

          val dateUsers = cate._2

          var map:Map[String, String] = Map()
          dateUsers.foreach(d => {
            map += (d._1 -> d._2)
          })

          map.keys.foreach( k => {
            // hbase key 日期加类目，
          })

        }
      })


    //获取当前时间
    val time = getCurrentTime("yyy-MM-dd")
    outputFile += time;
    println("outputFile is: "+outputFile)
    val outpath = new Path(outputFile)
    val fs = outpath.getFileSystem(new HdfsConfiguration())
    if (fs.exists(outpath)){
      fs.delete(outpath, true)
    }
    results.saveAsTextFile(outputFile)


  }

}
