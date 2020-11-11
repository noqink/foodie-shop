package com.imooc.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "用户对象BO", description = "从客户端又用户传递的数据，封装在此entity中")
public class UserBO {

    // 接收前端传入的属性
    @ApiModelProperty(value = "用户名", name = "username", example = "imooc", required = true)
    private String username;

    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;

    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123", required = false)
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
