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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by yuhaibin on 2019/4/25.
 */
public class QueryRawData {

    public static final String TABLE_NAME = "raw_sample.csv";
    public static final String FAMILY_NAME_RAW = "raw";

    public static final String QUALIFIER_NAME_USER_ID = "user_id";
    public static final String QUALIFIER_NAME_ADGROUP_ID = "adgroup_id";
    public static final String QUALIFIER_NAME_BRAND = "brand";
    public static final String QUALIFIER_NAME_TIME_STAMP = "time_stamp";
    public static final String QUALIFIER_NAME_PID = "pid";
    public static final String QUALIFIER_NAME_NOCLK = "noclk";
    public static final String QUALIFIER_NAME_CLK = "clk";

    private Connection connection = null;

    /**
     *
     * @param userid
     * @param startDate
     * @param endDate
     * @param flag  clk=1
     * @return
     * @throws Exception
     */
    public List<RawSamplePo> execute(String userid,String startDate,String endDate,String flag) throws Exception {
        List<RawSamplePo> list = new ArrayList<RawSamplePo>();
        //establish the connection to the cluster.
        Connection connection = ConnectionFactory.createConnection();
        //retrieve a handler to the target table
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes(getStartRow(userid,startDate)));
        scan.setStopRow(Bytes.toBytes(getEndRow(userid,endDate)));
        scan.setCaching(100);

        Filter filter = new SingleColumnValueFilter(Bytes.toBytes(FAMILY_NAME_RAW), Bytes.toBytes(QUALIFIER_NAME_CLK), CompareOp.EQUAL, Bytes.toBytes(flag));
        scan.setFilter(filter);

        ResultScanner results = table.getScanner(scan);

        for(Result result: results){
            //System.out.println(Bytes.toString(result.getRow()) + " : " + (result.isEmpty() ? 0 : result.listCells().size()));
            Cell cell0 = result.getColumnLatestCell(Bytes.toBytes(FAMILY_NAME_RAW),Bytes.toBytes(QUALIFIER_NAME_ADGROUP_ID));
            if(cell0!=null){
                String adgroupId = Bytes.toString(cell0.getValueArray(),cell0.getValueOffset());
                System.out.println(adgroupId);
            }

            Cell cell1 = result.getColumnLatestCell(Bytes.toBytes(FAMILY_NAME_RAW),Bytes.toBytes(QUALIFIER_NAME_BRAND));
            if(cell1!=null){
                String brand = Bytes.toString(cell1.getValueArray(),cell1.getValueOffset());
                System.out.println(brand);
            }

            Cell cell2 = result.getColumnLatestCell(Bytes.toBytes(FAMILY_NAME_RAW),Bytes.toBytes(QUALIFIER_NAME_TIME_STAMP));
            if(cell2!=null){
                String timeStamp = Bytes.toString(cell2.getValueArray(),cell2.getValueOffset());
                System.out.println(timeStamp);
            }

        }



        if (table != null) {
            table.close();
        }
        if (connection != null) {
            connection.close();
        }

        return list;
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

    }
}
