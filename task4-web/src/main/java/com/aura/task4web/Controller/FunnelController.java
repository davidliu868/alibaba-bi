package com.aura.task4web.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aura.task4web.entity.FunnelAnalysis;
import com.aura.task4web.entity.dto.FunnelDTO;
import com.aura.task4web.service.FunnelAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 留存分析数据查询
 */
@RequestMapping("/funnel")
@RestController
public class FunnelController {

    @Autowired
    private FunnelAnalysisService funnelAnalysisService;

    @CrossOrigin
    @RequestMapping(value = "/get", produces = "application/json;charset=utf-8")
    public String selectByCateId(@RequestBody FunnelDTO dto){
        FunnelAnalysis one = funnelAnalysisService.findOne(dto);
        JSONArray array = new JSONArray();

        JSONObject pvObject = new JSONObject();
        pvObject.put("id", 1);
        pvObject.put("action", "浏览");
        pvObject.put("pv", one.getPv());

        JSONObject cartObject = new JSONObject();
        cartObject.put("id", 2);
        cartObject.put("action", "放入购物车");
        cartObject.put("pv", one.getCart());

        JSONObject favorObject = new JSONObject();
        favorObject.put("id", 3);
        favorObject.put("action", "喜欢");
        favorObject.put("pv", one.getFavour());

        JSONObject buyObject = new JSONObject();
        buyObject.put("id", 4);
        buyObject.put("action", "购买");
        buyObject.put("pv", one.getBuy());

        array.add(pvObject);
        array.add(cartObject);
        array.add(favorObject);
        array.add(buyObject);

        return array.toJSONString();
    }

    @CrossOrigin
    @RequestMapping("/cateids")
    public String listCateIds(){
        List<Integer> allCateId = funnelAnalysisService.getAllCateId();
        return JSONObject.toJSONString(allCateId);
    }


}
