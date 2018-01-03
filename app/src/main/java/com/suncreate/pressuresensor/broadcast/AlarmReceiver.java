package com.suncreate.pressuresensor.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.api.remote.PressureSensorApi;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//		TLog.log("onReceive ->com.suncreate.fireiot收到定时获取消息");
//		NoticeUtils.requestNotice(context);
        Log.i(TAG, "<-----Keep alive interface------>");
        if (AppContext.getInstance().isLogin()) {
            //保持会话
            PressureSensorApi.keepalived(new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    AppContext.getInstance().cleanCookie();
                    AppContext.getInstance().cleanLoginInfo();
                    Log.e(TAG, "keep alive failure!");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                        }.getType());
                        if (resultBean != null && resultBean.getCode() == 1) {
                            Log.i(TAG, "keep alive success!");
//                            intervalTask();
                        } else {
                            AppContext.getInstance().cleanCookie();
                            AppContext.getInstance().cleanLoginInfo();
                            Log.e(TAG, "keep alive failure, unknown response!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onFailure(statusCode, headers, responseString, e);
                    }
                }
            });
        } else {
            Log.i(TAG, "Not logged in yet, can not keep alive!");
        }
    }

    /**
     * 定时任务
     */
    private void intervalTask() {
        Double lon = AppContext.lon;
        Double lat = AppContext.lat;
        if (null != lon && null != lat) {
            MonkeyApi.uploadLonLat(lon, lat, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e(TAG, "upload lon&lat failure!");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                        }.getType());
                        if (resultBean != null && resultBean.getCode() == 1) {
                            Log.i(TAG, "upload lon&lat success!");
                        } else {
                            Log.e(TAG, "upload lon&lat failure, unknown response!");
                        }
                    } catch (Exception e) {
                        onFailure(statusCode, headers, responseString, e);
                    }
                }
            });
        }
    }
}
