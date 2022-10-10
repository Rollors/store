package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.apache.ibatis.annotations.Param;

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
    User findByUsername(@Param("username") String username);

}
