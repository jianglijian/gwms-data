package com.best.gwms.data.helper;

import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.common.po.stock.snap.StockSnap;
import com.best.gwms.common.vo.bas.BasOwnerVo;
import com.best.gwms.common.vo.bas.BasSkuCategoryVo;
import com.best.gwms.common.vo.bas.BasSkuVo;
import com.best.gwms.common.vo.bas.BasWarehouseVo;
import com.best.gwms.common.vo.bas.wrap.SimpleSkuAbcInfoVo;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.model.po.AbcAnalyzedPo;
import com.best.gwms.data.model.vo.AbcAnalyzedVo;
import com.best.gwms.data.util.CollectionUtil;
import com.best.gwms.data.util.EasyUtil;
import com.best.gwms.data.util.MathUtil;
import com.best.gwms.master.xing.BasOwnerXingService;
import com.best.gwms.master.xing.BasSkuCategoryXingService;
import com.best.gwms.master.xing.BasSkuXingService;
import com.best.gwms.util.ListSegmentIterator;
import com.best.xingng.service.annotation.XClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class AnalyzedVoConvertHelper {


    @Autowired
    private SpringBeanHelper beanHelper;

    @XClient
    private BasSkuXingService skuXingService;


    public List<AbcAnalyzedVo> convertVo(Long total,  BasWarehouseVo warehouseVo, List<AbcAnalyzedPo> poList) {
        ClientInfo clientInfo = new ClientInfo();
        Long whId=warehouseVo.getId();
        String whCode=warehouseVo.getWhCode();
        List<AbcAnalyzedVo> list = new ArrayList<>();
        if (total == null || EasyUtil.isCollectionEmpty(poList)) {
            return list;
        }
        AtomicReference<Double> totalPercent = new AtomicReference<>(0d);

        List<SimpleSkuAbcInfoVo> abcInfoVoList = new ArrayList<>();
        List<Long> skuIdList = CollectionUtil.getPropertyListFromList(poList, FieldConstant.SKU_ID);
        ListSegmentIterator iterator = new ListSegmentIterator<>(skuIdList, 1000);
        Long start = System.currentTimeMillis();
        while (iterator.hasNext()) {
            List<Long> segSkuList=iterator.next();
            List list1 = skuXingService.listSkuAbcInfo(clientInfo,whId, segSkuList);
            abcInfoVoList.addAll(list1);
        }
        log.info("sku convert to  analyze vo  cost time{0}", System.currentTimeMillis() - start);
        Map<Long, SimpleSkuAbcInfoVo> simpleSkuAbcInfoVoMap = CollectionUtil.getSingleMap(abcInfoVoList, FieldConstant.SKU_ID);
        poList.stream().forEach(o -> {
            SimpleSkuAbcInfoVo simpleSkuAbcInfoVo = simpleSkuAbcInfoVoMap.get(o.getSkuId());
            AbcAnalyzedVo vo = new AbcAnalyzedVo();
            beanHelper.copyPropertiesIgnoreNull(o, vo);
            vo.setTotalQty(Double.valueOf(total));
            Double skuPercent = MathUtil.divide(o.getSkuQty(), total, 2);
            vo.setPercentage(skuPercent);
            totalPercent.set(MathUtil.add(totalPercent.get(), skuPercent));
            if (totalPercent.get() > 1) {
                vo.setGrandTotalPercent(1d);
            } else {
                vo.setGrandTotalPercent(totalPercent.get());
            }
            if (simpleSkuAbcInfoVo != null) {
                vo.setSkuCode(simpleSkuAbcInfoVo.getSkuCode());
                vo.setCategoryName(simpleSkuAbcInfoVo.getCategoryName());
                vo.setOwnerCode(simpleSkuAbcInfoVo.getOwnerCode());
            }
            vo.setWhCode(whCode);
            list.add(vo);
        });
        simpleSkuAbcInfoVoMap.clear();
        return list;
    }

    public List<AbcAnalyzedVo> convertZeroVo(boolean allZero,AtomicReference<Integer> rank, BasWarehouseVo warehouseVo, List<BasSkuVo> skuVoList) {
        ClientInfo clientInfo = new ClientInfo();
        List<AbcAnalyzedVo> list = new ArrayList<>();
        List<SimpleSkuAbcInfoVo> abcInfoVoList = new ArrayList<>();
        List<Long> skuIdList = CollectionUtil.getPropertyListFromList(skuVoList, FieldConstant.ID);
        Long whId = warehouseVo.getId();
        String whCode = warehouseVo.getWhCode();
        ListSegmentIterator iterator = new ListSegmentIterator<>(skuIdList, 1000);
        Long start = System.currentTimeMillis();
        while (iterator.hasNext()) {
            List list1 = skuXingService.listSkuAbcInfo(clientInfo, whId, iterator.next());
            abcInfoVoList.addAll(list1);
        }
        log.info("0 sku wrap cost time{0}", System.currentTimeMillis() - start);
        Map<Long, SimpleSkuAbcInfoVo> simpleSkuAbcInfoVoMap = CollectionUtil.getSingleMap(abcInfoVoList, FieldConstant.SKU_ID);

        skuVoList.stream().forEach(o -> {
            AbcAnalyzedVo vo = new AbcAnalyzedVo();
            beanHelper.copyPropertiesIgnoreNull(o, vo);
            vo.setRank(rank.get());
            vo.setTotalQty(0d);
            vo.setPercentage(0d);
            if(allZero){
                vo.setGrandTotalPercent(0d);
            }else {
                vo.setGrandTotalPercent(1d);
            }
            vo.setSkuCode(o.getSkuCode());
            SimpleSkuAbcInfoVo simpleSkuAbcInfoVo = simpleSkuAbcInfoVoMap.get(o.getId());
            if (simpleSkuAbcInfoVo != null) {
                vo.setCategoryName(simpleSkuAbcInfoVo.getCategoryName());
                vo.setOwnerCode(simpleSkuAbcInfoVo.getOwnerCode());
            }
            vo.setWhCode(whCode);
            list.add(vo);
            rank.set(rank.get() + 1);
        });
        return list;
    }
}
