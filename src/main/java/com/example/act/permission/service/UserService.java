package com.example.act.permission.service;

import com.example.act.permission.dao.UserDao;
import com.example.act.permission.domain.Permission;
import com.example.act.permission.domain.User;
import com.example.act.permission.domain.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    public User selectUserByname(User user) {
        return userDao.selectUserByname(user);
    }

    public User selectUser(User user) {
        return userDao.selectUser(user);
    }

    public List<User> selectUsers(Map<String, Object> map) {
        return userDao.selectUsers(map);
    }

    public int selectCount(Map<String, Object> map) {
        return userDao.selectCount(map);
    }

    public List<Permission> selectPermissionByUser(User user) {
        return userDao.selectPermissionByUser(user);
    }

    public Permission queryAllPermisson(User user) {//通用的方法可以抽取出来
        List<Permission> p = selectPermissionByUser(user);//得到用户相应权限的菜单
        Map<Integer, Permission> map = new HashMap<>();
        Permission root = new Permission();

        for (Permission pe : p) {//遍历数据库表中的许可
            map.put(pe.getId(), pe);
        }
        for (Permission pe : p) {
            Permission child = pe;//假设都是子节点，然后一步步筛选，取出所有pe对象
            if (child.getPid() == 0) {//取出根节点
                root = child;
            } else {//如果有其他pid等于自身id的，说明他是父节点
                Permission parent = map.get(child.getPid());//取出父节点
                if (parent != null) {
                    parent.getChildren().add(child);
                }//添加子节点
            }
        }
        return root;
    }

    public User selectUserByEmail(String email) {
        return userDao.selectUserByEmail(email);
    }

    public void resetPwd(User user) {
        userDao.resetPwd(user);
    }

    public void insertToken(User user) {
        userDao.insertToken(user);
    }

    public User selectUserByToken(String token) {
        return userDao.selectUserByToken(token);
    }

    public void insertLoginToken(UserToken userToken) {
        userDao.insertLoginToken(userToken);
    }

    public User selectUserById(User loginUser) {
        return userDao.selectUserById(loginUser);
    }

    public void updateLoginToken(UserToken userToken) {
        userDao.updateLoginToken(userToken);
    }

    public UserToken selectUserByLoginToken(String token) {
        return userDao.selectUserByLoginToken(token);
    }

    public User selectUserByid(Integer userId) {
        return userDao.selectUserByid(userId);
    }
}
