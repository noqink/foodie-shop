package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrdersBO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrdersService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrdersBO ordersBO) {
        String userId = ordersBO.getUserId();
        String itemSpecIds = ordersBO.getItemSpecIds();
        String addressId = ordersBO.getAddressId();
        Integer payMethod = ordersBO.getPayMethod();
        String leftMsg = ordersBO.getLeftMsg();

        // 设置邮费
        Integer postAmount = 0;

        //1. 设置Orders表信息

        // 全局唯一ID
        String orderId = sid.nextShort();
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        UserAddress address = addressService.queryUserAddress(userId, addressId);
        // 查询收货地址
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(
                        address.getCity() + " "
                        + address.getProvince() + " "
                        + address.getDistrict() + " "
                        + address.getDetail());

        newOrder.setPostAmount(postAmount);


        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        //2. itemSpecIds拆分 循环保存对应order_item表
        String[] itemSpecIdArr = itemSpecIds.split(",");

        Integer totalAmount = 0;    // 商品原价累计
        Integer realPayAmount = 0;  // 优惠后的实际支付价格累计
        for (String itemSpecId : itemSpecIdArr) {
            // 2.1查找对应的itemSpec
            ItemsSpec itemSpec = itemService.queryItemsSpecById(itemSpecId);
            Items item = itemService.queryItemById(itemSpec.getItemId());
            // 通过itemSpec查找商品主图
            String imgUrl = itemService.queryItemImgUrl(itemSpec.getItemId());

            // 2.2计算价格
            // TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCount = 1;
            totalAmount += itemSpec.getPriceNormal() * buyCount;
            realPayAmount += itemSpec.getPriceDiscount() * buyCount;

            // 2.3设置order_item表
            OrderItems newOrderItems = new OrderItems();
            String newOrderItemsId = sid.nextShort();
            newOrderItems.setId(newOrderItemsId);
            newOrderItems.setItemId(itemSpec.getItemId());
            newOrderItems.setBuyCounts(buyCount);
            newOrderItems.setItemImg(imgUrl);
            newOrderItems.setItemName(item.getItemName());
            newOrderItems.setItemSpecId(itemSpecId);
            newOrderItems.setItemSpecName(itemSpec.getName());
            newOrderItems.setOrderId(orderId);
            newOrderItems.setPrice(itemSpec.getPriceDiscount());
            orderItemsMapper.insert(newOrderItems);

            // 2.4 减少库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCount);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);

        //3. 保存order_status
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        return orderId;
    }
}
