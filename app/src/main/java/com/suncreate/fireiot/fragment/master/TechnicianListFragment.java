package com.suncreate.fireiot.fragment.master;

import android.app.Activity;
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
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.base.BaseSpinnerAdapter;
import com.suncreate.fireiot.adapter.general.TechnicianListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Technician;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * 技师列表
 * <p>
 * desc
 */
public class TechnicianListFragment extends GeneralListFragment<Technician> {

    public static final String ITEM_TECH = "technician";

    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    private String carBrandId;//默认车辆品牌id，根据技师专修品牌筛选技师
    private String mLevel;//技师级别
    private String mCategory;//技师类别
    private String mService;//服务分类
    private String sortName = "dist";// 排序字段名称(1评价最高 order_num接单最多dist距离最近，默认id)
    private String sortType = "asc";// 排序方式（ desc或 asc），默认desc

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

    @Bind(R.id.spn_level)
    Spinner mLevelSpinner;
    @Bind(R.id.spn_category)
    Spinner mCategorySpinner;
    @Bind(R.id.spn_service)
    Spinner mServiceSpinner;
    @Bind(R.id.spn_dimensions)
    Spinner sortNameSpinner;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        carBrandId = CarUtils.getCarBrandId();

        carBrandBox.setOnClickListener(this);

        mLevelSpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.technician_level_item)));
        mCategorySpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.technician_category_item)));
        mServiceSpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.service_item)));
        sortNameSpinner.setAdapter(new BaseSpinnerAdapter(getActivity(), getResources().getStringArray(R.array.technician_dimensions_item)));

        mLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //技师级别 1-初级 2-中极 3-高级
                mLevel = position == 0 ? "" : String.valueOf(position);
                if (!mIsRefresh)
                    onRefreshing();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //技师类别 MAINTENANCE-机修 PAINTING-钣金喷漆 CAPACITANCE-电器 BMODIFIED-美容改装 查询多个请用逗号隔开
                switch (position) {
                    case 0:
                        mCategory = null;
                        break;
                    case 1:
                        mCategory = "MAINTENANCE";
                        break;
                    case 2:
                        mCategory = "PAINTING";
                        break;
                    case 3:
                        mCategory = "CAPACITANCE";
                        break;
                    case 4:
                        mCategory = "BMODIFIED";
                        break;
                }
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
                //服务分类 CREPAIRS-汽车维修 MAINTENANCE-保养修护 BDECORATION-美容装饰 IALTERATION-安装改装
                switch (position) {
                    case 0:
                        mService = null;
                        break;
                    case 1:
                        mService = "CREPAIRS";
                        break;
                    case 2:
                        mService = "MAINTENANCE";
                        break;
                    case 3:
                        mService = "BDECORATION";
                        break;
                    case 4:
                        mService = "IALTERATION";
                        break;

                }
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
    protected int getLayoutId() {
        return R.layout.fragment_technician_list;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.car_brand_model_series_box:
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
//        CACHE_NAME = ITEM_TECH;
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
        MonkeyApi.getTechList(carBrandId, mLevel, mService, mCategory, sortType, sortName, AppContext.lon,
                AppContext.lat, null, StringUtils.isEmpty(AppContext.citycode) ? "340100" : AppContext.citycode, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
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
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Technician technician = mAdapter.getItem(position);
        if (technician != null) {
            Bundle b = new Bundle();
            b.putLong("id", technician.getId());
            b.putString("dist",technician.getDist());
            //  b.putString("dist", technician.getDist());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TECHNICIAN_DETAIL, b);
        }
    }
}
