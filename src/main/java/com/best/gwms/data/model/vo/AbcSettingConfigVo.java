package com.best.gwms.data.model.vo;

import com.best.gwms.data.model.bas.AbstractVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类的描述信息
 *
 * @author BG236820
 * @version 1.0.1
 */
@ApiModel("配置对象")
@Data
public class AbcSettingConfigVo extends AbstractVo {

    private static final long serialVersionUID = -1L;
    @ApiModelProperty("仓库id")
    private Long whId;
    @ApiModelProperty("a级别")
    private String levelA;
    @ApiModelProperty("b级别")
    private String levelB;
    @ApiModelProperty("c级别")
    private String levelC;
    @ApiModelProperty("d级别")
    private String levelD;
    @ApiModelProperty("e级别")
    private String levelE;
    @ApiModelProperty("分析方式CIC")
    private Long analyzeType;
    @ApiModelProperty("配置对象")
    private Integer degreeAmount;

    @ApiModelProperty("仓库总共的sku数量")
    private Integer total;


}
