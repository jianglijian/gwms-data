package com.best.gwms.data.model.bas;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @descreption: 需要有批次信息的业务对象PO需要继承这个基类
 * @author: Created by maz on 2018/2/27.
 */
public class AbstractBatchPo extends AbstractPo {

    /** 货物状态（良品、不良品） */
    private Long skuStatusId;

    /** 货物状态code */
    private String skuStatusCode;

    /** 生产日期 生产日期和失效日志不考虑时区的转化且取整到天，为了系统简单，这两个时间设置为String。 */
    private transient LocalDate mfgDate;

    /** 失效日期 */
    private transient LocalDate expDate;
                                                                              
    /** 批次号 */
    private String batchNo;

    /** 生产国家 */
    private String originCountry;

    /** 自定义批次1 */
    private String lot1;

    /** 自定义批次2 */
    private String lot2;

    /** 自定义批次3 */
    private String lot3;

    /** 自定义批次4 */
    private String lot4;

    /** 自定义批次5 */
    private String lot5;

    /** 自定义批次6 */
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

    public LocalDate getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(LocalDate mfgDate) {
        this.mfgDate = mfgDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractBatchPo)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AbstractBatchPo that = (AbstractBatchPo) o;
        return Objects.equals(getSkuStatusId(), that.getSkuStatusId()) && Objects.equals(getSkuStatusCode(), that.getSkuStatusCode()) && Objects.equals(getMfgDate(), that.getMfgDate())
                && Objects.equals(getExpDate(), that.getExpDate()) && Objects.equals(getBatchNo(), that.getBatchNo()) && Objects.equals(getOriginCountry(), that.getOriginCountry())
                && Objects.equals(getLot1(), that.getLot1()) && Objects.equals(getLot2(), that.getLot2()) && Objects.equals(getLot3(), that.getLot3()) && Objects.equals(getLot4(), that.getLot4())
                && Objects.equals(getLot5(), that.getLot5()) && Objects.equals(getLot6(), that.getLot6());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getSkuStatusId(), getSkuStatusCode(), getMfgDate(), getExpDate(), getBatchNo(), getOriginCountry(), getLot1(), getLot2(), getLot3(), getLot4(), getLot5(),
                getLot6());
    }
}
