package com.example.act.permission.controller;

import com.example.act.permission.domain.AjaxResult;
import com.example.act.permission.domain.Page;
import com.example.act.permission.domain.User;
import com.example.act.permission.service.PermissionService;
import com.example.act.permission.service.UserService;
import com.example.act.util.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    UserService userService;
    @Autowired
    PermissionService permissionService;

    @RequestMapping("users")//普通方式实现分页查询
    public String user(@RequestParam(required = false, defaultValue = "1") Integer pageNo, @RequestParam(required =
            false, defaultValue = "4") Integer pageNum, Model model, @RequestParam(required = false, defaultValue = "")
                               String selectText) {//一个是默认的当前页数，一个是默认的展现条数
        Map<String, Object> map = new HashMap<>();//使用hashmap实现map
        map.put("start", (pageNo - 1) * pageNum);
        map.put("pageNum", pageNum);//通过计算获得开始的值和个数
        map.put("loginaccout", selectText);

        List<User> users = userService.selectUsers(map);
        //判断输入的值
        model.addAttribute("users", users);
        model.addAttribute("pageNo", pageNo);//把User对象和当前页数传给前台
        //1.先获取最大数据库中的最大条数
        int maxCount = userService.selectCount(map);
        int maxpageNo = 1;
        //2.计算总的页数
        if (maxCount % pageNum == 0) {
            maxpageNo = maxCount / pageNum;
        } else {
            maxpageNo = maxCount / pageNum + 1;
        }
        model.addAttribute("maxpageNo", maxpageNo);//需要将总页码和当前页码都传递给前台
        return "user";
    }


    @RequestMapping("pageQueryDo")
    @ResponseBody
    public Object pageQuery(String queryText, Integer pageNo, Integer pageNum) {//一个是默认的当前页数，一个是默认的展现条数
        AjaxResult ajaxResult = new AjaxResult();
        try {
//            pageNo = 1;
//            pageNum = 3;
            Map<String, Object> map = new HashMap<>();//使用hashmap实现map
            map.put("start", (pageNo - 1) * pageNum);
            map.put("pageNum", pageNum);//通过计算获得开始的值和个数
            map.put("queryText", queryText);
            List<User> users = userService.selectUsers(map);
            //        model.addAttribute("users", users);//不跳转页面了，所以不需要传递
            //        model.addAttribute("pageNo",pageNo);//把User对象和当前页数传给前台
            //1.先获取最大数据库中的最大条数
            int maxCount = userService.selectCount(map);
            int maxpageNo = 1;
            //2.计算总的页数
            if (maxCount % pageNum == 0) {
                maxpageNo = maxCount / pageNum;
            } else {
                maxpageNo = maxCount / pageNum + 1;
            }

            Page<User> userPage = new Page<>();
            userPage.setMaxCount(maxCount);
            userPage.setMaxpageNo(maxpageNo);
            userPage.setPageNo(pageNo);
            userPage.setObject(users);
            //【将已知的全部加入封装属性,在将封装好的属性添加到ajaxResult中】
            ajaxResult.setObject(userPage);//***************
            //       model.addAttribute("maxpageNo", maxpageNo);//需要将总页码和当前页码都传递给前台
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }


    @RequestMapping("addUserDo")
    @ResponseBody
    public Object adduser(User user) {
        AjaxResult ajaxResult = new AjaxResult();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(date);
        if (permissionService.queryLoginaccount(user) != null) {
            ajaxResult.setSuccess(false);
        } else {
            try {
                user.setCreatetime(time);
                user.setPassword(Md5.md5("123456"));//加密密码，然后校验密码时也需要加密
                permissionService.insertUser(user);
                ajaxResult.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                ajaxResult.setSuccess(false);
            }
        }

        return ajaxResult;
    }

    @RequestMapping("editUserDo")
    @ResponseBody
    public Object edituser(User user) {
        AjaxResult ajaxResult = new AjaxResult();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = sdf.format(date);
        try {
            user.setCreatetime(time);
            permissionService.updateUser(user);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }
        return ajaxResult;
    }

    @RequestMapping("deleteUserDo")
    @ResponseBody
    public Object deleteuser(Integer id) {
        AjaxResult ajaxResult = new AjaxResult();
        try {
            permissionService.deleteUserById(id);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("deleteUsersDo")
    @ResponseBody
    public Object deleteusers(Integer[] userid) {
        AjaxResult ajaxResult = new AjaxResult();
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        try {
            permissionService.deleteUsers(map);
            ajaxResult.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
        }

        return ajaxResult;
    }

    @RequestMapping("edit")
    public String edit(Integer id, Model model) {
        User user = permissionService.selectUserById(id);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("loginaccount", user.getLoginaccount());
        model.addAttribute("email", user.getEmail());

        return "edituser";
    }

    @RequestMapping("loginout")
    public void loginout(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        session.removeAttribute("user");
        Cookie[] cookies = request.getCookies();//获得cookies
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("loginToken")) {
                    Cookie cookie1 = new Cookie("loginToken", "");//这边得用"",不能用null
                    cookie1.setPath("/");//匹配之前设置的cookies
                    cookie1.setMaxAge(0);//如果需要删除。只需要把其最大周期变为0
                    response.addCookie(cookie1);
                }
            }
        }
        response.sendRedirect("/");
    }

}
