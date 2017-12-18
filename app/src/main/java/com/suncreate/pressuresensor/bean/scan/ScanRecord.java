package com.suncreate.pressuresensor.bean.scan;

import java.io.Serializable;
import java.util.Map;

/**
 * author: linking
 * date: 2017/10/23.
 * description: 巡检记录实体类
 */
public class ScanRecord implements Serializable {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 系统编码
     */
    private String cmpyId;
    /**
     * 关联设备ID
     */
    private Long equipId;
    /**
     * 巡检员ID
     */
    private Long patrolInspector;
    /**
     * 上次巡检时间
     */
    private Long checkTime;
    /**
     * 下次巡检时间
     */
    private Long nextCheckTime;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 负责人
     */
    private String fireEquipmentPersonIncharge;
    /**
     * 设备状态 0：正常 1：异常 2：未知
     */
    private Long fireEquipmentState;
    /**
     * 具体描述
     */
    private String desciption;
    /**
     * 手持设备IP
     */
    private String checkEquipIp;
    /**
     * 安装位置
     */
    private String equipmentInstallPostion;
    /**
     * 设备厂商
     */
    private String equipmentProductor;
    /**
     * 实际巡检点经度
     */
    private Double checkLongitude;
    /**
     * 实际巡检点纬度
     */
    private Double checkLatitude;
    /**
     * 手持设备编码
     */
    private String checkEquipNo;
    private Map map;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCmpyId() {
        return cmpyId;
    }

    public void setCmpyId(String cmpyId) {
        this.cmpyId = cmpyId;
    }

    public Long getEquipId() {
        return equipId;
    }

    public void setEquipId(Long equipId) {
        this.equipId = equipId;
    }

    public Long getPatrolInspector() {
        return patrolInspector;
    }

    public void setPatrolInspector(Long patrolInspector) {
        this.patrolInspector = patrolInspector;
    }

    public Long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Long checkTime) {
        this.checkTime = checkTime;
    }

    public Long getNextCheckTime() {
        return nextCheckTime;
    }

    public void setNextCheckTime(Long nextCheckTime) {
        this.nextCheckTime = nextCheckTime;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getFireEquipmentPersonIncharge() {
        return fireEquipmentPersonIncharge;
    }

    public String getEquipmentInstallPostion() {
        return equipmentInstallPostion;
    }

    public void setEquipmentInstallPostion(String equipmentInstallPostion) {
        this.equipmentInstallPostion = equipmentInstallPostion;
    }

    public void setFireEquipmentPersonIncharge(String fireEquipmentPersonIncharge) {
        this.fireEquipmentPersonIncharge = fireEquipmentPersonIncharge;
    }

    public Long getFireEquipmentState() {
        return fireEquipmentState;
    }

    public void setFireEquipmentState(Long fireEquipmentState) {
        this.fireEquipmentState = fireEquipmentState;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getCheckEquipIp() {
        return checkEquipIp;
    }

    public void setCheckEquipIp(String checkEquipIp) {
        this.checkEquipIp = checkEquipIp;
    }

    public String getEquipmentProductor() {
        return equipmentProductor;
    }

    public void setEquipmentProductor(String equipmentProductor) {
        this.equipmentProductor = equipmentProductor;
    }

    public Double getCheckLongitude() {
        return checkLongitude;
    }

    public void setCheckLongitude(Double checkLongitude) {
        this.checkLongitude = checkLongitude;
    }

    public Double getCheckLatitude() {
        return checkLatitude;
    }

    public void setCheckLatitude(Double checkLatitude) {
        this.checkLatitude = checkLatitude;
    }

    public String getCheckEquipNo() {
        return checkEquipNo;
    }

    public void setCheckEquipNo(String checkEquipNo) {
        this.checkEquipNo = checkEquipNo;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
