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

    /**
     * 用户登录功能
     * @param username
     * @param password
     * @return 当前匹配的用户数据，如果没有则返回null值
     */
    User login(String username, String password);

    /**
     * 修改密码
     * @param uid
     * @param username
     * @param oldPassword
     * @param newPassword
     */
    void changePassword(Integer uid, String username, String oldPassword, String newPassword);

    /**
     * 根据用户id查询用户数据
     * @param uid
     * @return
     */
    User getByUid(Integer uid);

    /**
     * 更新用户数据
     * @param uid
     * @param avatar
     * @param username
     */
    void changeUserInfo(Integer uid , String avatar, User username);

    /**
     *
     * @param uid
     * @param avatar
     * @param username
     */
    void changeAvatar(Integer uid, String avatar, String username);
}
