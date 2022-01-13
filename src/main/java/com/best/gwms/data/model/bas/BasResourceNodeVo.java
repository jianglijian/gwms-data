package com.best.gwms.data.model.bas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Package : com.best.gwms.common.vo.bas @Author : Shen.Ziping[zpshen@best-inc.com] @Date :
 * 2018/1/26 16:16 " @Version : V1.0
 */
public class BasResourceNodeVo implements Serializable {

    private static final long serialVersionUID = 3184943297614312884L;
    // 资源编号
    private Long id;

    // 资源名称
    private String name;

    // 资源代码
    private String code;

    // 资源url
    private String url;

    // 用于给前台展示是否选中的效果
    private Boolean selected = false;

    // 资源类型
    private String resourceType;

    // 终端类型
    private String deviceType;

    /**
     * 版本号
     *
     * 2.0：SAAS
     */
    private String version;

    // 子资源节点
    private List<BasResourceNodeVo> childs = new ArrayList<>();

    public BasResourceNodeVo() {}

    public BasResourceNodeVo(Long id, String name, String code, String url, String resourceType, String deviceType, Boolean selected, String version) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.url = url;
        this.resourceType = resourceType;
        this.deviceType = deviceType;
        this.selected = selected;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<BasResourceNodeVo> getChilds() {
        return childs;
    }

    public void setChilds(List<BasResourceNodeVo> childs) {
        this.childs = childs;
    }

    public void addChild(BasResourceNodeVo childNode) {
        this.childs.add(childNode);
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
