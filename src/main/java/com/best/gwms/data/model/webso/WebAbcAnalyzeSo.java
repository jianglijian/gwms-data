package com.best.gwms.data.model.webso;

import com.best.gwms.data.model.bas.WebSearchObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * ABC 分类查询 前端查询对象
 *
 * @author BG236820
 */
@ApiModel(value = "ABC 分类查询 前端查询对象")
@Data
public class WebAbcAnalyzeSo extends WebSearchObject {

    @ApiModelProperty("仓库id,登录账号拥有权限的仓库")
    private Long whId;
    @ApiModelProperty("货主,登录账号拥有权限的可用货主")
    private List<Long> ownerIdList;
    @ApiModelProperty("单据类型,B2C、B2B、General、Disposal、Transfer、FBA 、VAS、Standard")
    private Long orderTypeId;
    @ApiModelProperty("商品分类,选中仓库可用状态的Item Category，未选中仓库时无数据展示")
    private Long skuCategoryId;
    @ApiModelProperty("发运方式,Freight、Parcel、Disposal、Will Call、Routing")
    private Long shippingTypeId;
    @ApiModelProperty("订单优先级,Low、Normal、Urgent、Crimal")
    private Long priority;
    @ApiModelProperty("排序方式,订货数量IQ、订货次数IK、订货体积IV，默认选中订货数量IQ")
    private Long sortType;

}
