package com.imooc.service.impl;

import com.imooc.enums.AddressIsDefault;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAllAddress(String userId) {

        Example addressExample = new Example(UserAddress.class);
        Example.Criteria criteria = addressExample.createCriteria();
        criteria.andEqualTo("userId", userId);

        List<UserAddress> addressList = userAddressMapper.selectByExample(addressExample);

        return addressList;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewAddress(AddressBo addressBo) {

        int isDefault = AddressIsDefault.NO.type;
        // 1.查询用户有无收货地址，如果有，则这次增加的地址设为默认地址
        List<UserAddress> userAddressList = this.queryAllAddress(addressBo.getUserId());
        if (userAddressList.isEmpty() || userAddressList == null || userAddressList.size() == 0){
            isDefault = AddressIsDefault.YES.type;
        }
        // 2.新增地址
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBo, userAddress);
        userAddress.setId(sid.nextShort());
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddress.setIsDefault(isDefault);

        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAddress(AddressBo addressBo) {

        String addressId = addressBo.getAddressId();
        UserAddress updateAddress = new UserAddress();
        BeanUtils.copyProperties(addressBo, updateAddress);

        // 不要忘记给updateAddress加入id属性
        updateAddress.setId(addressId);
        updateAddress.setUpdatedTime(new Date());

        // 有Selective不会覆盖现有值导致为NULL(is_default仍未改变)
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAddress(String userID, String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userID);
        userAddress.setId(addressId);

        UserAddress result = userAddressMapper.selectOne(userAddress);
        if (AddressIsDefault.YES.type.equals(result.getIsDefault())){
            // 如果删除的本身就是默认地址 查询是否还有其他地址 如果有 设置其中一个为默认地址
            userAddressMapper.delete(userAddress);
            List<UserAddress> userAddressList = this.queryAllAddress(userID);
            if (userAddressList.isEmpty() || userAddressList == null || userAddressList.size() == 0){
                return;
            }
            // 如果删除的本身就是默认地址 查询是否还有其他地址 如果有 设置其中一个为默认地址
            UserAddress newDefaultAddress = new UserAddress();
            newDefaultAddress.setUserId(userID);
            List<UserAddress> updateList = userAddressMapper.select(newDefaultAddress);
            newDefaultAddress = updateList.get(0);
            newDefaultAddress.setIsDefault(AddressIsDefault.YES.type);
            userAddressMapper.updateByPrimaryKeySelective(newDefaultAddress);
        }else {
            userAddressMapper.delete(userAddress);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAddressToBeDefault(String userID, String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userID);
        userAddress.setIsDefault(AddressIsDefault.YES.type);

        // 修改原先的默认地址为不默认
        UserAddress queryAddress = userAddressMapper.selectOne(userAddress);
        queryAddress.setIsDefault(AddressIsDefault.NO.type);
        userAddressMapper.updateByPrimaryKeySelective(queryAddress);

        // 修改目标addressId的地址为默认
        UserAddress updateAddress = new UserAddress();
        updateAddress.setUserId(userID);
        updateAddress.setId(addressId);
        updateAddress.setIsDefault(AddressIsDefault.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }
}
