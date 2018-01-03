package com.suncreate.pressuresensor.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.api.remote.PressureSensorApi;
import com.suncreate.pressuresensor.bean.Update;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;

import cz.msebera.android.httpclient.Header;

/**
 * 更新管理类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年11月18日 下午4:21:00
 */

public class UpdateManager {

    private Update mUpdate;

    private Context mContext;

    private boolean isShow = false;

    private ProgressDialog _waitDialog;

    private AsyncHttpResponseHandler mCheckUpdateHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            hideCheckDialog();
            if (isShow) {
                showFaileDialog();
            }
        }

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            hideCheckDialog();
            mUpdate = XmlUtils.toBean(Update.class, new ByteArrayInputStream(arg2));
            onFinshCheck();
        }
    };

    public UpdateManager(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
    }

    public boolean haveNew() {
        if (this.mUpdate == null) {
            return false;
        }
        boolean haveNew = false;
        int curVersionCode = TDevice.getVersionCode(AppContext
                .getInstance().getPackageName());
        if (curVersionCode < mUpdate.getUpdate().getAndroid()
                .getVersionCode()) {
            haveNew = true;
        }
        return haveNew;
    }

    public void checkUpdate() {
        if (isShow) {
            showCheckDialog();
        }
        PressureSensorApi.checkUpdate(mCheckUpdateHandler);
    }

    private void onFinshCheck() {
        if (haveNew()) {
            showUpdateInfo();
        } else {
            if (isShow) {
                showLatestDialog();
            }
        }
    }

    private void showCheckDialog() {
        if (_waitDialog == null) {
            _waitDialog = DialogHelp.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
        }
        _waitDialog.show();
    }

    private void hideCheckDialog() {
        if (_waitDialog != null) {
            _waitDialog.dismiss();
        }
    }

    private void showUpdateInfo() {
        if (mUpdate == null) {
            return;
        }
        AlertDialog.Builder dialog = DialogHelp.getMessageDialog(mContext, mUpdate.getUpdate().getAndroid().getUpdateLog(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NetUtils.isConnected(mContext) && !NetUtils.isWifi(mContext)) {
                    DialogHelp.getConfirmDialog(mContext, "提示", "当前处于非Wifi网络，是否确定下载更新包？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UIHelper.openDownLoadService(mContext, mUpdate.getUpdate().getAndroid().getDownloadUrl(), mUpdate.getUpdate().getAndroid().getVersionName());
                        }
                    }).show();
                } else {
                    UIHelper.openDownLoadService(mContext, mUpdate.getUpdate().getAndroid().getDownloadUrl(), mUpdate.getUpdate().getAndroid().getVersionName());
                }
            }
        });
        dialog.setTitle("发现新版本");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showLatestDialog() {
        DialogHelp.getMessageDialog(mContext, "已经是最新版本了").show();
    }

    private void showFaileDialog() {
        DialogHelp.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
    }
}
