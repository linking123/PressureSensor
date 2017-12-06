package com.suncreate.fireiot.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 用户信息类
 *
 * @author chenzhao
 *         created on 2015/12/20 16:33
 */
@SuppressWarnings("serial")
@XStreamAlias("appUser")
public class AppUser extends Entity {

    public static final String MALE = "0";//男
    public static final String FEMALE = "1";//女

    @XStreamAlias("id")
    private int uId;

    @XStreamAlias("mobile_phone")
    private String mobile_phone;

    @XStreamAlias("mobile_pwd")
    private String mobile_pwd;

    @XStreamAlias("mobile_mac")
    private String mobile_mac;

    @XStreamAlias("qq")
    private String qq;

    @XStreamAlias("weixin")
    private String weixin;

    @XStreamAlias("weibo")
    private String weibo;

    @XStreamAlias("nick_name")
    private String nick_name;

    @XStreamAlias("head_path")
    private String head_path;

    @XStreamAlias("id_card")
    private String id_card;

    @XStreamAlias("name")
    private String name;

    @XStreamAlias("sex")
    private String sex;

    @XStreamAlias("plate_num")
    private String plate_num;

    @XStreamAlias("constellation")
    private String constellation;

    @XStreamAlias("autograph")
    private String autograph;

    @XStreamAlias("user_type")
    private String user_type;

    @XStreamAlias("area_code")
    private String area_code;

    @XStreamAlias("is_del")
    private String is_del;

    @XStreamAlias("register_time")
    private Long register_time ;

    @XStreamAlias("extend1")
    private String extend1;

    @XStreamAlias("extend2")
    private String extend2;

    @XStreamAlias("extend3")
    private String extend3;

    @XStreamAlias("extend4")
    private String extend4;

    private boolean isRememberMe;

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getMobile_pwd() {
        return mobile_pwd;
    }

    public void setMobile_pwd(String mobile_pwd) {
        this.mobile_pwd = mobile_pwd;
    }

    public String getMobile_mac() {
        return mobile_mac;
    }

    public void setMobile_mac(String mobile_mac) {
        this.mobile_mac = mobile_mac;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getHead_path() {
        return head_path;
    }

    public void setHead_path(String head_path) {
        this.head_path = head_path;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPlate_num() {
        return plate_num;
    }

    public void setPlate_num(String plate_num) {
        this.plate_num = plate_num;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getAutograph() {
        return autograph;
    }

    public void setAutograph(String autograph) {
        this.autograph = autograph;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public Long getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Long register_time) {
        this.register_time = register_time;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }

    public String getExtend3() {
        return extend3;
    }

    public void setExtend3(String extend3) {
        this.extend3 = extend3;
    }

    public String getExtend4() {
        return extend4;
    }

    public void setExtend4(String extend4) {
        this.extend4 = extend4;
    }

    public boolean isRememberMe() {
        return isRememberMe;
    }

    public void setRememberMe(boolean isRememberMe) {
        this.isRememberMe = isRememberMe;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + uId +
                ", mobile_phone='" + mobile_phone + '\'' +
                ", mobile_pwd='" + mobile_pwd + '\'' +
                ", mobile_mac='" + mobile_mac + '\'' +
                ", qq='" + qq + '\'' +
                ", weixin='" + weixin + '\'' +
                ", weibo='" + weibo + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", head_path='" + head_path + '\'' +
                ", id_card='" + id_card + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", plate_num='" + plate_num + '\'' +
                ", constellation='" + constellation + '\'' +
                ", autograph='" + autograph + '\'' +
                ", user_type='" + user_type + '\'' +
                ", area_code='" + area_code + '\'' +
                ", is_del='" + is_del + '\'' +
                ", register_time='" + register_time + '\'' +
                ", extend1='" + extend1 + '\'' +
                ", extend2='" + extend2 + '\'' +
                ", extend3='" + extend3 + '\'' +
                ", extend4='" + extend4 + '\'' +
                ", isRememberMe=" + isRememberMe +
                '}';
    }
}
