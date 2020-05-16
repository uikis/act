package com.example.act.permission.controller;

import com.example.act.permission.domain.AjaxResult;
import com.example.act.permission.domain.Permission;
import com.example.act.permission.domain.User;
import com.example.act.permission.domain.UserToken;
import com.example.act.permission.service.UserService;
import com.example.act.util.Email;
import com.example.act.util.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {

    @Autowired
    UserService userService;
    @Autowired
    Email emailSend;
    @Autowired
    Md5 md5;

    @RequestMapping("registDo")
    @ResponseBody
    public Object registDo(String loginaccount, String password, String email, String role) {

        AjaxResult ajaxResult = new AjaxResult();//创建返回的jason对象
        User user = new User();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        try {
            user.setId(0);
            user.setPassword(Md5.md5(password));//加密密码，然后校验密码时也需要加密
            user.setLoginaccount(loginaccount);
            user.setRole(role);
            user.setEmail(email);
            user.setCreatetime(time);//设置将前台传输过来的数据封装成对象
            userService.insertUser(user);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("remoteRegistDo")
    @ResponseBody
    public boolean remoteRegistDo(String loginaccount) {

//        AjaxResult ajaxResult = new AjaxResult();//创建返回的jason对象
        User user = new User();
        user.setLoginaccount(loginaccount);//设置将前台传输过来的数据封装成对象
        if (userService.selectUserByname(user) == null) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("remoteEmailDo")
    @ResponseBody
    public boolean remoteEmailDo(String email) {

//        AjaxResult ajaxResult = new AjaxResult();//创建返回的jason对象
        User user = userService.selectUserByEmail(email);
//        user.setEmail(email);//设置将前台传输过来的数据封装成对象
        if (user == null) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("loginDo")
    @ResponseBody
    public Object loginDo(String loginaccount, String password, HttpSession session, HttpServletResponse response,
                          Integer flag) {
        if (flag == 1) {

        }
        AjaxResult ajaxResult = new AjaxResult();//创建返回的jason对象
        User user = new User();
        user.setLoginaccount(loginaccount);
        user.setPassword(Md5.md5(password));//设置将前台传输过来的数据封装成对象
        user.setRole("会员");//设置默认角色
        User loginUser = userService.selectUser(user);
        Set<String> set = new HashSet<>();
        if (loginUser != null) {
            session.setAttribute("user", loginUser);
            Permission root = userService.queryAllPermisson(loginUser);
            List<Permission> permissions = userService.selectPermissionByUser(loginUser);//得到用户所拥有的权限结合
            for (Permission pe : permissions) {//遍历数据库表中的许可
                if (pe.getUrl() != null && !"".equals(pe.getUrl())) {
                    set.add(pe.getUrl());//存放需要验证的地址
                }
            }
            session.setAttribute("permissionset", set);
            session.setAttribute("permissions", root);//将根节点保存到session中
            ajaxResult.setSuccess(true);//如果遍历它本身就只有一个本身
            ajaxResult.setObject(userService.selectUser(user));
        } else {
            ajaxResult.setSuccess(false);
        }
        if (flag == 1) {//判断是否为自动登录
            String uuid = UUID.randomUUID().toString();
            UserToken userToken = new UserToken();
            userToken.setLogintoken(uuid);
            userToken.setUserid(loginUser.getId());
            User userAuto = userService.selectUserById(loginUser);
            if (userAuto != null) {
                userService.updateLoginToken(userToken);//避免重复插入
            } else {
                userService.insertLoginToken(userToken);//插入令牌信息
            }
            Cookie cookie = new Cookie("loginToken", uuid);
            cookie.setMaxAge(3600 * 24 * 7);//七天的时间
            response.addCookie(cookie);//增加cookie
        }
        return ajaxResult;
    }

    @RequestMapping("forgetPwdDo")
    @ResponseBody
    public Object forgetPwdDo(String email, HttpSession session) {

        AjaxResult ajaxResult = new AjaxResult();
        User user = userService.selectUserByEmail(email);
        String uuid = UUID.randomUUID().toString();//创建令牌
        try {
            user.setToken(uuid);
            userService.insertToken(user);
            emailSend.sendSimpleMail(user);//必须注入到容器当中才能使用该方法
            session.setAttribute("token", uuid);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("resetPwdDo")
    @ResponseBody
    public Object resetPwd(User user) {
        String pwd = user.getPassword();
        AjaxResult ajaxResult = new AjaxResult();
        try {
            user.setPassword(md5.md5(pwd));//对密码加密
            userService.resetPwd(user);
            User user1 = userService.selectUserByToken(user.getToken());//根据令牌可以取出当前用户
            user1.setToken(null);//重置令牌
            userService.insertToken(user1);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }
}
