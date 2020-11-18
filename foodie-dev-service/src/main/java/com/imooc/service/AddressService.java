package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;

import java.util.List;

public interface AddressService {

    /**
     * 用于CURD用户收货地址以及设置默认收货地址
     */

    /**
     * 根据userId查询用户收货地址
     * @param userId
     * @return
     */
    public List<UserAddress> queryAllAddress(String userId);

    /**
     * 根据前端传入的AddressBo，新增用户收货地址
     * @param addressBo
     */
    public void addNewAddress(AddressBo addressBo);

    /**
     * 根据前端传入的AddressBo，修改地址
     * @param addressBo
     */
    public void updateAddress(AddressBo addressBo);

    /**
     * 根据前端传入的userId和addressID删除地址
     * @param userID
     * @param addressId
     */
    public void deleteAddress(String userID, String addressId);

    /**
     * 根据前端传入的userId和addressID将收货地址改为默认
     * @param userID
     * @param addressId
     */
    public void updateAddressToBeDefault(String userID, String addressId);
}
