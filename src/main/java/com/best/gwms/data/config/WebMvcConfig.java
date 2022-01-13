package com.best.gwms.data.config;

import java.util.List;
import java.util.Locale;

import com.best.gwms.data.interceptor.SessionInterceptor;
import com.best.gwms.data.resolver.JsonArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
 
/*    @Value("${file.userfiles-path}")
    private String filePath;*/

    private String[] excludePaths = {
            "/userLogin", "/userLogout", "/swagger-ui.html",
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/swagger-resources",
            "/delP",
            "/addP",
            "/configuration/*",
            "/v2/api-docs",
            "/swagger*",
            "/**/*.jpg",
            "/**/*.png",
            "/**/*.js",
            "/**/*.css",
            "/**/*.ico",
            "/**/*.css.map",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.ttf",
            "/**/*.html"
    };



/*    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }*/

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        // 默认语言
        slr.setDefaultLocale(Locale.US);
        return slr;
    }


    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        lci.setParamName("lang");
        return lci;
    }



    /**
     * 登录校验拦截器
     *
     * @return
     */
    @Bean
    public SessionInterceptor loginRequiredInterceptor() {
        return new SessionInterceptor();
    }
 

    /**
     * 参数解析器
     *
     * @param argumentResolvers
     */
    @Override
    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new JsonArgumentResolver());
    }


    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(localeChangeInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(excludePaths);
        super.addInterceptors(registry);
    }


/*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/static/page/")
                .addResourceLocations("classpath:/static/templates/")
                .addResourceLocations("file:" + filePath);
    }*/
 
   /* @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
        fastJsonConfig.setSerializeFilters((ValueFilter) (o, s, source) -> {
            if (null != source && (source instanceof Long || source instanceof BigInteger) && source.toString().length() > 15) {
                return source.toString();
            } else {
                return null == source ? EMPTY : source;
            }
        });
 
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }*/
}