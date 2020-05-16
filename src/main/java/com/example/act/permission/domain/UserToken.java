package com.example.act.permission.domain;

public class UserToken {
    private Integer id;
    private Integer userid;
    private String logintoken;

    @Override
    public String toString() {
        return "UserToken{" +
                "id=" + id +
                ", userid=" + userid +
                ", logintoken='" + logintoken + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getLogintoken() {
        return logintoken;
    }

    public void setLogintoken(String logintoken) {
        this.logintoken = logintoken;
    }
}
