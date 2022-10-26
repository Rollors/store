package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.service.ex.*;
import com.cy.store.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/**
 * 控制层基类
 */
public class BaseController {
    // 操作成功的状态码
    public static final int OK = 200;

    // 请求处理方法，这个方法的返回值就是需要传递给前端的数据
    // 自动将异常对象传递给此方法的参数列表上
    // 当前项目中产生了异常，被统一拦截到此方法中
    @ExceptionHandler({ServiceException.class,FileUploadException.class}) //用于统一处理抛出的异常
    public JsonResult<Void> handleException(Throwable e){
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicatedException){
            result.setMessage("用户名被占用");
            result.setState(4000);
        }else if (e instanceof UserNotFoundException){
            result.setMessage("用户不存在");
            result.setState(4001);
        }else if (e instanceof PasswordNotMatchException){
            result.setMessage("用户的密码错误");
            result.setState(4002);
        }else if (e instanceof AddressCountLimitException){
            result.setMessage("用户收货地址超出上限");
            result.setState(4003);
        }else if (e instanceof InsertException){
            result.setMessage("注册时产生未知异常");
            result.setState(5000);
        } else if (e instanceof UpadateException){
            result.setMessage("更新时产生未知异常");
            result.setState(5003);
        }  else if (e instanceof FileEmptyException) {
            result.setState(6000);
        } else if (e instanceof FileSizeException) {
            result.setState(6001);
        } else if (e instanceof FileTypeException) {
            result.setState(6002);
        } else if (e instanceof FileStateException) {
            result.setState(6003);
        } else if (e instanceof FileUploadIOException) {
            result.setState(6004);
        }
        return result;
    }

    /**
     * 获取session对象中的uid
     * @param session session对象
     * @return 当前登录用户的uid值
     */
    protected final Integer getUidFromSession(HttpSession session){
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    /**
     * 获取当前登录用户的username
     * @param session session对象
     * @return 当前登录用户的用户名
     *
     * 在实现类中重写了父类中的toString()，不是句柄信息的输出
     */
    protected final String getUsernameFromSession(HttpSession session){
        return session.getAttribute("username").toString();
    }


}
