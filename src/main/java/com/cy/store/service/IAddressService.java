package com.cy.store.service;

import com.cy.store.entity.Address;
import com.cy.store.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;

/*收货地址业务层接口*/
public interface IAddressService {

    void addNewAddress(Integer uid, String username, Address address);
}
