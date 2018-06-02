package com.suncreate.pressuresensor.widget.ps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.suncreate.pressuresensor.R;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * 心电图视图
 *
 * @author linking
 *         date 2018/05/18
 */
public class EcgView extends SurfaceView implements SurfaceHolder.Callback {

    private Context mContext;
    private SurfaceHolder surfaceHolder;
    public static boolean isRunning;
    private Canvas mCanvas;

    private float ecgMax = 150;//心电的最大值  ，理论最大值120，但不能就是这么大吧
    private String bgColor = "#151F28";
    private int wave_speed = 15;//波速: x mm/s   5 太小太短， 25稍快， 35太快了
    private int sleepTime = 50; //每次锁屏的时间间距，单位:ms；这个时间和后面的开始结束时间差，
    // 以及画波的时间之间的关系需要验证；
    // 8不行，20不行  50可以 100太慢
    private float lockWidth;//每次锁屏需要画的
    private int ecgPerCount = 1;//每次画心电数据的个数，心电每秒有500个数据包

    private static Queue<Integer> ecgDatas = new LinkedList<>();

    //小网格颜色
    protected int mSGridColor = Color.parseColor("#092100");
    //背景颜色
    protected int mBackgroundColor = Color.parseColor("#151F28");
    //网格宽度
    protected int mGridWidth = 50;
    //小网格的宽度
    protected int mSGridWidth = 10;
    //网格颜色
    protected int mGridColor = Color.parseColor("#1b4200");


    private Paint mPaint;//画波形图的画笔
    private int mWidth;//控件宽度
    private int mHeight;//控件高度
    private int startY;  // y轴开始位置
    private Rect rect;
    private float ecgYRatio; //相对y轴的比例

    private int startX;//每次画线的X坐标起点
    private double ecgXOffset;//每次X坐标偏移的像素
    private int blankLineWidth = 6;//右侧空白点的宽度

    private static SoundPool soundPool;
    private static int soundId;//心跳提示音

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        rect = new Rect();
        converXOffset();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#7EA52F"));
        mPaint.setStrokeWidth(6);

        soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
        soundId = soundPool.load(mContext, R.raw.heartbeat, 1);

        ecgXOffset = lockWidth / ecgPerCount;
        startY = mHeight - 10;  // 坐标轴原点在左上角（0，0），向下为y轴正向
        ecgYRatio = mHeight / ecgMax;

    }

    /**
     * 根据波速计算每次X坐标增加的像素
     * <p>
     * 计算出每次锁屏应该画的px值
     */
    private void converXOffset() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        //获取屏幕对角线的长度，单位:px
        double diagonalMm = Math.sqrt(width * width + height * height) / dm.densityDpi;//单位：英寸
        diagonalMm = diagonalMm * 2.54 * 10;//转换单位为：毫米
        double diagonalPx = width * width + height * height;
        diagonalPx = Math.sqrt(diagonalPx);
        //每毫米有多少px
        double px1mm = diagonalPx / diagonalMm;
        //每秒画多少px
        double px1s = wave_speed * px1mm * 1.2;
        //每次锁屏所需画的宽度
        lockWidth = (float) (px1s * (sleepTime / 1000f));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
//        canvas.drawColor(Color.parseColor(bgColor));
        holder.unlockCanvasAndPost(canvas);
        startThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        isRunning = true;
        init();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopThread();
    }

    private void startThread() {
        isRunning = true;
        new Thread(drawRunnable).start();
    }

    private void stopThread() {
        isRunning = false;
    }

    Runnable drawRunnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                long startTime = System.currentTimeMillis();

                startDrawWave();

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < sleepTime) {
                    try {
                        Thread.sleep(sleepTime - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    private void startDrawWave() {
        rect.set(startX, 0, (int) (startX + lockWidth + blankLineWidth), mHeight);
        mCanvas = surfaceHolder.lockCanvas(rect);
        if (mCanvas == null) return;
        mCanvas.drawColor(Color.parseColor(bgColor));

        drawBackground();
        drawYaxis();
        drawWave();

        surfaceHolder.unlockCanvasAndPost(mCanvas);

        startX = (int) (startX + lockWidth);
        if (startX > mWidth) {
            startX = 0;
        }
    }

    /**
     * 画背景
     */
    private void drawBackground() {
        try {
            mCanvas.drawColor(mBackgroundColor);
            //画小网格

            //竖线个数
            int vSNum = mWidth / mSGridWidth;

            //横线个数
            int hSNum = mHeight / mSGridWidth;
            mPaint.setColor(mSGridColor);
            mPaint.setStrokeWidth(2);
            //画竖线
            for (int i = 0; i < vSNum + 1; i++) {
                mCanvas.drawLine(i * mSGridWidth, 0, i * mSGridWidth, mHeight, mPaint);
            }
            //画横线
            for (int i = 0; i < hSNum + 1; i++) {
                mCanvas.drawLine(0, i * mSGridWidth, mWidth, i * mSGridWidth, mPaint);
            }

            //竖线个数
            int vNum = mWidth / mGridWidth;
            //横线个数
            int hNum = mHeight / mGridWidth;
            mPaint.setColor(mGridColor);
            mPaint.setStrokeWidth(2);
            //画竖线
            for (int i = 0; i < vNum + 1; i++) {
                mCanvas.drawLine(i * mGridWidth, 0, i * mGridWidth, mHeight, mPaint);
            }
            //画横线
            for (int i = 0; i < hNum + 1; i++) {
                mCanvas.drawLine(0, i * mGridWidth, mWidth, i * mGridWidth, mPaint);
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 画波
     */
    private void drawWave() {
        try {
            mPaint.setColor(Color.parseColor("#7EA52F"));
            mPaint.setStrokeWidth(6);
            float mStartX = startX;
            if (ecgDatas.size() > ecgPerCount) {
//                for (int i = 0; i < ecgPerCount; i++) {
                float newX = (float) (mStartX + ecgXOffset);
                int newY = ecgConver(ecgDatas.poll());
                mCanvas.drawLine(mStartX, startY, newX, newY, mPaint);
                mStartX = newX;
                startY = newY;
//                }
            } else {
                /**
                 * 如果没有数据
                 * 因为有数据一次画ecgPerCount个数，那么无数据时候就应该画ecgPercount倍数长度的中线
                 */
                int newX = (int) (mStartX + ecgXOffset * ecgPerCount);
                int newY = mHeight - 10;
                mCanvas.drawLine(mStartX, startY, newX, newY, mPaint);
                startY = newY;
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    /**
     * 画y坐标轴
     */
    private void drawYaxis() {
        try {
            mPaint.setColor(Color.parseColor("#F5F5F5"));
            mPaint.setStrokeWidth(6);
            mPaint.setTextSize(20);

            //画竖状白线
            mCanvas.drawLine(0, 0, 0, mHeight, mPaint);

            //画纵坐标值
            mCanvas.drawText("-0", 0, mHeight - 10, mPaint);
            mCanvas.drawText("-30", 0, (mHeight - 45) * 3 / 4, mPaint);
            mCanvas.drawText("-60", 0, (mHeight - 45) / 2, mPaint);
            mCanvas.drawText("-90", 0, (mHeight - 45) / 4, mPaint);
            mCanvas.drawText("-120", 0, 35, mPaint);

            //画纵坐标名称
            mCanvas.drawText("压力值大小(mmHg)", 2, 15, mPaint);

        } catch (
                NoSuchElementException e)

        {
            e.printStackTrace();
        }

    }

    /**
     * 将心电数据转换成用于显示的Y坐标
     *
     * @param data data
     * @return 转换后的y坐标
     */
    private int ecgConver(int data) {
        data = (int) (ecgMax - data);
        data = (int) (data * ecgYRatio);
        Log.d("ecgconver data ", data + "");
        return data;
    }

    public static void addEcgData(int data) {
        ecgDatas.add(data);
    }

}