package com.aura;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.InputStreamReader;

//import redis.clients.jedis.Jedis;

/**
 * Created by hadoopuser on 4/24/19.
 */
public class ImportAdData {

    public static final String SEPARATOR = ",";
    //public static final String path = "hdfs://hadoopnode:9000/test/behavior_log.csv";
    public static final String path = "file:///home/hadoopuser/IdeaProjects/AuraCasesTraining-master/log-analysis/hbase/hbase-ingest/command/ad_feature.csv";
    private static final String AD_HASHKEY = "advice";

    public void execute() throws Exception {
        Configuration conf = new Configuration();
        Path myPath = new Path(path);
        FileSystem fs = myPath.getFileSystem(conf);
        FSDataInputStream hdfsInStream = fs.open(myPath);
        BufferedReader in = new BufferedReader(new InputStreamReader(hdfsInStream));

        String line = null;
        int i = -1;

        Jedis jedis = JavaRedisClient.get().getResource();
        while ((line = in.readLine()) != null) {
            System.out.println(line);

            //the first is title
            i++;
            if(i==0) {
                continue;
            }

            String[] attributes = line.split(SEPARATOR);
            if(attributes.length != 6){
                continue;
            }

            // 0-adgroup_id 4-brand
            jedis.hset(AD_HASHKEY,attributes[0],attributes[4]);


            System.out.println("insert redis data :" + i);
        }

        jedis.close();

        if (in != null) {
            in.close();
        }


    }



    public static void main(String[] args) throws Exception {
        ImportAdData i = new ImportAdData();
        i.execute();
    }
}
