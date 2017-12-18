package com.suncreate.pressuresensor.fragment.order;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseActionAdapter;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.general.GoodsOrdersListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.GoodsOrder;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 配件订单
 * <p>
 * desc
 */
public class GoodsOrderFragment extends GeneralListFragment<GoodsOrder> {
    public static final String TAG = "GoodsOrderFragment";
    private static final String CACHE_KEY_PREFIX = "goods_order_list_";
    private int mCatalog = 0;

    private BaseActionAdapter mBaseActionAdapter;
    private int[] positions = {1, 0, 0, 0, 0};

    @Bind(R.id.gv_service_order)
    GridView mGridView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_order;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mBaseActionAdapter = new BaseActionAdapter(getActivity(), R.array.accessory_order_item, positions);
        mGridView.setAdapter(mBaseActionAdapter);
        int size = positions.length;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int itemWidth = (screenWidth-20) / size;
        mGridView.setColumnWidth(itemWidth);
        mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mGridView.setNumColumns(size);
        mGridView.setItemChecked(mCatalog, true);
        updateAction(mCatalog);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCatalog = position;
                ((GoodsOrdersListAdapter) mAdapter).setActionPosition(position);
                if (!mIsRefresh) {
                    mIsRefresh = true;
                }
                updateAction(position);
                if (positions[position] == 1) {
                    requestEventDispatcher();
                }
            }
        });
    }

    /**
     * According to the distribution network is events
     */
    private void requestEventDispatcher() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
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
     * notify action data
     *
     * @param position position
     */
    private void updateAction(int position) {
        int len = positions.length;
        for (int i = 0; i < len; i++) {
            if (i != position) {
                positions[i] = 0;
            } else {
                positions[i] = 1;
            }
        }
        mBaseActionAdapter.notifyDataSetChanged();
    }

    /**
     * request local cache
     */
    @SuppressWarnings("unchecked")
    private void requestLocalCache() {
        mBean = (PageBean<GoodsOrder>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<GoodsOrder>());
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            mCatalog = Integer.valueOf(bundle.getString("catalog"));
        }

    }

        @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

    @Override
    public BaseListAdapter<GoodsOrder> getListAdapter() {
        return new GoodsOrdersListAdapter(this,GoodsOrderFragment.this);
    }
    @Override
    protected void requestData() {
        super.requestData();
        MonkeyApi.getOwnerPartsOrder(getOrderStatusByCatalog(), mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE,  mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<GoodsOrder>>>() {
        }.getType();
    }
    @Override
    protected void setListData(ResultBean<PageBean<GoodsOrder>> resultBean) {
        super.setListData(resultBean);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        GoodsOrder ownerPartsOrder = mAdapter.getItem(position);
        if (ownerPartsOrder != null) {
            Bundle b = new Bundle();
            b.putString("id", ownerPartsOrder.getOrderId().toString());
            b.putString("status", ownerPartsOrder.getOrderStatus().toString());
            b.putString("catalog", String.valueOf(mCatalog));
            //单条订单点击跳转到详情页
            UIHelper.showSimpleBackForResult(this, 1,SimpleBackPage.ORDER_DETAIL,b);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
        String id = data.getStringExtra("id");
        String flag = data.getStringExtra("flag");
        String catalog = data.getStringExtra("catalog");
            if("-1".equals(flag)||catalog.equals(String.valueOf(mCatalog))) {
                for (int i = 0; i < mAdapter.getDatas().size(); i++) {
                    if (mAdapter.getDatas().get(i).getOrderId().equals(id)) {
                        mAdapter.getDatas().get(i).setOrderStatus(flag);
                        mAdapter.getDatas().remove(mAdapter.getDatas().get(i));
                        break;
                    }
                }
            }else {
                mCatalog=Integer.valueOf(catalog);
                mGridView.setItemChecked(mCatalog, true);
                ((GoodsOrdersListAdapter) mAdapter).setActionPosition(mCatalog + 1);
                if (!mIsRefresh) {
                    mIsRefresh = true;
                }
                updateAction(mCatalog);
                if (positions[mCatalog] == 1) {
                    requestEventDispatcher();
                }
            }
            mAdapter.notifyDataSetChanged();
            onRefreshing();
        }
    }

    private String getOrderStatusByCatalog() {
        String orderStatus;
        String cacheSuffix;
        switch (mCatalog) {
            case 0://全部
                orderStatus = null;
                cacheSuffix = "all";
                break;
            case 1://待付款
                orderStatus = "10";
                cacheSuffix = "quote";
                break;
            case 2://待发货
                orderStatus = "20";
                cacheSuffix = "confirm";
                break;
            case 3://待收货
                orderStatus = "30";
                cacheSuffix = "account";
                break;
            case 4://待评价
                orderStatus = "40";
                cacheSuffix = "evaluate";
                break;
            default:
                orderStatus = null;
                cacheSuffix = "all";
                break;
        }
        CACHE_NAME = CACHE_KEY_PREFIX + cacheSuffix;
        return orderStatus;
    }
}
