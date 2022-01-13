package com.best.gwms.data.model.bas;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @descreption:
 * @author: Created by maz on 2018/2/23.
 */
public class AuthSearchObject extends SearchObject {

    /** 仓库 */
    @ApiModelProperty(value = "仓库id", required = true)
    private Long whId;

    /** 客户 */
    @ApiModelProperty("客户id")
    private Long ownerId;

    /** 查询多个仓库的业务数据 */
    @ApiModelProperty("仓库id list")
    private List<Long> whIdList;

    /** 查询多个客户的业务数据 */
    @ApiModelProperty("客户id list")
    private List<Long> ownerIdList;
    /** 是否内部系统调用 true: 内部系统调用 false:外部系统调用 */
    @ApiModelProperty("edi和job用到")
    private Boolean innerInvokeFlag = false;

    public Long getWhId() {
        return whId;
    }

    public void setWhId(Long whId) {
        this.whId = whId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getWhIdList() {
        return whIdList;
    }

    public void setWhIdList(List<Long> whIdList) {
        this.whIdList = whIdList;
    }

    public List<Long> getOwnerIdList() {
        return ownerIdList;
    }

    public void setOwnerIdList(List<Long> ownerIdList) {
        this.ownerIdList = ownerIdList;
    }

    public Boolean getInnerInvokeFlag() {
        return innerInvokeFlag;
    }

    public void setInnerInvokeFlag(Boolean innerInvokeFlag) {
        this.innerInvokeFlag = innerInvokeFlag;
    }
}
