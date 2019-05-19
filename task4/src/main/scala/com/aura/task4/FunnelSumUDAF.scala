package com.aura.task4


import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DataTypes, StructType}

class FunnelSumUDAF extends UserDefinedAggregateFunction{
  override def inputSchema: StructType =
    new StructType()
    .add("events", DataTypes.IntegerType)
    .add("cnt", DataTypes.IntegerType)

  override def bufferSchema: StructType =
    new StructType()
    .add("events", DataTypes.IntegerType)
    .add("counts", DataTypes.createArrayType(DataTypes.LongType))

  override def dataType: DataType = DataTypes.createArrayType(DataTypes.LongType)

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer.update(0, 0)
    buffer.update(1, new java.util.ArrayList[Long]())
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    val events = input.getInt(0)
    buffer.update(0, events)
    val cnts = new java.util.ArrayList[Long]()
    val buffList = new java.util.ArrayList[Long]()
    buffList.addAll(buffer.getList(1))

    val maxEvent = input.getInt(1)
    for(i <- 0 to events){
      var inrc = 0L
      if(i < maxEvent){
        inrc = 1L
      }
      if (buffList.isEmpty){
        cnts.add(inrc)
      }else{
        cnts.add(buffList.get(i) + inrc)
      }
    }
    buffer.update(1, cnts)
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    val events = buffer2.getInt(0)
    buffer1.update(0, events)

    val l1 = new java.util.ArrayList[Long]()
    l1.addAll(buffer1.getList(1))
    val l2 = new java.util.ArrayList[Long]()
    l2.addAll(buffer2.getList(1))

    if (l1.isEmpty){
      buffer1.update(1, l2)
    }else{
      val result = new java.util.ArrayList[Long](l1.size())
      for(i <- 0 until events){
        result.add(l1.get(i) + l2.get(i))
      }
      buffer1.update(1, result)
    }
  }

  override def evaluate(buffer: Row): Any = {
    buffer.getList(1)
  }
}
