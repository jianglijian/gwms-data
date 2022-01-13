package com.best.gwms.data.service;


import com.best.gwms.data.model.so.AbcAnalyzeSo;
import com.best.gwms.data.model.vo.AbcAnalyzedVo;
import com.best.gwms.data.model.vo.AbcAnalyzedWrapVo;
import com.best.gwms.data.model.webso.WebAbcAnalyzeSo;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
*
* 类的描述信息
*
* @author BG236820
* @version 1.0.1
*/
public interface AbcAnalyzeQueryService extends InitializingBean{
    List<AbcAnalyzedVo> listAnalyzedSku(AbcAnalyzeSo webSo);
}
