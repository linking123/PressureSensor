package com.suncreate.pressuresensor.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewPageFragmentAdapter;
import com.suncreate.pressuresensor.base.BaseListFragment;
import com.suncreate.pressuresensor.base.BaseViewPagerFragment;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.fragment.order.ServicesOrderFragment;
import com.suncreate.pressuresensor.interf.OnTabReselectListener;

/**
 * 订单Tab界面
 */
public class ServiceOrderViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {
   // public static final String ORDER_TYPE = "ORDER_TYPE";
  //  private String OrderType;
    public final static int CATALOG_ALL = 0;//全部
    public final static int CATALOG_WAIT_RECEIVE = 1;//待报价
    public final static int CATALOG_WAIT_SERVICE = 2;//待服务
    public final static int CATALOG_SERVICING = 3;//服务中
    public final static int CATALOG_WAIT_ACCOUNT = 4;//待结算
    public final static int CATALOG_WAIT_EVALUATE = 5;//待评价
    public final static int CATALOG_EVALUATED = 6;//已评价
    public final static int CATALOG_CANCELED = 9999;//已取消

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.service_order_item);

        adapter.addTab(title[0], "all", ServicesOrderFragment.class,
                getBundle(CATALOG_ALL));
        adapter.addTab(title[1], "wait_receive", ServicesOrderFragment.class,
                getBundle(CATALOG_WAIT_RECEIVE));
        adapter.addTab(title[2], "wait_service", ServicesOrderFragment.class,
                getBundle(CATALOG_WAIT_SERVICE));
//        adapter.addTab(title[3], "servicing", ServicesOrderFragment.class,
//                getBundle(CATALOG_SERVICING));
        adapter.addTab(title[3], "wait_account", ServicesOrderFragment.class,
                getBundle(CATALOG_WAIT_ACCOUNT));
        adapter.addTab(title[4], "wait_evaluate", ServicesOrderFragment.class,
                getBundle(CATALOG_WAIT_EVALUATE));
//        adapter.addTab(title[6], "evaluated", ServicesOrderFragment.class,
//                getBundle(CATALOG_EVALUATED));
//        adapter.addTab(title[7], "canceled", ServicesOrderFragment.class,
//                getBundle(CATALOG_CANCELED));

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

    /**
     * 基类会根据不同的catalog展示相应的数据
     *
     * @param catalog 要显示的数据类别
     * @return
     */
    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();

//        bundle.putString(BlogFragment.BUNDLE_BLOG_TYPE, catalog);
        return bundle;
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