package com.best.gwms.data.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.best.gwms.data.annotation.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class JsonArgumentResolver implements HandlerMethodArgumentResolver {
    public static final Logger logger = LoggerFactory.getLogger(JsonArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonObject.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        try {
            JSONObject para = getRequestInfo(nativeWebRequest);
            Class<?> type = methodParameter.getParameterType();
            String name = methodParameter.getParameterName();
            // 获取参数注解JsonObject,如果为空，则使用参数名，不为空，则使用注解中的value
            Annotation[] annotations = methodParameter.getParameterAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof JsonObject) {
                    String value = ((JsonObject) annotation).value();
                    if (StringUtils.isNotEmpty(value)) {
                        name = value;
                    }
                }
            }
            if (null != para && para.containsKey(name)) {
                if (String.class.equals(type)) {
                    return para.getString(name);
                }
                if (List.class.equals(type)) {
                    String typeName = methodParameter.getGenericParameterType().getTypeName();
                    typeName = typeName.substring(typeName.indexOf("<") + 1);
                    typeName = typeName.substring(0, typeName.length() - 1);

                    return JSON.parseArray(para.getString(name), Class.forName(typeName));
                }
                return JSON.parseObject(para.getString(name), type);
            }
        } catch (Exception e) {

            logger.info("", e);
        }
        return null;
    }

    private JSONObject getRequestInfo(NativeWebRequest webRequest) throws IOException {
        JSONObject para = new JSONObject();
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String method = httpServletRequest.getMethod();
        if (!method.equals("GET") && !method.equals("DELETE")) {

            if (null != httpServletRequest.getAttribute("para")) {
                try {
                    para = JSON.parseObject(httpServletRequest.getAttribute("para").toString());
                } catch (Exception e) {

                    logger.info("", e);
                }
            } else {
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = httpServletRequest.getReader();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                httpServletRequest.setAttribute("para", buffer.toString());

                try {
                    para = JSON.parseObject(buffer.toString());
                } catch (Exception e) {

                    logger.info("", e);
                }
            }
        } else {
            Map<String, String[]> parameterMap = webRequest.getParameterMap();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String values = StringUtils.join(entry.getValue());
                para.put(key, values);
            }
        }
        return para;
    }
}
