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

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by hadoopuser on 4/24/19.
 */
public class ImportBehaviorData {

    public static final String SEPARATOR = ",";
    //public static final String path = "hdfs://hadoopnode:9000/test/behavior_log.csv";
    public static final String path = "file:///home/hadoopuser/IdeaProjects/AuraCasesTraining-master/log-analysis/hbase/hbase-ingest/command/behavior_log.csv";
    public static final String TABLE_NAME = "user_data";
    public static final String FAMILY_NAME_BEHAVIOR = "behavior";

    public static final String QUALIFIER_NAME_USER = "user";
    public static final String QUALIFIER_NAME_TIME_STAMP = "time_stamp";
    public static final String QUALIFIER_NAME_BTAG = "btag";
    public static final String QUALIFIER_NAME_CATE = "cate";
    public static final String QUALIFIER_NAME_BRAND = "brand";



    private Configuration conf = null;
    private Connection connection = null;
    private Table table = null;

    public void execute() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        //establish the connection to the cluster.
        connection = ConnectionFactory.createConnection(conf);
        //retrieve a handler to the target table
        table = connection.getTable(TableName.valueOf(TABLE_NAME));

        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        if (!admin.tableExists(TABLE_NAME)) {
            HTableDescriptor tableDescriptor   = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
            HColumnDescriptor columnDescriptor = new HColumnDescriptor(Bytes.toBytes(FAMILY_NAME_BEHAVIOR));
            tableDescriptor.addFamily(columnDescriptor);
            admin.createTable(tableDescriptor);
        }

        conf = new Configuration();
        Path myPath = new Path(path);
        FileSystem fs = myPath.getFileSystem(conf);
        FSDataInputStream hdfsInStream = fs.open(myPath);
        BufferedReader in = new BufferedReader(new InputStreamReader(hdfsInStream));

        String line = null;
        int i = -1;

        while ((line = in.readLine()) != null) {
            System.out.println(line);
            i++;
            if(i==0) {
                continue;
            }


            String[] attributes = line.split(SEPARATOR);
            if(attributes.length != 5){
                continue;
            }
            String row_key = String.format("%07d",Long.valueOf(attributes[0]));
            if(!attributes[1].equals(null)){
                row_key = row_key + "-" + attributes[1] + "-" + (int)(Math.random()*900 + 100);
            }
            Put put = new Put(Bytes.toBytes(row_key));
            if(!attributes[0].equals(null)){
                put.addColumn(Bytes.toBytes(FAMILY_NAME_BEHAVIOR), Bytes.toBytes(QUALIFIER_NAME_USER), Bytes.toBytes(attributes[0]));
            }
            if(!attributes[1].equals(null)){
                put.addColumn(Bytes.toBytes(FAMILY_NAME_BEHAVIOR), Bytes.toBytes(QUALIFIER_NAME_TIME_STAMP), Bytes.toBytes(attributes[1]));
            }
            if(!attributes[2].equals(null)){
                put.addColumn(Bytes.toBytes(FAMILY_NAME_BEHAVIOR), Bytes.toBytes(QUALIFIER_NAME_BTAG), Bytes.toBytes(attributes[2]));
            }
            if(!attributes[3].equals(null)){
                put.addColumn(Bytes.toBytes(FAMILY_NAME_BEHAVIOR), Bytes.toBytes(QUALIFIER_NAME_CATE), Bytes.toBytes(attributes[3]));
            }
            if(!attributes[4].equals(null)){
                put.addColumn(Bytes.toBytes(FAMILY_NAME_BEHAVIOR), Bytes.toBytes(QUALIFIER_NAME_BRAND), Bytes.toBytes(attributes[4]));
            }

            //send the data
            table.put(put);
            System.out.println("insert data :" + i);
        }
        if (in != null) {
            in.close();
        }
        if (table != null) {
            table.close();
        }
        if (connection != null) {
            connection.close();
        }

    }



    public static void main(String[] args) throws Exception {
        ImportBehaviorData i = new ImportBehaviorData();
        i.execute();
    }
}
