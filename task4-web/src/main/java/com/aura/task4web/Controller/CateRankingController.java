package com.aura.task4web.Controller;

import com.alibaba.fastjson.JSONObject;
import com.aura.task4web.entity.CateRanking;
import com.aura.task4web.service.CateRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/cate")
@RestController
public class CateRankingController {

    @Autowired
    private CateRankingService cateRankingService;


    /**
     * 根据日期查询类目浏览量排名
     * @param date
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/listbydate", produces = "application/json;charset=utf-8")
    public String listCateBydate(String date){

        List<CateRanking> cateRankings = cateRankingService.listByDate(date);
        JSONObject object = new JSONObject();
        object.put("cates", cateRankings);
        return object.toJSONString();
    }

}
