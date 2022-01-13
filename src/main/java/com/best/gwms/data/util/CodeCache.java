package com.best.gwms.data.util;

import com.best.gwms.common.vo.bas.BasCodeClassVo;
import com.best.gwms.common.vo.bas.BasCodeInfoVo;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @descreption:
 * @author
 */
public class CodeCache {
    protected static final Map<Long, BasCodeInfoVo> cicMap = Maps.newHashMap();
    protected static final Map<Long, BasCodeClassVo> cccMap = Maps.newHashMap();
    protected static final Map<String, BasCodeInfoVo> ccc_cicMap = Maps.newHashMap();

//    protected static final Map<String, CodeInfoVo> gwbCicMap = Maps.newHashMap();

    private CodeCache() {}

    public static Map<Long, BasCodeInfoVo> getCicMap() {
        return cicMap;
    }

    public static Map<Long, BasCodeClassVo> getCccMap() {
        return cccMap;
    }

    public static Map<String, BasCodeInfoVo> getCcc_cicMap() {
        return ccc_cicMap;
    }

//    public static Map<String, CodeInfoVo> getGwbCicMap() {
//        return gwbCicMap;
//    }
}
