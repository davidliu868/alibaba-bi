package com.aura.task4web.service;

import com.aura.task4web.entity.vo.RetentionAnalysisVO;

public interface RetentionAnalysisService {

    RetentionAnalysisVO getRetentionByRowKey(String rowKey);
}
