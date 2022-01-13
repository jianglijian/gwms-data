package com.best.gwms.data.model.bas;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package : com.best.gwms.common.vo @Author : Shen.Ziping[zpshen@best-inc.com] @Date : 2018/1/18
 * 16:54 " @Version : V1.0
 */
public class PackVo<T> {

    // 返回成功or失败
    @ApiModelProperty("成功 or 失败")
    private boolean success = true;

    // 异常信息
    @ApiModelProperty("异常信息（可以多条）")
    private List<CodeMessage> message = new ArrayList<>();

    // 返回数据，多条记录放在rows里
    @ApiModelProperty("返回数据(多条)")
    private List<T> rows = new ArrayList();

    // 返回数据，一条记录放在row里
    @ApiModelProperty("返回数据（一条）")
    private T row;

    // 记录行数,分页的话显示的是总的行数
    @ApiModelProperty("记录行数")
    private Long totalCount;

    // 备注
    @ApiModelProperty("备注")
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public T getRow() {
        return row;
    }

    public void setRow(T row) {
        this.row = row;
    }

    public PackVo() {}

    public void addRow(T o) {
        this.rows.add(o);
    }

    public void addMsg(String code, String msg) {
        this.message.add(new CodeMessage(code, msg));
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CodeMessage> getMessage() {
        return message;
    }

    public void setMessage(List<CodeMessage> message) {
        this.message = message;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void setSuccessMessage() {
        ArrayList list = new ArrayList();
        list.add("operation success!");
        setMessage(list);
    }
}
