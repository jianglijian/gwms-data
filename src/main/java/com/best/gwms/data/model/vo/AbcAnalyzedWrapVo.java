package com.best.gwms.data.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author BG236820
 */
@Data
public class AbcAnalyzedWrapVo {

    private List<AbcAnalyzedVo> skuAnalyzedVoList;

    /**
     * 临界值列表，计算所得 【200，500，...,total】
     */
    private List<Integer> criticalValueList;
}
