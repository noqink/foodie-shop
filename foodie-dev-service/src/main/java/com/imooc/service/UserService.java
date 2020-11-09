package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    /**
     * 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     */
    public Users createUser(UserBO userBO);
}
