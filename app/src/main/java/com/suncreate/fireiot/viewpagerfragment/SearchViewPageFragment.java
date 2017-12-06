package com.suncreate.fireiot.viewpagerfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewPageFragmentAdapter;
import com.suncreate.fireiot.base.BaseViewPagerFragment;
import com.suncreate.fireiot.bean.SearchList;
import com.suncreate.fireiot.fragment.Field.FieldSearchListFragment;
import com.suncreate.fireiot.fragment.master.GoodsSearchListFragment;
import com.suncreate.fireiot.fragment.master.GoodsStoreListFragment;
import com.suncreate.fireiot.fragment.master.TechnicianListSearchFragment;
import com.suncreate.fireiot.util.TDevice;

import butterknife.Bind;

public class SearchViewPageFragment extends BaseViewPagerFragment {
    private SearchView mSearchView;
    private SearchView mSearchViewBtn;
    int mCurrentRole;
    @Bind(R.id.iv_search)
    ImageView iv_search;
    @Bind(R.id.search_src_text)
    TextView search_src_text;
    ViewPageFragmentAdapter adapter;
    @Bind(R.id.search_btn)
    Button search_btn;

    public static SearchViewPageFragment newInstance() {
        return new SearchViewPageFragment();
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        mCurrentRole = AppContext.getInstance().getCurrentRole();
        String[] title = getResources().getStringArray(1 == mCurrentRole ? R.array.search : 2 == mCurrentRole ? R.array.search_tech : R.array.search_field);
        adapter.addTab(title[0], "search_goods", GoodsSearchListFragment.class, getBundle(SearchList.CATALOG_GOODS));
        adapter.addTab(title[1], "search_business", GoodsStoreListFragment.class, getBundle(SearchList.CATALOG_BUSINESS));
        adapter.addTab(title[2], "search_tech", 2 == mCurrentRole ? FieldSearchListFragment.class : TechnicianListSearchFragment.class, getBundle(SearchList.CATALOG_TECH));
        if (1 == mCurrentRole) {
            adapter.addTab(title[3], "search_garage", FieldSearchListFragment.class, getBundle(SearchList.CATALOG_GARAGE));
        }
    }

    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString("goods", catalog);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchContent = menu.findItem(R.id.search_content);
        mSearchView = (SearchView) searchContent.getActionView();
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setMaxWidth(500);
        MenuItem searchImg = menu.findItem(R.id.search_btn);
        mSearchView.setQueryHint("请输入搜索内容");
        mSearchView.setFocusable(true);
        mSearchView.requestFocus();
        mSearchView.setIconified(false);
        final TextView textView = (TextView) mSearchView.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.GRAY);
        textView.setBackgroundResource(R.drawable.bg_event_end);
        textView.setHintTextColor(0x90ccccff);
        searchImg.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String key = textView.getText().toString();
                if (!TextUtils.isEmpty(key)) {
                    switch (mViewPager.getCurrentItem()) {
                        case 0:
                            GoodsSearchListFragment sglFrag = (GoodsSearchListFragment) mTabsAdapter.getItem(0);
                            sglFrag.searchByKey(key);
                            break;
                        case 1:
                            GoodsStoreListFragment storeFrag = (GoodsStoreListFragment) mTabsAdapter.getItem(1);
                            storeFrag.searchByKey(key);
                            break;
                        case 2:
                            if (2 == mCurrentRole) {
                                FieldSearchListFragment fieldFrag = (FieldSearchListFragment) mTabsAdapter.getItem(2);
                                fieldFrag.searchByKey(key);
                            } else {
                                TechnicianListSearchFragment telFrag = (TechnicianListSearchFragment) mTabsAdapter.getItem(2);
                                telFrag.searchByKey(key);
                            }
                            break;
                        case 3:
                            FieldSearchListFragment fieldFrag = (FieldSearchListFragment) mTabsAdapter.getItem(3);
                            fieldFrag.searchByKey(key);
                            break;
                    }

                }
                return true;
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                TDevice.hideSoftKeyboard(mSearchView);
//				search(arg0);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                return false;
            }
        });
        mSearchView.requestFocus();
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setSearch() {

    }

    private void search(String content) {
        int index = mViewPager.getChildCount();
        for (int i = 0; i < index; i++) {
//            SearchFragment fragment = (SearchFragment) getChildFragmentManager().getFragments().get(i);
//            if (fragment != null) {
//                fragment.search(content);
//            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        TextView tab_title = (TextView) mSearchView.findViewById(R.id.tab_title);
        switch (id) {
            case R.id.search_bar:

                break;
            default:
                break;
        }
    }

    /**
     * 搜索接口
     */
    public interface Search {
        public void searchByKey(String key);
    }


}











