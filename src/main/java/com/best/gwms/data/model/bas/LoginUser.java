package com.best.gwms.data.model.bas;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class LoginUser implements Serializable {

    private static final long serialVersionUID = 3619252057391688782L;

    @ApiModelProperty(value = "登录用户功能权限")
    private List<BasResourceNodeVo> loginPermission;

    public List<BasResourceNodeVo> getLoginPermission() {
        return loginPermission;
    }

    public BasUserVo loginUser;


    public BasUserVo getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(BasUserVo loginUser) {
        this.loginUser = loginUser;
    }

    public void setLoginPermission(List<BasResourceNodeVo> loginPermission) {
        this.loginPermission = loginPermission;
    }
}
