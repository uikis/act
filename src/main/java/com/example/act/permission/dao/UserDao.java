package com.example.act.permission.dao;

import com.example.act.permission.domain.Permission;
import com.example.act.permission.domain.User;
import com.example.act.permission.domain.UserToken;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserDao {
    @Insert("insert into user(id, loginaccount, password, email, role, createtime) " +
            "values(#{id}, #{loginaccount}, #{password}, #{email}, #{role}, #{createtime})")
    void insertUser(User user);

    @Select("select * from user where loginaccount = #{loginaccount}")
    User selectUserByname(User user);

    @Select("select * from user where loginaccount = #{loginaccount} and password = #{password}")
    User selectUser(User user);

    List<User> selectUsers(Map<String, Object> map);

    int selectCount(Map<String, Object> map);

    List<Permission> selectPermissionByUser(User user);

    @Select("select * from user where email = #{email}")
    User selectUserByEmail(String email);

    void resetPwd(User user);

    @Update("update user set token = #{token} where email = #{email}")
    void insertToken(User user);

    @Select("select * from user where token = #{token}")
    User selectUserByToken(String token);

    @Insert("insert into user_token (userid, logintoken) values(#{userid}, #{logintoken})")
    void insertLoginToken(UserToken userToken);

    @Select("select * from user_token where userid = #{id}")
    User selectUserById(User loginUser);

    @Update("update user_token set logintoken = #{logintoken} where userid = #{userid}")
    void updateLoginToken(UserToken userToken);

    @Select("select * from user_token where logintoken = #{token}")
    UserToken selectUserByLoginToken(String token);

    @Select("select * from user where id = #{userId}")
    User selectUserByid(Integer userId);
}
