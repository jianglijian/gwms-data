package com.best.gwms.data.model.bas;

import com.best.gwms.data.util.EasyUtil;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class SearchObject implements Serializable {
    // 前台不传pageNo和pageSize，就不会分页

    @ApiModelProperty("创建时间From")
    private ZonedDateTime createTimeFrom;

    @ApiModelProperty("创建时间To")
    private ZonedDateTime createTimeTo;

    @ApiModelProperty("更新时间From")
    private ZonedDateTime updateTimeFrom;

    @ApiModelProperty("更新时间To")
    private ZonedDateTime updateTimeTo;
    // 前台不传pageNo和pageSize，就不会分页
    @ApiModelProperty("第几页")
    private Integer pageNo;

    @ApiModelProperty("每页记录数")
    private Integer pageSize;

    @ApiModelProperty("域id")
    private Long domainId;
    /** 排序，ex： id desc */
    @ApiModelProperty("排序")
    private String orderBy;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        // 由于前端传递的值为驼峰字符串，所以需要转化成下划线
        // 例如前端传递的是locCode desc,通过该方法转换成loc_code desc
        if (!EasyUtil.isStringEmpty(orderBy)) {
            StringBuilder sb = new StringBuilder();
            String[] parts = orderBy.split(",");
            for (String part : parts) {
                sb.append(EasyUtil.toHibField(part) + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            this.orderBy = sb.toString();
        }
    }

    public ZonedDateTime getCreateTimeFrom() {
        return createTimeFrom;
    }

    public void setCreateTimeFrom(ZonedDateTime createTimeFrom) {
        this.createTimeFrom = createTimeFrom;
    }

    public ZonedDateTime getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(ZonedDateTime createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public ZonedDateTime getUpdateTimeFrom() {
        return updateTimeFrom;
    }

    public void setUpdateTimeFrom(ZonedDateTime updateTimeFrom) {
        this.updateTimeFrom = updateTimeFrom;
    }

    public ZonedDateTime getUpdateTimeTo() {
        return updateTimeTo;
    }

    public void setUpdateTimeTo(ZonedDateTime updateTimeTo) {
        this.updateTimeTo = updateTimeTo;
    }

}
