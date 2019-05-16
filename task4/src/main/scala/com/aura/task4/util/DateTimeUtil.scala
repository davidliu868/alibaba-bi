package com.aura.task4.util

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

object DateTimeUtil {

  def long2string(time: Long): String = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd")

    sdf.format(new Date(time))
  }

  def getCurrentTime(format: String):String ={
    val sdf = new SimpleDateFormat(format)
    sdf.format(new Date())
  }

  /**
    * 根据格式把日期转换成毫秒值
    * @param date
    * @param format
    */
  def string2long(date:String, format:String):Long = {
    val sdf = new SimpleDateFormat(format)
    sdf.parse(date).getTime
  }

  def stringAddDay(date:String, format:String, day:Int):String = {
    val sdf = new SimpleDateFormat(format)
    val calendar = Calendar.getInstance()
    calendar.setTime( sdf.parse(date))
    calendar.add(Calendar.DATE, day)
    sdf.format(calendar.getTime)
  }

}
