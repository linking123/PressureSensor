package com.suncreate.pressuresensor;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.suncreate.pressuresensor.adapter.general.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initDots();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.app_start_first, null));
        views.add(inflater.inflate(R.layout.app_start_second, null));
        views.add(inflater.inflate(R.layout.app_start_third, null));
        views.add(inflater.inflate(R.layout.app_start_fourth, null));

        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.id_viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[4];

        //循环取得小点图片
        for (int i = 0; i < 4; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }


    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > 3 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    //当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    //当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    //当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        //设置底部小点选中状态
        setCurDot(arg0);
    }
}