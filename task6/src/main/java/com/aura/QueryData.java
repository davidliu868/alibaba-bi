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
public class QueryData {

    public static final String TABLE_NAME = "raw_sample.csv";
    public static final String FAMILY_NAME_RAW = "raw";
    public static final String FAMILY_NAME_BEHAVIOR = "behavior";

    public static final String QUALIFIER_NAME_USER_ID = "user_id";
    public static final String QUALIFIER_NAME_ADGROUP_ID = "adgroup_id";
    public static final String QUALIFIER_NAME_BRAND = "brand";
    public static final String QUALIFIER_NAME_TIME_STAMP = "time_stamp";

    public static final String QUALIFIER_NAME_BTAG = "btag";

    public static final String QUALIFIER_NAME_CLK = "clk";

    private Connection connection = null;

    /**
     *
     * @param userid
     * @param startDate
     * @param endDate
     * @param btag  btag=buy
     * @param clkFlag  clk=1
     * @return
     * @throws Exception
     */
    public List<RawSamplePo> execute(String userid,String startDate,String endDate,String btag,String clkFlag) throws Exception {
        List<RawSamplePo> list = new ArrayList<RawSamplePo>();

        QueryBehaviorData i = new QueryBehaviorData();
        boolean b = i.execute( userid, startDate, endDate, btag);
        if(b){
            QueryRawData r = new QueryRawData();
            list = r.execute(userid, startDate, endDate, clkFlag);
        }

        return list;
    }




    public static void main(String[] args) throws Exception {
        QueryData i = new QueryData();
        String userid = "228116";
        String startDate = "1970-01-17";
        String endDate = "1970-01-18";
        String btag = "buy";
        String clkFlag = "1";
        i.execute( userid, startDate, endDate, btag, clkFlag);
    }
}
