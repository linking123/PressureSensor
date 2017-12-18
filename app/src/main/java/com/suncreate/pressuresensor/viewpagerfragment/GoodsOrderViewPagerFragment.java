package com.suncreate.pressuresensor.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewPageFragmentAdapter;
import com.suncreate.pressuresensor.base.BaseListFragment;
import com.suncreate.pressuresensor.base.BaseViewPagerFragment;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.interf.OnTabReselectListener;

/**
 * 订单Tab界面
 */
public class GoodsOrderViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = null;
        if (isAdded()){
            title = getResources().getStringArray(
                    R.array.accessory_order_item);
        }

//        adapter.addTab(title[0], "all", GoodsOrderFragment.class,
//                getBundle(GoodsOrderFragment.CATALOG_ALL));
//        adapter.addTab(title[1], "obligation", GoodsOrderFragment.class,
//                getBundle(GoodsOrderFragment.CATALOG_OBLIGATION));
//        adapter.addTab(title[2], "shipped", GoodsOrderFragment.class,
//                getBundle(GoodsOrderFragment.CATALOG_SHIPPED));
//        adapter.addTab(title[3], "eceipt", GoodsOrderFragment.class,
//                getBundle(GoodsOrderFragment.CATALOG_RECEIPT));
//        adapter.addTab(title[4], "evaluated", GoodsOrderFragment.class,
//                getBundle(GoodsOrderFragment.CATALOG_EVALUATED));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(1);
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