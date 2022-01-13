package com.best.gwms.data.model.bas;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * @Package : com.best.gwms.common.po
 * @Author : Shen.Ziping[zpshen@best-inc.com]
 * @Date : 2018/1/18 12:44
 * "
 * @Version : V1.0
 **/
public abstract class AbstractPo implements Serializable {

    private static final long serialVersionUID = 2657952505536208991L;
    private ZonedDateTime createdTime;
    private ZonedDateTime updatedTime;
    private Long id;
    private Long domainId;
    private Long creatorId;
    private Long updatorId;
    private Long lockVersion = 0L;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(ZonedDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    // 两个对象id一样，即认为equal
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!getClass().isInstance(obj)) {
            return false;
        }

        AbstractPo other = (AbstractPo) obj;

        Long id1 = getId();
        Long id2 = other.getId();
        if (id1 == null) {
            return false;
        } else {
            return id1.equals(id2);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + "[id=" + getId() + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdatorId() {
        return updatorId;
    }

    public void setUpdatorId(Long updatorId) {
        this.updatorId = updatorId;
    }

    public Long getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Long lockVersion) {
        this.lockVersion = lockVersion;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }
}
