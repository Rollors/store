package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

// @SpringBootTest；表示标注当前类是测试类，不会随同项目打包发送
@SpringBootTest
// @RunWith：表示启动单元测试类(不写的话不能运行)，需要传递一个参数，
@RunWith(SpringRunner.class)
public class UserServiceTests {
    @Autowired
    private IUserService iUserService;
    /**
     * 满足一下四点，不必启动整个项目，就可以单元测试
     * 1.必须被@Test注解修饰
     * 2.返回必须是void
     * 3.方法参数列表不指定任何类型
     * 4.方法的访问修饰符必须是public
     */
    @Test
    public void reg(){
        try {
            User user = new User();
            user.setUsername("Yep1");
            user.setPassword("123456");
            iUserService.reg(user);
            System.out.println("insert success");
        } catch (ServiceException e) {
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void login(){
        User user = iUserService.login("yep1","123456");
        System.out.println(user);
    }

    @Test
    public void changePassword(){
        iUserService.changePassword(4,"yep1","123456","654321");
    }

    @Test public void getByUid(){
        System.out.println(iUserService.getByUid(4));
    }

    @Test
    public void changeInfo(){
        Integer uid =4;
        String username = "yep1";
        User user = new User();
        user.setPhone("18279202702");
        user.setEmail("25467431@qq.com");
        user.setGender(0);
        iUserService.changeUserInfo(uid,username,user);
    }
}
