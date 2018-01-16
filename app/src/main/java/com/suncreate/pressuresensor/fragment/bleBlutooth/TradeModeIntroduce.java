package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.PressureSensorApi;
import com.suncreate.pressuresensor.base.BaseActivity;
import com.suncreate.pressuresensor.base.BaseFragment;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.ui.LoginActivity;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.TypeChk;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ps_trade_mode_introduce, container,
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
