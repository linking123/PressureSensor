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
import com.suncreate.pressuresensor.adapter.general.GoodsListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Goods;
import com.suncreate.pressuresensor.bean.user.GoodsBrand;
import com.suncreate.pressuresensor.bean.user.GoodsType;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.viewpagerfragment.SearchViewPageFragment;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * 配件订单
 * <p>
 * desc
 */
public class GoodsSearchListFragment extends GeneralListFragment<Goods> implements SearchViewPageFragment.Search {

    public static final String ITEM_TECH = "";
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    private String mGoodsName = "goods";

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
        return R.layout.fragment_goods_search_list;
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
        mBean = (PageBean<Goods>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<Goods>());
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
    protected BaseListAdapter<Goods> getListAdapter() {
        return new GoodsListAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        if (mGoodsName != null) {
            MonkeyApi.getGoodsList(null, null, null, null, null
                    , null, null, null, mGoodsName, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
        }
    }
    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Goods>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Goods>> resultBean) {
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
        Goods goods = mAdapter.getItem(position);
        if (goods != null) {
            Bundle b = new Bundle();
            b.putString("id", goods.getId());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_DETAIL, b);
        }
    }

    TextHttpResponseHandler hHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String str = responseString;
            mRefreshLayout.onLoadComplete();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<GoodsType>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<GoodsType>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {

                    }
                } else {
                    //mRefreshLayout.setNoMoreData();
                }
                mRefreshLayout.onLoadComplete();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };
    TextHttpResponseHandler bHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String str = responseString;
            mRefreshLayout.onLoadComplete();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<GoodsBrand>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<GoodsBrand>>>() {
                }.getType());
//                if (resultBean != null && resultBean.isSuccess()) {
                if (resultBean != null) {
                    if (resultBean.getResult().getItems() != null) {

                    }
                } else {
                    //mRefreshLayout.setNoMoreData();
                }
                mRefreshLayout.onLoadComplete();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

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


