package com.best.gwms.data.dao;


import com.best.gwms.data.model.po.AbcAnalyzedPo;
import com.best.gwms.data.model.so.AbcAnalyzeSo;

import java.util.List;

/**
*
* 类的描述信息
*
* @author BG236820
* @version 1.0.1
*/
public interface AbcAnalyzedDao  {

    List<AbcAnalyzedPo> listAnalyzedSkuQty(AbcAnalyzeSo so);

    Long  getWhTotalShippedQty(AbcAnalyzeSo so);

    List<AbcAnalyzedPo> listAnalyzedSkuIkQty(AbcAnalyzeSo so);

    Long  getWhTotalShippedIk(AbcAnalyzeSo so);

}