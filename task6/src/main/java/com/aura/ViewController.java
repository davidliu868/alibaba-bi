package com.aura;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hadoopuser on 5/14/19.
 */
@RestController
@RequestMapping("view")
public class ViewController {

    @RequestMapping(value="/list", method = RequestMethod.POST)
    public Map<String,Object> listRecord(String userId, String startDate, String endDate, String btag, String clkFlag) throws Exception {
        Map<String,Object> map = new HashMap<String,Object>();
        if(btag==null || btag.equals("")){
            btag = "buy";
        }
        if(clkFlag==null || clkFlag.equals("")){
            clkFlag = "1";
        }

        QueryData query = new QueryData();
        List<RawSamplePo> list =  query.execute(userId, startDate, endDate, btag, clkFlag);

        map.put("list",list);
        return map;
    }

    private List<RawSamplePo> getTestList() {
        List<RawSamplePo> list = new ArrayList<RawSamplePo>();
        RawSamplePo po = new RawSamplePo();
        po.setUserId("914836");
        po.setTimeStamp("2017-01-01");
        po.setAdgroupId("248909");
        po.setBrand("32233");
        list.add(po);

        po = new RawSamplePo();
        po.setUserId("914836");
        po.setTimeStamp("2017-05-19");
        po.setAdgroupId("279841");
        po.setBrand("57034");
        list.add(po);
        return list;
    }
}
