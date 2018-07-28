package com.suncreate.pressuresensor.util;

import android.os.Handler;

/**
 * runnable
 * Created by MyTimeRunnable on 2018/07/10
 */
public abstract class MyTimeRunnable implements Runnable {
    Handler mHandler;
    int time = 0;

    public MyTimeRunnable(Handler handler, int time) {
        mHandler = handler;
        this.time = time;
    }

    @Override
    public void run() {
        if (time == 0){
            onPlayMusic0s();
        }
        time = time + 1;
        turnoverTime(time);//更新显示时间
        if (time == 30) {
            onPlayMusic30s();
        }
        if (time == 20) {
            onPlayMusic20s();
        }
        if (time > 0) {
            mHandler.postDelayed(this, 1000);
        } else {
            overTime();
        }
    }

    protected abstract void onPlayMusic0s(); //0秒时执行

    protected abstract void onPlayMusic20s(); //20秒时执行

    protected abstract void onPlayMusic30s(); //30秒时执行

    protected abstract void overTime();

    protected abstract void turnoverTime(int time);
}
