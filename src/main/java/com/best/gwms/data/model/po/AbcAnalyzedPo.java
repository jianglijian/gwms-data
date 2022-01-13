package com.best.gwms.data.model.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;


/**
 * @author BG236820
 */
@ApiModel("sku 分析结果对象信息")
@Data
public class AbcAnalyzedPo implements Serializable {
    private static final long serialVersionUID = 48732161896975267L;
    @ApiModelProperty("sku id")
    private Long skuId;

    @ApiModelProperty("仓库id")
    private Long whId;

    @ApiModelProperty("排名")
    private Integer rank;

    @ApiModelProperty("客户id")
    private Long ownerId;

    @ApiModelProperty("skuIQ数量，某个sku的总量，按不同维度统计出来的总量，按IQ/IK/IV")
    private Double skuQty;

}
