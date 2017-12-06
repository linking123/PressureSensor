package com.suncreate.fireiot.fragment.Field;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.FieldListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Garage;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.viewpagerfragment.SearchViewPageFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 场地列表
 */
public class FieldSearchListFragment extends GeneralListFragment<Garage> implements SearchViewPageFragment.Search{

    public static final String ITEM_FIELD ="item_field";
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;
    private String mFieldName = "fieldName";
   // private  long aLong = Long.parseLong(null);

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
        return R.layout.fragment_field_search_list;
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
        mBean = (PageBean<Garage>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {

            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<Garage>());
            onRefreshing();
        }
    }

    @Override
    protected void initData() {
        CACHE_NAME = ITEM_FIELD;
        super.initData();
    //    requestEventDispatcher();

    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

    @Override
    protected BaseListAdapter<Garage> getListAdapter() {
        return new FieldListAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        if (mFieldName !=null)
        {
            MonkeyApi.getGarageList(null, null, null, null, null, null, mFieldName, null,null, null,AppContext.lon,
                    AppContext.lat, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Garage>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Garage>> resultBean) {
        CACHE_NAME = ITEM_FIELD;
        verifyCacheType();
        super.setListData(resultBean);
    }

    /**
     * verify cache type
     */
    private void verifyCacheType() {

        switch (catalog) {
            case 1:
                CACHE_NAME = ITEM_FIELD;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Garage field = mAdapter.getItem(position);
        if (field != null) {
            Bundle b = new Bundle();
            b.putString("id", field.getId());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GARAGE_DETAIL, b);
        }
    }

    @Override
    public void searchByKey(String key){
                   mFieldName = key;
                   onRefreshing();
    }
}
