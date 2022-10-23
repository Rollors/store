package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 用户模块的持久层接口
 */
public interface UserMapper {

    /**
     * 插入用户的数据
     * @param user
     * @return 受影响的行数(增、删、改，都会有受影响的行数作为返回值)
     */
    Integer insert(User user);

    /**
     * 根据用户名来查询用户的数据
     * @param username
     * @return 如果找到对应的用户则返回用户的数据，否则为null值
     */
    User findByUsername(String username);

    /**
     * 根据uid修改用户信息
     * @param uid
     * @param password
     * @param modifiedUser
     * @param modifiedTime
     * @return
     */
    Integer updatePasswordByUid(@Param("uid") Integer uid,
                                @Param("password") String password,
                                @Param("modifiedUser") String modifiedUser,
                                @Param("modifiedTime") Date modifiedTime);

    /**
     * 更新用户数据
     * @param user
     * @return
     */
    Integer updateInfoByUid(User user);
    /**
     * 根据用户id查询用户的数据
     * @param uid
     * @return
     */
    User findByUid(@Param("uid") Integer uid);

    Integer updateAvatarByUid(@Param("uid") Integer uid,
                              @Param("avatar") String avatar,
                              @Param("modifiedUser") String modifiedUser,
                              @Param("modifiedTime") Date modifiedTime);

}
