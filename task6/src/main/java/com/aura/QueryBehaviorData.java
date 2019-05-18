package com.aura;


import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by yuhaibin on 2019/4/25.
 */
public class QueryBehaviorData {

    public static final String TABLE_NAME = "user_data";
    public static final String FAMILY_NAME_BEHAVIOR = "behavior";

    public static final String QUALIFIER_NAME_USER = "user";
    public static final String QUALIFIER_NAME_TIME_STAMP = "time_stamp";
    public static final String QUALIFIER_NAME_BTAG = "btag";
    public static final String QUALIFIER_NAME_CATE = "cate";
    public static final String QUALIFIER_NAME_BRAND = "brand";

    private Connection connection = null;


    public boolean execute(String userid,String startDate,String endDate,String btag) throws Exception {
        boolean b = false;

        //establish the connection to the cluster.
        Connection connection = ConnectionFactory.createConnection();
        //retrieve a handler to the target table
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(getStartRow(userid,startDate)));
        scan.setStopRow(Bytes.toBytes(getEndRow(userid,endDate)));

        scan.setCaching(100);

        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(FAMILY_NAME_BEHAVIOR), Bytes.toBytes(QUALIFIER_NAME_BTAG), CompareOp.EQUAL, Bytes.toBytes(btag));
        scan.setFilter(filter);

        ResultScanner results = table.getScanner(scan);

        for(Result result: results){
            //System.out.println(Bytes.toString(result.getRow()) + " : " + (result.isEmpty() ? 0 : result.listCells().size()));
            Cell cell = result.getColumnLatestCell(Bytes.toBytes(FAMILY_NAME_BEHAVIOR),Bytes.toBytes(QUALIFIER_NAME_USER));
            if(cell!=null){
                String user = Bytes.toString(cell.getValueArray(),cell.getValueOffset());
                System.out.println(user);
                b = true;
            }
        }



        if (table != null) {
            table.close();
        }
        if (connection != null) {
            connection.close();
        }

        return  b;
    }

    public static String getStartRow(String userid,String thisDate) {
        String row = "";
        if(userid!=null && thisDate!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(thisDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dateTime = date.getTime();
            row = String.format("%07d",Long.valueOf(userid)) + "-" + dateTime;
        }
        return row;
    }

    public static String getEndRow(String userid,String thisDate) {
        String row = "";
        if(userid!=null && thisDate!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = null;
            try {
                Date date = simpleDateFormat.parse(thisDate);

                Calendar c = Calendar.getInstance();
                c.setTime(date);
                c.add(Calendar.DAY_OF_MONTH, 1);

                endDate = c.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long dateTime = endDate.getTime();
            row = String.format("%07d",Long.valueOf(userid)) + "-" + dateTime;
        }
        return row;
    }

    public static String longToString(long thisTime) {
        Date date = new Date(thisTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static void main(String[] args) throws Exception {
        QueryBehaviorData i = new QueryBehaviorData();
        String userid = "228116";
        String startDate = "1970-01-17";
        String endDate = "1970-01-18";
        String btag = "buy";
        i.execute( userid, startDate, endDate, btag);

//        System.out.println(longToString(1493522901));  //1970-01-17
//        System.out.println(longToString(1493522358));  //1970-01-17
//        1497600000  1970-01-18

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        java.util.Date date = null;
//        try {
//            date = simpleDateFormat.parse("1970-01-18");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long dateTime = date.getTime();
//        System.out.println(dateTime);
    }
}
