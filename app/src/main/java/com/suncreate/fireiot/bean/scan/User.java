package com.suncreate.fireiot.bean.scan;

import android.util.Log;

import java.io.Serializable;

/**
 * @author linking
 *         date: 2017/10/24.
 *         description: 用户实体类
 */

public class User implements Serializable {

    public static final String TAG = "User";

    /**
     * 用户ID
     */
    private int userId;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 登陆名
     */
    private String userLogin;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 用户排序
     */
    private Long userPriority;
    /**
     * 部门排序
     */
    private Long deptPriority;
    /**
     * 工作级别
     */
    private Long userLevel;
    /**
     * 系统ID
     */
    private Long cmpyId;
    /**
     * 系统名称
     */
    private String cmpyName;
    /**
     * 管理部门ID
     */
    private Long adminDeptId;
    /**
     * 管理部门名称
     */
    private String adminDeptName;
    /**
     * 所属部门ID
     */
    private Long parentId;
    /**
     * 所属部门名称
     */
    private String deptName;
    /**
     * 电子邮箱
     */
    private String userEmail;
    /**
     * 办公电话
     */
    private String userOfficePhone;
    /**
     * 家庭电话
     */
    private String userHomePhone;
    /**
     * 暂无信息
     */
    private String userHandyPhone;
    /**
     * 移动电话
     */
    private String userMobile;
    /**
     * 办公地址
     */
    private String userOfficeRoom;
    /**
     * 家庭地址
     */
    private String userHomeRoom;
    /**
     * 刷新时间
     */
    private Double userRefreshTime;
    /**
     * 暂无信息
     */
    private String userAlertSound;
    /**
     * 暂无信息
     */
    private Long userSubId;
    /**
     * 暂无信息
     */
    private Long userStatus;
    /**
     * 1为系统用户，2为被禁用，3为还未开通用户名，9为被删除
     */
    private Long userFlag;
    /**
     * 暂无信息
     */
    private Long userWebed;
    /**
     * 否是为管理人员
     */
    private Long userDutyed;
    /**
     * 暂无信息
     */
    private String machineinfoAccount;
    /**
     * 暂无信息
     */
    private String machineinfoUserid;
    /**
     * 暂无信息
     */
    private String machineinfoPwd;
    /**
     * 暂无信息
     */
    private String userSearchStr;
    /**
     * 暂无信息
     */
    private String accountStartTime;
    /**
     * 暂无信息
     */
    private String accountStopTime;
    /**
     * 备注
     */
    private String areaCode;
    /**
     * 账户余额
     */
    private Double accountBalance;
    /**
     * 商户最大扣费限额
     */
    private Double maxLimit;

    /**
     * 获取用户ID
     *
     * @return
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * 获取姓名
     *
     * @return
     */
    public String getUserName() {
        String username = "用户名";
        try {
            username = this.userName;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取用户名失败");
        }
        return username;
    }

    /**
     * 设置姓名
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = (userName == null ? null : userName.trim());
    }

    /**
     * 获取登陆名
     *
     * @return
     */
    public String getUserLogin() {
        return this.userLogin;
    }

    /**
     * 设置登陆名
     *
     * @param userLogin
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = (userLogin == null ? null : userLogin.trim());
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getUserPassword() {
        return this.userPassword;
    }

    /**
     * 设置密码
     *
     * @param userPassword
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = (userPassword == null ? null : userPassword.trim());
    }

    /**
     * 获取用户排序
     *
     * @return
     */
    public Long getUserPriority() {
        return this.userPriority;
    }

    /**
     * 设置用户排序
     *
     * @param userPriority
     */
    public void setUserPriority(Long userPriority) {
        this.userPriority = userPriority;
    }

    /**
     * 获取部门排序
     *
     * @return
     */
    public Long getDeptPriority() {
        return this.deptPriority;
    }

    /**
     * 设置部门排序
     *
     * @param deptPriority
     */
    public void setDeptPriority(Long deptPriority) {
        this.deptPriority = deptPriority;
    }

    /**
     * 获取工作级别
     *
     * @return
     */
    public Long getUserLevel() {
        return this.userLevel;
    }

    /**
     * 设置工作级别
     *
     * @param userLevel
     */
    public void setUserLevel(Long userLevel) {
        this.userLevel = userLevel;
    }

    /**
     * 获取系统ID
     *
     * @return
     */
    public Long getCmpyId() {
        return this.cmpyId;
    }

    /**
     * 设置系统ID
     *
     * @param cmpyId
     */
    public void setCmpyId(Long cmpyId) {
        this.cmpyId = cmpyId;
    }

    /**
     * 获取系统名称
     *
     * @return
     */
    public String getCmpyName() {
        return this.cmpyName;
    }

    /**
     * 设置系统名称
     *
     * @param cmpyName
     */
    public void setCmpyName(String cmpyName) {
        this.cmpyName = (cmpyName == null ? null : cmpyName.trim());
    }

    /**
     * 获取管理部门ID
     *
     * @return
     */
    public Long getAdminDeptId() {
        return this.adminDeptId;
    }

    /**
     * 设置管理部门ID
     *
     * @param adminDeptId
     */
    public void setAdminDeptId(Long adminDeptId) {
        this.adminDeptId = adminDeptId;
    }

    /**
     * 获取管理部门名称
     *
     * @return
     */
    public String getAdminDeptName() {
        return this.adminDeptName;
    }

    /**
     * 设置管理部门名称
     *
     * @param adminDeptName
     */
    public void setAdminDeptName(String adminDeptName) {
        this.adminDeptName = (adminDeptName == null ? null : adminDeptName.trim());
    }

    /**
     * 获取所属部门ID
     *
     * @return
     */
    public Long getParentId() {
        return this.parentId;
    }

    /**
     * 设置所属部门ID
     *
     * @param parentId
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取所属部门名称
     *
     * @return
     */
    public String getDeptName() {
        return this.deptName;
    }

    /**
     * 设置所属部门名称
     *
     * @param deptName
     */
    public void setDeptName(String deptName) {
        this.deptName = (deptName == null ? null : deptName.trim());
    }

    /**
     * 获取电子邮箱
     *
     * @return
     */
    public String getUserEmail() {
        return this.userEmail;
    }

    /**
     * 设置电子邮箱
     *
     * @param userEmail
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = (userEmail == null ? null : userEmail.trim());
    }

    /**
     * 获取办公电话
     *
     * @return
     */
    public String getUserOfficePhone() {
        return this.userOfficePhone;
    }

    /**
     * 设置办公电话
     *
     * @param userOfficePhone
     */
    public void setUserOfficePhone(String userOfficePhone) {
        this.userOfficePhone = (userOfficePhone == null ? null : userOfficePhone.trim());
    }

    /**
     * 获取家庭电话
     *
     * @return
     */
    public String getUserHomePhone() {
        return this.userHomePhone;
    }

    /**
     * 设置家庭电话
     *
     * @param userHomePhone
     */
    public void setUserHomePhone(String userHomePhone) {
        this.userHomePhone = (userHomePhone == null ? null : userHomePhone.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getUserHandyPhone() {
        return this.userHandyPhone;
    }

    /**
     * 设置暂无信息
     *
     * @param userHandyPhone
     */
    public void setUserHandyPhone(String userHandyPhone) {
        this.userHandyPhone = (userHandyPhone == null ? null : userHandyPhone.trim());
    }

    /**
     * 获取移动电话
     *
     * @return
     */
    public String getUserMobile() {
        return this.userMobile;
    }

    /**
     * 设置移动电话
     *
     * @param userMobile
     */
    public void setUserMobile(String userMobile) {
        this.userMobile = (userMobile == null ? null : userMobile.trim());
    }

    /**
     * 获取办公地址
     *
     * @return
     */
    public String getUserOfficeRoom() {
        return this.userOfficeRoom;
    }

    /**
     * 设置办公地址
     *
     * @param userOfficeRoom
     */
    public void setUserOfficeRoom(String userOfficeRoom) {
        this.userOfficeRoom = (userOfficeRoom == null ? null : userOfficeRoom.trim());
    }

    /**
     * 获取家庭地址
     *
     * @return
     */
    public String getUserHomeRoom() {
        return this.userHomeRoom;
    }

    /**
     * 设置家庭地址
     *
     * @param userHomeRoom
     */
    public void setUserHomeRoom(String userHomeRoom) {
        this.userHomeRoom = (userHomeRoom == null ? null : userHomeRoom.trim());
    }

    /**
     * 获取刷新时间
     *
     * @return
     */
    public Double getUserRefreshTime() {
        return this.userRefreshTime;
    }

    /**
     * 设置刷新时间
     *
     * @param userRefreshTime
     */
    public void setUserRefreshTime(Double userRefreshTime) {
        this.userRefreshTime = userRefreshTime;
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getUserAlertSound() {
        return this.userAlertSound;
    }

    /**
     * 设置暂无信息
     *
     * @param userAlertSound
     */
    public void setUserAlertSound(String userAlertSound) {
        this.userAlertSound = (userAlertSound == null ? null : userAlertSound.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public Long getUserSubId() {
        return this.userSubId;
    }

    /**
     * 设置暂无信息
     *
     * @param userSubId
     */
    public void setUserSubId(Long userSubId) {
        this.userSubId = userSubId;
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public Long getUserStatus() {
        return this.userStatus;
    }

    /**
     * 设置暂无信息
     *
     * @param userStatus
     */
    public void setUserStatus(Long userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 获取1为系统用户，2为被禁用，3为还未开通用户名，9为被删除
     *
     * @return
     */
    public Long getUserFlag() {
        return this.userFlag;
    }

    /**
     * 设置1为系统用户，2为被禁用，3为还未开通用户名，9为被删除
     *
     * @param userFlag
     */
    public void setUserFlag(Long userFlag) {
        this.userFlag = userFlag;
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public Long getUserWebed() {
        return this.userWebed;
    }

    /**
     * 设置暂无信息
     *
     * @param userWebed
     */
    public void setUserWebed(Long userWebed) {
        this.userWebed = userWebed;
    }

    /**
     * 获取否是为管理人员
     *
     * @return
     */
    public Long getUserDutyed() {
        return this.userDutyed;
    }

    /**
     * 设置否是为管理人员
     *
     * @param userDutyed
     */
    public void setUserDutyed(Long userDutyed) {
        this.userDutyed = userDutyed;
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getMachineinfoAccount() {
        return this.machineinfoAccount;
    }

    /**
     * 设置暂无信息
     *
     * @param machineinfoAccount
     */
    public void setMachineinfoAccount(String machineinfoAccount) {
        this.machineinfoAccount = (machineinfoAccount == null ? null : machineinfoAccount.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getMachineinfoUserid() {
        return this.machineinfoUserid;
    }

    /**
     * 设置暂无信息
     *
     * @param machineinfoUserid
     */
    public void setMachineinfoUserid(String machineinfoUserid) {
        this.machineinfoUserid = (machineinfoUserid == null ? null : machineinfoUserid.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getMachineinfoPwd() {
        return this.machineinfoPwd;
    }

    /**
     * 设置暂无信息
     *
     * @param machineinfoPwd
     */
    public void setMachineinfoPwd(String machineinfoPwd) {
        this.machineinfoPwd = (machineinfoPwd == null ? null : machineinfoPwd.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getUserSearchStr() {
        return this.userSearchStr;
    }

    /**
     * 设置暂无信息
     *
     * @param userSearchStr
     */
    public void setUserSearchStr(String userSearchStr) {
        this.userSearchStr = (userSearchStr == null ? null : userSearchStr.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getAccountStartTime() {
        return this.accountStartTime;
    }

    /**
     * 设置暂无信息
     *
     * @param accountStartTime
     */
    public void setAccountStartTime(String accountStartTime) {
        this.accountStartTime = (accountStartTime == null ? null : accountStartTime.trim());
    }

    /**
     * 获取暂无信息
     *
     * @return
     */
    public String getAccountStopTime() {
        return this.accountStopTime;
    }

    /**
     * 设置暂无信息
     *
     * @param accountStopTime
     */
    public void setAccountStopTime(String accountStopTime) {
        this.accountStopTime = (accountStopTime == null ? null : accountStopTime.trim());
    }

    /**
     * 获取备注
     *
     * @return
     */
    public String getAreaCode() {
        return this.areaCode;
    }

    /**
     * 设置备注
     *
     * @param areaCode
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = (areaCode == null ? null : areaCode.trim());
    }

    /**
     * 获取账户余额
     *
     * @return
     */
    public Double getAccountBalance() {
        return this.accountBalance;
    }

    /**
     * 设置账户余额
     *
     * @param accountBalance
     */
    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    /**
     * 获取商户最大扣费限额
     *
     * @return
     */
    public Double getMaxLimit() {
        return this.maxLimit;
    }

    /**
     * 设置商户最大扣费限额
     *
     * @param maxLimit
     */
    public void setMaxLimit(Double maxLimit) {
        this.maxLimit = maxLimit;
    }
}
