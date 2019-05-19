package com.aura.task4web.service.impl;

import com.aura.task4web.entity.AgeDistributed;
import com.aura.task4web.entity.GenderDistributed;
import com.aura.task4web.entity.PvalueDistributed;
import com.aura.task4web.mapper.AgeDistributedMapper;
import com.aura.task4web.mapper.GenderDistributedMapper;
import com.aura.task4web.mapper.PvalueDistributedMapper;
import com.aura.task4web.service.DistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class DistributedServiceImpl implements DistributedService {

    @Autowired
    private AgeDistributedMapper ageDistributedMapper;

    @Autowired
    private GenderDistributedMapper genderDistributedMapper;

    @Autowired
    private PvalueDistributedMapper pvalueDistributedMapper;

    @Override
    public List<AgeDistributed> ageList(String cate) {

        Example example = new Example(AgeDistributed.class);
        example.createCriteria().andEqualTo("cateId", cate);

        return ageDistributedMapper.selectByExample(example);
    }

    @Override
    public List<GenderDistributed> genderList(String cate) {
        Example example = new Example(GenderDistributed.class);
        example.createCriteria().andEqualTo("cateId", cate);
        return genderDistributedMapper.selectByExample(example);
    }

    @Override
    public List<PvalueDistributed> pvalueList(String cate) {
        Example example = new Example(PvalueDistributed.class);
        example.createCriteria().andEqualTo("cateId", cate);
        return pvalueDistributedMapper.selectByExample(example);
    }
}
