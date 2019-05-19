package com.aura.task4

import com.aura.task4.dao.JavaDao
import com.aura.task4.db.DBHelper
import org.apache.spark.sql.{RowFactory, SparkSession}
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.{SparkConf}


/**
  * 喜欢该商品的用户年龄、性别、消费档次分布
  * 1. 过滤是喜欢的数据
  * 2. map (用户ID-cateid)
  * 3. 去重
  * 4. join表中的数据
  * 5. 写SQL查count
  */
object LikeDistributed {

  val schema = new StructType()
    .add("userid", StringType, nullable = false)
    .add("cateid", StringType, nullable=false)
    .add("gender", StringType, nullable=false)
    .add("age", StringType, nullable=false)
    .add("pvalue", StringType, nullable=false)

  def main(args: Array[String]): Unit = {
    //行为日志
    var behaviorInputFile = "hdfs://hadoop:9000/bi/behavior_log"
    //用户信息
    val userInputFile = "hdfs://hadoop:9000/bi/user_profile"

    val conf = new SparkConf().setAppName("LikeDistributed")

    if(args.length > 0) {
      behaviorInputFile = args(0)
    }else{
      conf.setMaster("local[2]")
    }

    val ss = SparkSession.builder().config(conf).getOrCreate()
    val sc = ss.sparkContext

    val behaviorRDD = sc.textFile(behaviorInputFile)
    val userRDD = sc.textFile(userInputFile)

    //过滤完之后的行为数据，key是用户id，value是类目ID
    val behaviorPairRdd = behaviorRDD
      .filter(_.contains("fav"))
      .map(s => {
        val splits = s.split(",")
        (splits(0),splits(3))
      })
      .distinct()

    //用户信息，可以是用户ID，value是 "男女，年龄层次，消费档次"
    val userPairRdd = userRDD.map(s => {
      val splits = s.split(",")
      (splits(0), splits(3)+","+splits(4)+","+splits(5))
    })
    val joinRdd = behaviorPairRdd.join(userPairRdd).map(s => {
      val infos = s._2._2.split(",")
      RowFactory.create(s._1, s._2._1, infos(0), infos(1), infos(2)) // userid ,类目ID,男女，年龄层次，消费档次
    })
    val behaviorDateset = ss.createDataFrame(joinRdd, schema)
    //创建本地视图
    behaviorDateset.createOrReplaceTempView("behaviors")
    //性别统计
    val genderCounts = ss.sql("select cateid, gender, count(1) as gendernum from behaviors group by cateid, gender ")
    genderCounts.foreachPartition(rows => {
      val conn = DBHelper.getConnection()
      rows.foreach(row => {
        JavaDao.saveGenderDistributed(conn,row.getString(0), row.getString(1).toInt, row.getLong(2).toInt)
      })
      conn.close()
    })
    //年龄统计
    val ageCounts = ss.sql("select cateid, age, count(1) as agenum from behaviors group by cateid, age ")
    ageCounts.foreachPartition(rows => {
      val conn = DBHelper.getConnection()
      rows.foreach(row => {
        JavaDao.saveAgeDistributed(conn,row.getString(0), row.getString(1).toInt, row.getLong(2).toInt)
      })
      conn.close()
    })
    //消费档次统计
    val pvalueCounts = ss.sql("select cateid, pvalue, count(1) as pvaluenum from behaviors group by cateid, pvalue ")
    pvalueCounts.foreachPartition(rows => {
      val conn = DBHelper.getConnection()
      rows.foreach(row => {
        JavaDao.savePvalueDistributed(conn,row.getString(0), row.getString(1).toInt, row.getLong(2).toInt)
      })
      conn.close()
    })

  }

}
