package com.suncreate.fireiot.fragment.master;

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
import com.suncreate.fireiot.adapter.general.GoodsStoreListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.GoodsStore;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.viewpagerfragment.SearchViewPageFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * 配件商店铺列表
 * <p>
 * desc
 */
public class GoodsStoreListFragment extends GeneralListFragment<GoodsStore> implements SearchViewPageFragment.Search {

    public static final String ITEM_TECH = "";
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    private String mGoodsName = "goodsStore";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void initWidget(View root) {
//        mbtn_type.setOnClickListener(this);
//        mbtn_brand.setOnClickListener(this);
//        user_area.setOnClickListener(this);
//        btn_dimensions.setOnClickListener(this);
//        bt_search.setOnClickListener(this);
        super.initWidget(root);
//        MonkeyApi.getGoodsType(0, hHandler);
//        MonkeyApi.getGoodsBrand("0", bHandler);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_store_list;
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
        mBean = (PageBean<GoodsStore>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<GoodsStore>());
//            onRefreshing();
        }
    }

    @Override
    protected void initData() {
        super.initData();
//        requestEventDispatcher();

    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

    @Override
    protected BaseListAdapter<GoodsStore> getListAdapter() {
        return new GoodsStoreListAdapter(this);

    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        if (mGoodsName != null) {
            MonkeyApi.getGoodsStoreList(mGoodsName, null, null, null, null, null, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
//            MonkeyApi.getGoodsNewList(null, null, null, null, null
//                    , null, null, null, null, mGoodsName, null, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
        }
    }
    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<GoodsStore>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<GoodsStore>> resultBean) {
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
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        GoodsStore goods = mAdapter.getItem(position);
        if (goods != null) {
            Bundle b = new Bundle();
            b.putString("storeId", goods.getId());
          //  b.putString("storeId","13");
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_SUPPLIER_SHOP, b);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {


            default:
                break;
        }
    }

    @Override
    public void searchByKey(String key) {
        mGoodsName = key;
        onRefreshing();
    }

}


