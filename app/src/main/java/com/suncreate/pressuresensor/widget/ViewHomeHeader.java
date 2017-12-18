package com.suncreate.pressuresensor.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.Banner;
import com.suncreate.pressuresensor.widget.indicator.CirclePagerIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by huanghaibin
 * on 16-6-2.
 */
public class ViewHomeHeader extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private ViewPager vp_news;
    private List<ViewHomeBanner> banners = new ArrayList<>();
    private HomePagerAdapter adapter;
    private CirclePagerIndicator indicator;
    private TextView tv_news_title;
    private ScheduledExecutorService mSchedule;
    private int mCurrentItem = 0;
    private Handler handler;
    private boolean isMoving = false;
    private boolean isScroll = false;


    public ViewHomeHeader(Context context) {
        super(context);
        init(context);
    }

    public ViewHomeHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_list_news_header, this, true);
        vp_news = (ViewPager) findViewById(R.id.vp_news);
        indicator = (CirclePagerIndicator) findViewById(R.id.indicator);
//        tv_news_title = (TextView) findViewById(R.id.tv_news_title);
        adapter = new HomePagerAdapter();
        vp_news.setAdapter(adapter);
        indicator.bindViewPager(vp_news);
        new SmoothScroller(getContext()).bingViewPager(vp_news);
        vp_news.addOnPageChangeListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                vp_news.setCurrentItem(mCurrentItem);
            }
        };
        mSchedule = Executors.newSingleThreadScheduledExecutor();
        mSchedule.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (!isMoving && !isScroll) {
                    mCurrentItem = (mCurrentItem + 1) % banners.size();
                    handler.obtainMessage().sendToTarget();
                }
            }
        }, 2, 4, TimeUnit.SECONDS);

    }

    public void initData(RequestManager manager, List<Banner> banners) {
        this.banners.clear();
        for (Banner banner : banners) {
            ViewHomeBanner homeBanner = new ViewHomeBanner(getContext());
            homeBanner.initData(manager, banner);
            this.banners.add(homeBanner);
        }
        adapter.notifyDataSetChanged();
        indicator.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        isMoving = mCurrentItem != position;
    }

    @Override
    public void onPageSelected(int position) {
        isMoving = false;
        mCurrentItem = position;
        isScroll = false;
        //TODO: 新闻标题
//        tv_news_title.setText(banners.get(position).getTitle());
//        tv_news_title.setText("新闻轮播图" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        isMoving = state != ViewPager.SCROLL_STATE_IDLE;
        isScroll = state != ViewPager.SCROLL_STATE_IDLE;
    }


    private class HomePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return banners.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(banners.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(banners.get(position));
            return banners.get(position);
        }
    }
}
