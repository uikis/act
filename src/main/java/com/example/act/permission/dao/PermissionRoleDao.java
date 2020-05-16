package com.example.act.permission.dao;

import com.example.act.permission.domain.Permission;
import com.example.act.permission.domain.Role;
import com.example.act.permission.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PermissionRoleDao {

    List<Role> selectRoles(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    @Insert("insert into role (name) values(#{name})")
    void insertRole(Role role);

    void deleteRoles(Map<String, Object> map);

    @Select("select * from role where id = #{id}")
    Role selectRoleById(Integer id);

    @Delete("delete from role where id = #{id}")
    void deleteRoleById(Integer id);

    @Select("select * from role")
    List<Role> selectAllRoles();

    @Select("select * from user where id = #{id}")
    User selectUserByid(Integer id);

    void unassignDo(Map<String, Object> map);

    void assignDo(Map<String, Object> map);

    @Select("select role_id from user_role where user_id = #{id}")
    List<Integer> queryAssignedRole(Integer id);

    void assignPermission(Map<String, Object> map);

    @Select("select * from permission")
    List<Permission> queryPermission();

    @Select("select permissionid from role_permission where roleid = #{id}")
    List<Integer> queryPermissionIdByRoleid(Integer id);

    @Delete("delete from role_permission where roleid = #{roleid}")
    void deletePermissionByRoleid(Map<String, Object> map);

    @Update("update role set name = #{name} where id = #{id}")
    void updateRole(Role role);
}
