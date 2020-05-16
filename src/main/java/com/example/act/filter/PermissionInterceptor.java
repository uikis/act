package com.example.act.filter;

import com.example.act.permission.domain.Permission;
import com.example.act.permission.service.PermissionAllowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    PermissionAllowService permissionAllowService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();//获得请求地址
//        String path = session.getServletContext().getContextPath();//获取上下文路径
        List<Permission> permissions = permissionAllowService.queryPermission();//需要验证权限的所有对象
        Set<String> allpermissionset = new HashSet<>();//不需要有重复值，用来存放地址
        for (Permission permission : permissions) {
            if (permission.getUrl() != null && !"".equals(permission.getUrl())) {
                allpermissionset.add(permission.getUrl());//存放需要验证的地址
            }
        }
        if (allpermissionset.contains(uri)) {//先判断是否请求地址在权限地址集合中
            Set<String> permissionset = (Set<String>) session.getAttribute("permissionset");//拥有权限的地址
            if (permissionset.contains(uri)) {//再判断用户所拥有的权限集合能不能响应请求
                return true;
            } else {
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write("您的权限不够！");
                response.setHeader("refresh", "3;url=/");
                return false;
            }
        } else {
            return true;
        }
    }
}
