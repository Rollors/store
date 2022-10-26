package com.cy.store.controller;

import com.cy.store.controller.ex.*;
import com.cy.store.entity.User;
import com.cy.store.service.IUserService;
import com.cy.store.service.ex.InsertException;
import com.cy.store.service.ex.UsernameDuplicatedException;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @RequestMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session){
        User user = iUserService.getByUid(getUidFromSession(session));
        return new JsonResult<User>(OK,user);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfoByUid(HttpSession session,User user){
        // user对象有四部分数据：username、phone、email、gender
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        iUserService.changeUserInfo(uid,username,user);
        return new JsonResult<>(OK);
    }

    /**
     * MultipartFile接口是SpringMVC提供的一个接口，包装了获取文件类型的数据
     * ，springboot有整合SpringMVC,只需要在处理请求的方法参数列表上声明一个参数类型为
     * MultipartFile的参数，然后Springboot自动将传递服务的文件数据赋值给这个参数
     * @param session
     * @param file
     * @return
     */
    // 设置上传文件的大小最大值
    public static final int AVATAR_MAX_SIZE = 10*1024*1024;
    // 设置上传文件的类型
    public static final List<String> AVATAR_TYPE = new ArrayList<>();
    static {
        AVATAR_TYPE.add("image/jpeg");
        AVATAR_TYPE.add("image/png");
        AVATAR_TYPE.add("image/bmp");
        AVATAR_TYPE.add("image/gif");
        AVATAR_TYPE.add("image/jpg");
    }

    @RequestMapping("change_avatar")
    public JsonResult<String> changeAvatar(HttpSession session,
                                           @RequestParam("file") MultipartFile file){
        // 判断文件是否为空
        if (file.isEmpty()){
            throw new FileEmptyException("文件为空");
        }
        if (file.getSize()>AVATAR_MAX_SIZE){
            throw new FileSizeException("文件大小超出限制");
        }
        // 判断文件类型是否符合规定
        String contentType = file.getContentType();
//        System.out.println("contentType="+contentType);
        if (!AVATAR_TYPE.contains(contentType)){
            throw new FileTypeException("文件类型不对");
        }
        // 上传文件   /upload/文件.png
        String parent = session.getServletContext().getRealPath("upload");
        // File对象指向这个路径，File是否存在
        File dir = new File(parent);
        if (!dir.exists()){ // 检查目录是否存在
            dir.mkdirs(); //创建当前的目录
        }
        // 获取文件名称，UUID生成新的字符串作为文件名
        // 例如：avatar01.png
        String originalFilename = file.getOriginalFilename();
        System.out.println("OriginalFilename="+originalFilename);
        int index = originalFilename.lastIndexOf(".");
        // 文件后缀
        String suffix = originalFilename.substring(index);
        // 新的随机生成的文件名
        String filename = UUID.randomUUID().toString().toUpperCase()+suffix;
        // 此时是空文件
        File dest = new File(dir,filename);
        // 参数file中的数据写入到dest中
        try {
            file.transferTo(dest);
        }catch (FileStateException e){
            throw  new FileStateException("文件状态异常");
        } catch (IOException e) {
            throw new FileUploadIOException("文件读写异常");
        }
        Integer uid = getUidFromSession(session);
        String username = getUsernameFromSession(session);
        // 返回头像的路径/upload/test.png
        String avatar = "/upload/"+filename;
        iUserService.changeAvatar(uid,avatar,username);

        return new JsonResult<>(OK,avatar);
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
