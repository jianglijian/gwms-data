package com.best.gwms.data.model.bas;

import io.swagger.annotations.ApiModelProperty;

/**
 * @descreption: 需要有批次信息的业务对象VO需要继承这个基类
 * @author: Created by maz on 2018/2/27.
 */
public class AbstractBatchVo extends AbstractVo {
    // 货物状态（良品、不良品）
    @ApiModelProperty("货物状态id")
    private Long skuStatusId;

    // 货物状态
    @ApiModelProperty("货物状态Code")
    private String skuStatusCode;

    /** 生产日期 生产日期和失效日志不考虑时区的转化且取整到天，为了系统简单，这两个时间设置为String。 */
    @ApiModelProperty("生产日期")
    private String mfgDate;

    /** 失效日期 */
    @ApiModelProperty("失效日期")
    private String expDate;

    // 批次号
    @ApiModelProperty("批次号")
    private String batchNo;

    // 生产国家
    @ApiModelProperty("生产地国家")
    private String originCountry;

    // 自定义批次1
    @ApiModelProperty("自定义批次1")
    private String lot1;

    // 自定义批次2
    @ApiModelProperty("自定义批次2")
    private String lot2;

    // 自定义批次3
    @ApiModelProperty("自定义批次3")
    private String lot3;

    // 自定义批次4
    @ApiModelProperty("自定义批次4")
    private String lot4;

    // 自定义批次5
    @ApiModelProperty("自定义批次5")
    private String lot5;

    // 自定义批次6
    @ApiModelProperty("自定义批次6")
    private String lot6;

    public Long getSkuStatusId() {
        return skuStatusId;
    }

    public void setSkuStatusId(Long skuStatusId) {
        this.skuStatusId = skuStatusId;
    }

    public String getSkuStatusCode() {
        return skuStatusCode;
    }

    public void setSkuStatusCode(String skuStatusCode) {
        this.skuStatusCode = skuStatusCode;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getLot1() {
        return lot1;
    }

    public void setLot1(String lot1) {
        this.lot1 = lot1;
    }

    public String getLot2() {
        return lot2;
    }

    public void setLot2(String lot2) {
        this.lot2 = lot2;
    }

    public String getLot3() {
        return lot3;
    }

    public void setLot3(String lot3) {
        this.lot3 = lot3;
    }

    public String getLot4() {
        return lot4;
    }

    public void setLot4(String lot4) {
        this.lot4 = lot4;
    }

    public String getLot5() {
        return lot5;
    }

    public void setLot5(String lot5) {
        this.lot5 = lot5;
    }

    public String getLot6() {
        return lot6;
    }

    public void setLot6(String lot6) {
        this.lot6 = lot6;
    }

    public void copyBatch(AbstractBatchVo source) {
        setSkuStatusId(source.getSkuStatusId());
        setSkuStatusCode(source.getSkuStatusCode());
        setMfgDate(source.getMfgDate());
        setExpDate(source.getExpDate());
        setBatchNo(source.getBatchNo());
        setOriginCountry(source.getOriginCountry());
        setLot1(source.getLot1());
        setLot2(source.getLot2());
        setLot3(source.getLot3());
        setLot4(source.getLot4());
        setLot5(source.getLot5());
        setLot6(source.getLot6());
    }
}
