package com.example.act.permission.service;

import com.example.act.permission.dao.PermissionDao;
import com.example.act.permission.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PermissionService {
    @Autowired
    PermissionDao permissionDao;

    public void insertUser(User user) {
        permissionDao.insertUser(user);
    }

    public User queryLoginaccount(User user) {
        return permissionDao.queryLoginaccount(user);
    }

    public User selectUserById(Integer id) {
        return permissionDao.selectUserById(id);
    }

    public void deleteUserById(Integer id) {
        permissionDao.deleteUserById(id);
    }

    public void deleteUsers(Map<String, Object> map) {
        permissionDao.deleteUsers(map);
    }

    public void updateUser(User user) {
        permissionDao.updateUser(user);
    }
}