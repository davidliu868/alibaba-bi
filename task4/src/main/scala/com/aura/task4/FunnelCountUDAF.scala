package com.aura.task4

import java.util.Comparator

import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DataTypes, StructType}

import scala.util.control.Breaks._

class FunnelCountUDAF extends UserDefinedAggregateFunction{

  private var event_pos:Map[String, Map[String, Int]] = Map()

  override def inputSchema: StructType =
    new StructType()
    .add("event_type", DataTypes.StringType)
    .add("time_stamp", DataTypes.LongType)
    .add("window", DataTypes.LongType)
    .add("events", DataTypes.StringType)


  override def bufferSchema: StructType =
    new StructType()
      .add("found_first", DataTypes.BooleanType)
      .add("event_count", DataTypes.IntegerType)
      .add("window", DataTypes.LongType)
      .add("event_ts_list", DataTypes.createArrayType(DataTypes.LongType))

  override def dataType: DataType = DataTypes.IntegerType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer.update(0, false) // contain first event
    buffer.update(1, 0) // event count
    buffer.update(2, 0L) // window
    buffer.update(3, new java.util.ArrayList[Long]()) // event timestamp

  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    val eventId = input.getString(0)
    val timestamp = input.getLong(1)
    val window = input.getLong(2)
    val events = input.getString(3)
    if (!event_pos.contains(events)) {
      initEvents(events)
    }
    val idx = event_pos(events)(eventId)
    if (idx == 0) {
      buffer.update(0, true)
    }
    buffer.update(1, event_pos.get(events).size)
    buffer.update(2, window)
    val result = new java.util.ArrayList[Long]()
    result.addAll(buffer.getList(3))
    result.add(timestamp * 10 + idx)
    buffer.update(3, result)
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1.update(0, buffer1.getBoolean(0) || buffer2.getBoolean(0))
    buffer1.update(1, buffer2.getInt(1)) //事件数量，不变
    buffer1.update(2, buffer2.getLong(2)) //窗口期，不变

    val l1 = buffer1.getList(3)
    val l2 = buffer2.getList(3)
    if (l1.isEmpty) { //如果第一个buffer为空，则直接更新第二个
      buffer1.update(3, l2)
    }
    else { //否则，将两个结合合并
      val result = new java.util.ArrayList[Long]()
      result.addAll(l1)
      result.addAll(l2)
      buffer1.update(3, result)
    }
  }

  override def evaluate(buffer: Row): Any = {
    val foundFirst = buffer.getBoolean(0) //是不是从0开始
    val eventCount = buffer.getInt(1)//事件数量
    if (!foundFirst){
      0
    }else{
      val window = buffer.getLong(2) //时间窗口
      val eventTsList = new java.util.ArrayList[Long]()
      eventTsList.addAll(buffer.getList(3))
      //排序
      eventTsList.sort(new Comparator[Long] {
        override def compare(o1: Long, o2: Long): Int = {
          (o1 - o2).toInt
        }
      })

      val temp = new java.util.ArrayList[Array[Long]]()
      var max_event = 0

      breakable(
        for(i <- 0 until eventTsList.size()){
          val eventTs = eventTsList.get(i)

          val ts = eventTs / 10    //时间
          val event = eventTs % 10 //事件

          if (event == 0){
            temp.add(Array(ts, event))
          }else{
            breakable(
              for(i <- (0 until temp.size()).reverse){
                val flag = temp.get(i)
                if ((ts - flag(0))>=window){
                  break()
                }else if(event == (flag(1)+1)){
                  flag(1) = event
                  if (max_event < event){
                    max_event = event.toInt
                  }
                  break()
                }
              }
            )
            if (max_event+1 == eventCount){
              break()
            }
          }
        }
      )
      max_event+1
    }
  }

  private def initEvents(events: String):Unit={
    var pos:Map[String, Int] = Map()
    val es = events.split(",")
    for (i <- 0 until es.length ){
      pos = pos.+((es(i), i))
    }
    event_pos = event_pos.+(kv = (events, pos))
  }
}
