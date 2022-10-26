package com.cy.store.service.impl;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import com.cy.store.service.IAddressService;
import com.cy.store.service.ex.AddressCountLimitException;
import com.cy.store.service.ex.InsertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class AddressServiceImp implements IAddressService {
    @Autowired
    private AddressMapper addressMapper;

    // 配置文件application.properties中配置过
    @Value("${user.address.max-count}")
    private int maxCount;
    @Override
    public void addNewAddress(Integer uid, String username, Address address) {
        // 调用收货地址调试统计方法
        Integer count = addressMapper.countByUid(uid);
        if (count >= maxCount){
            throw new AddressCountLimitException("用户收货地址超出上限");
        }

        // uid,IsDefault
        address.setUid(uid);
        Integer isDefault = count ==0 ? 1:0;
        address.setIsDefault(isDefault);
        // 补全日志
        address.setCreatedUser(username);
        address.setCreatedTime(new Date());
        address.setModifiedUser(username);
        address.setModifiedTime(new Date());

        // 插入收货地址的方法
        Integer rows = addressMapper.insert(address);
        if (rows != 1){
            throw new InsertException("插入用户地址时产生未知异常");
        }
    }
}
