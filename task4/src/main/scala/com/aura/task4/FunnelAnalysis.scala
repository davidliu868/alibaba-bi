package com.aura.task4

import com.aura.task4.db.DBHelper
import com.aura.task4.util.DateTimeUtil.{getCurrentTime, long2string}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hdfs.HdfsConfiguration
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 漏斗分析  2017-04-22 ~ 2017-05-13  窗口期7天
  */
object FunnelAnalysis {

  def main(args: Array[String]): Unit = {
    var inputFile = "hdfs://hadoop:9000/bi/behavior_log_test"
    var outputFile = "hdfs://hadoop:9000/bi/behavior_out/funnel/"

    val conf = new SparkConf().setAppName("FunnelAnalysis")

    if(args.length > 0) {
      inputFile = args(0)
    }else{
      conf.setMaster("local[2]")
    }

    val sc = new SparkContext(conf)
    //设置日志级别
    sc.setLogLevel("error")

    val behaviorRdd = sc.textFile(inputFile)

    // 日期， 类目ID， pv,cart,favour,buy
    //1. key 日期+类目ID
    val results = behaviorRdd.filter(f => {
      val splits = f.split(",")
      (f.trim!="") && (!f.contains("user")) && (splits(1).toInt > 1451577600) && (splits(1).toInt < System.currentTimeMillis()/1000)
    })
      .map(f => {
      val splits =  f.split(",")
      (long2string(splits(1).toLong*1000)+","+splits(3)+","+splits(2), 1)
    })
      .reduceByKey(_ + _)
      .map(s => {
        val splits = s._1.split(",")
        (splits(0)+","+splits(1), (splits(2)+","+s._2))
      })
      .reduceByKey("%s*%s".format(_,_))

    //将数据按partition写入mysql
    results.foreachPartition(partition => {
      val conn = DBHelper.getConnection()
      while (partition.hasNext){
        val p = partition.next()
        val dateAndCate = p._1.split(",")
        val catesFunnel = p._2.split("\\*")

        var infoMap:Map[String, Int] = Map()
        catesFunnel.foreach(info => {
          val splits = info.split(",")
          infoMap += (splits(0) -> splits(1).toInt)
        })

        val sql = new StringBuilder("INSERT INTO funnel_analysis ( date, cate_id, pv, cart, favour, buy ) VALUES ")

        sql.append("('")
        sql.append(dateAndCate(0))
        sql.append("',")
        sql.append(dateAndCate(1))
        sql.append(",")
        if (infoMap.contains("pv")){
          sql.append(infoMap("pv"))
        }else{
          sql.append(0)
        }
        sql.append(",")
        if (infoMap.contains("cart")){
          sql.append(infoMap("cart"))
        }else{
          sql.append(0)
        }
        sql.append(",")
        if (infoMap.contains("fav")){
          sql.append(infoMap("fav"))
        }else{
          sql.append(0)
        }
        sql.append(",")
        if (infoMap.contains("buy")){
          sql.append(infoMap("buy"))
        }else{
          sql.append(0)
        }
        sql.append(")")

        println("sql is:"+sql.toString())
        val statement = conn.createStatement()
        statement.execute(sql.toString())
        statement.close()
      }
      conn.close()
    })

    //获取当前时间
    val time = getCurrentTime("yyy-MM-ddHHmmss")
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
