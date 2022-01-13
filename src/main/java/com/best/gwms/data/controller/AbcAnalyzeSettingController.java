package com.best.gwms.data.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.best.gwms.data.annotation.JsonObject;
import com.best.gwms.data.model.bas.PackVo;
import com.best.gwms.data.model.vo.AbcSettingConfigVo;
import com.best.gwms.data.service.AbcSettingConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiModel(value = "abc分类查询设置")
@Api(tags = "SETTING", description = "abc分类分析设置")
@RestController
public class AbcAnalyzeSettingController {
    private Logger logger = LoggerFactory.getLogger(AbcAnalyzeSettingController.class);

    @Autowired
    private AbcSettingConfigService configService;


    @ApiOperation(value = "查询仓库abc分析配置")
    @PostMapping("/abc/setting/list")
    public PackVo<AbcSettingConfigVo> list(@JsonObject Long whId) {
        logger.info("/abc/setting/list");
        PackVo packVo = new PackVo();
        AbcSettingConfigVo vo = configService.getAbcConfig(whId);
        packVo.setRow(vo);
        return packVo;
    }


    @ApiOperation(value = "保存abc分析配置")
    @PostMapping("/abc/setting/createOrUpdate")
    public PackVo createOrUpdate(@JsonObject AbcSettingConfigVo vo) {
        logger.info("/abc/setting/createOrUpdate");
        PackVo packVo = new PackVo();
        AbcSettingConfigVo vo1 = configService.createPo(vo);
        packVo.setRow(vo1);
        return packVo;
    }

    public static void main(String[] args) {
        AbcSettingConfigVo vo= new AbcSettingConfigVo();
        System.out.println(JSON.toJSONString(vo,SerializerFeature.WriteMapNullValue));
    }

}
