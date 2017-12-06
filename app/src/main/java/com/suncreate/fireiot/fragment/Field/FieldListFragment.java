package com.suncreate.fireiot.fragment.Field;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.FieldListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Area;
import com.suncreate.fireiot.bean.user.Garage;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 工位列表
 */
public class FieldListFragment extends GeneralListFragment<Garage> {

    public static final String ITEM_FIELD = "FieldListFragment";
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;
    // private  long aLong = Long.parseLong(null);

    private String mLevel;//技师级别
    private String mService;//服务分类
    private String mDimensions;//距离远近
    private String storeCarmodel;
    private String orderNum;
    private String openPlace = "1";
    private String sortType = "asc";
    private String sortName = "dist";
    private String stationNum;

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
    @Bind(R.id.tv_word_stations)
    TextView tv_word_stations;
    @Bind(R.id.user_area)
    TextView user_area;
    @Bind(R.id.btn_dimensions)
    TextView btn_dimensions;

    private List<Area> areaitem;
    private PopupWindow popupwindow1;
    private PopupWindow popupwindow2;
    private PopupWindow popupwindow3;
    private GridView mgridView1;
    private SimpleAdapter myAdapter1;
    private SimpleAdapter myAdapter2;
    private SimpleAdapter myAdapter3;
    private List<Map<String, Object>> listitem1;
    private List<Map<String, Object>> listitem2;
    private List<Map<String, Object>> listitem3;
    private String[] names1;
    private String[] ids1;
    private Map<String, Object> showitem1;
    private View gridinflate;
    private String areacode;  //市级编码
    private String areacodeChild;  //地区编码

    //新建广播，接收默认车辆改变的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
                    onRefreshing();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gridinflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_selecttype, null);
        mgridView1 = (GridView) gridinflate.findViewById(R.id.gd_name);
        areacode = StringUtils.isEmpty(AppContext.citycode) ? "340100" : AppContext.citycode;
        MonkeyApi.getArea(areacode, areaHandler);//获取市级下级县区
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
        storeCarmodel = CarUtils.getCarBrandId();

        carBrandBox.setOnClickListener(this);
        tv_word_stations.setOnClickListener(this);
        user_area.setOnClickListener(this);
        btn_dimensions.setOnClickListener(this);
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
            case R.id.tv_word_stations:
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                }
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                }
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                } else {
                    showWorkPlace();
                    popupwindow1.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.user_area:
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                }
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                }
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                } else {
                    showArea();
                    popupwindow2.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.btn_dimensions:
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                }
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                }
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                } else {
                    showSelect();
                    popupwindow3.showAsDropDown(v, 0, 0);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_field_list;
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
//            onRefreshing();
        }
    }

    @Override
    protected void initData() {
//        CACHE_NAME = ITEM_FIELD;
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
        MonkeyApi.getGarageList(areacode,orderNum, storeCarmodel, null, null, openPlace, null, stationNum, sortType,
                sortName, AppContext.lon, AppContext.lat, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
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
                CACHE_NAME = ITEM_FIELD;
                break;
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
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FIELD_DETAIL, b);
        }
    }

    public void showArea() {
        listitem1 = new ArrayList<Map<String, Object>>();
        //填充对话框的布局
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id1", areacode);
        map1.put("name1", "区域");
        listitem1.add(map1);
        for (Area items : areaitem) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id1", items.getXzqhCode());
            map.put("name1", items.getName());
            listitem1.add(map);
        }
        if (listitem1.size() > 1) {
            myAdapter1 = new SimpleAdapter(getContext(), listitem1, R.layout.fragment_item_selecttype, new String[]{"name1", "id1"}, new int[]{R.id.text1, R.id.id1});
            mgridView1.setAdapter(myAdapter1);
            mgridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                    String id1 = (String) tvid1.getText();
                    TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                    String text1 = (String) tvtext1.getText();
                    if (text1.length() > 4) {
                        text1 = text1.substring(0, 4);
                    }
                    user_area.setText(text1);
                    areacodeChild = id1;
                    areacode = areacodeChild;
                    popupwindow2.dismiss();
                    mAdapter.notifyDataSetChanged();
                    onRefreshing();
                }
            });
        }
        popupwindow2 = new PopupWindow(gridinflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupwindow2.setFocusable(true);
        popupwindow2.setOutsideTouchable(true);
        // 自定义view添加触摸事件
        gridinflate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                    popupwindow2 = null;
                }
                return false;
            }
        });
    }

    TextHttpResponseHandler areaHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String str = responseString;
            mRefreshLayout.onLoadComplete();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<Area>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<Area>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (resultBean.getResult().getItems() != null) {
                        areaitem = resultBean.getResult().getItems();
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

    public void showWorkPlace() {
        //填充对话框的布局
        names1 = new String[]{"工位数", "1-3个", "4-6个", "7个以上"};
        ids1 = new String[]{null, "1", "2", "3"};
        listitem1 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names1.length; i++) {
            showitem1 = new HashMap<String, Object>();
            showitem1.put("name1", names1[i]);
            showitem1.put("id1", ids1[i]);
            listitem1.add(showitem1);
        }
        myAdapter1 = new SimpleAdapter(getContext(), listitem1, R.layout.fragment_item_selecttype, new String[]{"name1", "id1"}, new int[]{R.id.text1, R.id.id1});
        mgridView1.setNumColumns(4);
        mgridView1.setAdapter(myAdapter1);
        mgridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                String id1 = (String) tvid1.getText();
                stationNum = id1;
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }
                tv_word_stations.setText(text1);
                popupwindow1.dismiss();
                mAdapter.notifyDataSetChanged();
                onRefreshing();
            }
        });
        popupwindow1 = new PopupWindow(gridinflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow1.setFocusable(true);
        popupwindow1.setOutsideTouchable(true);
        // 自定义view添加触摸事件
        gridinflate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                    popupwindow1 = null;
                }

                return false;
            }
        });
    }

    public void showSelect() {
        //填充对话框的布局
        names1 = new String[]{"离我最近", "评价最高", "接单最多"};
        ids1 = new String[]{"dist", "1", "order_num"};
        listitem1 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names1.length; i++) {
            showitem1 = new HashMap<String, Object>();
            showitem1.put("name1", names1[i]);
            showitem1.put("id1", ids1[i]);
            listitem1.add(showitem1);
        }
        myAdapter3 = new SimpleAdapter(getContext(), listitem1, R.layout.fragment_item_selecttype, new String[]{"name1", "id1"}, new int[]{R.id.text1, R.id.id1});
        mgridView1.setNumColumns(3);
        mgridView1.setAdapter(myAdapter3);
        mgridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                String id1 = (String) tvid1.getText();
                sortName = id1;
                if ("dist".equals(sortName)) {
                    if (AppContext.lon == null || AppContext.lat == null) {
                        Toast.makeText(getContext(), "定位信息无法获取，请打开GPS", Toast.LENGTH_SHORT).show();
                        sortName = "1";
                        sortType = "desc";
                    } else {
                        sortName = "dist";
                        sortType = "asc";
                    }
                } else {
                    sortType = "desc";
                }
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }
                btn_dimensions.setText(text1);
                popupwindow3.dismiss();
                mAdapter.notifyDataSetChanged();
                onRefreshing();
            }
        });
        popupwindow3 = new PopupWindow(gridinflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow3.setFocusable(true);
        popupwindow3.setOutsideTouchable(true);
        // 自定义view添加触摸事件
        gridinflate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                    popupwindow3 = null;
                }
                return false;
            }
        });
    }
}
