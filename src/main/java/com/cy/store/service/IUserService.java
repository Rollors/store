package com.cy.store.service;

import com.cy.store.entity.User;

/**
 * 用户模块业务层接口
 */
public interface IUserService {

    /**
     * 用户注册
     * @param user 用户类型的数据
     */
    void reg(User user);
}
