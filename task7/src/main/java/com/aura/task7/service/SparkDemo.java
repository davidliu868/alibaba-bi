package com.aura.task7.service;

/**
 * Spark 数据处理demo
 * Created by liuxibiao on 19-5-10.
 */
public class SparkDemo {

    /**
     * 统计广告数据，用spark sql
     */
    public void manageAdData(){

        //获得过去1天点击数据，分组后，统计出1天的点击次数，存到hbase里
        //获得过去7天点击数据，分组后，统计出7天的点击次数，存到hbase里
        //获得过去1天展示数据，分组后，统计出1天的展示次数，存到hbase里
        //获得过去7天展示数据，分组后，统计出7天的展示次数，存到hbase里
    }

    /**
     * 统计用户购买数据，用spark sql
     */
    public void manageUserData(){

        //注意关注用户的更新

        //获得过去7天浏览数据，分组后，统计出7天浏览次数，存到hbase里
        //获得过去14天浏览数据，分组后，统计出14天浏览次数，存到hbase里
        //获得过去7天购买数据，分组后，统计出7天购买次数，存到hbase里
        //获得过去14天购买数据，分组后，统计出14天购买次数，存到hbase里
    }
}
