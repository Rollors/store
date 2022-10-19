package com.cy.store.mapper;

import com.cy.store.entity.User;
import org.junit.Test;
import com.cy.store.mapper.UserMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

// @SpringBootTest；表示标注当前类是测试类，不会随同项目打包发送
@SpringBootTest
// @RunWith：表示启动单元测试类(不写的话不能运行)，需要传递一个参数，
@RunWith(SpringRunner.class)
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;
    /**
     * 满足一下四点，不必启动整个项目，就可以单元测试
     * 1.必须被@Test注解修饰
     * 2.返回必须是void
     * 3.方法参数列表不指定任何类型
     * 4.方法的访问修饰符必须是public
     */
    @Test
    public void insert(){
        User user = new User();
        user.setUsername("tim");
        user.setPassword("123456");
        Integer rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void findByUsername(){
        User user = userMapper.findByUsername("tim");
        System.out.println(user);
    }

   @Test
    public void updatePasswordByUidTest(){
        Integer rows = userMapper.updatePasswordByUid(5,
                "654321","admin",new Date());
       System.out.println(rows);
   }

    @Test
    public void findByUidTest(){
        System.out.println(userMapper.findByUid(5));
    }

}
