package com.suncreate.pressuresensor.fragment.master;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.base.BaseSpinnerAdapter;
import com.suncreate.pressuresensor.adapter.general.GarageListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Garage;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.Car.CarUtils;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 快修站列表
 * <p>
 * desc
 */
public class GarageListFragment extends GeneralListFragment<Garage> {
    //
    public static final String GARAGE_ITEM = "garagelist";

    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    private String mLevel;//快修站类别[0社区快修站，1区域服务站]
    private String mService;//服务分类
    private String sortName = "dist";// 排序字段名称(1评价最高 order_num接单最多dist距离最近，默认id)
    private String sortType = "asc";// 排序方式（ desc或 asc），默认desc
    private String carBrandId;//默认车辆品牌id，根据技师专修品牌筛选快修站
    private String orderNum;//接单数量

    @Bind(R.id.spn_level)
    Spinner mLevelSpinner;
    @Bind(R.id.spn_service)
    Spinner mServiceSpinner;
    @Bind(R.id.spn_dimensions)
    Spinner sortNameSpinner;

    //从车辆管理的默认车型带过来车型,没有的话重新选择
    //选过车型，显示现有车型，也可选择更改
    @Bind(R.id.car_brand_model_series_box)
    LinearLayout carBrandBox;
    @Bind(R.id.brand_icon)
    ImageView brand_icon;
    @Bind(R.id.car_brand_text)
    TextView car_brand_text;
    @Bind(R.id.tv_choose_change)
    TextView tv_choose_change;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_garage_list;
    }

    //新建广播，接收默认车辆改变的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
                    carBrandId = AppContext.getInstance().getProperty("user.userBrandid").trim();
                    onRefreshing();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_DEFAULT_CAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        carBrandId = CarUtils.getCarBrandId();
        carBrandBox.setOnClickListener(this);

        mLevelSpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.garage_level_item)));
        mServiceSpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.service_item)));
        sortNameSpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.technician_dimensions_item)));
        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //快修站类别[0社区快修站，1区域服务站]
                mLevel = position == 0 ? "" : String.valueOf(position - 1);
                if (!mIsRefresh)
                    onRefreshing();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mServiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mService = position == 0 ? "" : Constants.getServiceTypeByPosition(position - 1);
                if (!mIsRefresh)
                    onRefreshing();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sortNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //dist距离最近 1评价最高 order_num接单最多 ，默认id
                switch (position) {
                    case 0:
                        if (AppContext.lon == null || AppContext.lat == null) {
                            Toast.makeText(getContext(),"定位信息无法获取，请打开GPS",Toast.LENGTH_SHORT).show();
                            sortName = "1";
                            sortType = "desc";
                        } else {
                            sortName = "dist";
                            sortType = "asc";
                        }
                        break;
                    case 1:
                        sortName = "1";
                        sortType = "desc";
                        break;
                    case 2:
                        sortName = "order_num";
                        sortType = "desc";
                        break;
                }
                if (!mIsRefresh)
                    onRefreshing();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.car_brand_model_series_box:
                //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型
                if (AppContext.getInstance().isLogin()) {
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_CAR_MANAGER);
                } else {
                    UIHelper.showLoginActivity(getContext());
                }
                break;
        }
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
    protected BaseListAdapter<Garage> getListAdapter() {
        return new GarageListAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        MonkeyApi.getGarageList(StringUtils.isEmpty(AppContext.citycode) ? "340100" : AppContext.citycode,orderNum, carBrandId,
                mService, mLevel, null, null, null, sortType, sortName, AppContext.lon, AppContext.lat,
                mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Garage>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Garage>> resultBean) {
        verifyCacheType();
        super.setListData(resultBean);
    }

    /**
     * verify cache type
     */
    private void verifyCacheType() {

        switch (catalog) {
            case 1:
                CACHE_NAME = GARAGE_ITEM;
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Garage garage = mAdapter.getItem(position);
        if (garage != null) {
            Bundle b = new Bundle();
            b.putString("id", garage.getId());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GARAGE_DETAIL, b);
        }
    }
}
