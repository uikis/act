package com.example.act.permission.service;

import com.example.act.permission.dao.PermissionRoleDao;
import com.example.act.permission.domain.Permission;
import com.example.act.permission.domain.Role;
import com.example.act.permission.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionRoleService {

    @Autowired
    PermissionRoleDao permissionRoleDao;

    public List<Role> selectRoles(Map<String, Object> map) {
        return permissionRoleDao.selectRoles(map);
    }

    public int selectCount(Map<String, Object> map) {
        return permissionRoleDao.selectCount(map);
    }

    public void insertRole(Role role) {
        permissionRoleDao.insertRole(role);
    }

    public void deleteRoles(Map<String, Object> map) {
        permissionRoleDao.deleteRoles(map);
    }

    public Role selectRoleById(Integer id) {
        return permissionRoleDao.selectRoleById(id);
    }

    public void deleteRoleById(Integer id) {
        permissionRoleDao.deleteRoleById(id);
    }

    public List<Role> selectAllRoles() {
        return permissionRoleDao.selectAllRoles();
    }

    public User selectUserById(Integer id) {
        return permissionRoleDao.selectUserByid(id);
    }

    public void unassignDo(Map<String, Object> map) {
        permissionRoleDao.unassignDo(map);
    }

    public void assignDo(Map<String, Object> map) {
        permissionRoleDao.assignDo(map);
    }

    public List<Integer> queryAssignedRole(Integer id) {
        return permissionRoleDao.queryAssignedRole(id);
    }

    public void assignPermission(Map<String, Object> map) {
        permissionRoleDao.assignPermission(map);
    }

    public List<Permission> queryPermission() {
        return permissionRoleDao.queryPermission();
    }

    public List<Permission> queryAllPermisson(Integer id) {//通用的方法可以抽取出来
        List<Permission> p = queryPermission();
        List<Integer> permissionIDs = queryPermissionIdByRoleid(id);//获取当前角色已经分配的许可信息
        Map<Integer, Permission> map = new HashMap<>();
        List<Permission> ps = new ArrayList<>();

        for (Permission pe : p) {//遍历数据库表中的许可

            if (permissionIDs.contains(pe.getId())) {//如果当前许可在许可集合当中
                pe.setChecked(true);
            } else {
                pe.setChecked(false);
            }
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

    public List<Integer> queryPermissionIdByRoleid(Integer id) {
        return permissionRoleDao.queryPermissionIdByRoleid(id);
    }

    public void deletePermissionByRoleid(Map<String, Object> map) {
        permissionRoleDao.deletePermissionByRoleid(map);
    }

    public void updateRole(Role role) {
        permissionRoleDao.updateRole(role);
    }
}
