package com.best.gwms.data.service.impl;

import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.common.po.bas.BasActivityType;
import com.best.gwms.common.vo.bas.BasPackagingVo;
import com.best.gwms.common.vo.bas.BasWarehouseVo;
import com.best.gwms.data.constant.CodeConstant;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.dao.AbcAnalyzedDao;
import com.best.gwms.data.helper.AbcQueryFactory;
import com.best.gwms.data.helper.AnalyzedVoConvertHelper;
import com.best.gwms.data.helper.MasterXingHelper;
import com.best.gwms.data.model.po.AbcAnalyzedPo;
import com.best.gwms.data.model.so.AbcAnalyzeSo;
import com.best.gwms.data.model.vo.AbcAnalyzedVo;
import com.best.gwms.data.service.AbcAnalyzeQueryService;
import com.best.gwms.data.util.CollectionUtil;
import com.best.gwms.data.util.MathUtil;
import com.best.gwms.master.xing.BasPackagingXingService;
import com.best.xingng.service.annotation.XClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author BG236820
 */
@Service
public class AbcIvAnalyzeService implements AbcAnalyzeQueryService {
    @Autowired
    private AbcAnalyzedDao dao;

    @XClient
    private BasPackagingXingService xingService;

    @Autowired
    private AnalyzedVoConvertHelper convertHelper;

    @Autowired
    private MasterXingHelper masterXingHelper;

    @Override
    public List<AbcAnalyzedVo> listAnalyzedSku(AbcAnalyzeSo so) {
        List<AbcAnalyzedPo> abcAnalyzedPos = dao.listAnalyzedSkuQty(so);
        Long whTotalShippedQty = dao.getWhTotalShippedQty(so);
        BasWarehouseVo warehouse = masterXingHelper.getWarehouseById(so.getWhId());
        List<AbcAnalyzedVo> abcAnalyzedVoList = convertHelper.convertVo(whTotalShippedQty, warehouse, abcAnalyzedPos);

        List<Long> skuList = CollectionUtil.getPropertyListFromList(abcAnalyzedPos, FieldConstant.SKU_ID);
        ClientInfo clientInfo = new ClientInfo();
        List<BasPackagingVo> basPackagingVos = xingService.listBySkuIds(clientInfo, skuList);
        Map<Long, BasPackagingVo> skuPackMapping = CollectionUtil.getSingleMap(basPackagingVos, FieldConstant.SKU_ID);
        abcAnalyzedVoList.stream().forEach(o -> {
            BasPackagingVo packagingVo = skuPackMapping.get(o.getSkuId());
            if (packagingVo != null && packagingVo.getLength() != null && packagingVo.getWidth() != null && packagingVo.getHeight() != null) {
                Double volume = MathUtil.multiply(packagingVo.getLength(), packagingVo.getWidth());
                volume = MathUtil.multiply(volume, packagingVo.getHeight());
                o.setSkuQty(MathUtil.multiply(o.getSkuQty(), volume));
            }
        });
        List<AbcAnalyzedVo> newList = abcAnalyzedVoList.stream().sorted(Comparator.comparing(AbcAnalyzedVo::getSkuQty).reversed()).collect(Collectors.toList());
        double totalVolume = abcAnalyzedVoList.stream().mapToDouble(o -> o.getSkuQty()).sum();
        AtomicReference<Integer> rank = new AtomicReference<>(1);
        AtomicReference<Double> totalPercent = new AtomicReference<>(0d);
        newList.stream().forEach(o ->{
            o.setRank(rank.get());
            rank.set(Math.addExact(rank.get(), 1));
            o.setTotalQty(totalVolume);
            o.setPercentage(MathUtil.divide(o.getSkuQty(),totalVolume,2));
            totalPercent.set(MathUtil.add(totalPercent.get(), o.getPercentage()));
            if (totalPercent.get() > 1) {
                o.setGrandTotalPercent(1d);
            } else {
                o.setGrandTotalPercent(totalPercent.get());
            }
        } );

        return newList;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        AbcQueryFactory.register(CodeConstant.CIC_SORT_IV, this);
    }


}
