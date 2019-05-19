package com.aura.task4web.service;

import com.aura.task4web.entity.AgeDistributed;
import com.aura.task4web.entity.GenderDistributed;
import com.aura.task4web.entity.PvalueDistributed;

import java.util.List;

public interface DistributedService {

    List<AgeDistributed> ageList(String cate);
    List<GenderDistributed> genderList(String cate);
    List<PvalueDistributed> pvalueList(String cate);

}
