package com.best.gwms.data.service.impl;

import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.common.vo.bas.BasCodeInfoVo;
import com.best.gwms.common.vo.bas.BasSkuVo;
import com.best.gwms.common.vo.bas.BasWarehouseVo;
import com.best.gwms.data.constant.CodeConstant;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.dao.AbcAnalyzedDao;
import com.best.gwms.data.dao.AbcSettingConfigDao;
import com.best.gwms.data.helper.AbcQueryFactory;
import com.best.gwms.data.helper.AnalyzedVoConvertHelper;
import com.best.gwms.data.helper.MasterXingHelper;
import com.best.gwms.data.helper.WebSoConvertHelper;
import com.best.gwms.data.model.po.AbcSettingConfig;
import com.best.gwms.data.model.so.AbcAnalyzeSo;
import com.best.gwms.data.model.vo.AbcAnalyzedVo;
import com.best.gwms.data.model.vo.AbcAnalyzedWrapVo;
import com.best.gwms.data.model.webso.WebAbcAnalyzeSo;
import com.best.gwms.data.service.AbcAnalyzeQueryService;
import com.best.gwms.data.service.AbcAnalyzeService;
import com.best.gwms.data.util.CollectionUtil;
import com.best.gwms.data.util.EasyUtil;
import com.best.gwms.data.util.MathUtil;
import com.best.gwms.master.xing.BasSkuXingService;
import com.best.xingng.service.annotation.XClient;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Transactional
@Slf4j
public class AbcAnalyzeServiceImpl implements AbcAnalyzeService {
    @Autowired
    private WebSoConvertHelper convertHelper;

    @Autowired
    private AbcAnalyzedDao dao;

    @Autowired
    private AbcSettingConfigDao configDao;

    @XClient
    private BasSkuXingService skuXingService;

    @Autowired
    private MasterXingHelper masterXingHelper;

    @Autowired
    private AnalyzedVoConvertHelper voConvertHelper;

    @Override
    public AbcAnalyzedWrapVo listAnalyzedSkus(WebAbcAnalyzeSo webSo) {
        AbcAnalyzedWrapVo wrapVo = new AbcAnalyzedWrapVo();
        AbcAnalyzeSo so = (AbcAnalyzeSo) convertHelper.convert(webSo, AbcAnalyzeSo.class);
        BasCodeInfoVo sortType = masterXingHelper.getCodeInfoById(so.getSortType());
        String type = CodeConstant.CIC_SORT_IQ;
        if (sortType != null) {
            type = sortType.getCode();
        }
        BasCodeInfoVo shipped = masterXingHelper.getCodeInfoByClassCodeAndInfoCode(CodeConstant.CCC_ORDER_STAGE, CodeConstant.CIC_ORDER_STAGE_SHIPPED);
        so.setStage(shipped.getId());
        AbcAnalyzeQueryService service = AbcQueryFactory.getService(type);
        List<AbcAnalyzedVo> abcAnalyzedVoList = service.listAnalyzedSku(so);
        ClientInfo clientInfo = new ClientInfo();
        Long whId = webSo.getWhId();
        AbcSettingConfig config = configDao.getConfigByWhId(whId);
        BasWarehouseVo warehouse = masterXingHelper.getWarehouseById(whId);
        //查询仓库总sku数量
        Long total = skuXingService.listWhSkuQty(clientInfo, whId);

        //根据配置计算出临界值
        List<Integer> criticalValueList = getCriticalValueList(total, abcAnalyzedVoList, config);
        // //还需要加上没查出来的sku,查出该仓库下所有的sku，拼接其他不在条件内的sku对象，所有属性为0
        List<BasSkuVo> basSkuVos = skuXingService.listAllActiveSku(clientInfo, whId);
        List<Long> skuIdList = CollectionUtil.getPropertyListFromList(abcAnalyzedVoList, FieldConstant.SKU_ID);
        List<Long> allId = CollectionUtil.getPropertyListFromList(basSkuVos, FieldConstant.ID);
        log.info("skuId:{}",skuIdList.toString());
        log.info("all skuId:{}",allId.toString());
        basSkuVos=basSkuVos.stream().filter(o -> !skuIdList.contains(o.getId())).collect(Collectors.toList());
        AtomicReference<Integer> rank = new AtomicReference<>(abcAnalyzedVoList.size() + 1);
        List<AbcAnalyzedVo> zeroVoList = voConvertHelper.convertZeroVo(EasyUtil.isCollectionEmpty(abcAnalyzedVoList),rank, warehouse, basSkuVos);
        abcAnalyzedVoList.addAll(zeroVoList);
        wrapVo.setSkuAnalyzedVoList(abcAnalyzedVoList);
        wrapVo.setCriticalValueList(criticalValueList);
        return wrapVo;
    }


    private List<Integer> getCriticalValueList(Long totalSku, List<AbcAnalyzedVo> voList, AbcSettingConfig config) {
        //按默认
        List<Integer> list = new ArrayList<>();
        BasCodeInfoVo analyzeMethod = null;
        if (config == null) {
            analyzeMethod = masterXingHelper.getCodeInfoByClassCodeAndInfoCode(CodeConstant.CCC_ANALYZE_METHOD, CodeConstant.CIC_ANALYZE_OB_QTY);
        } else {
            analyzeMethod = masterXingHelper.getCodeInfoById(config.getAnalyzeType());
        }
        switch (analyzeMethod.getCode()) {
            case CodeConstant.CIC_ANALYZE_OB_QTY:
                computeByQty(totalSku, voList, config, list);
                break;
            case CodeConstant.CIC_ANALYZE_SKU_QTY:
                computeBySkuPercentage(totalSku, config, list);
                break;
            case CodeConstant.CIC_ANALYZE_SKU_RANK:
                computeByRank(totalSku, voList, config, list);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return list;
    }


    /**
     * 按排名也不会出现没有配置的情况
     *
     * @param skuTotalQty
     * @param voList
     * @param config
     * @param list
     */
    private void computeByRank(Long skuTotalQty, List<AbcAnalyzedVo> voList, AbcSettingConfig config, List<Integer> list) {
        boolean aFlag = true;
        boolean bFlag = true;
        boolean cFlag = true;
        boolean dFlag = true;
        Double levelA = config.getLevelA();
        Double levelB = config.getLevelB();
        Double levelC = config.getLevelC();
        Double levelD = config.getLevelD();
        Double totalLevelB = MathUtil.add(config.getLevelA(),config.getLevelB());
        Double totalLevelC = MathUtil.add(totalLevelB,config.getLevelC());
        Double totalLevelD = MathUtil.add(totalLevelC,config.getLevelD());
        for (AbcAnalyzedVo vo : voList) {

            if (MathUtil.isBiggerThanZero(levelA) && MathUtil.isBiggerOrEqualThan(Double.valueOf(vo.getRank().toString()), levelA) && aFlag) {
                list.add(vo.getRank());
                aFlag = false;
            }

            if (MathUtil.isBiggerThanZero(levelB) && MathUtil.isBiggerOrEqualThan(Double.valueOf(vo.getRank().toString()), totalLevelB) && bFlag) {
                list.add(vo.getRank());
                bFlag = false;
            }

            if (MathUtil.isBiggerThanZero(levelC) && MathUtil.isBiggerOrEqualThan(Double.valueOf(vo.getRank().toString()), totalLevelC) && cFlag) {
                if(list.size()+1==config.getDegreeAmount()){
                    list.add(skuTotalQty.intValue());
                   return;
                }else {
                    list.add(vo.getRank());
                    cFlag = false;
                }

            }
            if (MathUtil.isBiggerThanZero(levelD) && MathUtil.isBiggerOrEqualThan(Double.valueOf(vo.getRank().toString()), totalLevelD) && dFlag) {
                if(list.size()+1==config.getDegreeAmount()){
                    list.add(skuTotalQty.intValue());
                    return;
                }else {
                    list.add(vo.getRank());
                    dFlag = false;
                }
            }

            if(list.size()+1==config.getDegreeAmount()) {
                list.add(skuTotalQty.intValue());
            }
        }

        if (list.size() < config.getDegreeAmount()) {
            for (int i = 0; i < config.getDegreeAmount() - list.size(); i++) {
                list.add(skuTotalQty.intValue());
            }
        }
    }

    // 按sku 百分百就不会出现不配置的情况
    private void computeBySkuPercentage(Long totalSku, AbcSettingConfig config, List<Integer> list) {
        Double levelA = config.getLevelA();
        Double levelB = config.getLevelB();
        Double levelC = config.getLevelC();
        Double levelD = config.getLevelD();
        Double totalLevelB = MathUtil.add(config.getLevelA(),config.getLevelB());
        Double totalLevelC = MathUtil.add(totalLevelB,config.getLevelC());
        Double totalLevelD = MathUtil.add(totalLevelC,config.getLevelD());
        if (MathUtil.isBiggerThanZero(levelA)) {
            list.add((int) Math.ceil(MathUtil.multiply(totalSku, levelA)));
        }
        if (MathUtil.isBiggerThanZero(levelB)) {
            list.add((int) Math.ceil(MathUtil.multiply(totalSku, totalLevelB)));
        }

        if (MathUtil.isBiggerThanZero(levelC)) {
            if(list.size()+1==config.getDegreeAmount()){
                list.add(totalSku.intValue());
                return;
            }else {
                list.add((int) Math.ceil(MathUtil.multiply(totalSku, totalLevelC)));
            }
        }

        if (MathUtil.isBiggerThanZero(levelD)) {
            if(list.size()+1==config.getDegreeAmount()){
                list.add(totalSku.intValue());
                return;
            }else {
                list.add((int) Math.ceil(MathUtil.multiply(totalSku, totalLevelD)));
            }
        }

        if(list.size()+1==config.getDegreeAmount()) {
            list.add(totalSku.intValue());
        }


    }


    private void computeByQty(Long skuTotalQty, List<AbcAnalyzedVo> voList, AbcSettingConfig config, List<Integer> list) {
        boolean aFlag = true;
        boolean bFlag = true;
        boolean cFlag = true;
        boolean dFlag = true;
        boolean eFlag = true;
        if (null == config) {
            //没有值,全部给0
            if (EasyUtil.isCollectionEmpty(voList)) {
                for (int i = 0; i < config.getDegreeAmount(); i++) {
                    list.add(0);
                }
                return;
            }
            //默认按sku累计出库量占比，即使，选按什么排序就是排序的那些累计值加起来，如果是按sku占比，那就按查询排序的，前百分之多少的sku数量
            voList.stream().sorted(Comparator.comparing(AbcAnalyzedVo::getRank)).collect(Collectors.toList());
            for (AbcAnalyzedVo vo : voList) {
                if (MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), 0.7) && aFlag) {
                    list.add(vo.getRank());
                    aFlag = false;
                }

                if (MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), 0.9) && bFlag) {
                    list.add(vo.getRank());
                    bFlag = false;
                }
            }
            list.add(skuTotalQty.intValue());
            if (list.size() < 3) {
                for (int i = 0; i < 3 - list.size(); i++) {
                    list.add(skuTotalQty.intValue());
                }
            }
            //有配置
        } else {
            Double levelA = config.getLevelA();
            Double levelB = config.getLevelB();
            Double levelC = config.getLevelC();
            Double levelD = config.getLevelD();

            Double totalLevelB = MathUtil.add(levelA, config.getLevelB());
            Double totalLevelC = MathUtil.add(totalLevelB, config.getLevelC());
            Double totalLevelD = MathUtil.add(totalLevelC, config.getLevelD());

            for (AbcAnalyzedVo vo : voList) {

                if (MathUtil.isBiggerThanZero(levelA) && MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), levelA) && aFlag) {
                    list.add(vo.getRank());
                    aFlag = false;
                }

                if (MathUtil.isBiggerThanZero(levelB) && MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), totalLevelB) && bFlag) {
                    list.add(vo.getRank());
                    bFlag = false;
                }

                if (MathUtil.isBiggerThanZero(levelC) && MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), totalLevelC) && cFlag && list.size() < config.getDegreeAmount()) {
                    if (list.size() + 1 == config.getDegreeAmount()) {
                        list.add(skuTotalQty.intValue());
                    } else {
                        list.add(vo.getRank());
                    }
                    cFlag = false;
                }
                if (MathUtil.isBiggerThanZero(levelD) && MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), totalLevelD) && dFlag && list.size() < config.getDegreeAmount()) {
                    if (list.size() + 1 == config.getDegreeAmount()) {
                        list.add(skuTotalQty.intValue());
                    } else {
                        list.add(vo.getRank());
                    }
                    dFlag = false;
                }

                if (MathUtil.isBiggerOrEqualThan(vo.getGrandTotalPercent(), 1D) && eFlag && list.size() < config.getDegreeAmount()) {
                    list.add(skuTotalQty.intValue());
                    eFlag = false;
                }
            }
            int max=0;
            if(EasyUtil.isCollectionEmpty(voList)){
                max=config.getDegreeAmount();
            }else {
                max=config.getDegreeAmount()-list.size();
            }

            for (int i = 0; i < max; i++) {
                list.add(skuTotalQty.intValue());
            }
        }
    }


}
