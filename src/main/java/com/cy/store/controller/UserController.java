package com.cy.store.controller;

import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 *
 */
@RestController
//@Controller
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private IUserService iUserService;

    @RequestMapping("reg")
    //@ResponseBody //表示此方法的响应结果以json格式进行响应到前端
    /**
     * 1.接收数据方式：请求处理方法的参数列表设置为pojo类型来接收前端数据
     *  springBoot会将前端的url地址中的参数名和pojo类型的属性名进行比较，
     *  如果这两个名称相同，则将值注入到pojol类型的对应属性上
     */
    public JsonResult<Void> reg(User user){

        iUserService.reg(user);
        return new JsonResult<>(OK);
    }

    /**
     * 2.接收数据方式：请求处理方法的参数列表为非pojo类型
     * springBoot会直接将请求的参数名和方法的参数名直接进行比较，
     * 如果名称相同则自动完成依赖注入
     *
     */
    @RequestMapping("login")
    public JsonResult<User> login(String username, String password,
                                  HttpSession session){
        User user = iUserService.login(username,password);;
        // 向session对象中完成数据的绑定(session全局的)
        session.setAttribute("uid",user.getUid());
        session.setAttribute("username",user.getUsername());

        // 获取session中绑定的数据
        System.out.println(getUidFromSession(session));
        System.out.println(getUsernameFromSession(session));

        return new JsonResult<User>(OK,user);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword,
                                           String newPassword,
                                           HttpSession session) {
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        iUserService.changePassword(uid, username, oldPassword, newPassword);
        return new JsonResult<>(OK);
    }
//    @RequestMapping("reg")
//    //@ResponseBody //表示此方法的响应结果以json格式进行响应到前端
//    public JsonResult<Void> reg(User user){
//        // 创建响应对象
//        JsonResult<Void> result = new JsonResult<>();
//        try {
//            iUserService.reg(user);
//            result.setMessage("用户注册成功");
//            result.setState(200);
//        } catch (UsernameDuplicatedException e) {
//            result.setMessage("用户名被占用");
//            result.setState(4000);
//        } catch (InsertException e){
//            result.setMessage("注册时产生未知异常");
//            result.setState(5000);
//        }
//        return result;
//    }
}
