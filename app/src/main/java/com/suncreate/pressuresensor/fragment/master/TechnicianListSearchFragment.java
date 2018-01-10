package com.suncreate.pressuresensor.fragment.master;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.general.TechnicianListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Technician;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.viewpagerfragment.SearchViewPageFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 技师搜索列表 Pan Zhaoxuan
 * <p>
 * desc
 */
public class TechnicianListSearchFragment extends GeneralListFragment<Technician> implements SearchViewPageFragment.Search{

    public static final String ITEM_TECH = "item_tech";
    private String mTechName = "tech";
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_technician_search_list;
    }

    /**
     * According to the distribution network is events
     */
    private void requestEventDispatcher() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            boolean connectedOrConnecting = networkInfo.isConnectedOrConnecting();
            NetworkInfo.State state = networkInfo.getState();
            if (connectedOrConnecting && state == NetworkInfo.State.CONNECTED) {
                requestData();
            } else {
                requestLocalCache();
            }
        } else {
            requestLocalCache();
        }
    }

    /**
     * request local cache
     */
    @SuppressWarnings("unchecked")
    private void requestLocalCache() {
        verifyCacheType();
        mBean = (PageBean<Technician>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<Technician>());
        }
    }

    @Override
    protected void initData() {
        CACHE_NAME = ITEM_TECH;
        super.initData();
//        requestEventDispatcher();

    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

    @Override
    protected BaseListAdapter<Technician> getListAdapter() {
        return new TechnicianListAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        if (mTechName != null) {
            MonkeyApi.getTechList(null, null, null, null,null,null,AppContext.lon,
                    AppContext.lat, mTechName, StringUtils.isEmpty(AppContext.citycode) ? "340100" : AppContext.citycode, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE,mHandler);
        }
    }
    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Technician>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Technician>> resultBean) {
        verifyCacheType();
        super.setListData(resultBean);
    }

    /**
     * verify cache type
     */
    private void verifyCacheType() {

        switch (catalog) {
            case 1:
                CACHE_NAME = ITEM_TECH;
                break;
//            case 2:
//                CACHE_NAME = QUES_SHARE;
//                break;
//            case 3:
//                CACHE_NAME = QUES_COMPOSITE;
//                break;
//            case 4:
//                CACHE_NAME = QUES_PROFESSION;
//                break;
//            case 5:
//                CACHE_NAME = QUES_WEBSITE;
//                break;
            default:
                break;
        }

    }
    @Override
    public void searchByKey(String key){

        mTechName = key;
        onRefreshing();

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Technician technician = mAdapter.getItem(position);
        if (technician != null) {
            Bundle b = new Bundle();
            b.putLong("id", technician.getId());
            b.putString("dist",technician.getDist());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TECHNICIAN_DETAIL, b);
        }
    }
}
