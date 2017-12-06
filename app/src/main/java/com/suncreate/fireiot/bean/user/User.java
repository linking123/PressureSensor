package com.suncreate.fireiot.bean.user;

import java.io.Serializable;

/**
 * Created by xushengji on 2016/11/14.
 */

public class User implements Serializable {

    /** 主键 */
    private Long id;
    /** 真实姓名 */
    private String userRealname;
    /** 用户名 */
    private String userName;
    /** 密码 */
    private String userPassword;
    /** 添加时间 */
    private Long userAddtime;
    /** 用户状态1锁定0正常 */
    private Long userStatus;
    /** QQ */
    private String userQq;
    /** 地址 */
    private String userAddress;
    /** 可用余额 */
    private Double userBalance;
    /** 邮箱 */
    private String userEmail;
    /** 冻结金额 */
    private Double userFreezeblance;
    /** 生日 */
    private Long userBirthday;
    /** 积分 */
    private Long userIntegral;
    /** 最后登录时间 */
    private Long userLastlogindate;
    /** 最后登录IP */
    private String userLastloginip;
    /** 登录次数 */
    private Long userLogincount;
    /** 手机号码 */
    private String userMobile;
    /** 性别：-1报名 1-男 0-女 */
    private Long userSex;
    /** 用户角色:1会员中心 2技师中心 3商家中心 4代理商中心 */
    private String userRole;
    /** 会员信誉数 */
    private Long userCredit;
    /** 头像ID */
    private Long userPhotoId;
    /** 店铺ID */
    private Long userStoreId;
    /** QQ互联 */
    private String userQqOpenid;
    /** sina互联 */
    private String userSinaOpenid;
    /** 地区 */
    private Long userAreaId;
    /** 地区编码 */
    private Long region;
    /** 用户车辆ID */
    private String userCarid;
    /** 品牌ID */
    private Long userBrandid;
    /** 车型ID */
    private Long userModelid;
    /** 适用车型名称 */
    private String userModelname;
    /** 审核编号 */
    private String auditNumber;
    /** 用户当前角色:1会员中心 2技师中心 3商家中心 4代理商中心 */
    private String currentRole;
    /** 经度 */
    private String longitude;
    /** 纬度 */
    private String latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Long getUserAddtime() {
        return userAddtime;
    }

    public void setUserAddtime(Long userAddtime) {
        this.userAddtime = userAddtime;
    }

    public Long getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Long userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserQq() {
        return userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public Double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(Double userBalance) {
        this.userBalance = userBalance;
    }

    public Long getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(Long userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getUserFreezeblance() {
        return userFreezeblance;
    }

    public void setUserFreezeblance(Double userFreezeblance) {
        this.userFreezeblance = userFreezeblance;
    }

    public Long getUserIntegral() {
        return userIntegral;
    }

    public void setUserIntegral(Long userIntegral) {
        this.userIntegral = userIntegral;
    }

    public Long getUserLastlogindate() {
        return userLastlogindate;
    }

    public void setUserLastlogindate(Long userLastlogindate) {
        this.userLastlogindate = userLastlogindate;
    }

    public String getUserLastloginip() {
        return userLastloginip;
    }

    public void setUserLastloginip(String userLastloginip) {
        this.userLastloginip = userLastloginip;
    }

    public Long getUserLogincount() {
        return userLogincount;
    }

    public void setUserLogincount(Long userLogincount) {
        this.userLogincount = userLogincount;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Long getUserSex() {
        return userSex;
    }

    public void setUserSex(Long userSex) {
        this.userSex = userSex;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Long getUserCredit() {
        return userCredit;
    }

    public void setUserCredit(Long userCredit) {
        this.userCredit = userCredit;
    }

    public Long getUserPhotoId() {
        return userPhotoId;
    }

    public void setUserPhotoId(Long userPhotoId) {
        this.userPhotoId = userPhotoId;
    }

    public Long getUserStoreId() {
        return userStoreId;
    }

    public void setUserStoreId(Long userStoreId) {
        this.userStoreId = userStoreId;
    }

    public String getUserQqOpenid() {
        return userQqOpenid;
    }

    public void setUserQqOpenid(String userQqOpenid) {
        this.userQqOpenid = userQqOpenid;
    }

    public String getUserSinaOpenid() {
        return userSinaOpenid;
    }

    public void setUserSinaOpenid(String userSinaOpenid) {
        this.userSinaOpenid = userSinaOpenid;
    }

    public Long getUserAreaId() {
        return userAreaId;
    }

    public void setUserAreaId(Long userAreaId) {
        this.userAreaId = userAreaId;
    }

    public Long getRegion() {
        return region;
    }

    public void setRegion(Long region) {
        this.region = region;
    }

    public Long getUserBrandid() {
        return userBrandid;
    }

    public String getUserCarid() {
        return userCarid;
    }

    public void setUserCarid(String userCarid) {
        this.userCarid = userCarid;
    }

    public void setUserBrandid(Long userBrandid) {
        this.userBrandid = userBrandid;
    }

    public Long getUserModelid() {
        return userModelid;
    }

    public void setUserModelid(Long userModelid) {
        this.userModelid = userModelid;
    }

    public String getUserModelname() {
        return userModelname;
    }

    public void setUserModelname(String userModelname) {
        this.userModelname = userModelname;
    }

    public String getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(String auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(String currentRole) {
        this.currentRole = currentRole;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
