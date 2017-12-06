package com.suncreate.fireiot.fragment.master;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.GoodsListAdapter;
import com.suncreate.fireiot.adapter.general.GoodsTypeFirstAdapter;
import com.suncreate.fireiot.adapter.general.GoodsTypeSecondAdapter;
import com.suncreate.fireiot.adapter.general.GoodsTypeThirdAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Goods;
import com.suncreate.fireiot.bean.user.GoodsBrand;
import com.suncreate.fireiot.bean.user.GoodsType;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;


/**
 * 配件订单
 * <p>
 * desc
 */
public class GoodsListFragment extends GeneralListFragment<Goods> {

    public static final String ITEM_TECH = "goodslist";
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    @Bind(R.id.goods_item)
    FrameLayout goods_item;
    @Bind(R.id.fl_goods_type)
    FrameLayout mFlGoodsType;
    @Bind(R.id.superRefreshLayout)
    SwipeRefreshLayout superRefreshLayout;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.icon_line_2)
    View icon_line_2;
    @Bind(R.id.btn_type)
    TextView mbtn_type;
    @Bind(R.id.btn_brand)
    TextView mbtn_brand;
    @Bind(R.id.btn_dimensions)
    TextView btn_dimensions;
    @Bind(R.id.et_search)
    TextView et_search;
    @Bind(R.id.bt_search)
    TextView bt_search;
    @Bind(R.id.user_area)
    TextView user_area;
    @Bind(R.id.goods_linearLayout)
    LinearLayout goods_linearLayout;

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

    private View inflate;
    private View gridinflate;
    private PopupWindow popupwindow1;
    private PopupWindow popupwindow2;
    private PopupWindow popupwindow3;
    private PopupWindow popupwindow4;
    private ListView mlistView1;
    private ListView mlistView2;
    private ListView mlistView3;
    private GridView mgridView1;
    private SimpleAdapter myAdapter1;
    private SimpleAdapter myAdapter2;
    private SimpleAdapter myAdapter3;
    private SimpleAdapter myAdapter4;
    private List<Map<String, Object>> listitem1;
    private List<Map<String, Object>> listitem2;
    private List<Map<String, Object>> listitem3;
    private String[] names1;
    private String[] ids1;
    private Map<String, Object> showitem1;
    private List<GoodsType> goodsTypesitem;
    private List<GoodsBrand> goodsBrandsitem;

    private String usergoodsclass = null;
    private String storeId = null;
    private String brandIds = null;//默认车辆品牌id
    private String sortType = null;
    private String sortName = null;
    private String goodsBrandId = null;
    private String xzqhCode = null;
    private String goodsName = null;
    private String cateId = null;

    private GoodsTypeFirstAdapter firstAdapter;
    private GoodsTypeSecondAdapter secondAdapter;
    private GoodsTypeThirdAdapter thirdAdapter;
    /**
     * 回调接口对象
     */
//    private OnPopupWindowClickListener listener;

    //新建广播，接收默认车辆改变的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
                    brandIds = AppContext.getInstance().getProperty("user.userBrandid").trim();
                    onRefreshing();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    private OnPopupWindowClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_type, null);
        mlistView1 = (ListView) inflate.findViewById(R.id.listView1);
        mlistView2 = (ListView) inflate.findViewById(R.id.listView2);
        mlistView3 = (ListView) inflate.findViewById(R.id.listView3);
        gridinflate = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_selecttype, null);
        mgridView1 = (GridView) gridinflate.findViewById(R.id.gd_name);
        listitem1 = new ArrayList<Map<String, Object>>();
        listitem2 = new ArrayList<Map<String, Object>>();
        listitem3 = new ArrayList<Map<String, Object>>();
        MonkeyApi.getGoodsType(null, null, null, null, hHandler);//获取所有类别
        MonkeyApi.getGoodsBrand(cateId, bHandler);//获取所有配件品牌

        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_DEFAULT_CAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        brandIds = CarUtils.getCarBrandId();
        carBrandBox.setOnClickListener(this);

        mbtn_type.setOnClickListener(this);
        mbtn_brand.setOnClickListener(this);
        user_area.setOnClickListener(this);
        btn_dimensions.setOnClickListener(this);
        bt_search.setOnClickListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_list;
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
        }
    }

    @Override
    protected void initData() {
        super.initData();


    }

    public void setOnPopupWindowClickListener(OnPopupWindowClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopupWindowClickListener {
        /**
         * 当点击PopupWindow的ListView 的item的时候调用此方法，用回调方法的好处就是降低耦合性
         *
         * @param position 位置
         */
        void onPopupWindowItemClick(int position);

    }

    private void initFirstListView() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id1", "-1");
        map1.put("name1", "所有类型");
        listitem1.add(map1);
        for (GoodsType goodstype : goodsTypesitem) {
            if (goodstype.getTotalLevel().equals("0")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id1", goodstype.getId());
                map.put("name1", goodstype.getTotalName());
                listitem1.add(map);
            }
        }
        firstAdapter = new GoodsTypeFirstAdapter(this.mContext, listitem1);
        firstAdapter.setSelectPosition(0);
        mlistView1.setAdapter(firstAdapter);
        initSecondListView();
        mlistView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                String id1 = (String) tvid1.getText();
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }
                if (position == 0) {
                    mbtn_type.setText(text1);
                    cateId = null;
                    firstAdapter.setSelectPosition(0);
                    listitem2.clear();
                    listitem3.clear();
                    secondAdapter = new GoodsTypeSecondAdapter(mContext, listitem2);
                    mlistView2.setAdapter(secondAdapter);
                    thirdAdapter = new GoodsTypeThirdAdapter(mContext, listitem3);
                    mlistView3.setAdapter(thirdAdapter);
                    secondAdapter.notifyDataSetChanged();
                    thirdAdapter.notifyDataSetChanged();
                    myAdapter1=null;
                    mbtn_brand.setText("品牌");
                    goodsBrandId=null;
                    popupwindow1.dismiss();
                    mAdapter.notifyDataSetChanged();
                    onRefreshing();
                    return;
                }
                updataListView(position, id1, text1);
                if (listener != null) {
                    listener.onPopupWindowItemClick(position);
                }
            }
        });

        popupwindow1 = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, 1000);
        popupwindow1.setFocusable(true);
        popupwindow1.setOutsideTouchable(true);
        // 自定义view添加触摸事件
        inflate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                    popupwindow1 = null;
                }

                return false;
            }
        });
    }

    private void initSecondListView() {
        listitem2.clear();
        secondAdapter = new GoodsTypeSecondAdapter(this.mContext, listitem2);
        mlistView2.setAdapter(secondAdapter);
        secondAdapter.notifyDataSetChanged();
        mlistView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                String id1 = (String) tvid1.getText();
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }
                updataListView2(position, id1, text1);
                if (listener != null) {
                    listener.onPopupWindowItemClick(position);
                }
            }
        });

        initThirdListView();
    }

    private void initThirdListView() {
        //更新第三ListView
        listitem3.clear();
        thirdAdapter = new GoodsTypeThirdAdapter(this.mContext, listitem3);
        thirdAdapter.notifyDataSetChanged();
        mlistView3.setAdapter(thirdAdapter);
        mlistView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                String id1 = (String) tvid1.getText();
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }

                if (listener != null) {
                    listener.onPopupWindowItemClick(position);
                }

                thirdAdapter.setSelectPosition(position);
                mbtn_type.setText(text1);
                cateId = id1;
                popupwindow1.dismiss();
                mAdapter.notifyDataSetChanged();
                onRefreshing();
            }
        });
    }

    private void updataListView(int position, String id1, String text1) {
        listitem2.clear();
        firstAdapter.setSelectPosition(position);
        firstAdapter.notifyDataSetChanged();
        //更新第二ListView
        for (GoodsType goodstype : goodsTypesitem) {
            if (goodstype.getTotalLevel().equals("1")) {
                if (goodstype.getTotalParentId().equals(id1)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id1", goodstype.getId());
                    map.put("name1", goodstype.getTotalName());
                    listitem2.add(map);
                }
            }
        }
        if (listitem2.size() > 0) {
            secondAdapter.setDatas(listitem2);
            secondAdapter.setSelectPosition(0);
            secondAdapter.notifyDataSetChanged();
        } else {
            mbtn_type.setText(text1);
            cateId = id1;
            popupwindow1.dismiss();
            mAdapter.notifyDataSetChanged();
            onRefreshing();
        }
        listitem3.clear();
        thirdAdapter.setDatas(listitem3);
        thirdAdapter.notifyDataSetChanged();
    }

    private void updataListView2(int position, String id1, String text1) {
        listitem3.clear();
        secondAdapter.setSelectPosition(position);
        secondAdapter.notifyDataSetChanged();
        //更新第三ListView
        for (GoodsType goodstype : goodsTypesitem) {
            if (goodstype.getTotalLevel().equals("2")) {
                if (goodstype.getTotalParentId().equals(id1)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id1", goodstype.getId());
                    map.put("name1", goodstype.getTotalName());
                    listitem3.add(map);
                }
            }
        }
        if (listitem3.size() > 0) {
            thirdAdapter.setDatas(listitem3);
            thirdAdapter.setSelectPosition(0);
            thirdAdapter.notifyDataSetChanged();
        } else {
            mbtn_type.setText(text1);
            cateId = id1;
            popupwindow1.dismiss();
            mAdapter.notifyDataSetChanged();
            onRefreshing();
        }
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

    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss:S");

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        if (StringUtils.isEmpty(sortType) && StringUtils.isEmpty(sortName)) {
            sortType = "desc";
            sortName = "1";
        }
        brandIds = StringUtils.isEmpty(brandIds) ? null : brandIds;
        MonkeyApi.getGoodsList(goodsBrandId, brandIds, usergoodsclass, cateId, storeId, sortType, sortName
                , xzqhCode, goodsName, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
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
            b.putLong("id", Long.parseLong(goods.getId()));
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_DETAIL, b);
        }
    }

    TextHttpResponseHandler hHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            mRefreshLayout.onLoadComplete();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<GoodsType>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<GoodsType>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                        goodsTypesitem = resultBean.getResult().getItems();
                        initFirstListView();
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
            mRefreshLayout.onLoadComplete();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<GoodsBrand>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<GoodsBrand>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (resultBean.getResult().getItems() != null) {
                        goodsBrandsitem = resultBean.getResult().getItems();
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
            case R.id.car_brand_model_series_box:
                //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型
                if (AppContext.getInstance().isLogin()) {
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_CAR_MANAGER);
                } else {
                    UIHelper.showLoginActivity(getContext());
                }
                break;
            case R.id.btn_type:
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                }
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                }
                if (popupwindow4 != null && popupwindow4.isShowing()) {
                    popupwindow4.dismiss();
                }
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                } else {
                    popupwindow1.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.btn_brand:
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                }
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                }
                if (popupwindow4 != null && popupwindow4.isShowing()) {
                    popupwindow4.dismiss();
                }
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                } else {
                    if (!StringUtils.isEmpty(cateId) && cateId != "-1") {
                        showBrand();
                        if (myAdapter1 != null) {
                            popupwindow2.showAsDropDown(v, 0, 0);
                        } else {
                            AppContext.showToast("该分类下无品牌");
                        }
                    } else {
                        AppContext.showToast("请先选择配件分类");
                    }
                }
                break;
            case R.id.user_area:
                if (popupwindow1 != null && popupwindow1.isShowing()) {
                    popupwindow1.dismiss();
                }
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                }
                if (popupwindow4 != null && popupwindow4.isShowing()) {
                    popupwindow4.dismiss();
                }
                if (popupwindow3 != null && popupwindow3.isShowing()) {
                    popupwindow3.dismiss();
                } else {
                    showArea();
                    popupwindow3.showAsDropDown(v, 0, 0);
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
                }
                if (popupwindow4 != null && popupwindow4.isShowing()) {
                    popupwindow4.dismiss();
                } else {
                    showSelect();
                    popupwindow4.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.bt_search:
                if (StringUtils.isEmpty(et_search.getText().toString())) {
                    AppContext.showToast("请填写搜索信息");
                } else {
                    goodsName = et_search.getText().toString();
                    onRefreshing();
                }
            default:
                break;
        }
    }

    public void showBrand() {
        listitem1 = new ArrayList<Map<String, Object>>();
        //填充对话框的布局
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id1", "-1");
        map1.put("name1", "品牌");
        listitem1.add(map1);
        for (GoodsBrand goodsBrand : goodsBrandsitem) {
            if (cateId.equals(goodsBrand.getClassId())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id1", goodsBrand.getId());
                map.put("name1", goodsBrand.getGoodsbrandName());
                listitem1.add(map);
            }
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
                    mbtn_brand.setText(text1);
                    if (id1.equals("-1")) {
                        goodsBrandId = null;
                    } else {
                        goodsBrandId = id1;
                    }
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

    public void showArea() {
        //填充对话框的布局
        names1 = new String[]{"全国", StringUtils.isEmpty(AppContext.city) ? "合肥市" : AppContext.city};
        ids1 = new String[]{null, StringUtils.isEmpty(AppContext.citycode) ? "340100" : AppContext.citycode};
        listitem1 = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names1.length; i++) {
            showitem1 = new HashMap<String, Object>();
            showitem1.put("name1", names1[i]);
            showitem1.put("id1", ids1[i]);
            listitem1.add(showitem1);
        }
        myAdapter2 = new SimpleAdapter(getContext(), listitem1, R.layout.fragment_item_selecttype, new String[]{"name1", "id1"}, new int[]{R.id.text1, R.id.id1});
        mgridView1.setAdapter(myAdapter2);
        mgridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvid1 = (TextView) view.findViewById(R.id.id1);
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }
                user_area.setText(text1);
                popupwindow3.dismiss();
                xzqhCode = StringUtils.isEmpty(tvid1.getText().toString()) ? null : tvid1.getText().toString();
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

    public void showSelect() {
        //填充对话框的布局
        names1 = new String[]{"评价最高", "价格由低到高", "价格由高到低"};
        ids1 = new String[]{"1", "2", "3"};
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
                if (id1.equals("1")) {
                    sortType = "desc";
                    sortName = "1";
                }
                if (id1.equals("2")) {
                    sortType = "asc";
                    sortName = "2";
                }
                if (id1.equals("3")) {
                    sortType = "desc";
                    sortName = "2";
                }
                TextView tvtext1 = (TextView) view.findViewById(R.id.text1);
                String text1 = (String) tvtext1.getText();
                if (text1.length() > 4) {
                    text1 = text1.substring(0, 4);
                }
                btn_dimensions.setText(text1);
                popupwindow4.dismiss();
                mAdapter.notifyDataSetChanged();
                onRefreshing();
            }
        });
        popupwindow4 = new PopupWindow(gridinflate, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupwindow4.setFocusable(true);
        popupwindow4.setOutsideTouchable(true);
        // 自定义view添加触摸事件
        gridinflate.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow4 != null && popupwindow4.isShowing()) {
                    popupwindow4.dismiss();
                    popupwindow4 = null;
                }

                return false;
            }
        });
    }
}
