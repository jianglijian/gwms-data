package com.best.gwms.data.controller;
import com.best.gwms.data.annotation.JsonObject;
import com.best.gwms.data.model.bas.PackVo;
import com.best.gwms.data.model.vo.AbcAnalyzedWrapVo;
import com.best.gwms.data.model.webso.WebAbcAnalyzeSo;
import com.best.gwms.data.service.AbcAnalyzeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiModel(value = "ABC分类分析查询")
@Api(tags = "ANALYZE", description = "ABC分类查询")
@RestController
public class AbcAnalyzeController {
    private Logger logger = LoggerFactory.getLogger(AbcAnalyzeController.class);

    @Autowired
    private AbcAnalyzeService abcAnalyzeService;

    @ApiOperation(value = "按条件查询abc")
    @PostMapping("/abc/list")
    public PackVo<AbcAnalyzedWrapVo> list(@JsonObject WebAbcAnalyzeSo webSo) {
        logger.info("/abc/list");
        PackVo packVo = new PackVo();
        AbcAnalyzedWrapVo wrapVo=abcAnalyzeService.listAnalyzedSkus(webSo);
        packVo.setRow(wrapVo);
        return packVo;
    }


}
