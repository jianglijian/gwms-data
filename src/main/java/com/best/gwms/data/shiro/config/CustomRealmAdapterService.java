package com.best.gwms.data.shiro.config;

import com.best.gwms.master.xing.BasCodeXingService;
import com.best.gwms.master.xing.BasDomainXingService;
import com.best.gwms.master.xing.BasUserXingService;
import com.best.xingng.service.annotation.XClient;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * xingng专用类，用以解决shiro与xingng启动顺序问题
 */
@Service
public class CustomRealmAdapterService implements Serializable {

    @XClient(failover = true)
    private BasUserXingService userXingService;

    @XClient(failover = true)
    private BasCodeXingService codeXingService;

    @XClient(failover = true)
    private BasDomainXingService domainXingService;

    public BasUserXingService getUserXingService() {
        return userXingService;
    }

    public BasCodeXingService getCodeXingService() {
        return codeXingService;
    }

    public BasDomainXingService getDomainXingService() {
        return domainXingService;
    }
}
