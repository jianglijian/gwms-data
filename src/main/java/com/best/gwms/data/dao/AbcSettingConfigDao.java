package com.best.gwms.data.dao; 
import com.best.gwms.data.basinterface.IBasDao;
import com.best.gwms.data.model.so.AbcSettingConfigSo; 
import com.best.gwms.data.model.po.AbcSettingConfig; 
/**
*
* 类的描述信息
*
* @author BG236820
* @version 1.0.1
*/
public interface AbcSettingConfigDao extends IBasDao<AbcSettingConfig, AbcSettingConfigSo> {
    AbcSettingConfig getConfigByWhId(Long whId);

    void deleteByWhId(Long whId);
}