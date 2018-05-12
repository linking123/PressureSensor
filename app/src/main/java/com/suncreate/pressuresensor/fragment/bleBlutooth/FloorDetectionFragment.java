package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.Ble.CardioData;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.widget.CardiographView;
import com.suncreate.pressuresensor.widget.PathView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;

/**
 * 盆底检测
 *
 * @author linking
 *         created on 2018/5/5 22:31
 */
public class FloorDetectionFragment extends BaseFragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    protected static final String TAG = FloorDetectionFragment.class.getSimpleName();

    @Bind(R.id.cgv_card_view)
    CardiographView cardiographView;
    @Bind(R.id.pv_path_view)
    PathView pathView;
    @Bind(R.id.btn_test)
    Button btn_test;

    int detectionType; // 检测类型 0-前静息期，1-耐力检测，2-快肌检测\nIIA类肌纤维，3-快肌检测\nIIB类肌纤维，
    // 4-慢肌检测\nI类肌纤维，5-慢肌检测\nI类肌纤维
    private static final int dtcType0 = 0;
    private static final int dtcType1 = 1;
    private static final int dtcType2 = 2;
    private static final int dtcType3 = 3;
    private static final int dtcType4 = 4;

    List<CardioData> dataList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (dataList.size() != 0) {
                        pathView.setData(dataList);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ps_floor_detection;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        btn_test.setOnClickListener(this);
    }

    @Override
    public void initData() {
        dataList = new ArrayList<>();
    }

    private void startRun() {
        new Thread() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    float num = 0;

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_test:
                float random = (float) Math.random();
                CardioData data = new CardioData();
                data.setPressureData(num + 200 * random);
                data.setTime(num + 100 * random);
                dataList.add(data);
                num = num + 10;
                startRun();
//                pathView.setPlaying(true);
//                pathView.setPause(false);
//                pathView.run();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        detectionType = checkedId;
        switch (checkedId) {
            case dtcType0:
                break;
            default:
                break;
        }
    }
}
