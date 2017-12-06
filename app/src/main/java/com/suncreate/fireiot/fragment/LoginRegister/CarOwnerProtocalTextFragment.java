package com.suncreate.fireiot.fragment.LoginRegister;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.fragment.base.BaseFragment;

/**
 * 技师详情
 * <p>
 * desc
 */
public class CarOwnerProtocalTextFragment extends BaseFragment implements View.OnClickListener {


    @Override
    protected void initWidget(View root) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reg_carowner_protocal;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = (WebView) getView().findViewById(R.id.webview_statement);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setBackgroundColor(Color.TRANSPARENT);  //  WebView 背景透明效果
    }

    @Override
    public void onClick(View v) {

    }
}
