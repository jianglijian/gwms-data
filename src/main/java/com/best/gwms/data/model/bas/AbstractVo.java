package com.best.gwms.data.model.bas;

import com.best.gwms.data.annotation.DateExport;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Package : com.best.gwms.common.po
 * @Author : Shen.Ziping[zpshen@best-inc.com]
 * @Date : 2018/1/18 12:49
 * "
 * @Version : V1.0
 **/
public abstract class AbstractVo implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("创建时间")
    @DateExport
    private String createdTime;

    @ApiModelProperty("更新时间")
    @DateExport
    private String updatedTime;

    @ApiModelProperty("域id")
    private Long domainId;

    @ApiModelProperty("域名称")
    private String domainName;

    @ApiModelProperty("创建人id")
    private Long creatorId;

    @ApiModelProperty("更新人id")
    private Long updatorId;

    @ApiModelProperty("创建人")
    private String creatorName;

    @ApiModelProperty("更新人")
    private String updatorName;

    @ApiModelProperty("版本号")
    private Long lockVersion = 0L;

    @ApiModelProperty("用户自定义1")
    private String udf1;

    @ApiModelProperty("用户自定义2")
    private String udf2;

    @ApiModelProperty("用户自定义3")
    private String udf3;

    @ApiModelProperty("用户自定义4")
    private String udf4;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
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

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
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
