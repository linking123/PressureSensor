package com.suncreate.fireiot.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by ASUS on 2016/11/8.
 */

public class MapApi {
    /**
     * 获取定位信息
     *
     * @param location
     * @param handler
     */
    public static void getLocation(String location,
                             AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        ApiHttpClient.setApiUrl("http://api.map.baidu.com/");
        params.put("location", location);
        params.put("key", "xvxsK3L2ups9kmEK13jF9F32oLmIPfSj");
        String loginurl = "geocoder?output=json";
        ApiHttpClient.get(loginurl, params, handler);
    }
}
