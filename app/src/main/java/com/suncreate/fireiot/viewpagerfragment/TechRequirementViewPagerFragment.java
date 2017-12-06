package com.suncreate.fireiot.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewPageFragmentAdapter;
import com.suncreate.fireiot.base.BaseListFragment;
import com.suncreate.fireiot.base.BaseViewPagerFragment;
import com.suncreate.fireiot.bean.NewsList;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementFragment;
import com.suncreate.fireiot.fragment.requirement.ServiceRequirementFragment;
import com.suncreate.fireiot.interf.OnTabReselectListener;

/**
 * 需求Tab界面
 */
public class TechRequirementViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.tech_requirement_viewpager_arrays);

        adapter.addTab(title[0], "news", ServiceRequirementFragment.class,
                getBundle(NewsList.CATALOG_ALL));
        adapter.addTab(title[1], "latest_blog", AccessoryRequirementFragment.class,
                getBundle(NewsList.CATALOG_WEEK));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
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

    //加菜单栏合肥，搜索
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ImageView iv_search=(ImageView)getActivity().findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
    }
//    //加菜单栏发布需求按钮
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_activity_menu,menu);
//        menu.findItem(R.id.action_publish_serviceRequirement).setVisible(true);
//        menu.findItem(R.id.action_city).setVisible(false);
////        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
//        switch (id){
//            case R.id.action_publish_serviceRequirement:
//                if(fragment instanceof ServiceRequirementFragment){
//                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.SERVICE_REQUIREMENT_PUBLISH);
//                } else{
//                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.ACCESSORY_REQUIREMENT_PUBLISH);
//                }
//                break;
//            default:
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onTabReselect() {
        Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null && fragment instanceof GeneralListFragment) {
            ((GeneralListFragment) fragment).onTabReselect();
        }
    }
}