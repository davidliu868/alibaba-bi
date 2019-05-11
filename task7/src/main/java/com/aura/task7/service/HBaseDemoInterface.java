package com.aura.task7.service;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

/**
 * Created by hadoopuser on 19-4-27.
 */
public interface HBaseDemoInterface {

    // create 'tablename','cf1','cf2'
    public void createTable(String tableName, String[] family) throws Exception;

    public void createTable(String tableName, HTableDescriptor htd) throws Exception ;

    // desc 'person'
    public void descTable(String tableName) throws Exception ;

    public void modifyTable(String tableName) throws Exception ;

    // list
    public void getAllTables() throws Exception ;

    // put 'tablename','rowkey','familyname:key','value'
    public void putData(String tableName, String rowKey, String familyName, String columnName, String value) throws Exception ;

    /**
     * @param tableName
     *            表名
     * @param rowKey
     *            rowkey
     * @param column1
     *            第一个列簇的key数组
     * @param value1
     *            第一个列簇的value数组，key数组和value数组长度必须一样
     * @param column2
     *            第二列簇的key数组
     * @param value2
     *            第二个列簇的values数组， 同上同理
     * @throws Exception
     */
    public void addData(String tableName, String rowKey, String[] column1, String[] value1, String[] column2, String[] value2) throws Exception ;

    // get 'tablename','rowkey'
    public Result getResult(String tableName, String rowKey) throws Exception ;

    public Result getResult(String tableName, String rowKey, String familyName, String columnName) throws Exception ;


    public ResultScanner getResultScann(String tableName) throws Exception ;


    public ResultScanner getResultScann(String tableName, Scan scan) throws Exception ;


    public Result getResultByColumn(String tableName, String rowKey, String familyName, String columnName) throws Exception ;

    // get 'person','p001',{COLUMNS => 'cf1:name', VERSIONS => 3}
    public Result getResultByVersion(String tableName, String rowKey, String familyName, String columnName, int versions) throws Exception ;


    public void deleteColumn(String tableName, String rowKey, String falilyName, String columnName) throws Exception ;

    public void deleteColumn(String tableName, String rowKey) throws Exception ;

    public void disableTable(String tableName) throws Exception ;


    public void dropTable(String tableName) throws Exception ;
}
