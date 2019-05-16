package com.aura.task4web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aura.task4web.entity.vo.RetentionAnalysisVO;
import com.aura.task4web.service.RetentionAnalysisService;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetentionAnalysisServiceImpl implements RetentionAnalysisService {

    private static Logger logger = LoggerFactory.getLogger(RetentionAnalysisServiceImpl.class);

    @Autowired
    private HbaseTemplate hbaseTemplate;

    private static final String TABLE_NAME="retention_analysis";

    @Override
    public RetentionAnalysisVO getRetentionByRowKey(String rowKey) {

        RetentionAnalysisVO analysisVO = hbaseTemplate.get(TABLE_NAME, rowKey, new RowMapper<RetentionAnalysisVO>() {
            @Override
            public RetentionAnalysisVO mapRow(Result result, int i) throws Exception {
                logger.debug(result.toString());
                List<Cell> cells = result.listCells();
                if (cells != null && cells.size() > 0) {

                    String rowKey = "";
                    JSONObject object = new JSONObject();
                    for (Cell cell : cells) {
                        if (StringUtils.isEmpty(rowKey)) {
                            rowKey = Bytes.toString(CellUtil.cloneRow(cell));
                            object.put("id", rowKey);
                        }
                        String column = Bytes.toString(CellUtil.cloneQualifier(cell));
                        String value = Bytes.toString(CellUtil.cloneValue(cell));
                        System.out.println("column:" + column + " value:" + value);
                        object.put(column, value);
                    }

                    return object.toJavaObject(RetentionAnalysisVO.class);
                }
                return null;
            }
        });
        return analysisVO;
    }
}
