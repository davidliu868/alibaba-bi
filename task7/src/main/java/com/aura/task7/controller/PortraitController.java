package com.aura.task7.controller;


import com.alibaba.fastjson.JSONObject;
import com.aura.task7.service.Hbasedome;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PortraitController {

    @RequestMapping(method = RequestMethod.GET,produces = "application/json;charset=UTF-8",value="/hello")
    public String say(){
        return "hello胜多负少的==asd=";
    }

    @RequestMapping(value = "/abc",produces = "application/json;charset=utf8") //中文转义
    public String testApi(){
        return JSONObject.toJSONString("safsaf:safs");
    }

    @RequestMapping(value = "/get_ad_portrait",method = RequestMethod.GET,produces = "application/json;charset=utf8") //中文转义
    public String getAdPortrait(String id){
        Hbasedome a =new Hbasedome();
        Map<String ,String> map = new HashMap<String,String>();
        try {
            Result result = a.getResult("ad_portrait", id);

            List<KeyValue> list = result.list();
            for (int i = 0; i < list.size(); i++) {
                KeyValue kv = list.get(i);
                String keyF = Bytes.toString(kv.getQualifier());
                map.put(keyF, Bytes.toString(kv.getValue()));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return JSONObject.toJSONString(map);
    }

    @RequestMapping(value = "/get_user_portrait",method = RequestMethod.GET,produces = "application/json;charset=utf8") //中文转义
    public String getUserPortrait(String id){
        Hbasedome a =new Hbasedome();
        Map<String ,String> map = new HashMap<String,String>();
        try {
            Result result = a.getResult("user_portrait", id);

            List<KeyValue> list = result.list();
            for (int i = 0; i < list.size(); i++) {
                KeyValue kv = list.get(i);
                String keyF = Bytes.toString(kv.getQualifier());
                map.put(keyF, Bytes.toString(kv.getValue()));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return JSONObject.toJSONString(map);
    }
}
