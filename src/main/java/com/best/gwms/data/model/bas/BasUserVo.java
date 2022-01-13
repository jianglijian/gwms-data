package com.best.gwms.data.model.bas;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;


/**
 *
 * @author BG236820
 */
@ApiModel
public class BasUserVo extends AbstractVo {

    private static final long serialVersionUID = -1007342871412152448L;

    @ApiModelProperty(value = "用户代码，登录用", required = true)
    private String loginCode;

    // 用户名称
    private String userName;

    // 登录密码
    private String password;

    // 记录状态 // Active or Inactive
    private Long status;
    // private String statusName;

    // 默认仓库编号
    private Long defaultWhId;

    // 默认语言
    private String language;

    // 登录仓库名称
    private String warehouseName;

    // 仓库所在城市
    private String warehouseCity;

    // 仓库所在时区
    private String warehouseZone;

    // 仓库距离0时区的偏移
    private Integer warehouseOffsite = 0;

    // 岗位
    private String position;

    // 部门
    private String department;

    // 公司
    private String company;

    // 电话
    private String mobile;

    // 邮件
    private String email;

    // 是否锁定
    private Long lockState;
    // private String lockStateName;
    private String remark;

    // 类型
    private Long typeId;

    // 类型code
    private String typeCode;

    // 上次登录时间
    private String lastLoginTime;

    // 上次设置密码的时间
    private String lastSetPwdTime;
    /**
     * 用户类别 （管理员用户/普通用户）
     */
    private Long category;

    // user是否为operator
    private Boolean operator = Boolean.FALSE;

    // user,owner权限是否follow team
    private Boolean ownerPriorityFollowTeam;

    // user,taskType权限是否follow team
    private Boolean taskTypePriorityFollowTeam;

    // user,是否加入多个team
    private Boolean multipleTeam;

    // user,加入的operatorTeam
    private List<String> operatorTeams;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    @Deprecated
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getLockState() {
        return lockState;
    }

    public void setLockState(Long lockState) {
        this.lockState = lockState;
    }

    @Override
    public String toString() {
        return "BasUserVo{" + "loginCode='" + loginCode + '\'' + ", userName='" + userName + '\'' + ", status=" + status + ", defaultWhId=" + defaultWhId + ", language='" + language + '\''
                + ", warehouseName='" + warehouseName + '\'' + ", warehouseCity='" + warehouseCity + '\'' + ", warehouseZone='" + warehouseZone + '\'' + ", warehouseOffsite=" + warehouseOffsite
                + ", position='" + position + '\'' + ", department='" + department + '\'' + ", company='" + company + '\'' + ", mobile='" + mobile + '\'' + ", email='" + email + '\'' + ", lockState="
                + lockState + ", remark='" + remark + '\''+", category=" + category  + '}';
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDefaultWhId() {
        return defaultWhId;
    }

    public void setDefaultWhId(Long defaultWhId) {
        this.defaultWhId = defaultWhId;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseCity() {
        return warehouseCity;
    }

    public void setWarehouseCity(String warehouseCity) {
        this.warehouseCity = warehouseCity;
    }

    public String getWarehouseZone() {
        return warehouseZone;
    }

    public void setWarehouseZone(String warehouseZone) {
        this.warehouseZone = warehouseZone;
    }

    public Integer getWarehouseOffsite() {
        return warehouseOffsite;
    }

    public void setWarehouseOffsite(Integer warehouseOffsite) {
        this.warehouseOffsite = warehouseOffsite;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastSetPwdTime() {
        return lastSetPwdTime;
    }

    public void setLastSetPwdTime(String lastSetPwdTime) {
        this.lastSetPwdTime = lastSetPwdTime;
    }

    public Boolean getOperator() {
        return operator;
    }

    public void setOperator(Boolean operator) {
        this.operator = operator;
    }

    public Boolean getOwnerPriorityFollowTeam() {
        return ownerPriorityFollowTeam;
    }

    public void setOwnerPriorityFollowTeam(Boolean ownerPriorityFollowTeam) {
        this.ownerPriorityFollowTeam = ownerPriorityFollowTeam;
    }

    public Boolean getTaskTypePriorityFollowTeam() {
        return taskTypePriorityFollowTeam;
    }

    public void setTaskTypePriorityFollowTeam(Boolean taskTypePriorityFollowTeam) {
        this.taskTypePriorityFollowTeam = taskTypePriorityFollowTeam;
    }

    public Boolean getMultipleTeam() {
        return multipleTeam;
    }

    public void setMultipleTeam(Boolean multipleTeam) {
        this.multipleTeam = multipleTeam;
    }

    public List<String> getOperatorTeams() {
        return operatorTeams;
    }

    public void setOperatorTeams(List<String> operatorTeams) {
        this.operatorTeams = operatorTeams;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
}
