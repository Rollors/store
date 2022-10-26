package com.cy.store.mapper;

import com.cy.store.entity.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AddressMapperTests {
    @Autowired
    private AddressMapper addressMapper;

    @Test
    public void insert(){
        Address address = new Address();
        address.setUid(4);
        address.setPhone("18270387673");
        address.setName("John");
        addressMapper.insert(address);
    }

    @Test
    public void countByUid(){
        Integer rows = addressMapper.countByUid(4);
        System.out.println(rows);
    }
}
