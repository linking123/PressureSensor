package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by Pan Zhaoxuan on 2016/11/29
 * 我的信息详情类
 */

public class MyInfo implements Serializable {

    //    id	long	用户编号
//    userRealname	string	真实姓名
//    userName	string	昵称
//    userQq	string	qq
//    userMobile	string	电话
//    userAddress	string	地址
//    "id": "1",
//            "user_name": "admin",
//            "user_qq": "666666666",
//            "user_address": "安徽合肥",
//            "user_mobile": "15056541450",
//            "user_realname": "配件商测试用户"
    private String id;
    private String user_realname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_realname() {
        return user_realname;
    }

    public void setUser_realname(String user_realname) {
        this.user_realname = user_realname;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_qq() {
        return user_qq;
    }

    public void setUser_qq(String user_qq) {
        this.user_qq = user_qq;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    private String user_name;
    private String user_qq;
    private String user_mobile;
    private String user_address;



}
