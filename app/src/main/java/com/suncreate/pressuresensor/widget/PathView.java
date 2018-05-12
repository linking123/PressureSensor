package com.suncreate.pressuresensor.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.suncreate.pressuresensor.bean.Ble.CardioData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模拟心电图
 * date: 2018/05/05 22:58
 */
public class PathView extends CardiographView {

    private long overTime;// 游戏结束的时刻
    private long startTime;// 游戏开始的时刻
    private long lastTime;// 上次画人物的时间

    private boolean isPlaying;//是否在开始游戏了
    private boolean isPause;//是否暂停了

    List<CardioData> dataList;
    CardioData lastTimeLastData; //上一次最后一个数据，移动到这个位置


    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPath = new Path();
        // 重置path
        mPath.reset();
        dataList = new ArrayList<>();
    }

    /**
     * @param listData <time, pressureData></>
     */
    public void setData(List<CardioData> listData) {
        this.dataList = listData;
        lastTimeLastData = listData.get(listData.size() - 1);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        drawPathTest(canvas);
//        scrollBy(1, 0); //x轴前进一位
//        postInvalidateDelayed(5);  //持续5s？
    }

    // 一次传入多个数据，如1s的数据可能就有多对，<时间，压力值>，模拟1s跳动多个步骤
    // 可以一次传入多条数据，则用path；
    private void drawPathTest(Canvas canvas) {

        if (lastTimeLastData != null) {
            mPath.moveTo(lastTimeLastData.getTime(), lastTimeLastData.getPressureData());
        } else {
            mPath.moveTo(10, mHeight / 2);//mHeight这里无效，必须在onDraw执行后才有值，400
        }

        //设置画笔style
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(5);

        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                mPath.lineTo(dataList.get(i).getTime(), dataList.get(i).getPressureData());
            }

            canvas.drawPath(mPath, mPaint);
        } else {
            // 或者一次传入一条，则用drawLine
//            canvas.drawLine(dataList.get(0).getTime(), dataList.get(0).getPressureData());
        }
    }

    public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

}
