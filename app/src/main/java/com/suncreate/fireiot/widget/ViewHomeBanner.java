package com.suncreate.fireiot.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.RequestManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Banner;
import com.suncreate.fireiot.util.UIHelper;

/**
 * 新闻轮播图view
 */
public class ViewHomeBanner extends RelativeLayout implements View.OnClickListener {
    private Banner banner;
    private ImageView iv_banner;
    //private TextView tv_title;

    public ViewHomeBanner(Context context) {
        super(context, null);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_news_banner, this, true);
        iv_banner = (ImageView) findViewById(R.id.iv_banner);
        //tv_title = (TextView) findViewById(R.id.tv_title);
        setOnClickListener(this);
    }

    public void initData(final RequestManager manager, Banner banner) {
        this.banner = banner;
        //tv_title.setText(banner.getName());
        //TODO: 加载轮播图
         manager.load(MonkeyApi.getPartImgUrl(banner.getAdAccId())).into(iv_banner);
    }

    @Override
    public void onClick(View v) {
        UIHelper.showBannerDetail(getContext(), banner);
    }

    public String getTitle() {
        return banner.getAdTitle();
    }
}
