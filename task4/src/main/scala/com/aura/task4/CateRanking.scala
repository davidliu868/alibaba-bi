package com.aura.task4

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hdfs.HdfsConfiguration
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}
import com.aura.task4.util.ConnectionPool
import com.aura.task4.util.DateTimeUtil.{long2string, getCurrentTime}

object CateRanking {

  def main(args: Array[String]): Unit = {

    var inputFile = "hdfs://hadoop:9000/bi/behavior_log"
    var outputFile = "hdfs://hadoop:9000/bi/behavior_out/"

    val conf = new SparkConf().setAppName("CateRanking")

    if(args.length > 0) {
      inputFile = args(0)
    }else{
      conf.setMaster("local[2]")

    }

    var sc = new SparkContext(conf)
    var behaviorRdd = sc.textFile(inputFile)
    val results = behaviorRdd
      //过滤行为是浏览的数据
      .filter(_.contains("pv"))
      //对数据拆分，key为日期和类目，value为1
      .map(s=> {
        val splits = s.split(",")
        (long2string(splits(1).toLong*1000)+","+splits(3), 1)
      })
      //根据key进行统计
      .reduceByKey(_ + _)
      //数据转换成key为日期，value为类目和访问量的元组
      .map(s => {
        val splits = s._1.split(",")
        (splits(0), (splits(1), s._2))
      })
      //根据日期进行分组
      .groupByKey()
      //处理每个组里的数据
      .map(group => {
        val date = group._1
        val cateInfos = group._2

        var lists = List[(String, Int)]()
        for (i <- cateInfos){
          val cate = i._1 //类目
          val nums = i._2 //浏览次数
          lists = ((cate, nums)) :: lists
        }
        //取每个组里的前10
        lists = lists.sortBy(_._2).reverse.take(10)
        new Tuple2[String, Iterable[(String, Int)]](date, lists)
      })
      .partitionBy(new HashPartitioner(20))
      .sortByKey()


    //将数据按partition写入mysql
    results.foreachPartition(partition => {
      val conn = new ConnectionPool().getConnection()
      while (partition.hasNext){
        var p = partition.next()
        val date = p._1
        val cates = p._2
        var sql = new StringBuilder("INSERT INTO cate_ranking ( date, cate_id, nums ) VALUES ")
        for (elem <- cates) {
          sql.append("('")
          sql.append(date)
          sql.append("',")
          sql.append(elem._1)
          sql.append(",")
          sql.append(elem._2)
          sql.append(")")
          sql.append(",")
        }
        val sqlString = sql.substring(0, sql.length-1)

        println("sql is:"+sqlString)
        val statement = conn.createStatement()
        statement.execute(sqlString)
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
