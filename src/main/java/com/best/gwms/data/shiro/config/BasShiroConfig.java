package com.best.gwms.data.shiro.config;

import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.shiro.filter.CustomFormAuthenticationFilter;
import com.best.gwms.data.shiro.realm.CustomRealm;
import com.best.gwms.data.shiro.redis.RedisSessionDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;

/**
 * shiro 核心类
 * spring 起来时注入
 */
@Configuration
public class BasShiroConfig {
    private static final Log logger = LogFactory.getLog(BasShiroConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.ssl}")
    private boolean ssl;

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        //shiro升級到1.6.0後，cookie的samesite会默认置为Lax，导致跨域的问题。显式的置为null，不可置为SameSiteOptions.NONE。
        sessionManager.getSessionIdCookie().setSameSite(null);
        sessionManager.setSessionDAO(redisSessionDAO());
        // session全局过期时间--4小时
        sessionManager.setGlobalSessionTimeout(4 * 3600 * 1000L);
        sessionManager.setCacheManager(cacheManager());
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    /**
     * cacheManager 缓存 redis实现 使用的是shiro-redis开源插件
     *
     * @return
     */
    private RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDao redisSessionDAO() {
        RedisSessionDao redisSessionDAO = new RedisSessionDao();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 配置shiro redisManager 使用的是shiro-redis开源插件
     *
     * @return
     */
    private RedisManager redisManager() {
        logger.info("shiro redis connect");

        ShiroRedisManager redisManager = new ShiroRedisManager(host);
        redisManager.setHost(host);
        redisManager.setPort(port);
        // redis配置缓存过期时间
        redisManager.setExpire(3600 * 24 * 7);
        redisManager.setTimeout(timeout);
        redisManager.setPassword(password);
        redisManager.setSsl(ssl);
        return redisManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }


    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。 注意：单独一个ShiroFilterFactoryBean配置是或报错的，在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * spring注入起来后实例化这个bean（重写了ShiroFilterFactoryBean），BasShiroAuthority里面注入的是这个重写的bean
     *
     * <p>Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     *
     * <p>anon:所有url都都可以匿名访问;
     *
     * <p>authc: 需要认证才能进行访问;
     *
     * <p>user:配置记住我或认证通过可以访问；
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 获取filters
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 将自定义 的FormAuthenticationFil
        filters.put("authc", new CustomFormAuthenticationFilter());
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login"页面
        shiroFilterFactoryBean.setLoginUrl(FieldConstant.LOGIN_URL);

        /** 未授权界面;*/
        shiroFilterFactoryBean.setUnauthorizedUrl(FieldConstant.UNAUTHORIZED_URL);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        customRealm.setCacheManager(cacheManager());
        /** customRealm.setCredentialsMatcher(retryLimitSimpleMatcher()); */
        customRealm.setCredentialsMatcher(retryLimitHashMatcher());
        return customRealm;
    }

    /**
     * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了 所以我们需要修改下doGetAuthenticationInfo中的代码; ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        /** 散列算法:这里使用MD5算法;*/
        hashedCredentialsMatcher.setHashAlgorithmName(FieldConstant.HASH_ALGORITHM_NAME);
        /** 散列的次数，比如散列两次，相当于 md5(md5(""));*/
        hashedCredentialsMatcher.setHashIterations(FieldConstant.HASH_ITERATIONS);
        return hashedCredentialsMatcher;
    }

    @Bean
    public RetryLimitHashMatcher retryLimitHashMatcher() {
        RetryLimitHashMatcher hashMatcher = new RetryLimitHashMatcher();
        /** 散列算法:这里使用MD5算法; */
        hashMatcher.setHashAlgorithmName(FieldConstant.HASH_ALGORITHM_NAME);
        /**  散列的次数，比如散列两次，相当于 md5(md5(""));*/
        hashMatcher.setHashIterations(FieldConstant.HASH_ITERATIONS);
        return hashMatcher;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
/*    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }*/
}
