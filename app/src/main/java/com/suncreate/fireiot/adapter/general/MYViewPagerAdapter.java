package com.suncreate.fireiot.adapter.general;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */

public class MYViewPagerAdapter extends PagerAdapter{
    private List<View> views;

    public MYViewPagerAdapter(List<View> views){
        this.views = views;
    }
    @Override
    public void destroyItem(View container, int position, Object object) {
     ((ViewPager) container).removeView(views.get(position));
    }
    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }
    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(views.get(position),0);
        return views.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub

    }

}
