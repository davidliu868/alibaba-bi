package db

import java.sql.{Connection, DriverManager}
import config.Config

object JDBCHelper {
  
  def getConnection(): Connection = {
    Class.forName(Config.driver_class)
    DriverManager.getConnection(Config.db_url, Config.username, Config.password)
	}
  
  def main(args: Array[String]): Unit = {
    JDBCHelper.getConnection()
  }
}