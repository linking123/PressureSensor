package com.suncreate.pressuresensor.bean.scan;

import java.io.Serializable;

/**
 * @author linking
 *         date: 2017/10/31.
 *         description: 消防设备信息实体类
 */
public class FirefightEquipment implements Serializable {

    /**设备ID*/
    private Long id;
    /**系统编码*/
    private String cmpyId;
    /**设备名称*/
    private String equipmentName;
    /**设备编号*/
    private String equipmentNo;
    /**设备编码*/
    private String equipmentCode;
    /**标签编码*/
    private String labelCode;
    /**设备类型*/
    private Long equipmentType;
    /**生产厂商*/
    private String manufacturer;
    /**经度*/
    private Double longitude;
    /**纬度*/
    private Double latitude;
    /**关联楼宇*/
    private String relatedBuilding;
    /**安装楼层*/
    private String relatedFloor;
    /**安装位置说明*/
    private String installAddr;
    /**负责人*/
    private Long personInCharge;
    /**最后巡检时间*/
    private Long lastCheckTime;
    /**计划巡检时间*/
    private Long planCheckTime;
    /**巡检周期 0:天 1:周 2:月 3:年*/
    private Long checkCycle;
    /**巡检周期数值*/
    private Long checkCycleNum;
    /**设备状态 0：正常 1：异常 2：未知*/
    private Long fireEquipmentState;
    /**是否删除 0：否 1：是*/
    private Long isDel;
    /**添加时间，时间戳*/
    private Long addTime;
    /**添加用户*/
    private String addUser;
    /**修改时间，时间戳*/
    private Long modifyTime;
    /**修改用户*/
    private String modifyUser;
    /**描述*/
    private String equipmentDesc;
    /**
     * 是否到有效巡检期内  2:过期未检测 1:待检测（有效检测期内）  0：不可检测（已检测）
     * check_cycle=0 巡检周期为天时，  半天内为有效巡检期。
     * check_cycle=1 巡检周期为周时，  一天内为有效巡检期。
     * check_cycle=2 巡检周期为月时，  一周内为有效巡检期。
     * check_cycle=3 巡检周期为年时，  一月内为有效巡检期。
     */
    private String checkFlag;

    /**
     * 获取设备ID
     * @return
     */
    public Long getId(){
        return this.id;
    }

    /**
     * 设置设备ID
     * @param id
     */
    public void setId(Long id){
        this.id=id;
    }
    /**
     * 获取系统编码
     * @return
     */
    public String getCmpyId(){
        return this.cmpyId;
    }

    /**
     * 设置系统编码
     * @param cmpyId
     */
    public void setCmpyId(String cmpyId){
        this.cmpyId=(cmpyId == null ? null : cmpyId.trim());
    }
    /**
     * 获取设备名称
     * @return
     */
    public String getEquipmentName(){
        return this.equipmentName;
    }

    /**
     * 设置设备名称
     * @param equipmentName
     */
    public void setEquipmentName(String equipmentName){
        this.equipmentName=(equipmentName == null ? null : equipmentName.trim());
    }
    /**
     * 获取设备编号
     * @return
     */
    public String getEquipmentNo(){
        return this.equipmentNo;
    }

    /**
     * 设置设备编号
     * @param equipmentNo
     */
    public void setEquipmentNo(String equipmentNo){
        this.equipmentNo=(equipmentNo == null ? null : equipmentNo.trim());
    }
    /**
     * 获取设备编码
     * @return
     */
    public String getEquipmentCode(){
        return this.equipmentCode;
    }

    /**
     * 设置设备编码
     * @param equipmentCode
     */
    public void setEquipmentCode(String equipmentCode){
        this.equipmentCode=(equipmentCode == null ? null : equipmentCode.trim());
    }
    /**
     * 获取标签编码
     * @return
     */
    public String getLabelCode(){
        return this.labelCode;
    }

    /**
     * 设置标签编码
     * @param labelCode
     */
    public void setLabelCode(String labelCode){
        this.labelCode=(labelCode == null ? null : labelCode.trim());
    }
    /**
     * 获取设备类型
     * @return
     */
    public Long getEquipmentType(){
        return this.equipmentType;
    }

    /**
     * 设置设备类型
     * @param equipmentType
     */
    public void setEquipmentType(Long equipmentType){
        this.equipmentType=equipmentType;
    }
    /**
     * 获取生产厂商
     * @return
     */
    public String getManufacturer(){
        return this.manufacturer;
    }

    /**
     * 设置生产厂商
     * @param manufacturer
     */
    public void setManufacturer(String manufacturer){
        this.manufacturer=(manufacturer == null ? null : manufacturer.trim());
    }
    /**
     * 获取经度
     * @return
     */
    public Double getLongitude(){
        return this.longitude;
    }

    /**
     * 设置经度
     * @param longitude
     */
    public void setLongitude(Double longitude){
        this.longitude=longitude;
    }
    /**
     * 获取纬度
     * @return
     */
    public Double getLatitude(){
        return this.latitude;
    }

    /**
     * 设置纬度
     * @param latitude
     */
    public void setLatitude(Double latitude){
        this.latitude=latitude;
    }
    /**
     * 获取关联楼宇
     * @return
     */
    public String getRelatedBuilding(){
        return this.relatedBuilding;
    }

    /**
     * 设置关联楼宇
     * @param relatedBuilding
     */
    public void setRelatedBuilding(String relatedBuilding){
        this.relatedBuilding=(relatedBuilding == null ? null : relatedBuilding.trim());
    }
    /**
     * 获取安装楼层
     * @return
     */
    public String getRelatedFloor(){
        return this.relatedFloor;
    }

    /**
     * 设置安装楼层
     * @param relatedFloor
     */
    public void setRelatedFloor(String relatedFloor){
        this.relatedFloor=(relatedFloor == null ? null : relatedFloor.trim());
    }
    /**
     * 获取安装位置说明
     * @return
     */
    public String getInstallAddr(){
        return this.installAddr;
    }

    /**
     * 设置安装位置说明
     * @param installAddr
     */
    public void setInstallAddr(String installAddr){
        this.installAddr=(installAddr == null ? null : installAddr.trim());
    }
    /**
     * 获取负责人
     * @return
     */
    public Long getPersonInCharge(){
        return this.personInCharge;
    }

    /**
     * 设置负责人
     * @param personInCharge
     */
    public void setPersonInCharge(Long personInCharge){
        this.personInCharge=personInCharge;
    }
    /**
     * 获取最后巡检时间
     * @return
     */
    public Long getLastCheckTime(){
        return this.lastCheckTime;
    }

    /**
     * 设置最后巡检时间
     * @param lastCheckTime
     */
    public void setLastCheckTime(Long lastCheckTime){
        this.lastCheckTime=lastCheckTime;
    }
    /**
     * 获取计划巡检时间
     * @return
     */
    public Long getPlanCheckTime(){
        return this.planCheckTime;
    }

    /**
     * 设置计划巡检时间
     * @param planCheckTime
     */
    public void setPlanCheckTime(Long planCheckTime){
        this.planCheckTime=planCheckTime;
    }
    /**
     * 获取巡检周期 0:天 1:周 2:月 3:年
     * @return
     */
    public Long getCheckCycle(){
        return this.checkCycle;
    }

    /**
     * 设置巡检周期 0:天 1:周 2:月 3:年
     * @param checkCycle
     */
    public void setCheckCycle(Long checkCycle){
        this.checkCycle=checkCycle;
    }
    /**
     * 获取巡检周期数值
     * @return
     */
    public Long getCheckCycleNum(){
        return this.checkCycleNum;
    }

    /**
     * 设置巡检周期数值
     * @param checkCycleNum
     */
    public void setCheckCycleNum(Long checkCycleNum){
        this.checkCycleNum=checkCycleNum;
    }
    /**
     * 获取设备状态 0：正常 1：异常 2：未知
     * @return
     */
    public Long getFireEquipmentState(){
        return this.fireEquipmentState;
    }

    /**
     * 设置设备状态 0：正常 1：异常 2：未知
     * @param fireEquipmentState
     */
    public void setFireEquipmentState(Long fireEquipmentState){
        this.fireEquipmentState=fireEquipmentState;
    }
    /**
     * 获取是否删除 0：否 1：是
     * @return
     */
    public Long getIsDel(){
        return this.isDel;
    }

    /**
     * 设置是否删除 0：否 1：是
     * @param isDel
     */
    public void setIsDel(Long isDel){
        this.isDel=isDel;
    }
    /**
     * 获取添加时间，时间戳
     * @return
     */
    public Long getAddTime(){
        return this.addTime;
    }

    /**
     * 设置添加时间，时间戳
     * @param addTime
     */
    public void setAddTime(Long addTime){
        this.addTime=addTime;
    }
    /**
     * 获取添加用户
     * @return
     */
    public String getAddUser(){
        return this.addUser;
    }

    /**
     * 设置添加用户
     * @param addUser
     */
    public void setAddUser(String addUser){
        this.addUser=(addUser == null ? null : addUser.trim());
    }
    /**
     * 获取修改时间，时间戳
     * @return
     */
    public Long getModifyTime(){
        return this.modifyTime;
    }

    /**
     * 设置修改时间，时间戳
     * @param modifyTime
     */
    public void setModifyTime(Long modifyTime){
        this.modifyTime=modifyTime;
    }
    /**
     * 获取修改用户
     * @return
     */
    public String getModifyUser(){
        return this.modifyUser;
    }

    /**
     * 设置修改用户
     * @param modifyUser
     */
    public void setModifyUser(String modifyUser){
        this.modifyUser=(modifyUser == null ? null : modifyUser.trim());
    }
    /**
     * 获取描述
     * @return
     */
    public String getEquipmentDesc(){
        return this.equipmentDesc;
    }

    /**
     * 设置描述
     * @param equipmentDesc
     */
    public void setEquipmentDesc(String equipmentDesc){
        this.equipmentDesc=(equipmentDesc == null ? null : equipmentDesc.trim());
    }

    /**
     * 是否到有效巡检期内 2:过期未检测 1:待检测（有效检测期内）  0：不可检测（已检测）
     */
    public String getCheckFlag() {
        return checkFlag;
    }

    /**
     * 是否到有效巡检期内 2:过期未检测 1:待检测（有效检测期内）  0：不可检测（已检测）
     */
    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }
}
