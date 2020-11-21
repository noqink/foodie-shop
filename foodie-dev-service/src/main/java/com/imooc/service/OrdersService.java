package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.SubmitOrdersBO;

public interface OrdersService {

    public String createOrder(SubmitOrdersBO ordersBO);

}
