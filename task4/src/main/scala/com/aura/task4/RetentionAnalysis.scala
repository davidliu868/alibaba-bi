package com.aura.task4

import org.apache.spark.{SparkConf, SparkContext}
import com.aura.task4.util.DateTimeUtil.{long2string, string2long, stringAddDay}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}

/**
  * 留存分析
  * 2017-04-22开始有数据，22天数据
  * 1. 目标： 分析每个商品类目浏览行为22天的留存率
  * 2. 过滤数据，过滤出行为数据中为浏览的
  * 3. 根据日期-类目-用户ID    key:2018-03-04,1234  value:12,14,5,6,7
  * 4. 存hdfs 每天的总人数,d0为第一天  
  * 5. rowKey: 日期-类目ID
  */

object RetentionAnalysis {

  def main(args: Array[String]): Unit = {

    var inputFile = "hdfs://hadoop:9000/bi/behavior_log"

    val TABLE_NAME = "retention_analysis"

    val conf = new SparkConf().setAppName("RetentionAnalysis")

    if(args.length > 0) {
      inputFile = args(0)
    }else{
      conf.setMaster("local[2]")
    }

    val sc = new SparkContext(conf)
    //设置日志级别
    sc.setLogLevel("error")

    val behaviorRDD = sc.textFile(inputFile)

    val startDateLong = string2long("2017-04-22", "yyyy-MM-dd")

    val endDateLong = string2long("2017-05-14", "yyyy-MM-dd")

    //1. 过滤数据，行为是浏览的，日期是给定日期之后的, 5月14之前的
    behaviorRDD
      .filter(_.contains("pv"))
      .filter(s => {
        val splits = s.split(",")
        splits(1).toInt > (startDateLong/1000) &&  splits(1).toInt< (endDateLong/1000)
      })
      .map(s => {
        val splits = s.split(",")
        (splits(3), (long2string(splits(1).toLong*1000), splits(0)))
      })
      .groupByKey()
      .foreachPartition( partition=> {
        // 与hbase建立连接

        val configuration = HBaseConfiguration.create()
        configuration.set("hbase.zookeeper.quorum", "hadoop:2181")
        configuration.set("zookeeper.znode.parent", "/hbase")

        val connection = ConnectionFactory.createConnection(configuration)
        val table = connection.getTable(TableName.valueOf(TABLE_NAME))


        while (partition.hasNext){
          val cate = partition.next()

          val cateId = cate._1// 类目ID

          //按日期分组
          val dateUsers = cate._2
          val dateUsersMap = dateUsers.groupBy(_._1)

          dateUsersMap.keys.foreach(m => {

            //rowkey 日期-类目ID
            val rowKey = "%s-%06d".format(m, cateId.toInt)

            val put = new Put(rowKey.getBytes())

            //该日期m下，用户的数量
            val users = dateUsersMap(m).map(_._2).toList.distinct
            println("日期："+m+" 用户IDS:"+users.toString())

            put.addColumn("f1".getBytes(), "d0".getBytes(), users.size.toString.getBytes)

            //进行22天循环

            for (index <- 1 to 22){
              val indexDate = stringAddDay(m, "yyyy-MM-dd", index)
              if (dateUsersMap.contains(indexDate)){
                val indexUsers = dateUsersMap(indexDate).map(_._2).toList.distinct
                //第n天取交集
                val tmpIntersect = users.intersect(indexUsers)
                val column = "d%d".format(index)
                //index 天的留存
                put.addColumn("f1".getBytes(), column.getBytes(), tmpIntersect.size.toString.getBytes)
              }
            }
            table.put(put)
          })
        }

        if (table != null){
          table.close()
        }
        connection.close()
      })
  }

}
