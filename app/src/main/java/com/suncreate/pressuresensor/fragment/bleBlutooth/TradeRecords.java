package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * 训练历史记录
 *
 * @author linking
 *         created on 2018/1/4 16:04
 */
public class TradeRecords extends BaseFragment {
    protected static final String TAG = TradeRecords.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ps_trade_record, container,
                false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
    }

    @Override
    public void initData() {
    }

    @Override
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
