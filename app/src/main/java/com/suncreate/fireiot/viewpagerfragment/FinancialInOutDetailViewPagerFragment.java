package com.suncreate.fireiot.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewPageFragmentAdapter;
import com.suncreate.fireiot.base.BaseListFragment;
import com.suncreate.fireiot.base.BaseViewPagerFragment;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterPaymentDetailListFragment;
import com.suncreate.fireiot.interf.OnTabReselectListener;

/**
 * 收支明细Tab界面
 */
public class FinancialInOutDetailViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    public static final int CATALOG_ALL =0;  //全部
    public static final int CATALOG_IN = 1;  //收入
    public static final int CATALOG_OUT = 2;  //支出

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.financial_states);

        adapter.addTab(title[0], "all", MyFinancialCenterPaymentDetailListFragment.class,
                getBundle(FinancialInOutDetailViewPagerFragment.CATALOG_ALL));
        adapter.addTab(title[1], "in", MyFinancialCenterPaymentDetailListFragment.class,
                getBundle(FinancialInOutDetailViewPagerFragment.CATALOG_IN));
        adapter.addTab(title[2], "out", MyFinancialCenterPaymentDetailListFragment.class,
                getBundle(FinancialInOutDetailViewPagerFragment.CATALOG_OUT));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(2);
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