package com.aura.task4web.service;

import com.aura.task4web.entity.FunnelAnalysis;
import com.aura.task4web.entity.dto.FunnelDTO;

import java.util.List;

public interface FunnelAnalysisService {

    /**
     * 根据日期和类目ID查找留存分析信息
     * @param dto
     * @return
     */
    FunnelAnalysis findOne(FunnelDTO dto);

    /**
     * 查询所有的类目ID
     * @return
     */
    List<Integer> getAllCateId();
}
