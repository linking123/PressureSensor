package com.suncreate.fireiot.fragment.order;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseActionAdapter;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.FieldOrdersListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.ServicesOrder;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 场地订单
 * <p>
 * desc
 */
public class FieldOrderFragment extends GeneralListFragment<ServicesOrder> {

    public static final String TAG = "FieldOrderFragment";
    private static final String CACHE_KEY_PREFIX = "field_order_list_";

    public static final int REQUEST_CODE_SERVICE_ORDER_DETAIL = 1;
    public static final int RESULT_CODE_SERVICE_ORDER_DETAIL = 1;


    private int mCatalog = 0;
    private BaseActionAdapter mBaseActionAdapter;
    private int[] positions = {1, 0, 0, 0};

    @Bind(R.id.gv_service_order)
    GridView mGridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != mBundle) {
            mCatalog = mBundle.getInt("mCatalog", 0);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service_order;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mBaseActionAdapter = new BaseActionAdapter(getActivity(), R.array.field_order_item, positions);
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
                ((FieldOrdersListAdapter) mAdapter).setActionPosition(position);
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
        getOrderStatusByCatalog();
        mBean = (PageBean<ServicesOrder>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<ServicesOrder>());
//            onRefreshing();
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

    @Override
    protected BaseListAdapter<ServicesOrder> getListAdapter() {
        return new FieldOrdersListAdapter(this,FieldOrderFragment.this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        getOrderStatusByCatalog();
        MonkeyApi.getPlaceOrderList(getOrderStatusByCatalog(), mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<ServicesOrder>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<ServicesOrder>> resultBean) {
        getOrderStatusByCatalog();
        super.setListData(resultBean);
    }

    private Integer getOrderStatusByCatalog() {
        Integer orderStatus;
        String cacheSuffix;
        switch (mCatalog) {
            case 0://全部(另含已完成，已取消)
                orderStatus = null;
                cacheSuffix = "all";
                break;
            case 1://已预约
                orderStatus = Constants.SERVICE_ORDER_TYPE.CONFIRM;
                cacheSuffix = "confirm";
                break;
            case 2://待结算
                orderStatus = Constants.SERVICE_ORDER_TYPE.ACCOUNT;
                cacheSuffix = "account";
                break;
            case 3://待评价
                orderStatus = Constants.SERVICE_ORDER_TYPE.EVALUATE;
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        ServicesOrder so = mAdapter.getItem(position);
        if (so != null) {
            Bundle b = new Bundle();
            b.putLong("id", so.getId());
            UIHelper.showSimpleBackForResult(this, REQUEST_CODE_SERVICE_ORDER_DETAIL, SimpleBackPage.FIELD_ORDER_DETAIL, b);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SERVICE_ORDER_DETAIL && resultCode == RESULT_CODE_SERVICE_ORDER_DETAIL) {
            Integer updateOrderStatus = data.getIntExtra("updateOrderStatus", 0);
            int position;
            switch (updateOrderStatus) {
                case 20:
                    position = 1;
                    break;
                case 30:
                    position = 2;
                    break;
                case 40:
                    position = 3;
                    break;
                default:
                    position = 0;
                    break;
            }
            mGridView.performItemClick(null, position, 0);
        }
    }
}
