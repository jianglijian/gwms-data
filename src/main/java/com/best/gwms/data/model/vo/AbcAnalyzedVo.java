package com.best.gwms.data.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author BG236820
 */
@ApiModel("sku 分析结果对象信息")
@Data
public class AbcAnalyzedVo {
    @ApiModelProperty("sku id")
    private Long skuId;

    @ApiModelProperty("sku编码")
    private String skuCode;

    @ApiModelProperty("仓库id")
    private Long whId;

    @ApiModelProperty("仓库编码")
    private String whCode;

    @ApiModelProperty("排名")
    private Integer rank;

    @ApiModelProperty("客户id")
    private Long ownerId;

    @ApiModelProperty("客户编码")
    private String ownerCode;

    @ApiModelProperty("sku category类别")
    private Long category;

    @ApiModelProperty("sku category类别名称")
    private String categoryName;

    @ApiModelProperty("skuIQ数量，某个sku的总量，按不同维度统计出来的总量，按IQ/IK/IV")
    private Double skuQty;

    @ApiModelProperty("sku占比，该sku占总量的百分比,按不同维度统计出来的总量，按IQ/IK/IV")
    private Double percentage;

    @ApiModelProperty("所有sku总数量，按不同维度统计出来的总量，按IQ/IK/IV")
    private Double totalQty;

    @ApiModelProperty("累计占比，排名到该sku为止占比的累加，包含该sku的占比")
    private Double grandTotalPercent;


}
