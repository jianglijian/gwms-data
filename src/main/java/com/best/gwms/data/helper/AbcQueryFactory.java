package com.best.gwms.data.helper;

import com.best.gwms.data.service.AbcAnalyzeQueryService;
import com.best.gwms.data.util.EasyUtil;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author BG236820
 */
@Component
public class AbcQueryFactory {
    private static Map<String, AbcAnalyzeQueryService> strategyMap = Maps.newHashMap();

    public static AbcAnalyzeQueryService getService(String analyzedType) {
        return strategyMap.get(analyzedType);
    }

    public static void register(String analyzedType, AbcAnalyzeQueryService service) {
        if (EasyUtil.isStringEmpty(analyzedType) || service == null) {
            return;
        }
        strategyMap.put(analyzedType, service);
    }

}


