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
    public JsonResult<Void> reg(User user){

        iUserService.reg(user);
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
