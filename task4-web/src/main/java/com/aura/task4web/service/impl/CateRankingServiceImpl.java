package com.aura.task4web.service.impl;

import com.aura.task4web.entity.CateRanking;
import com.aura.task4web.mapper.CateRankingMapper;
import com.aura.task4web.service.CateRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CateRankingServiceImpl implements CateRankingService {

    @Autowired
    private CateRankingMapper cateRankingMapper;

    @Override
    public List<CateRanking> listByDate(String date) {
        Example example = new Example(CateRanking.class);
        example.createCriteria().andEqualTo("date", date);
        example.setOrderByClause("nums desc");

        return cateRankingMapper.selectByExample(example);
    }
}
