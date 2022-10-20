package com.cy.store.service.impl;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.*;
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

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user==null){
            throw new UserNotFoundException("用户不存在");
        }
        // 检测用户的密码是否匹配

        // 1.先获取数据库中加密之后的密码
        String oldPassword = user.getPassword();
        // 2.和用户传递过来的密码进行比较

        // 2.1先获取盐值：上一次注册自动生成的
        String salt = user.getSalt();
        // 2.2将用户密码按照相同的MD5算法进行加密
        String newMd5Password = getMD5Password(password,salt);
        // 3.将密码进行比较
        if(!oldPassword.equals(newMd5Password)){
            throw new PasswordNotMatchException("密码不匹配");
        }

        // 4.判断是否删除 id_delete字段值是否为1
        if (user.getIsDelete()==1){
            throw new UserNotFoundException("用户不存在");
        }
        // 将当前用户数据返回，(uid,username,avatar),减少非不必要属性，提升响应速度
        User result = new User();
        result.setUid(user.getUid());
        result.setUsername(user.getUsername());
        result.setAvatar(user.getAvatar());

        return result;
    }

    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword){
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() ==1 ){
            throw new UserNotFoundException("用户不存在");
        }
        // 原始密码和数据库密码对比
        String oldMd5Password = getMD5Password(oldPassword,result.getSalt());
        if (!result.getPassword().equals(oldMd5Password)){
            throw new PasswordNotMatchException("密码错误");
        }
        // 将新密码设置到数据库中，新密码进行加密再更新
        String newMd5Password = getMD5Password(newPassword, result.getSalt());
        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password,username,new Date());
        if (rows!=1){
            throw new UpadateException("更新数据产生未知的异常");
        }
    }

    @Override
    public User getByUid(Integer uid) {
        User user = userMapper.findByUid(uid);
        if (user==null || user.getIsDelete()==1){
            throw new UserNotFoundException("用户不存在");
        }
        User result = new User();
        result.setUsername(user.getUsername());
        result.setPhone(user.getPhone());
        result.setEmail(user.getEmail());
        result.setGender(user.getGender());
        return  result;
    }

    /**
     *
     * @param uid
     * @param username
     * @param user phone/email/gender,收到将uid/username封装到user中
     */
    @Override
    public void changeUserInfo(Integer uid, String username, User user) {
        User result = userMapper.findByUid(uid);
        if (result==null || result.getIsDelete()==1){
            throw new UserNotFoundException("用户不存在");
        }
        user.setUid(uid);
//        user.setUsername(username);
        user.setModifiedUser(username);
        user.setModifiedTime(new Date());

        Integer rows = userMapper.updateInfoByUid(user);
        if (rows!=1){
            throw new UpadateException("更新时产生未知异常");
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
