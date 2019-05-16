package com.aura.task4web.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aura.task4web.entity.vo.RetentionAnalysisVO;
import com.aura.task4web.service.RetentionAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@RequestMapping("/retention")
@RestController
public class RetentionAnalysisController {

    @Autowired
    private RetentionAnalysisService retentionAnalysisService;

    @CrossOrigin
    @RequestMapping(value = "/get", produces = "application/json;charset=utf-8")
    public String getByRowkey(String row){

        RetentionAnalysisVO vo = retentionAnalysisService.getRetentionByRowKey(row);

        JSONArray array = new JSONArray();

        String date = row.substring(0, 10);

        if (vo != null){

            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(vo));

            for (int i=0; i<22; i++){
                JSONObject dObj = new JSONObject();
                String key = String.format("d%d", i);
                dObj.put("date", handleDate(date, i));
                dObj.put("value", object.get(key));
                array.add(dObj);
            }
        }

        return array.toJSONString();
    }

    private String handleDate(String date, int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(Calendar.DATE, day);
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
