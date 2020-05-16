package com.example.act.permission.controller;

import com.example.act.permission.domain.User;
import com.example.act.permission.domain.UserToken;
import com.example.act.permission.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("")
public class DispactherController {

    @Autowired
    UserService userService;

    @RequestMapping("regist")
    public String regist() {
        return "reg";
    }

    @RequestMapping("main")
    public String main() {
        return "main";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("forgetPwd")
    public String forgetPwd() {
        return "forgetPwd";
    }

    @RequestMapping("")
    public String login(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();//获得cookies
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("loginToken")) {
                    String token = cookie.getValue();
                    UserToken userToken = userService.selectUserByLoginToken(token);
                    Integer userId = userToken.getUserid();
                    User loginUser = userService.selectUserByid(userId);
                    if (loginUser != null) {
                        session.setAttribute("user", loginUser);
                        response.setContentType("text/html;charset=UTF-8");
                        response.sendRedirect("dispacther");
                    }
                }
            }
        }

        return "login";
    }

    @RequestMapping("dispacther")
    public void dispacther(HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("正在自动登录------------------");
        response.setHeader("refresh", "2;url=/main");
    }

    @RequestMapping("permission/user")
    public String user() {
        return "permissionuser";
    }

    @RequestMapping("permission/useradd")
    public String add() {
        return "adduser";
    }

    @RequestMapping("permission/permission")
    public String permission() {
        return "permission";
    }

    @RequestMapping("permission/role")
    public String role() {
        return "role";
    }

    @RequestMapping("permission/roleadd")
    public String roleAdd() {
        return "addrole";
    }

    @RequestMapping("permission/assign")
    public String assign() {
        return "assignPermission";
    }

    @RequestMapping("resetPwd")
    public String resetPwd(HttpSession session) {
        if (session.getAttribute("token") != null) {
            return "resetPwd";
        } else {
            return "login";
        }
    }

}
