package com.aura.task4web.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aura.task4web.entity.AgeDistributed;
import com.aura.task4web.entity.GenderDistributed;
import com.aura.task4web.entity.PvalueDistributed;
import com.aura.task4web.service.DistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/distributed")
@RestController
public class DistributedController {


    @Autowired
    private DistributedService service;

    /**
     * 根据cateID查询年龄分布
     * @param cate
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/age", produces = "application/json;charset=utf-8")
    public String age(String cate){

        List<AgeDistributed> list = service.ageList(cate);
        JSONArray array = new JSONArray();
        for (AgeDistributed ageDistributed: list){
            JSONObject object = new JSONObject();
            object.put("age", ageDistributed.getAge());
            object.put("count", ageDistributed.getAgeNums());
            array.add(object);
        }

        return array.toJSONString();
    }


    /**
     * 根据cateId查询年龄分布
     * @param cate
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/gender", produces = "application/json;charset=utf-8")
    public String gender(String cate){


        List<GenderDistributed> list = service.genderList(cate);
        JSONArray array = new JSONArray();
        for (GenderDistributed genderDistributed: list){
            JSONObject object = new JSONObject();
            object.put("age", genderDistributed.getGender());
            object.put("count", genderDistributed.getGenderNums());
            array.add(object);
        }

        return array.toJSONString();
    }

    /**
     * 消费档次分布
     * @param cate
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/pvalue", produces = "application/json;charset=utf-8")
    public String pvalue(String cate){

        List<PvalueDistributed> list = service.pvalueList(cate);
        JSONArray array = new JSONArray();
        for (PvalueDistributed pvalueDistributed: list){
            JSONObject object = new JSONObject();
            object.put("age", pvalueDistributed.getPvalue());
            object.put("count", pvalueDistributed.getPvalueNums());
            array.add(object);
        }

        return array.toJSONString();
    }


}
