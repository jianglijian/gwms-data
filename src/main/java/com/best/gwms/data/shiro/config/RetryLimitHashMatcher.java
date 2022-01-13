package com.best.gwms.data.shiro.config;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.best.gwms.data.shiro.redis.RedisClient;
import com.best.gwms.master.xing.BasUserXingService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Package : com.best.gwms.web.shiro @Author : Shen.Ziping[zpshen@best-inc.com] @Date : 2018/1/31
 * 16:15 " @Version : V1.0
 */
public class RetryLimitHashMatcher extends HashedCredentialsMatcher {
    @Autowired
    private RedisClient redisClient;
    private static final Log log = LogFactory.get();
    private BasUserXingService basUserXingService;

    public RetryLimitHashMatcher()  {
        //
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        boolean matches = super.doCredentialsMatch(token, info);

        log.info("using RetryLimitSimpleMatcher to execute authentication");
        String key = redisClient.buildKey(new String[] {RedisClient.PREFIX_LOGIN_FAIL_COUNT, token.getPrincipal() + ""});
        // 修改密码错误次数的key
        String pwdKey = redisClient.buildKey(new String[] {RedisClient.PREFIX_CHANG_PW_FAIL_COUNT, token.getPrincipal() + ""});
        log.info("lock key=" + key);
        log.info("chang password key=" + key);

        if (matches) {
            log.info("matched, remove the counter");
            redisClient.removeValueByKey(key);
            // 删除修改密码的错误次数
            redisClient.removeValueByKey(pwdKey);
        } else {

            Object countInCache = redisClient.getObject(key);
            log.info("query redis for lock count :" + countInCache);
            Integer loginCount = 0;
            if (null == countInCache) {
                log.info("loginCount is null");
                loginCount = 0;
            } else {
                loginCount = Integer.valueOf(countInCache.toString());
            }
            loginCount++;
            log.info("failed again, increase the counter to " + loginCount + " for user " + token.getPrincipal());
            redisClient.setObject(key, loginCount.toString());
            log.info("updated to redis");
        }
        return matches;
    }

    public BasUserXingService getBasUserXingService() {
        return basUserXingService;
    }

    public void setBasUserXingService(BasUserXingService basUserXingService) {
        this.basUserXingService = basUserXingService;
    }
}
