package com.best.gwms.data.service.impl;
import com.best.gwms.common.vo.bas.BasWarehouseVo;
import com.best.gwms.data.constant.CodeConstant;
import com.best.gwms.data.dao.AbcAnalyzedDao;
import com.best.gwms.data.helper.AbcQueryFactory;
import com.best.gwms.data.helper.AnalyzedVoConvertHelper;
import com.best.gwms.data.helper.MasterXingHelper;
import com.best.gwms.data.model.po.AbcAnalyzedPo;
import com.best.gwms.data.model.so.AbcAnalyzeSo;
import com.best.gwms.data.model.vo.AbcAnalyzedVo;
import com.best.gwms.data.service.AbcAnalyzeQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author BG236820
 */
@Service
public class AbcIkAnalyzeService implements AbcAnalyzeQueryService {
    @Autowired
    private AbcAnalyzedDao dao;

    @Autowired
    private AnalyzedVoConvertHelper convertHelper;
    @Autowired
    private MasterXingHelper masterXingHelper;

    @Override
    public List<AbcAnalyzedVo> listAnalyzedSku(AbcAnalyzeSo so) {
        List<AbcAnalyzedPo> abcAnalyzedPos = dao.listAnalyzedSkuIkQty(so);
        Long whTotalShippedIk = dao.getWhTotalShippedIk(so);
        BasWarehouseVo warehouse = masterXingHelper.getWarehouseById(so.getWhId());
        List<AbcAnalyzedVo> abcAnalyzedVoList = convertHelper.convertVo(whTotalShippedIk,warehouse, abcAnalyzedPos);
        return abcAnalyzedVoList;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        AbcQueryFactory.register(CodeConstant.CIC_SORT_IK, this);
    }

}
