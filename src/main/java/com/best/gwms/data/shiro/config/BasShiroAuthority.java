package com.best.gwms.data.shiro.config;
import com.best.gwms.common.exception.BizException;
import com.best.gwms.common.vo.bas.BasUserVo;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.shiro.filter.CustomFormAuthenticationFilter;
import com.best.gwms.data.shiro.filter.CustomRolesAuthorizationFilter;
import com.best.gwms.data.shiro.realm.CustomRealm;
import com.best.gwms.data.shiro.redis.RedisSessionDao;
import com.best.gwms.util.EasyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.*;

/**    加载权限类       @author bl03846    @version 1.0.1 
 * SmartLifecycle 是一个接口。当Spring容器加载所有bean并完成初始化之后，会接着回调实现该接口的类中对应的方法（start()方法）。 */
@Component
public class BasShiroAuthority implements SmartLifecycle {
    private static final Log logger = LogFactory.getLog(BasShiroAuthority.class);

    // 注入的是BasShiroConfig里面重写过的bean
    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private RedisSessionDao redisSessionDao;

    @Autowired
    private CustomRealm basBestRealm;

    @Autowired
    private CustomRolesAuthorizationFilter customRolesAuthorizationFilter;

    @Autowired
    private CustomFormAuthenticationFilter customFormAuthenticationFilter;

    /**
     * Start this component.
     *
     * <p>Should not throw an exception if the component is already running.
     *
     * <p>In the case of a container, this will propagate the start signal to all components that
     * apply.
     *
     * @see SmartLifecycle#isAutoStartup()
     */
    @Override
    public void start() {
        // 重新加载过滤连
        try {
            reloadAuthority();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /** 重新加载权限信息 */
    private void reloadAuthority() {
        AbstractShiroFilter shiroFilter = null;
        try {
            shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        } catch (Exception e) {
            throw new BizException("get ShiroFilter from shiroFilterFactoryBean error!");
        }

        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

        // 清空老的权限控制
        manager.getFilterChains().clear();

        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(createFilterChainDefinitionMap());
        // 重新构建生成
        Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
        for (Map.Entry<String, String> entry : chains.entrySet()) {
            String url = entry.getKey();
            String chainDefinition = entry.getValue().trim().replace(" ", "");
            manager.createChain(url, chainDefinition);
        }

        // 重设角色验证过滤器
        Map<String, Filter> filterMap = shiroFilterFactoryBean.getFilters();
        if (filterMap == null) {
            filterMap = new HashMap<>();
        }
        filterMap.remove("customRolesAuthorizationFilter");
        filterMap.put("roles", customRolesAuthorizationFilter);
        filterMap.put(FieldConstant.AUTHC, customFormAuthenticationFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        // 清理用户权限
        clearUserAuth();
    }

    /**
     * 创建权限关联信息
     *
     * @return
     */
    private LinkedHashMap<String, String> createFilterChainDefinitionMap() {
        // 功能权限，暂时没有做功能权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 登录退出，放开授权
        filterChainDefinitionMap.put(FieldConstant.LOGOUT_URL, "anon");
        // 登录，需要授权
        filterChainDefinitionMap.put(FieldConstant.LOGIN_URL, FieldConstant.AUTHC);
        // 登录异常，获取验证码图片，放开授权
        filterChainDefinitionMap.put(FieldConstant.VERIFICATION_IMG_URL, "anon");
        filterChainDefinitionMap.put(FieldConstant.GOMS_ATTACHMENT_DOWN_URL, "anon");
        // 手持端在登录前就会获取apk的版本以做更新校验，所以要放开授权
        filterChainDefinitionMap.put(FieldConstant.GET_VERSION_CONFIG, "anon");
        //网络校验接口
        filterChainDefinitionMap.put("/bas/net/test", "anon");
        //查询有效域接口
        filterChainDefinitionMap.put("/bas/active/domain/code", "anon");
        // swagger配置
        filterChainDefinitionMap.put("/monitor**", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/swagger-resources/configuration/ui", "anon");
        filterChainDefinitionMap.put("/swagger-resources", "anon");
        filterChainDefinitionMap.put("/swagger**", "anon");
        //放开xingng功能
        filterChainDefinitionMap.put("/xingng-services/**", "anon");

        // 重置密码配置
        filterChainDefinitionMap.put(FieldConstant.CHECK_LOGIN_CODE_URL, "anon");
        filterChainDefinitionMap.put(FieldConstant.SEND_EMAIL_URL, "anon");

        filterChainDefinitionMap.put("/configuration/*", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/swagger*", "anon");
        filterChainDefinitionMap.put("/**/*.jpg", "anon");
        filterChainDefinitionMap.put("/**/*.png", "anon");
        filterChainDefinitionMap.put("/**/*.js", "anon");
        filterChainDefinitionMap.put("/**/*.css", "anon");
        filterChainDefinitionMap.put("/**/*.ico", "anon");
        filterChainDefinitionMap.put("/**/*.css.map", "anon");
        filterChainDefinitionMap.put("/**/*.gif", "anon");
        filterChainDefinitionMap.put("/**/*.svg", "anon");
        filterChainDefinitionMap.put("/**/*.ttf", "anon");
        filterChainDefinitionMap.put("/**/*.html", "anon");
        filterChainDefinitionMap.put("/bas/excel/exportExcelTemplate", "anon");
        filterChainDefinitionMap.put("/bas/location/loctmpExport", "anon");
        filterChainDefinitionMap.put("/bas/location/locExport", "anon");
        //测试全部拿掉
        filterChainDefinitionMap.put("/**", FieldConstant.AUTHC);
        return filterChainDefinitionMap;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    /**
     * Stop this component, typically in a synchronous fashion, such that the component is fully
     * stopped upon return of this method. Consider implementing {@link SmartLifecycle} and its {@code
     * stop(Runnable)} variant when asynchronous stop behavior is necessary.
     *
     * <p>Note that this stop notification is not guaranteed to come before destruction: On regular
     * shutdown, {@code Lifecycle} beans will first receive a stop notification before the general
     * destruction callbacks are being propagated; however, on hot refresh during a context's lifetime
     * or on aborted refresh attempts, only destroy methods will be called.
     *
     * <p>Should not throw an exception if the component isn't started yet.
     *
     * <p>In the case of a container, this will propagate the stop signal to all components that
     * apply.
     *
     * @see SmartLifecycle#stop(Runnable)
     * @see DisposableBean#destroy()
     */
    @Override
    public void stop() {
        //
    }

    /**
     * Indicates that a Lifecycle component must stop if it is currently running.
     *
     * <p>The provided callback is used by the {@link } to support an ordered, and
     * potentially concurrent, shutdown of all components having a common shutdown order value. The
     * callback <b>must</b> be executed after the {@code SmartLifecycle} component does indeed stop.
     *
     * <p>The {@link } will call <i>only</i> this variant of the {@code stop}
     * method; i.e. {@link #stop()} will not be called for {@code SmartLifecycle}
     * implementations unless explicitly delegated to within the implementation of this method.
     *
     * @param callback
     * @see #stop()
     * @see #getPhase()
     */
    @Override
    public void stop(Runnable callback) {
        //
    }

    /**
     * 获取激活状态的session信息
     *
     * @return
     */
    private List<SimplePrincipalCollection> getSimplePrincipalCollection() {

        // 获取所有session
        Collection<Session> sessions = redisSessionDao.getActiveSessions();
        // 定义返回
        List<SimplePrincipalCollection> list = new ArrayList<>();
        for (Session session : sessions) {
            // 获取SimplePrincipalCollection
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (obj instanceof SimplePrincipalCollection) {
                // 强转
                SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
                // 判断用户，匹配用户ID。
                obj = spc.getPrimaryPrincipal();
                if (obj instanceof BasUserVo) {

                    // 比较用户ID，符合即加入集合

                    list.add(spc);
                }
            }
        }
        return list;
    }

    /** 清空缓存中用户权限 */
    private void clearUserAuth() {
        List<SimplePrincipalCollection> principalCollectionList = getSimplePrincipalCollection();
        if (EasyUtil.isCollectionEmpty(principalCollectionList)) {
            return;
        }
        for (SimplePrincipalCollection simplePrincipalCollection : principalCollectionList) {
            basBestRealm.clearCachedAuthorizationInfo(simplePrincipalCollection);
        }
    }

    /**
     * Check whether this component is currently running.
     *
     * <p>In the case of a container, this will return {@code true} only if <i>all</i> components that
     * apply are currently running.
     *
     * @return whether the component is currently running
     */
    @Override
    public boolean isRunning() {
        return false;
    }

    /** Return the phase value of this object. */
    @Override
    public int getPhase() {
        return FieldConstant.BEST_PHASE + 1;
    }
}
