package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBo;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "收货地址展示页面的相关接口", tags = {"收货地址展示页面的相关接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "展示用户收货地址接口", notes = "展示用户收货地址接口", httpMethod = "POST")
    @PostMapping("/list")
    public IMOOCJSONResult list(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId){

        if (StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }

        List<UserAddress> addressList = addressService.queryAllAddress(userId);
        return IMOOCJSONResult.ok(addressList);
    }

    @ApiOperation(value = "用户新增收货地址接口", notes = "用户新增收货地址接口", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestBody AddressBo addressBo){

        IMOOCJSONResult checkResult = checkAddress(addressBo);
        if (checkResult.getStatus() != 200){
            return checkResult;
        }

        addressService.addNewAddress(addressBo);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户修改收货地址接口", notes = "用户修改收货地址接口", httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestBody AddressBo addressBo){

        // 判断修改的地址是否为空
        if (StringUtils.isBlank(addressBo.getAddressId())){
            return IMOOCJSONResult.errorMsg("AddressId：参数不能为空！");
        }
        IMOOCJSONResult checkResult = checkAddress(addressBo);
        if (checkResult.getStatus() != 200){
            return checkResult;
        }

        addressService.updateAddress(addressBo);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户删除收货地址接口", notes = "用户删除收货地址接口", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "收货地址ID", required = true)
            @RequestParam String addressId){

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("");
        }

        addressService.deleteAddress(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户修改收货地址为默认地址接口", notes = "用户修改收货地址为默认地址接口", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public IMOOCJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "收货地址ID", required = true)
            @RequestParam String addressId){

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("");
        }

        addressService.updateAddressToBeDefault(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddress(AddressBo addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }

}
