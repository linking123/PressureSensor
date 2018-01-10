package com.suncreate.pressuresensor.fragment.requirement;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseActionAdapter;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.requirement.AccessoryRequirementListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.require.Requirement;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 配件需求
 * <p>
 * desc
 */
public class AccessoryRequirementFragment extends GeneralListFragment<Requirement> {

    public static final String TAG = "AccessoryRequirementFragment";
    private static final String CACHE_KEY_PREFIX = "accessory_require_list_";

    public static final int TYPE_ALL = 0;
    public static final int TYPE_PUBLISHED = 1;
    public static final int TYPE_QUOTED = 2;
    public static final int TYPE_COMFIRMED = 3;

    private int mCatalog = TYPE_ALL;
    private BaseActionAdapter mBaseActionAdapter;
    private int[] positions = {1, 0, 0};

    //通栏发布按钮
    private TextView mPublish;

    @Bind(R.id.gv_ques)
    GridView mGridView;

    @Bind(R.id.tv_requirement)
    TextView tv_requirement;

    private int mOrderState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != mBundle) {
            mCatalog = mBundle.getInt("mCatalog", TYPE_ALL);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_require_fragment;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        tv_requirement.setText("发布配件需求");
        tv_requirement.setOnClickListener(this);
        mBaseActionAdapter = new BaseActionAdapter(getActivity(), R.array.accessory_requirement_item, positions);
        mGridView.setAdapter(mBaseActionAdapter);
        int size = positions.length;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int itemWidth = (screenWidth - 20) / size;
        mGridView.setColumnWidth(itemWidth);
        mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mGridView.setNumColumns(size);
        mGridView.setItemChecked(mCatalog, true);
        updateAction(mCatalog);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCatalog = position;
                ((AccessoryRequirementListAdapter) mAdapter).setActionPosition(position + 1);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            //发布按钮被点击
            case R.id.tv_requirement:
                if (AppContext.getInstance().isLogin()) {
                    UIHelper.showSimpleBackForResult(AccessoryRequirementFragment.this, 1, SimpleBackPage.ACCESSORY_REQUIREMENT_PUBLISH);
                } else {
                    UIHelper.showLoginActivity(getContext());
                }
                break;
            default:
                break;
        }
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
        verifyCacheType();
        mBean = (PageBean<Requirement>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<Requirement>());
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
    protected BaseListAdapter<Requirement> getListAdapter() {
        return new AccessoryRequirementListAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        MonkeyApi.getPartRequirementList(mCatalog == 0 ? null : mCatalog, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Requirement>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Requirement>> resultBean) {
        verifyCacheType();
        super.setListData(resultBean);
    }

    /**
     * verify cache type
     */
    private void verifyCacheType() {
        String cacheSuffix;
        switch (mCatalog) {
            case TYPE_ALL://全部
                cacheSuffix = "all";
                break;
            case TYPE_PUBLISHED://待报价(新发布)
                cacheSuffix = "quote";
                break;
            case TYPE_QUOTED://已报价(待确认)
                cacheSuffix = "confirm";
                break;
//            case TYPE_COMFIRMED://已确认(生成订单)
//                cacheSuffix = "ordered";
//                break;
            default:
                cacheSuffix = "all";
                break;
        }
        CACHE_NAME = CACHE_KEY_PREFIX + cacheSuffix;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Requirement re = mAdapter.getItem(position);
        if (re != null) {
            Bundle b = new Bundle();
            b.putLong("id", Long.valueOf(re.getId()));
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.ACCESSORY_REQUIREMENT_DETAIL, b);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (2 == resultCode) {
            Integer updateRequireCatalog = data.getIntExtra("updateRequireCatalog", TYPE_ALL);
            mGridView.performItemClick(null, updateRequireCatalog, 0);
        }
    }

}