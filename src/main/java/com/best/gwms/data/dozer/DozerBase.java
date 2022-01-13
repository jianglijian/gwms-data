package com.best.gwms.data.dozer;

import com.best.gwms.common.vo.bas.BasUserVo;
import com.best.gwms.data.basinterface.ISecurity;
import com.best.gwms.data.helper.MasterXingHelper;
import com.best.gwms.data.model.bas.AbstractPo;
import com.best.gwms.data.model.bas.AbstractVo;
import com.best.gwms.data.util.DateGen;
import com.best.gwms.data.util.DateTimeZoneUtil;
import com.best.gwms.data.util.DozerBeanUtil;
import com.best.gwms.data.util.EasyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;


public class DozerBase<B, T> {
    private static final Logger logger = LoggerFactory.getLogger(DozerBase.class);
    @Autowired
    protected ISecurity security;

    @Autowired
    MasterXingHelper masterXingHelper;
    HashMap<Long, BasUserVo> userMap = new HashMap();



    protected DateTimeFormatter getFormatter() {

        return DateTimeFormatter.ofPattern(DateGen.COMMON_DATE_FORMAT_STR);
    }

    /**
     * 复制同名的数据域！
     *
     * @param base
     * @param target
     * @return
     */
    protected <B, T> T convert(B base, Class<T> target) {
        return DozerBeanUtil.convert(base, target);
    }

    protected <B, T> List<T> convertList(List<B> baseList, Class<T> target) {
        return DozerBeanUtil.convertList(baseList, target);
    }

    /**
     * 转换一些基类上的字段
     *
     * @param po
     * @param vo
     */
    public void convertAbstractPo2AbstractVo(AbstractPo po, AbstractVo vo) {
        if (po == null || vo == null) {
            return;
        }
        Long creatorId = po.getCreatorId();
        if (creatorId != null) {
            BasUserVo user = userMap.get(creatorId);
            if (EasyUtil.isObjectsNull(user)) {
                logger.info("user is null============="+creatorId);
                user = masterXingHelper.getUserById(creatorId);
                userMap.put(creatorId, user);
            }
            if (user != null) {
                vo.setCreatorId(user.getId());
                vo.setCreatorName(user.getUserName());
            }
        }
        Long updatorId = po.getUpdatorId();
        if (updatorId != null) {
            BasUserVo user = userMap.get(updatorId);
            if (EasyUtil.isObjectsNull(user)) {
                logger.info("user is null============="+updatorId);
                user = masterXingHelper.getUserById(updatorId);
                userMap.put(updatorId, user);
            }
            if (user != null) {
                vo.setUpdatorId(user.getId());
                vo.setUpdatorName(user.getUserName());
            }
        }
        String tz = security.getTimeZone();
        if (po.getCreatedTime() != null) {
            vo.setCreatedTime(DateTimeZoneUtil.zdt2String(po.getCreatedTime(), tz));

        }
        if (po.getUpdatedTime() != null) {
            vo.setUpdatedTime(DateTimeZoneUtil.zdt2String(po.getUpdatedTime(), tz));
        }

    }

    public void convertAbstractVo2AbstractPo(AbstractVo vo, AbstractPo po) {

        if (po == null || vo == null) {
            return;
        }

        String tz = null;//security.getTimeZone();

        if (EasyUtil.isStringNotEmpty(vo.getCreatedTime())) {

            po.setCreatedTime(DateTimeZoneUtil.string2ZDT(vo.getCreatedTime(), tz));
        }
        if (EasyUtil.isStringNotEmpty(vo.getUpdatedTime())) {

            po.setUpdatedTime(DateTimeZoneUtil.string2ZDT(vo.getUpdatedTime(), tz));
        }
    }


}
