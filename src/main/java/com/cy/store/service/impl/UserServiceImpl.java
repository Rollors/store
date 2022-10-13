package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * 用户模块业务层实现类
 */
// @Service将当前对象交给spring管理，自动创建对象以及管理
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        // 通过user参数获取传递过来的username
        String username = user.getUsername();
        // findByUsername(username)判断用户是否注册过
        User result = userMapper.findByUsername(username);
        // 判断结果集是否为null,不为null抛出用户名被占用的异常
        if (result!=null){
            // 抛出异常
            throw new UsernameDuplicatedException("用户名被占用");
        }

        // 密码加密处理：MD5算法的形式：67dhdsgh-yeuwrey121-yerui374-yrwirei-67123
        // （串+password+串） ---- md5算法进行加密，连续加密三次
        // 盐值即串，一个随机的字符串
        String oldPassword = user.getPassword();
        // 获取盐值(随机生成)
        String salt = UUID.randomUUID().toString().toUpperCase();
        user.setSalt(salt);
        // 将密码和盐值作为一个整体进行加密处理，忽略原有密码的强度，提升了数据的安全
        String md5Password = getMD5Password(oldPassword,salt);
        user.setPassword(md5Password);
        // 补全数据：is_delete=0 以及四个日志字段信息
        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        // 执行注册业务功能成功（rows==1）
        Integer rows = userMapper.insert(user);
        if (rows!=1){
            throw new InsertException("在用户注册过程中产生未知异常");
        }
    }

    /**
     * 定义MD5算法加密处理
     */
    private String getMD5Password(String password,String salt){
        for (int i=0;i<3;i++){
            // md5方法的调用
            password = DigestUtils.md5DigestAsHex((salt+password+salt).getBytes()).toUpperCase();
        }
        return password;
    }
}