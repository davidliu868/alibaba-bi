package com.aura.task4.util

import java.sql.{Connection, DriverManager}

class ConnectionPool extends java.io.Serializable{

  //获取连接
  def getConnection(): Connection ={
    //同步代码块
    AnyRef.synchronized({
      DriverManager.getConnection(
        "jdbc:mysql://10.0.0.191:3306/bi",
        "root",
        "root"
      )
    })
  }
}
