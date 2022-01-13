package com.best.gwms.data.model.po;

import com.best.gwms.data.model.bas.AbstractPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author BG236820
 */
@ApiModel
@Data
public class AbcSettingConfig extends AbstractPo {
    private static final long serialVersionUID = -4572942944371841616L;
    /**
     *仓库id
     */
    private Long whId;
    @ApiModelProperty("abc 分类级别A")
    private Double levelA;
    @ApiModelProperty("abc 分类级别B")
    private Double levelB;
    @ApiModelProperty("abc 分类级别C")
    private Double levelC;
    @ApiModelProperty("abc 分类级别D")
    private Double levelD;
    @ApiModelProperty("abc 分类级别E")
    private Double levelE;
    /**
     * 分析方式，CIC
     */
    private Long analyzeType;

    /**
     * 等级数量
     */
    private Integer   degreeAmount;

}
