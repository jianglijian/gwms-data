/*
 * EntityUtil.java
 *
 * @author bl03846 Created on 2016/12/26 11:23 版本 修改时间 修改内容 V1.0.1 2016/12/26  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.helper;

import com.best.gwms.data.model.po.AbcSettingConfig;
import com.best.gwms.data.model.po.BasPo;
import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**    根据指定的po结构生成对应的so,vo,orika,mapper,dao,daoimpl       @author bl03846    @version 1.0.1  */
public class Business_CodeGenerator {
    /** *************************************************************** */
    /** *************************************************************** */
    /** *****************必填参数********************************* */
    /** *************************************************************** */
    /** *************************************************************** */
    static AbcSettingConfig checkInterfaceConfigInfo = new AbcSettingConfig();

    public static List<String> excludeFidldList = new ArrayList<String>();
    /**
     * 生成文件时，若文件已存在，当delExistsFileFlag=true时，删除已存在的文件，并重新生成文件。
     * 若文件已存在，当delExistsFileFlag=false时，不删除文件，并报错
     */
    public static Boolean delExistsFileFlag = true;

    /** *****************以下参数不修改**************************************** */
    public static String projectPath = null;

    public static String voPath = null;
    public static String soPath = null;
    public static String webSoPath = null;

    public static String dozerPath = null;
    // mybatis 配置文件路径
    public static String mapperPath = null;

    // dao层路径
    public static String daoPath = null;
    // service层路径
    public static String servicePath = null;
    // serviceImpl层路径 + "\\service";
    public static String serviceImplPath = null;
    // controller
    public static String controllerPath = null;

    static String tableName = null;

    /**
     * 一键生成vo,packvo,orika,dao,daoimpl 各个参数不能为空
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        projectPath = getProjectRoot();

        voPath = projectPath + getVoPath(checkInterfaceConfigInfo);
        soPath = projectPath + getSoPath(checkInterfaceConfigInfo);
        webSoPath = projectPath + getWebSoPath(checkInterfaceConfigInfo);
        dozerPath = projectPath + getDozerPath(checkInterfaceConfigInfo);
        mapperPath = projectPath + "\\gwms-data\\src\\main\\resources\\config\\mybatis\\mappers";
        daoPath = projectPath + getDaoPath(checkInterfaceConfigInfo);
        servicePath = projectPath + getServicePath(checkInterfaceConfigInfo);
        serviceImplPath = projectPath + getServiceImplPath(checkInterfaceConfigInfo);
        controllerPath = projectPath + getControllerPath(checkInterfaceConfigInfo);

        tableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, checkInterfaceConfigInfo.getClass().getSimpleName());

        System.out.println(getCurrentModuleRoot());
        Business_CodeGenerator.delExistsFileFlag = true;

        // 创建各种对象
        // 创建vo
        String voPackagePath = createVo(checkInterfaceConfigInfo, voPath);
        // 创建so
        String soPackagePath = createSo(checkInterfaceConfigInfo, soPath);
        String webSoPackagePath = createWebSo(checkInterfaceConfigInfo, webSoPath);
        System.out.println("so created!!!");
        // dozer
        createDozer(checkInterfaceConfigInfo, voPackagePath, dozerPath);
        System.out.println("orika created!!!");

        // 创建dao
        String daoPackage = createDao(checkInterfaceConfigInfo, daoPath, soPackagePath);
        System.out.println("dao created!!!");
        // 创建mapper
        createMapper(checkInterfaceConfigInfo, tableName, mapperPath, daoPackage);
        // // 创建daoimpl
        // createDaoImpl(checkInterfaceConfigInfo, daoPath, soPackagePath);
        // System.out.println("daoimpl created!!!");

        // 创建service
        String servicePackage = createService(checkInterfaceConfigInfo, servicePath, soPackagePath, voPackagePath);
        createServiceImpl(checkInterfaceConfigInfo, serviceImplPath, soPackagePath, servicePackage, voPackagePath);

        // @createController
        // createController(checkInterfaceConfigInfo, controllerPath);

        // 建表语句
        cteateSql(checkInterfaceConfigInfo.getClass(), tableName);
    }

    /**
     * 获取当前module的根路径
     *
     * @return
     */
    public static String getCurrentModuleRoot() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        path = path.substring(0, path.lastIndexOf("target/"));
        return path;
    }

  

    /**
     * 获取当前project的根目录
     *
     * @return
     */
    public static String getProjectRoot() {
        String path = getCurrentModuleRoot();
        path = path.substring(0, path.substring(0, path.length() - 1).lastIndexOf("/") + 1);

        path = path.replace("\\", "/");
        if (!path.endsWith("/")) {
            path += "/";
        }
        return path;
    }

    /**
     * 创建service层
     *
     * @param obj
     * @param daoPath
     * @param soPackagePath
     */
    public static String createService(Object obj, String daoPath, String soPackagePath, String voPackagePath) throws Exception {

        daoPath = daoPath.replace("\\", "/");
        String packageName = getPackage(daoPath);

        String className = obj.getClass().getSimpleName() + "Service";
        String soName = obj.getClass().getSimpleName() + "So";
        String poName = obj.getClass().getSimpleName();
        String voName = poName + "Vo";
        if (!daoPath.endsWith("/")) {
            daoPath += "/";
        }
        daoPath += className + ".java";
        File file = new File(daoPath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + daoPath + " 已存在,请删除后重试!");
        }
        // 写入文件
        // 构建类信息
        // 头信息

        String content = "package " + packageName + ";" + " \n";
        content += " \n";
        content += "import com.best.gwms.data.basinterface.IBaseService;" + " \n";
        content += "import " + soPackagePath + ";" + " \n";
        content += "import " + voPackagePath + ";" + " \n";
        content += "import " + obj.getClass().getPackage().getName() + "." + obj.getClass().getSimpleName() + ";" + " \n";
        content += " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";

        content += "public interface " + className + " extends IBaseService<" + voName + ", " + soName + ">  {";

        content += "                                    " + "\n";

        content += "" + "\n";
        content += "" + "\n";
        content += "}" + "\n";

        File newFile = new File(daoPath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);

        System.out.println(packageName);
        return packageName;
    }

    /**
     * 创建service层
     *
     * @param obj
     * @param daoPath
     * @param soPackagePath
     */
    public static void createServiceImpl(Object obj, String daoPath, String soPackagePath, String servicePath, String voPackagePath) throws Exception {

        daoPath = daoPath.replace("\\", "/");
        String packageName = getPackage(daoPath);

        String parent = obj.getClass().getSimpleName() + "Service";
        String className = parent + "Impl";
        String soName = obj.getClass().getSimpleName() + "So";
        String poName = obj.getClass().getSimpleName();
        String voName = poName + "Vo";

        if (!daoPath.endsWith("/")) {
            daoPath += "/";
        }
        daoPath += className + ".java";
        File file = new File(daoPath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + daoPath + " 已存在,请删除后重试!");
        }
        // 将 daoimpl 写入文件
        // 构建类信息
        // 头信息

        String content = "package " + packageName + ";" + " \n";
        content += " \n";
        content += "import " + servicePath + "." + parent + ";" + " \n";
        content += "import org.springframework.stereotype.Service;\n";
        content += "import org.springframework.transaction.annotation.Transactional;\n";
        content += "import java.util.List;" + " \n";
        content += "import " + soPackagePath + ";" + " \n";
        content += "import " + voPackagePath + ";" + " \n";
        content += "import " + obj.getClass().getPackage().getName() + "." + obj.getClass().getSimpleName() + ";" + " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";

        content += "@Transactional\n";
        content += "@Service\n";
        content += "public class " + className + " implements " + parent + "  {\n";

        content += "   /**    " + "\n";
        content += "    * logger   " + "\n";
        content += "    */" + "\n";
        content += "   //private static final Log logger = LogFactory.getLog(" + className + ".class);" + "\n";
        content += "                                    " + "\n";
        content += "    @Override   " + "\n";
        content += "    public " + voName + " getPoById(Long id){ return null;}\n";
        content += "\n";
        content += "    @Override   " + "\n";
        content += "    public   List<" + voName + "> listPoByIdList(List<Long> idList){ return null;}\n";
        content += "\n";
        content += "    @Override   " + "\n";
        content += "    public   " + voName + " createPo(" + voName + " po){ return null;}\n";
        content += "\n";
        content += "    @Override   " + "\n";
        content += "    public   void deletePo(Long id){}\n";
        content += "\n";
        // content += " public void deletePo(AbstractPo po){}\n";
        content += "\n";
        content += "    @Override   " + "\n";
        content += "    public   List<" + voName + "> searchPo(" + soName + " so){ return null;}\n";
        content += "\n";
        content += "    @Override   " + "\n";
        content += "    public   Long countResult(" + soName + " so){ return null;}\n";
        content += "\n";
        content += "    @Override   " + "\n";
        content += "    public   " + voName + " updatePo(" + voName + " po){return null;}";

        content += "" + "\n";
        content += "" + "\n";
        content += "}" + "\n";

        File newFile = new File(daoPath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);
        System.out.println(packageName);
    }

    /**
     * 创建dao层
     *
     * @param obj
     * @param daoPath
     * @param soPackagePath
     */
    public static String createDao(Object obj, String daoPath, String soPackagePath) throws Exception {

        daoPath = daoPath.replace("\\", "/");
        String packageName = getPackage(daoPath);
        String className = obj.getClass().getSimpleName() + "Dao";
        String soName = obj.getClass().getSimpleName() + "So";
        if (!daoPath.endsWith("/")) {
            daoPath += "/";
        }
        daoPath += className + ".java";
        File file = new File(daoPath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + daoPath + " 已存在,请删除后重试!");
        }
        // 将 vo 写入文件
        // 构建类信息
        // 头信息
        String content = "package " + packageName + ";" + " \n";
        content += "import java.util.List;" + " \n";
        content += "import java.util.Map;" + " \n";
        content += "import com.best.gwms.data.basinterface.IBasDao;" + " \n";
        content += "import " + soPackagePath + ";" + " \n";
        content += "import " + obj.getClass().getPackage().getName() + "." + obj.getClass().getSimpleName() + ";" + " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";
        content += "public interface " + className + " extends IBasDao<" + obj.getClass().getSimpleName() + ", " + soName + "> {   }";
        File newFile = new File(daoPath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);

        System.out.println(packageName);
        return packageName + "." + className;
    }

    /**
     * 创建mapper
     *
     * @param obj
     * @param tablName
     * @param
     * @param mapperPath
     * @throws ClassNotFoundException
     */
    public static void createMapper(Object obj, String tablName, String mapperPath, String nameSpace) throws Exception {

        MapperHelper helper = new MapperHelper(obj.getClass().getPackage().getName() + "." + obj.getClass().getSimpleName(), tablName, nameSpace);
        mapperPath = mapperPath.replace("\\", "/");
        if (!mapperPath.endsWith("/")) {
            mapperPath = mapperPath + "/";
        }
        mapperPath += obj.getClass().getSimpleName() + "Mapper.xml";
        File file = new File(mapperPath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + mapperPath + " 已存在,请删除后重试!");
        }
        File newFile = new File(mapperPath);
        Files.createParentDirs(newFile);
        Files.write(helper.createMapper().getBytes(), newFile);
    }

    /**
     * 创建orika
     *
     * @param
     * @param voPackagePath vo 对应的包路径
     * @param orikaPath orika的物理路径
     */
    public static void createDozer(Object obj, String voPackagePath, String orikaPath) throws Exception {
        orikaPath = orikaPath.replace("\\", "/");
        String packageName = getPackage(orikaPath);

        String poName = obj.getClass().getSimpleName();
        String voName = poName + "Vo";
        String orikaName = "Dozer4" + poName;

        String filePath = null;
        if (orikaPath.endsWith("/")) {
            filePath = orikaPath + orikaName + ".java";
        } else {
            filePath = orikaPath + "/" + orikaName + ".java";
        }
        File file = new File(filePath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + filePath + " 已存在,请删除后重试!");
        }
        // 将 so 写入文件
        // 构建类信息
        // 头信息
        String content = "package " + packageName + ";" + " \n";
        content += "import java.util.List;" + " \n";
        content += "import java.util.ArrayList;" + " \n";
        content += "import com.best.gwms.data.util.EasyUtil;" + " \n";
        content += "import com.best.gwms.data.dozer.DozerBase;" + " \n";
        content += "import  org.springframework.stereotype.Component;" + " \n";
        content += "import " + voPackagePath + ";" + " \n";
        content += "import " + obj.getClass().getPackage().getName() + "." + poName + ";" + " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";
        content += "@Component" + "\n";

        content += "public class " + orikaName + " extends  DozerBase<" + poName + "," + voName + ">  {" + "\n";

        content += "" + "\n";
        content += "    private static final long serialVersionUID = -1L;" + "\n";
        content += "" + "\n";
        content += "    public " + poName + " convertVo2Po( " + voName + " vo) {" + "\n";
        content += "        if (vo == null) {" + "\n";
        content += "            return null;" + "\n";
        content += "        }" + "\n";
        content += "        " + poName + " po = this.convert(vo, " + poName + ".class);" + "\n";
        content += "        return po;" + "\n";
        content += "    }" + "\n";
        content += "" + "\n";
        content += "    public " + voName + " convertPo2Vo(" + poName + " po) {" + "\n";
        content += "        if (po == null) {" + "\n";
        content += "            return null;" + "\n";
        content += "        }" + "\n";
        content += "        " + voName + " vo = this.convert(po, " + voName + ".class);" + "\n";
        content += "        // 转换基类里面的字段" + "\n";
        content += "        super.convertAbstractPo2AbstractVo(po, vo);" + "\n";
        content += "        return vo;" + "\n";
        content += "    }" + "\n";

        content += "    public List<" + poName + "> convertVoList2PoList(List<" + voName + "> voList) {" + "\n";
        content += "        List<" + poName + "> rltList = new ArrayList<>();" + "\n";
        content += "        if (EasyUtil.isCollectionEmpty(voList)) {" + "\n";
        content += "           return rltList;" + "\n";
        content += "        }" + "\n";
        content += "        for (" + voName + " vo : voList) {" + "\n";
        content += "           " + poName + " po = this.convertVo2Po(vo);" + "\n";
        content += "            if (po != null) {" + "\n";
        content += "               rltList.add(po);" + "\n";
        content += "            }" + "\n";
        content += "        }" + "\n";
        content += "        return rltList;" + "\n";
        content += "    }\n";

        content += "    public List<" + voName + "> convertPoList2VoList(List<" + poName + "> poList) {" + "\n";
        content += "        List<" + voName + "> rltList = new ArrayList<>();" + "\n";
        content += "        if (EasyUtil.isCollectionEmpty(poList)) {" + "\n";
        content += "           return rltList;" + "\n";
        content += "        }" + "\n";
        content += "        for (" + poName + " po : poList) {" + "\n";
        content += "           " + voName + " vo = this.convertPo2Vo(po);" + "\n";
        content += "            if (vo != null) {" + "\n";
        content += "               rltList.add(vo);" + "\n";
        content += "            }" + "\n";
        content += "        }" + "\n";
        content += "        return rltList;" + "\n";
        content += "    }" + "\n";
        content += "}" + "\n";

        File newFile = new File(filePath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);

        System.out.println(packageName);
    }

    /**
     * 创建so
     *
     * @param obj
     * @param soPath
     */
    public static String createSo(Object obj, String soPath) throws Exception {
        soPath = soPath.replace("\\", "/");
        String packageName = getPackage(soPath);

        String poName = obj.getClass().getSimpleName();
        String soName = poName + "So";

        String filePath = null;
        if (soPath.endsWith("/")) {
            filePath = soPath + soName + ".java";
        } else {
            filePath = soPath + "/" + soName + ".java";
        }
        File file = new File(filePath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + filePath + " 已存在,请删除后重试!");
        }
        // 将 so 写入文件
        // 构建类信息
        // 头信息
        String content = "package " + packageName + ";" + " \n";
        content += "import com.best.gwms.data.model.bas.SearchObject;" + " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";
        content += " public class " + soName + " extends  SearchObject  {" + "\n";

        content += "" + "\n";
        content += "    private static final long serialVersionUID = -1L;" + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "}" + "\n";
        File newFile = new File(filePath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);

        System.out.println(packageName);
        return packageName + "." + soName;
    }

    public static String createWebSo(Object obj, String webSoPath) throws Exception {
        webSoPath = webSoPath.replace("\\", "/");
        String packageName = getPackage(webSoPath);

        String poName = obj.getClass().getSimpleName();
        String soName = "Web" + poName + "So";

        String filePath = null;
        if (webSoPath.endsWith("/")) {
            filePath = webSoPath + soName + ".java";
        } else {
            filePath = webSoPath + "/" + soName + ".java";
        }
        File file = new File(filePath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + filePath + " 已存在,请删除后重试!");
        }
        // 将 so 写入文件
        // 构建类信息
        // 头信息
        String content = "package " + packageName + ";" + " \n";
        content += "import com.best.gwms.data.model.bas.WebSearchObject;" + " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";
        content += " public class " + soName + " extends  WebSearchObject  {" + "\n";

        content += "" + "\n";
        content += "    private static final long serialVersionUID = -1L;" + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "}" + "\n";
        File newFile = new File(filePath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);

        System.out.println(packageName);
        return packageName + "." + soName;
    }

    /**
     * 创建controller层
     *
     * @param obj
     * @param
     * @param
     */
    public static String createController(Object obj, String controllerPath) throws Exception {

        controllerPath = controllerPath.replace("\\", "/");
        String packageName = getPackage(controllerPath);
        String className = obj.getClass().getSimpleName() + "Controller";
        if (!controllerPath.endsWith("/")) {
            controllerPath += "/";
        }
        controllerPath += className + ".java";
        File file = new File(controllerPath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + controllerPath + " 已存在,请删除后重试!");
        }
        // 写入文件
        // 构建类信息
        // 头信息

        String content = "package " + packageName + ";" + " \n";
        content += " \n";
        content += "import org.springframework.web.bind.annotation.RestController;" + " \n";
        content += "import org.slf4j.Logger;\n";
        content += "import org.slf4j.LoggerFactory;" + " \n";
        content += "import org.springframework.web.bind.annotation.PostMapping;" + " \n";
        content += "import com.best.gwms.data.annotation.JsonObject;" + " \n";
        content += "import " + obj.getClass().getPackage().getName() + "." + obj.getClass().getSimpleName() + "; \n";
        content += " \n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";

        content += "@RestController" + "\n";
        content += "public class " + className + "{";
        content += "                                    " + "\n";
        content += "private static final Logger log = LoggerFactory.getLogger(" + className + ".class);" + "\n";
        String poName = obj.getClass().getSimpleName();
        String first = poName.substring(0, 1).toLowerCase();
        String last = poName.substring(1);
        String controllMapper = first + last;
        content += "@PostMapping(\"/dao/" + controllMapper + "\")" + "\n";
        content += "public void swaggerJson(@JsonObject(\"test\") String Test) {}" + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "}" + "\n";
        File newFile = new File(controllerPath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);
        System.out.println(packageName);
        return packageName;
    }

    /**
     * 创建vo
     *
     * @param obj
     * @param voPath
     */
    public static String createVo(Object obj, String voPath) throws Exception {
        String poName = obj.getClass().getSimpleName();
        String voName = poName + "Vo";

        voPath = voPath.replace("\\", "/");

        String packageName = getPackage(voPath);

        Boolean extendLotFlag = false;//AbstractBatchPo.class.isAssignableFrom(obj.getClass());
        // key: po属性 value: po属性类型
        Map<String, String> allFieldMapfieldMap = getAllFieldMap(obj.getClass());

        if (extendLotFlag) {
            /*Map<String, String> lotAttFieldMap = getAllFieldMap(new BasLotAtt().getClass());
            for (String key : lotAttFieldMap.keySet()) {
                allFieldMapfieldMap.remove(key);
            }*/
        } else {
            Map<String, String> basFieldMap = getAllFieldMap(new BasPo().getClass());
            for (String key : basFieldMap.keySet()) {
                allFieldMapfieldMap.remove(key);
            }
        }

        String filePath = null;
        if (voPath.endsWith("/")) {
            filePath = voPath + voName + ".java";
        } else {
            filePath = voPath + "/" + voName + ".java";
        }
        File file = new File(filePath);
        if (file.exists() && !delExistsFileFlag) {
            file.delete();
            // throw new Exception("文件 " + filePath + " 已存在,请删除后重试!");
        }

        // 将 vo 写入文件
        // 构建类信息
        // 头信息
        String content = "package " + packageName + ";" + " \n";
        content += "\n";
        content += "import java.util.Date;" + " \n";
        content += "import com.best.gwms.data.model.bas.AbstractVo;" + " \n";
        content += "import com.best.gwms.data.model.bas.AbstractBatchVo;" + " \n";
        content += "\n";

        // 注释信息
        content += "/**" + "\n";
        content += "*" + "\n";
        content += "* 类的描述信息" + "\n";
        content += "*" + "\n";
        content += "* @author " + System.getProperty("user.name") + "\n";
        content += "* @version 1.0.1" + "\n";
        content += "*/" + "\n";
        content += " public class " + voName;
        if (extendLotFlag) {
            content += " extends  AbstractBatchVo  {" + "\n";
        } else {
            content += " extends  AbstractVo  {" + "\n";
        }

        content += "" + "\n";
        content += "    private static final long serialVersionUID = -1L;" + "\n";
        content += createFieldContent(allFieldMapfieldMap) + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "" + "\n";
        content += "}" + "\n";

        File newFile = new File(filePath);
        Files.createParentDirs(newFile);
        Files.write(content.getBytes(), newFile);

        System.out.println(packageName);

        return packageName + "." + voName;
    }

    /**
     * 生成成员属性以及相关的get,set方法
     *
     * @param allFieldMapfieldMap
     * @return
     */
    public static String createFieldContent(Map<String, String> allFieldMapfieldMap) {
        if (allFieldMapfieldMap == null || allFieldMapfieldMap.isEmpty()) {
            return "";
        }
        String res = "";
        String getSetRes = "";

        for (String key : allFieldMapfieldMap.keySet()) {
            String value = allFieldMapfieldMap.get(key);
            if ("ZonedDateTime".equals(value)) {
                value = "String";
            }
            if (key.equalsIgnoreCase("serialVersionUID")) {
                continue;
            }
            res += "    /** " + "\n";
            res += "     * " + "\n";
            res += "     */ " + "\n";
            res += "    private " + value + " " + key + ";\n";

            getSetRes += "    public " + value + " get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(){" + "\n";
            getSetRes += "        return " + key + "; " + "\n";
            getSetRes += "    } " + "\n";

            getSetRes += "    public void set" + key.substring(0, 1).toUpperCase() + key.substring(1) + "(" + value + " " + key + "){" + "\n";
            getSetRes += "        this." + key + "=" + key + "; " + "\n";
            getSetRes += "    } " + "\n";
        }

        if (!Strings.isNullOrEmpty(getSetRes)) {
            res += getSetRes;
        }
        return res;
    }

    /**
     * 获取所有属性以及对应的类型
     *
     * @param obj
     * @return
     */
    public static Map<String, String> getAllFieldMap(Class obj) {

        List<Class> superClassList = getSuperClass(obj);
        superClassList.add(obj);

        Map<String, String> res = Maps.newLinkedHashMap();

        for (Class clazz : superClassList) {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                res.put(field.getName(), field.getType().getSimpleName());
                // System.out.println(field.getOwnerCode() + " type : " +
                // field.getType().getSimpleName());
            }
        }

        return res;
    }

    /**
     * 获取一个类的除Object以外的所有父类
     *
     * @param
     * @return
     */
    public static List<Class> getSuperClass(Class clazz) {
        List<Class> res = new ArrayList<>();
        if (Object.class.equals(clazz) || (Object.class).equals(clazz.getSuperclass())) {
            return res;
        }

        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            res.add(superClass);
            res.addAll(getSuperClass(superClass));
        }

        return res;
    }

    /**
     * 获取包路径
     *
     * @param voPath
     * @return
     */
    public static String getPackage(String voPath) {
        if (Strings.isNullOrEmpty(voPath)) {
            return "vo";
        }
        String[] strings = voPath.split("main/java/");
        if (strings.length < 2) {
            return "vo";
        }
        if (strings[1].endsWith("/")) {
            strings[1] = strings[1].substring(0, strings[1].length() - 1);
        }
        return strings[1].replace("/", ".");
    }

    public void createFromDb() {

        ResultSet rs = getResult();

        try {
            ResultSetMetaData metaData = rs.getMetaData();
            System.out.println(metaData.getColumnCount());
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                System.out.println(metaData.getColumnLabel(i + 1));
                System.out.println(metaData.getColumnName(i + 1));
                System.out.println(metaData.getColumnType(i + 1));
                System.out.println(metaData.getColumnTypeName(i + 1));
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // jdbc:oracle:thin:@192.168.0.1:1521:yuewei"
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@orauat02.800best.com:1521:ORAUAT2", "bl00015", "orabl00015");
            Statement stmt = conn.createStatement();
            return stmt.executeQuery("select * from gv_bas_sku");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void cteateSql(Class<?> cls, String createTableName) {
        if ("".equals(createTableName)||createTableName.length()==0) createTableName = tableName;
        printLine("<!-- sql 语句-->");
        Field[] fieldlist = cls.getDeclaredFields();
        // <result column="TREEPATH" jdbcType="VARCHAR" property="treePath" />
        int basFieldCount = 12;
        int j = 0;
        String res = "";
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (isContinueField(fld)) {
                continue;
            }

            String fieldName = fld.getName();
            int index = basFieldCount + j;
            j++;
            String fieldIndex = "      <!--" + index + "-->";
           // WmsColumn WmsColumnAnnotation = fld.getAnnotation(WmsColumn.class);
            String oracleFieldName = "";
            String mybatisPoFieldName = fieldName;
            /*if (WmsColumnAnnotation != null) {
                oracleFieldName = WmsColumnAnnotation.name().toUpperCase();
            } else {*/
                if (fieldName.equalsIgnoreCase("serialVersionUID")) {
                    continue;
                }
                oracleFieldName = toHibField(fieldName);
                oracleFieldName = oracleFieldName.toUpperCase();
            //}
            String mysqlFieldType = getMySqlFieldType(fld);
            if ("varchar".equalsIgnoreCase(mysqlFieldType)) {
                mysqlFieldType = "varchar(255)";
            }
            res += "\n" + "  `" + oracleFieldName.toLowerCase() + "` " + mysqlFieldType + " DEFAULT NULL,";
            // printLine();
        }

        Boolean extendLotFlag = false;//AbstractBatchPo.class.isAssignableFrom(cls);
        String tmp = "CREATE TABLE `" + createTableName.toLowerCase() + "` (\n" + "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" + "  `CREATED_TIME`".toLowerCase() + "  datetime DEFAULT NULL,\n"
                + "  `CREATOR_ID`".toLowerCase() + " bigint(20) DEFAULT NULL,\n" + "  `UPDATED_TIME`".toLowerCase() + "  datetime DEFAULT NULL,\n" + "  `UPDATOR_ID`".toLowerCase()
                + " bigint(20) DEFAULT NULL,\n" + "  `LOCK_VERSION`".toLowerCase() + " bigint(20) DEFAULT NULL,";
        res = tmp + res;
        if (extendLotFlag) {

            res += "\n  `sku_status_id` bigint(20) DEFAULT NULL,\n" + "  `sku_status_code` varchar(255) DEFAULT " + "NULL,\n" + "  `mfg_date` varchar(255) DEFAULT NULL,\n"
                    + "  `exp_date` varchar(255) DEFAULT NULL,\n" + "  `batch_no` varchar(255) DEFAULT NULL,\n" + "  `origin_country` varchar(255) DEFAULT NULL,";
        }
        res += " \n  `udf1` varchar(255) DEFAULT NULL,\n" + "  `udf2` varchar(255) DEFAULT NULL,\n" + "  `udf3` " + "varchar(255) DEFAULT NULL,\n" + "  `udf4` varchar(255) DEFAULT NULL,";
        if (extendLotFlag) {
            res += "\n  `lot1` varchar(255) DEFAULT NULL,\n" + "  `lot2` varchar(255) DEFAULT NULL,\n" + "  `lot3` varchar(255) DEFAULT NULL,\n" + "  `lot4` varchar(255) DEFAULT NULL,\n"
                    + "  `lot5` varchar(255) DEFAULT NULL,\n" + "  `lot6` varchar(255) DEFAULT NULL,";
        }
        res += "\nPRIMARY KEY (`id`)\n" + ") ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;";
        printLine(res);
    }

    public static void printLine(String printString) {
        System.out.println(printString);
    }

    public static void printWhiteLine() {
        System.out.println();
    }

    public static String getMySqlFieldType(Field fld) {
        String mysqlFieldType = "";
        // if (fld.getAnnotation(ManyToOne.class) != null) {
        // oracleFieldType = "DECIMAL"+ "\n";
        // }

        String fieldType = fld.getType().toString();
        if ("class java.lang.Long".equals(fieldType)) {
            mysqlFieldType = "BIGINT";
        } else if ("class java.lang.String".equals(fieldType)) {
            mysqlFieldType = "VARCHAR";
        } else if ("class java.lang.Double".equals(fieldType)) {
            mysqlFieldType = "DOUBLE";
        } else if ("class java.time.ZonedDateTime".equals(fieldType)) {
            mysqlFieldType = "datetime";
        } else if ("class java.lang.Boolean".equals(fieldType)) {
            mysqlFieldType = "BIT";
        } else if ("class java.lang.Integer".equals(fieldType)) {
            mysqlFieldType = "INTEGER";
        } else if ("int".equals(fieldType)) {
            mysqlFieldType = "INTEGER";
        } else if ("boolean".equals(fieldType)) {
            mysqlFieldType = "BIT";
        } else if ("BigDecimal".equals(fieldType)) {
            mysqlFieldType = "DECIMAL";
        } else if ("byte".equals(fieldType)) {
            mysqlFieldType = "BIT";
        }
        return mysqlFieldType;
    }

    public static boolean isContinueField(Field fld) {
        boolean isContinueField = false;
        String fieldName = fld.getName();
        if (excludeFidldList.contains(fieldName.toUpperCase())) {
            isContinueField = true;
        }
        // Transient transientAnnotation = fld.getAnnotation(Transient.class);
        // if (transientAnnotation != null) {
        // isContinueField = true;
        // }
        return isContinueField;
    }

    public static String toHibField(String field) {
        if (StringUtils.isBlank(field)) {
            return "";
        }

        String string = field.replaceAll("[A-Z]", " $0").replaceAll(" ", "_"); // 正则替换
        return string.toLowerCase();
    }

    public static String getVoPath(Object obj) {
        return "gwms-data/src/main/java/" + obj.getClass().getPackage().getName().replace(".po", ".vo.").replace("" + ".", "/");
    }

    public static String getSoPath(Object obj) {
        return "gwms-data/src/main/java/" + obj.getClass().getPackage().getName().replace(".po", ".so.").replace(".", "/");
    }

    public static String getWebSoPath(Object obj) {
        return "gwms-data/src/main/java/" + obj.getClass().getPackage().getName().replace(".po", ".so.web.").replace(".", "/");
    }

    public static String getDozerPath(Object obj) {
        String[] packageSplit = obj.getClass().getPackage().getName().split("\\.");
        String res = "com.best.gwms.data.dozer" ;
        return "gwms-data/src/main/java/" + res.replace(".", "/");
    }

    public static String getServicePath(Object obj) {
        String[] packageSplit = obj.getClass().getPackage().getName().split("\\.");
        String res = "com.best.gwms.data.service" ;
        return "gwms-data/src/main/java/" + res.replace(".", "/");
    }

    public static String getServiceImplPath(Object obj) {
        String[] packageSplit = obj.getClass().getPackage().getName().split("\\.");
        String res = "com.best.gwms.data.service";
        return "gwms-data/src/main/java/" + res.replace(".", "/") + "/impl";
    }

    public static String getControllerPath(Object obj) {
        String[] packageSplit = obj.getClass().getPackage().getName().split("\\.");
        String res = "com.best.gwms.data.controller" ;
        return "gwms-data/src/main/java/" + res.replace(".", "/");
    }

    public static String getDaoPath(Object obj) {
        String[] packageSplit = obj.getClass().getPackage().getName().split("\\.");
        String res = "com.best.gwms.data.dao" ;
        return "gwms-data/src/main/java/" + res.replace(".", "/");
    }
}
