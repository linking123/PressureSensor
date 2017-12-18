package com.suncreate.pressuresensor.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.RequestManager;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Banner;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by huanghaibin
 * on 16-5-23.
 */
public class ViewNewsBanner extends RelativeLayout implements View.OnClickListener {
    private Banner banner;
    private ImageView iv_banner;
    //private TextView tv_title;

    public ViewNewsBanner(Context context) {
        super(context, null);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_news_banner, this, true);
        iv_banner = (ImageView) findViewById(R.id.iv_banner);
        //tv_title = (TextView) findViewById(R.id.tv_title);
        setOnClickListener(this);
    }

    public void initData(final RequestManager manager, final Banner banner) {
        this.banner = banner;
        //tv_title.setText(banner.getName());
        try {
            String accId = banner.getAdAccId(); //附件id
            MonkeyApi.getPartImg(accId, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Bitmap imap = BitmapFactory.decodeByteArray(responseBody,0,responseBody.length);
//                    manager.load(imap).into(iv_banner);
                    iv_banner.setImageBitmap(imap);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AppContext.showToastShort("viewnewsbanner");
                }
            });

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
//        UIHelper.showBannerDetail(getContext(), banner);
    }

    public String getTitle() {
        return banner.getAdTitle();
    }
}
