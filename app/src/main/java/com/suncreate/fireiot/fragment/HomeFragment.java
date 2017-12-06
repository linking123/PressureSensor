package com.suncreate.fireiot.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.Banner;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.widget.ViewHomeHeader;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.suncreate.fireiot.R.id.rl_master_report;

/**
 * 首页
 */

public class HomeFragment extends BaseDetailFragment implements View.OnClickListener {
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    private static final String NEWS_BANNER = "news_banner";

    public static final int REQUEST_CODE_FOR_BRAND_CARMANAGERADDFRAGMENT = 2;
    public static final int REQUEST_CODE_FOR_BRAND_CARMANAGEREDITFRAGMENT = 3;

    @Bind(R.id.layout_loop)
    LinearLayout mLoopView;
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
    //技师入口
    @Bind(R.id.rl_master_technician)
    RelativeLayout mTechnician;
    //配件入口
    @Bind(R.id.rl_master_accessories)
    RelativeLayout mRepair;
    //场地入口
    @Bind(R.id.rl_master_repair)
    RelativeLayout mField;
    //快修站入口
    @Bind(R.id.rl_master_detection)
    RelativeLayout mDetection;
    @Bind(R.id.rl_master_insurance)
    RelativeLayout mRlMasterInsurance;
    @Bind(R.id.rl_master_place)
    RelativeLayout mRlMasterPlace;
    @Bind(R.id.rl_master_report)
    RelativeLayout mRlMasterReport;

    private ViewHomeHeader mHeaderView;
    private Handler handler = new Handler();

    //新建广播，接收默认车辆改变
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    sendRequestDataForNet();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册监听，发布完成后，返回刷新列表
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_DEFAULT_CAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        sendRequestDataForNet();
        carBrandBox.setOnClickListener(this);

        mTechnician.setOnClickListener(this);
        mField.setOnClickListener(this);
        mDetection.setOnClickListener(this);
        mRepair.setOnClickListener(this);
        mRlMasterInsurance.setOnClickListener(this);
        mRlMasterPlace.setOnClickListener(this);
        mRlMasterReport.setOnClickListener(this);

        mHeaderView = new ViewHomeHeader(getActivity());
        mExeService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    final PageBean<Banner> pageBean = (PageBean<Banner>) CacheManager.readObject(getActivity(), NEWS_BANNER);
                                    if (pageBean != null) {
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mHeaderView.initData(getImgLoader(), pageBean.getItems());
                                            }
                                        });
                                    }
                                }
                            }
        );

        mLoopView.addView(mHeaderView);
        getBannerList();
    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView iv_search = (ImageView) getActivity().findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    //头部新闻活动图片轮播
    private void getBannerList() {

        MonkeyApi.getBannerList(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Toast.makeText(getContext(), "网络错误，无法获取新闻图", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    final ResultBean<PageBean<Banner>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<Banner>>>() {
                    }.getType());
                    if (resultBean != null && resultBean.isSuccess()) {
                        mExeService.execute(new Runnable() {
                            @Override
                            public void run() {
                                CacheManager.saveObject(getActivity(), resultBean.getResult(), NEWS_BANNER);
                            }
                        });
                        mHeaderView.initData(getImgLoader(), resultBean.getResult().getItems());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            case R.id.rl_master_technician:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TECHNICIAN_LIST);
                break;
            case R.id.rl_master_accessories:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_LIST);
                break;
            case R.id.rl_master_repair:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FIELD);
                break;
            case R.id.rl_master_detection:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GARAGE_LIST);
                break;
            case R.id.rl_master_insurance:
            case R.id.rl_master_place:
                AppContext.showToast(R.string.common_data_maintain_tip);
                break;
            case rl_master_report:
                UIHelper.openBrowser(getContext(), "https://manage.fireiot.com/web/mobile/Enroll/jx");
                break;
            default:
                break;
        }
    }

    // 加菜单栏搜索
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ImageView iv_search = (ImageView) getActivity().findViewById(R.id.iv_search);
        iv_search.setVisibility(View.VISIBLE);
    }

    @Override
    protected String getCacheKey() {
        return "homefragment_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
    }

    @Override
    protected Type getType() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
