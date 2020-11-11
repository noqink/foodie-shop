package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){

        // 1.判断用户名不能为空
        if (StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }

        // 2.查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        // 3.请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirm = userBO.getConfirmPassword();

        // 0.判断用户名和密码是否为空
        if (StringUtils.isBlank(username)
                ||StringUtils.isBlank(password)
                ||StringUtils.isBlank(confirm)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1.判断用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }

        // 2.判断密码是否小于六位数
        if (password.length()<6){
            return IMOOCJSONResult.errorMsg("密码不能小于六位数");
        }
        // 3.判断前后密码是否一致
        if (!password.equals(confirm)){
            return IMOOCJSONResult.errorMsg("两次输入的密码不一致");
        }

        Users userResult = userService.createUser(userBO);
        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response,
                "user", JsonUtils.objectToJson(userResult), true);

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0.判断用户名和密码是否为空
        if (StringUtils.isBlank(username)
                ||StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1.校验用户名和密码
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));
        if (userResult == null){
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response,
                "user", JsonUtils.objectToJson(userResult), true);

        return IMOOCJSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户退出", notes = "用户退出", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        // 清楚cookie数据
        CookieUtils.deleteCookie(request, response, "user");
        // TODO 用户退出登录,需要清楚购物车信息
        // TODO 分布式会话中需要清除用户信息

        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users userResult){

        userResult.setPassword(null);
        userResult.setNickname(null);
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setBirthday(null);
        userResult.setCreatedTime(null);

        return userResult;
    }

}
