package com.aura.task4web.service.impl;

import com.aura.task4web.entity.FunnelAnalysis;
import com.aura.task4web.entity.dto.FunnelDTO;
import com.aura.task4web.mapper.FunnelAnalysisMapper;
import com.aura.task4web.service.FunnelAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunnelAnalysisServiceImpl  implements FunnelAnalysisService {

    @Autowired
    private FunnelAnalysisMapper funnelAnalysisMapper;

    @Override
    public FunnelAnalysis findOne(FunnelDTO dto) {
        Example example = new Example(FunnelAnalysis.class);
        example.createCriteria().andEqualTo("date", dto.getDate()).andEqualTo("cateId", dto.getId());

        List<FunnelAnalysis> funnelAnalyses = funnelAnalysisMapper.selectByExample(example);

        if (funnelAnalyses != null && funnelAnalyses.size()>0){
            return funnelAnalyses.get(0);
        }
        return null;
    }

    @Override
    public List<Integer> getAllCateId() {
        Example example = new Example(FunnelAnalysis.class);
        example.createCriteria().andNotEqualTo("pv",0).andNotEqualTo("cart", 0).andNotEqualTo("favour",0).andNotEqualTo("buy",0);
        List<FunnelAnalysis> funnelAnalyses = funnelAnalysisMapper.selectByExample(example);
        return funnelAnalyses.stream().map(s -> s.getCateId()).distinct().sorted().collect(Collectors.toList());
    }
}
