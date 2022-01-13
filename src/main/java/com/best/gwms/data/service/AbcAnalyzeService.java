package com.best.gwms.data.service;

import com.best.gwms.data.model.vo.AbcAnalyzedWrapVo;
import com.best.gwms.data.model.webso.WebAbcAnalyzeSo;

/**
*
* 类的描述信息
*
* @author BG236820
* @version 1.0.1
*/
public interface AbcAnalyzeService   {
    AbcAnalyzedWrapVo listAnalyzedSkus(WebAbcAnalyzeSo so);
}
