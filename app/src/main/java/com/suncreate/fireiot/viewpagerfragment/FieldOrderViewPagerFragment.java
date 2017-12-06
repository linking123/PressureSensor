package com.suncreate.fireiot.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewPageFragmentAdapter;
import com.suncreate.fireiot.base.BaseListFragment;
import com.suncreate.fireiot.base.BaseViewPagerFragment;
import com.suncreate.fireiot.bean.NewsList;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.fragment.order.FieldOrderFragment;
import com.suncreate.fireiot.interf.OnTabReselectListener;

/**
 * 订单Tab界面
 */
public class FieldOrderViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.field_order_item);

        adapter.addTab(title[0], "news", FieldOrderFragment.class,
                getBundle(NewsList.CATALOG_ALL));
        adapter.addTab(title[1], "news_week", FieldOrderFragment.class,
                getBundle(NewsList.CATALOG_INTEGRATION));
        adapter.addTab(title[2], "latest_blog", FieldOrderFragment.class,
                getBundle(NewsList.CATALOG_SOFTWARE));
        adapter.addTab(title[3], "gara_blog", FieldOrderFragment.class,
                getBundle(NewsList.CATALOG_WEEK));


    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(5);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onTabReselect() {
        Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null && fragment instanceof GeneralListFragment) {
            ((GeneralListFragment) fragment).onTabReselect();
        }
    }
}