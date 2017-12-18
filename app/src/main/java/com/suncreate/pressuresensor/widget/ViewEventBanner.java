package com.suncreate.pressuresensor.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Banner;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import net.qiujuer.genius.blur.StackBlur;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by huanghaibin
 * on 16-5-23.
 */
public class ViewEventBanner extends RelativeLayout implements View.OnClickListener {
    private Banner banner;
    private ImageView iv_event_banner_img, iv_event_banner_bg;
    private TextView tv_event_banner_title, tv_event_banner_body;

    public ViewEventBanner(Context context) {
        super(context, null);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_event_banner, this, true);
        iv_event_banner_img = (ImageView) findViewById(R.id.iv_event_banner_img);
        iv_event_banner_bg = (ImageView) findViewById(R.id.iv_event_banner_bg);
        tv_event_banner_title = (TextView) findViewById(R.id.tv_event_banner_title);
        tv_event_banner_body = (TextView) findViewById(R.id.tv_event_banner_body);
        setOnClickListener(this);
    }

    public void initData(final RequestManager manager, final Banner banner) {
        this.banner = banner;
        tv_event_banner_title.setText(banner.getAdTitle());
        tv_event_banner_body.setText(banner.getAdText());
        manager.load(banner.getAdAccId()).into(iv_event_banner_img);
        try {
            String accId = banner.getAdAccId(); //附件id
            MonkeyApi.getPartImg(accId, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Bitmap imap = BitmapFactory.decodeByteArray(responseBody,0,responseBody.length);
                    manager.load(imap).centerCrop()
                            .transform(new BitmapTransformation(getContext()) {
                                @Override
                                protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
                                    toTransform = StackBlur.blur(toTransform, 25, true);
                                    return toTransform;
                                }

                                @Override
                                public String getId() {
                                    return "blur";
                                }
                            })
                            .into(iv_event_banner_bg);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    AppContext.showToastShort("vieweventbanner");
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        //轮播图点击
        UIHelper.showBannerDetail(getContext(), banner);
    }
}
