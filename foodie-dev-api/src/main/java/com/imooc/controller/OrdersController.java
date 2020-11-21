package com.imooc.controller;

import com.imooc.pojo.bo.SubmitOrdersBO;
import com.imooc.service.OrdersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "提交订单接口", tags = {"提交订单相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController{

    @Autowired
    private OrdersService ordersService;

    @ApiOperation(value = "创建订单", notes = "创建订单接口")
    @PostMapping("/create")
    public IMOOCJSONResult create(@RequestBody SubmitOrdersBO ordersBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response){

        // 1. 创建订单
        String orderId = ordersService.createOrder(ordersBO);
        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        // CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);
        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        return IMOOCJSONResult.ok(orderId);
    }

}
