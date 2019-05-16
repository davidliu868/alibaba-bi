package com.aura.task4web.service;

import com.aura.task4web.entity.CateRanking;

import java.util.List;

public interface CateRankingService {

    /**
     * 根据日期查询排名
     * @param date
     * @return
     */
    List<CateRanking> listByDate(String date);
}
