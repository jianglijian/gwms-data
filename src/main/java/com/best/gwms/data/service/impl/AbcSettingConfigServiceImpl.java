package com.best.gwms.data.service.impl;

import com.best.gwms.data.dao.AbcSettingConfigDao;
import com.best.gwms.data.dozer.Dozer4AbcSettingConfig;
import com.best.gwms.data.helper.SpringBeanHelper;
import com.best.gwms.data.model.po.AbcSettingConfig;
import com.best.gwms.data.service.AbcSettingConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.best.gwms.data.model.so.AbcSettingConfigSo;
import com.best.gwms.data.model.vo.AbcSettingConfigVo;

/**
 * 类的描述信息
 *
 * @author BG236820
 * @version 1.0.1
 */
@Transactional
@Service
@Slf4j
public class AbcSettingConfigServiceImpl implements AbcSettingConfigService {
    @Autowired
    private AbcSettingConfigDao dao;

    @Autowired
    private Dozer4AbcSettingConfig dozer;

    @Autowired
    private SpringBeanHelper beanHelper;


    @Override
    public AbcSettingConfigVo getPoById(Long id) {
        return dozer.convertPo2Vo(dao.getPoById(id));
    }

    @Override
    public List<AbcSettingConfigVo> listPoByIdList(List<Long> idList) {
        return dozer.convertPoList2VoList(dao.getPoByIdList(idList));
    }

    @Override
    public AbcSettingConfigVo createPo(AbcSettingConfigVo vo) {
        Long whId = vo.getWhId();
        log.info("whId:{}", whId);
        AbcSettingConfig newConfig = dozer.convertVo2Po(vo);
        AbcSettingConfig config = dao.getConfigByWhId(whId);
        if (config != null) {
            dao.deleteByWhId(whId);
        }
        dao.createPo(newConfig);
        return dozer.convertPo2Vo(newConfig);
    }

    @Override
    public void deletePo(Long id) {
        dao.deletePo(id);
    }


    @Override
    public List<AbcSettingConfigVo> searchPo(AbcSettingConfigSo so) {
        return dozer.convertPoList2VoList(dao.searchPo(so));
    }

    @Override
    public Long countResult(AbcSettingConfigSo so) {
        return dao.countResult(so);
    }

    @Override
    public AbcSettingConfigVo updatePo(AbcSettingConfigVo vo) {
        AbcSettingConfig newObj = dozer.convertVo2Po(vo);
        AbcSettingConfig oldObj = dao.getPoById(vo.getId());
        beanHelper.copyPropertiesIgnoreNull(newObj, oldObj);
        dao.updatePo(oldObj);
        return dozer.convertPo2Vo(oldObj);
    }

    @Override
    public AbcSettingConfigVo getAbcConfig(Long whId) {
        return dozer.convertPo2Vo(dao.getConfigByWhId(whId));
    }
}
