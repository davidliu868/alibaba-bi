package com.aura.task4.util

import java.text.SimpleDateFormat
import java.util.Date

object DateTimeUtil {

  def long2string(time: Long): String = {
    var sdf = new SimpleDateFormat("yyyy-MM-dd")

    return sdf.format(new Date(time))
  }

  def getCurrentTime(format: String):String ={
    var sdf = new SimpleDateFormat(format)
    return sdf.format(new Date())
  }

  /**
    * 根据格式把日期转换成毫秒值
    * @param date
    * @param format
    */
  def string2long(date:String, format:String):Long = {
    val sdf = new SimpleDateFormat(format);
    return sdf.parse(date).getTime
  }

}
