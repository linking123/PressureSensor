package com.suncreate.pressuresensor.base;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;

import com.suncreate.pressuresensor.interf.Permission;

import pub.devrel.easypermissions.EasyPermissions;

/**
` * 基础蓝牙活动类
 *
 * Created by linking on 12/7/17.
 */

public class BaseActivityBlueToothLE extends AppCompatActivity {

    String[] locations = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected boolean checkLocationPermission() {
        return EasyPermissions.hasPermissions(this, locations);
    }

    protected void requestLocationPermission() {
        EasyPermissions.requestPermissions(this, "Android 6.0以上扫描蓝牙需要该权限", Permission.LOCATION, locations);
    }
}
