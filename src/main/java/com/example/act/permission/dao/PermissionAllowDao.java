package com.example.act.permission.dao;


import com.example.act.permission.domain.Permission;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionAllowDao {

    @Select("select * from permission")
    List<Permission> queryPermission();

    @Insert("insert into permission (name, url, pid) values(#{name},#{url}, #{pid})")
    void insertPermission(Permission permission);

    @Update("update permission set name = #{name}, url = #{url} where id = #{id}")
    void updataPermission(Permission permission);

    @Select("select * from permission where id = #{id}")
    Permission queryPermissionById(Permission permission);

    @Delete("delete from permission where id = #{id}")
    void detelePermissionById(Permission permission);
}
