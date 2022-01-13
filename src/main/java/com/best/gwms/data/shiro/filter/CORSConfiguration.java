package com.best.gwms.data.shiro.filter;

import com.google.common.collect.Lists;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 解决跨域过程中 option多次请求的问题
 */
@Configuration
public class CORSConfiguration {
    @Bean
    public FilterRegistrationBean corsConfigurer() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config=new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.setAllowedHeaders(Lists.newArrayList("*"));
        config.setAllowedMethods(Lists.newArrayList("*"));
         config.setMaxAge(24*3600L);
         source.registerCorsConfiguration("/**",config);

       FilterRegistrationBean bean=new FilterRegistrationBean(new CorsFilter(source)) ;
       bean.setOrder(0);
       return bean;

    }
}