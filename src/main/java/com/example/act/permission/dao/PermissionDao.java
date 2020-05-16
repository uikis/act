package com.example.act.permission.dao;

import com.example.act.permission.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface PermissionDao {

    @Insert("insert into user(loginaccount, username, password, email, createtime) " +
            "values(#{loginaccount}, #{username}, #{password}, #{email}, #{createtime})")
    void insertUser(User user);

    @Select("select * from user where loginaccount = #{loginaccount}")
    User queryLoginaccount(User user);

    @Select("select * from user where id = #{id}")
    User selectUserById(Integer id);

    @Delete("delete from user where id = #{id}")
    void deleteUserById(Integer id);//delete中不存在*

    void deleteUsers(Map<String, Object> map);

    @Update("update user set loginaccount = #{loginaccount}, username = #{username}, email = #{email} where id = #{id}")
    void updateUser(User user);
}
