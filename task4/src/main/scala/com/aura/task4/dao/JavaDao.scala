package com.aura.task4.dao

import java.sql.{Connection}

object JavaDao {

  //性别分布
  val GENDER_DISTRIBUTED_INSERT = "INSERT INTO `bi`.`gender_distributed`(`cate_id`, `gender`, `gender_nums`) VALUES (?,?,?);"
  //年龄分布
  val AGE_DISTRIBUTED_INSERT = "INSERT INTO `bi`.`age_distributed`(`cate_id`, `age`, `age_nums`) VALUES (?,?,?);"
  //消费档次
  val PVALUE_DISTRIBUTED_INSERT = "INSERT INTO `bi`.`pvalue_distributed`(`cate_id`, `pvalue`, `pvalue_nums`) VALUES (?,?,?);"

  private def execute(conn: Connection, sql: String, params: Any*): Unit = {

    val pstmt = conn.prepareStatement(sql)
    for (index <- 0 until params.length){
      pstmt.setObject(index+1, params(index))
    }
    pstmt.execute()

  }

  def saveGenderDistributed(conn: Connection, cateId:String, gender:Int, nums:Int): Unit = {

    execute(conn, GENDER_DISTRIBUTED_INSERT, cateId, gender, nums)
  }

  def saveAgeDistributed(conn: Connection, cateId:String, gender:Int, nums:Int): Unit = {

    execute(conn, AGE_DISTRIBUTED_INSERT, cateId, gender, nums)
  }

  def savePvalueDistributed(conn: Connection, cateId:String, gender:Int, nums:Int): Unit = {

    execute(conn, PVALUE_DISTRIBUTED_INSERT, cateId, gender, nums)
  }


}
