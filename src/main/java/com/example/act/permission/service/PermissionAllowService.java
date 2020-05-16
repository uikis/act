package com.example.act.permission.service;

import com.example.act.permission.dao.PermissionAllowDao;
import com.example.act.permission.domain.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionAllowService {

    @Autowired
    PermissionAllowDao permissionAllowDao;

    public List<Permission> queryPermission() {
        return permissionAllowDao.queryPermission();
    }

    public List<Permission> queryAllPermisson() {//通用的方法可以抽取出来
        List<Permission> p = queryPermission();
        Map<Integer, Permission> map = new HashMap<>();
        List<Permission> ps = new ArrayList<>();

        for (Permission pe : p) {//遍历数据库表中的许可
            map.put(pe.getId(), pe);
        }
        for (Permission pe : p) {
            Permission child = pe;//假设都是子节点，然后一步步筛选，取出所有pe对象
            if (child.getPid() == 0) {//取出根节点
                ps.add(child);
            } else {//如果有其他pid等于自身id的，说明他是父节点
                Permission parent = map.get(child.getPid());//取出父节点
                parent.getChildren().add(child);//添加子节点
            }
        }
        return ps;
    }

    public void insertPermission(Permission permission) {
        permissionAllowDao.insertPermission(permission);
    }

    public void updataPermission(Permission permission) {
        permissionAllowDao.updataPermission(permission);
    }

    public Permission queryPermissionById(Permission permission) {
        return permissionAllowDao.queryPermissionById(permission);
    }

    public void deletePermissionById(Permission permission) {
        permissionAllowDao.detelePermissionById(permission);
    }

}
