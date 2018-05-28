package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.os.Bundle;
import android.view.View;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;

/**
 * 训练模式简介
 *
 * @author linking
 *         created on 2018/1/4 16:04
 */
public class TradeModeIntroduce extends BaseFragment {
    protected static final String TAG = TradeModeIntroduce.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ps_trade_mode_introduce;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    public void initData() {
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_register:
                break;
            default:
                break;
        }
    }
}
