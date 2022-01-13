package com.best.gwms.data.service; 
 
import com.best.gwms.data.basinterface.IBaseService; 
import com.best.gwms.data.model.so.AbcSettingConfigSo; 
import com.best.gwms.data.model.vo.AbcSettingConfigVo; 
import com.best.gwms.data.model.po.AbcSettingConfig; 
 
/**
*
* 类的描述信息
*
* @author BG236820
* @version 1.0.1
*/
public interface AbcSettingConfigService extends IBaseService<AbcSettingConfigVo, AbcSettingConfigSo>  {


    AbcSettingConfigVo getAbcConfig(Long whId);
}
