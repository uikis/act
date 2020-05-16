package com.example.act.permission.controller;

import com.example.act.util.Email;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("test")
public class TestController {

    private
    Email email;

    @RequestMapping("")
    @ResponseBody
    public Object test(HttpSession session) {
        Object user = session.getAttribute("user");
        Object permissions = session.getAttribute("permissions");
        return permissions;
    }

    @RequestMapping("dasd")
    @ResponseBody
    public Object test2(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        HttpSession session = request.getSession();
        String path2 = session.getServletContext().getContextPath();
        Object permissionset = session.getAttribute("permissionset");
        return permissionset;
    }

    @RequestMapping("id")
    @ResponseBody
    public Object test3(Integer id) {

        return id;
    }

    @RequestMapping("ceshi3")
    @ResponseBody
    public Object tceshi(HttpSession session) {
        String ss = (String) session.getAttribute("token");
        return ss;
    }
}
