package com.best.gwms.data.helper;

import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.common.constant.FieldConstant;
import com.best.gwms.common.po.bas.*;
import com.best.gwms.common.vo.bas.*;
import com.best.gwms.data.basinterface.ISecurity;
import com.best.gwms.data.shiro.redis.RedisClient;
import com.best.gwms.data.util.CodeCache;
import com.best.gwms.data.util.DozerBeanUtil;
import com.best.gwms.data.util.EasyUtil;
import com.best.gwms.master.xing.*;
import com.best.xingng.service.annotation.XClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @descreption:为了减少business xingng调用Master Data的次数（可能导致xingng日志的暴增），提供一个helper类，逻辑为： 1 先去缓存redis中获取
 * 2 获取不到或报异常才通过xingng服务调用 3 获取到则直接返回
 * @author: Created by maz on 2018/3/5.
 */
@Component
public class MasterXingHelper {
    public static final Logger logger = LoggerFactory.getLogger(MasterXingHelper.class);
    private static String PREFIX = "GWAYBILL";

    private static String SPLIT = "_";
    @Autowired
    RedisClient redisClient;

    @XClient
    private BasWarehouseXingService basWarehouseXingService;

    @XClient
    private BasSkuXingService basSkuXingService;

    @XClient
    private BasPackingMaterialXingService basPackingMaterialXingService;

    @XClient
    private BasOwnerXingService basOwnerXingService;

    @XClient
    private BasCodeXingService basCodeXingService;

    @XClient
    private BasUserXingService basUserXingService;

    @XClient
    private BasPackagingXingService basPackagingXingService;

    @XClient
    private BasBarCodeXingService basBarCodeXingService;

    @XClient
    private BasActivityTypeXingService basActivityTypeXingService;

    @XClient
    private BasSkuCategoryXingService basSkuCategoryXingService;

    @XClient
    private WaybillRequestXingService waybillRequestXingService;

    @XClient
    private BasDomainXingService basDomainXingService;
    @Autowired
    private SpringBeanHelper beanUtils;
    @Autowired
    private ISecurity security;


    // 根据id获取codeinfo
    public BasUserVo getUserById(Long id) {
        String key = PREFIX + SPLIT + "BasUser" + SPLIT + id;
        Object object = null;
        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        long st2 = System.currentTimeMillis();
        if (object == null) {
            logger.info("basUserXingService:userId:" + id);
            BasUserVo user = basUserXingService.getUser( new ClientInfo(), id);
            long st4 = System.currentTimeMillis();
            logger.info("getUserById  phases cost time:" + (st4 - st2));
            return user;
        }
        BasUser po = (BasUser) object;
        BasUserVo convert = DozerBeanUtil.convert(po, BasUserVo.class);
        return convert;
    }

    // 根据CCC和CIC获取codeinfo
    public BasCodeInfoVo getCodeInfoByClassCodeAndInfoCode(String codeClassCode, String codeInfoCode) {
        if (!EasyUtil.isStringEmpty(codeInfoCode)) {
            codeInfoCode = codeInfoCode.toUpperCase();
        }
        String key = codeClassCode + SPLIT + codeInfoCode;
        Object object = null;

        if (CodeCache.getCcc_cicMap().containsKey(key)) {
            object = CodeCache.getCcc_cicMap().get(key);
            return (BasCodeInfoVo) object;
        }

        // 获取不到或者报了异常，则调用xingng来获取
        ClientInfo gwmsClient= security.getClientInfo();
                object = basCodeXingService.getByClassCodeAndInfoCode(gwmsClient, codeClassCode, codeInfoCode);
        if (object == null) {
            return null;
        }
        BasCodeInfoVo rlt = (BasCodeInfoVo) object;
        // 同时放到内存中
        CodeCache.getCcc_cicMap().put(key, rlt);
        return rlt;
    }



    /**
     * @param
     * @return
     */
    public BasUserVo getLoginUser(String userCode) {
       /* logger.info("getLoginUser:{}",userCode);
        String key = PREFIX + SPLIT + "BasUser" + SPLIT + userCode+domainId; // 组装key
        Object object = null;
        try {
            logger.info("redisClient:{}",redisClient);
            object = redisClient.getObject(key); // 先去redis获取
            logger.info("redisClient object:{}",object);
        } catch (Exception e) {
            logger.info("redisClient Exception {}",e.getMessage());
            logger.error("", e);
        }
        long st2 = System.currentTimeMillis();
        if (object == null) { // 获取不到或者报了异常，则调用xingng来获取*/
        logger.info("basUserXingService:userId:" + userCode);
        BasUserVo user = basUserXingService.getByUserCodeForShiro(userCode);
        long st4 = System.currentTimeMillis();
           /* logger.info("getUserById  phases cost time:" + (st4 - st2));
            redisClient.setObject(key, user);*/
        return user;


    }

    /**
     * @param
     * @return
     */
    public List<BasResourceNodeVo> getUserResource(Long userId, String deviceType) {
        ClientInfo clientInfo = security.getClientInfo();
        logger.info("listWaybillUserResource start:[{}],deviceType[{}]", userId, deviceType);
        ClientInfo gwmsClient=new ClientInfo();
        beanUtils.copyProperties(clientInfo,gwmsClient);
        List<BasResourceNodeVo> basResourceNodeVos = waybillRequestXingService.listWaybillUserResource(gwmsClient, userId, deviceType);
        logger.info("listWaybillUserResource end:=resource size:[{}]" + basResourceNodeVos.size());
        return basResourceNodeVos;

    }



    public BasBarCodeVo getBarCodeById(Long id) {
        String key = PREFIX + SPLIT + "BasBarCode" + SPLIT + id;
        Object object = null;

        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (object == null) {
            ClientInfo clientInfo = security.getClientInfo();
            return basBarCodeXingService.getBarCodeById(clientInfo, id);
        }

        BasBarCode po = (BasBarCode) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasBarCodeVo.class);
    }

    // 根据id获取warehouse
    public BasWarehouseVo getWarehouseById(Long id) {
        String key = PREFIX + SPLIT + "BasWarehouse" + SPLIT + id;
        Object object = null;

        // 先去redis获取
        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        // 获取不到或者报了异常，则调用xingng来获取
        if (object == null) {
            object = basWarehouseXingService.getWarehouse(security.getClientInfo(), id);

        }

        if (object instanceof BasWarehouseVo) {
            return (BasWarehouseVo) object;
        }
        BasWarehouse po = (BasWarehouse) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasWarehouseVo.class);

    }

    // 根据id获取domain
    public BasDomainVo getDomainById(Long id) {
        String key = PREFIX + FieldConstant.UNDER_LINE + "BasDomain" + FieldConstant.UNDER_LINE + id;
        Object object = null;

        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (object == null) {
            return basDomainXingService.getDomainById(security.getClientInfo(), id);
        }

        BasUser po = (BasUser) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasDomainVo.class);
    }

    // 根据id获取sku
    public BasSkuVo getSkuById(Long id) {
        String key = PREFIX + SPLIT + "BasSku" + SPLIT + id;
        Object object = null;
        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (object == null) {
            ClientInfo clientInfo = security.getClientInfo();
            return basSkuXingService.getSku(clientInfo, id);
        }

        BasSku po = (BasSku) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasSkuVo.class);
    }

    public BasSkuCategoryVo getSkuCategoryById(Long id) {
        String key = PREFIX + SPLIT + "BasSkuCategory" + SPLIT + id;
        Object object = null;

        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (object == null) {
            ClientInfo clientInfo = security.getClientInfo();
            return basSkuCategoryXingService.getById(clientInfo, id);
        }

        BasSkuCategory po = (BasSkuCategory) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasSkuCategoryVo.class);
    }

    // 根据id获取owner
    public BasOwnerVo getOwnerById(Long id) {
        String key = PREFIX + SPLIT + "BasOwner" + SPLIT + id;
        Object object = null;

        try {
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (object == null) {
            ClientInfo clientInfo = security.getClientInfo();
            return basOwnerXingService.getOwner(clientInfo, id);
        }

        BasOwner po = (BasOwner) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasOwnerVo.class);
    }

    // 根据id获取codeclass
    public BasCodeClassVo getCodeClassById(Long id) {
        Object object = null;

        if (CodeCache.getCccMap().containsKey(id)) {
            object = CodeCache.getCccMap().get(id);
            return (BasCodeClassVo) object;
        }

        // 获取不到，则调用xingng来获取
        object = basCodeXingService.getCodeClassById(security.getClientInfo(), id);
        if (object == null) {
            return null;
        }

        BasCodeClassVo rlt = (BasCodeClassVo) object;
        // 同时放到内存中
        CodeCache.getCccMap().put(id, rlt);
        return rlt;
    }

    // 根据id获取codeinfo
    public BasCodeInfoVo getCodeInfoById(Long id) {
        Object object = null;

        if (CodeCache.getCicMap().containsKey(id)) {
            object = CodeCache.getCicMap().get(id);
            return (BasCodeInfoVo) object;
        }

        // 获取不到，则调用xingng来获取
        ClientInfo clientInfo = security.getClientInfo();
        object = basCodeXingService.getCodeInfoById(clientInfo, id);
        if (object == null) {
            return null;
        }

        BasCodeInfoVo rlt = (BasCodeInfoVo) object;
        // 同时放到内存中
        CodeCache.getCicMap().put(id, rlt);
        return rlt;
    }




    public BasPackagingVo getPackageById(Long id) {
        // 组装key
        String key = PREFIX + SPLIT + "BasPackaging" + SPLIT + id;
        Object object = null;

        try {
            // 先去redis获取
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        // 获取不到或者报了异常，则调用xingng来获取
        if (object == null) {
            ClientInfo clientInfo = security.getClientInfo();
            return basPackagingXingService.getById(clientInfo, id);
        }

        BasPackaging po = (BasPackaging) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasPackagingVo.class);
    }

    /**
     * 查询包装耗材
     *
     * @param id
     * @return
     */
    public BasPackingMaterialVo getPackingMaterial(Long id) {
        // 组装key
        String key = PREFIX + SPLIT + "BasPackingMaterial" + SPLIT + id;
        Object object = null;

        try {
            // 先去redis获取
            object = redisClient.getObject(key);
        } catch (Exception e) {
            logger.error("", e);
        }
        // 获取不到或者报了异常，则调用xingng来获取
        if (object == null) {
            ClientInfo clientInfo = security.getClientInfo();
            return basPackingMaterialXingService.getMaterial(clientInfo, id);
        }

        BasPackingMaterial po = (BasPackingMaterial) object;
        return com.best.gwms.util.DozerBeanUtil.convert(po, BasPackingMaterialVo.class);
    }




}
