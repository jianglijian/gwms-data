package com.best.gwms.data.dozer;

import java.util.List;
import java.util.ArrayList;

import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.data.util.EasyUtil;
import com.best.gwms.data.dozer.DozerBase;
import com.best.gwms.master.xing.BasSkuXingService;
import com.best.xingng.service.annotation.XClient;
import org.springframework.stereotype.Component;
import com.best.gwms.data.model.vo.AbcSettingConfigVo;
import com.best.gwms.data.model.po.AbcSettingConfig;

/**
 * 类的描述信息
 *
 * @author BG236820
 * @version 1.0.1
 */
@Component
public class Dozer4AbcSettingConfig extends DozerBase<AbcSettingConfig, AbcSettingConfigVo> {

    private static final long serialVersionUID = -1L;
    @XClient
    private BasSkuXingService skuXingService;

    public AbcSettingConfig convertVo2Po(AbcSettingConfigVo vo) {
        if (vo == null) {
            return null;
        }
        AbcSettingConfig po = this.convert(vo, AbcSettingConfig.class);
        return po;
    }

    public AbcSettingConfigVo convertPo2Vo(AbcSettingConfig po) {
        if (po == null) {
            return null;
        }
        AbcSettingConfigVo vo = this.convert(po, AbcSettingConfigVo.class);
        // 转换基类里面的字段
        super.convertAbstractPo2AbstractVo(po, vo);
        Long aLong = skuXingService.listWhSkuQty(new ClientInfo(), po.getWhId());
        vo.setTotal(aLong.intValue());
        return vo;
    }

    public List<AbcSettingConfig> convertVoList2PoList(List<AbcSettingConfigVo> voList) {
        List<AbcSettingConfig> rltList = new ArrayList<>();
        if (EasyUtil.isCollectionEmpty(voList)) {
            return rltList;
        }
        for (AbcSettingConfigVo vo : voList) {
            AbcSettingConfig po = this.convertVo2Po(vo);
            if (po != null) {
                rltList.add(po);
            }
        }
        return rltList;
    }

    public List<AbcSettingConfigVo> convertPoList2VoList(List<AbcSettingConfig> poList) {
        List<AbcSettingConfigVo> rltList = new ArrayList<>();
        if (EasyUtil.isCollectionEmpty(poList)) {
            return rltList;
        }
        for (AbcSettingConfig po : poList) {
            AbcSettingConfigVo vo = this.convertPo2Vo(po);
            if (vo != null) {
                rltList.add(vo);
            }
        }
        return rltList;
    }
}
