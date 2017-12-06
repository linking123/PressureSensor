package com.suncreate.fireiot.fragment.master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.ShoppingCarParentListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.bean.user.ShoppingStore;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 购物车详情
 * <p>
 * desc
 */
public class ShoppingCarFragment extends GeneralListFragment<ShoppingStore> {
    @Bind(R.id.cb4)
    CheckBox che_all;
    @Bind(R.id.sum_goods_price)
    TextView sum_goods_price;
    @Bind(R.id.tvids)
    TextView tvids;
    @Bind(R.id.btn_del)
    Button btn_del;
    @Bind(R.id.btn_save)
    Button btn_save;
    private String ids = "";
    private double sumprice=0.0;
    public static int DESTROY=1;

    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;

    public static String topType = "";
    public static String middleType = "";
    public static String belowType = "";
    ShoppingCarFragment instance=this;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void initWidget(View root) {
        btn_del.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        //全局变量重新加载页面需要初始化
        topType="";
        middleType = "";
        belowType = "";
        super.initWidget(root);
        AllChecks();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shoppingcar_list;
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


    @Override
    public void onRefreshing() {
        mIsRefresh = true;
        requestData();
    }

    /**
     * request local cache
     */
    @SuppressWarnings("unchecked")
    private void requestLocalCache() {
//        verifyCacheType();
        mBean = (PageBean<ShoppingStore>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<ShoppingStore>());
        }
    }

    @Override
    protected void initData() {
        CACHE_NAME = "";
        super.initData();
    }

    /**
     * 全选check监听
     */
    public void AllChecks() {
        ids="";
        che_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//true：全选被选中，false：全选取消
                    if (ShoppingCarFragment.topType.equals("")) {
                        //如果全选，则将店铺与店铺下所有配件设置为选中状态，并将金额统计为sumprice与所有配件id累加为ids
                        for (int i = 0; i < mAdapter.getDatas().size(); i++) {
                            String id="";
                            Double price=0.0;
                            mAdapter.getDatas().get(i).setChecks(true);
                            for (int j = 0; j < mAdapter.getDatas().get(i).getItems().size(); j++) {
                                ShoppingCar car = mAdapter.getDatas().get(i).getItems().get(j);
                                car.setChecks(true);
                                id += car.getId() + ",";
                                price += Double.valueOf(car.getGoodscartPrice()) * Long.valueOf(car.getGoodscartCount());
                            }
                            mAdapter.getDatas().get(i).setIds(id);
                            mAdapter.getDatas().get(i).setSumprice(price);
                            ids+=id;
                            sumprice+=price;
                        }
                    }
                    ShoppingCarFragment.topType = "top";
                    ShoppingCarFragment.middleType = "middle";
                    mAdapter.notifyDataSetChanged();

                } else {
                    if (ShoppingCarFragment.topType.equals("top")) {
                        for (int i = 0; i < mAdapter.getDatas().size(); i++) {
                            // 如果取消全选，那么就将列表的每一行都不选中
                            mAdapter.getDatas().get(i).setChecks(false);
                            mAdapter.getDatas().get(i).setIds("");
                            mAdapter.getDatas().get(i).setSumprice(0.0);
                            for(int j=0;j<mAdapter.getDatas().get(i).getItems().size();j++){
                                mAdapter.getDatas().get(i).getItems().get(j).setChecks(false);
                            }
                        }
                        ids="";
                        sumprice=0.0;
                        ShoppingCarFragment.topType = "";
                    }
                    mAdapter.notifyDataSetChanged();
                }
                BigDecimal bd = new BigDecimal(sumprice);
                sumprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                sum_goods_price.setText("合计：￥" + sumprice);
            }
        });
    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

   public static BaseListAdapter<ShoppingStore> adapter;

    @Override
    protected BaseListAdapter<ShoppingStore> getListAdapter() {
        adapter = new ShoppingCarParentListAdapter(this);
        adapter.setmItemOnCheckListener(new BaseListAdapter.ItemOnCheckListener() {
            @Override
            public void noticeCheck(boolean isChecked) {
                sumprice = 0.0;
                isChecked = false;
                ids = "";
                int i = 0, j = 0;
                //金额在店铺对象sumprice中，获取所有店铺
                for (ShoppingStore sc : adapter.getDatas()) {
                    if(sc.getChecks()==null){
                        j++;
                    }
                    else {
                        if (sc.getChecks().equals(true)) {
                            i++;
                        }
                        if (sc.getChecks().equals(false)) {
                            j++;
                        }
                    }
                    sumprice += sc.getSumprice();
                    if (!StringUtils.isEmpty(sc.getIds())) {
                        ids += sc.getIds();
                    }
                }

                if (adapter.getDatas().size() == i) {
                    isChecked = true;
                    ShoppingCarFragment.topType = "top";
                } else if (adapter.getDatas().size() == j && StringUtils.isEmpty(ids)) {
                    isChecked = false;
                    ShoppingCarFragment.topType = "";
                } else if (i > 0 && i < adapter.getDatas().size()) {
                    isChecked = false;
                    ShoppingCarFragment.middleType = "middle";
                } else if (adapter.getDatas().size() == j && !StringUtils.isEmpty(ids)) {
                    ShoppingCarFragment.belowType = "below";
                }
                che_all.setChecked(isChecked);
                BigDecimal bd = new BigDecimal(sumprice);
                sumprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                sum_goods_price.setText("合计：￥" + sumprice);
                if (!StringUtils.isEmpty(ids)) {
                    ids = ids.substring(0, ids.length() - 1);
                    tvids.setText(ids);
                }
                sumprice = 0.0;
            }
        });
        return adapter;
    }

    @Override
    protected void requestData() {
        super.requestData();
//        verifyCacheType();
        MonkeyApi.getShoppingCar(mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<ShoppingStore>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<ShoppingStore>> resultBean) {
//        verifyCacheType();
        super.setListData(resultBean);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        ShoppingStore shoppingCar = mAdapter.getItem(position);
        if (shoppingCar != null) {
            Bundle b = new Bundle();
            b.putString("id", shoppingCar.getId().toString());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TECHNICIAN_DETAIL, b);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_del:
                if (StringUtils.isEmpty(ids)) {
                    AppContext.showToast("请选择需要删除的配件");
                } else {
                    MonkeyApi.deleteShoppingCar(ids, tHandler);
                }
                break;
            case R.id.btn_save:
                if(StringUtils.isEmpty(ids)){
                    AppContext.showToast("请选择需要结算的配件");
                }else {
                    Bundle b = new Bundle();
                    if(ids.length()>1){
                        if(ids.substring(ids.length()-1,ids.length()).equals(","))
                        ids=ids.substring(0,ids.length()-1);
                    }
                    b.putString("ids", ids);
                    b.putString("buyType","shoppingcar");
                    UIHelper.showSimpleBackForResult(ShoppingCarFragment.this,ShoppingCarFragment.DESTROY, SimpleBackPage.ORDER_FILL_DETAIL, b);
                }
                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DESTROY){
            onRefreshing();
            sumprice=0.0;
            che_all.setChecked(false);
            BigDecimal bd = new BigDecimal(sumprice);
            sumprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            sum_goods_price.setText("合计：￥" + sumprice);
        }
    }

    TextHttpResponseHandler tHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String str = responseString;
            mRefreshLayout.onLoadComplete();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<ShoppingCar> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<ShoppingCar>>() {
                }.getType());
                if (resultBean != null) {
                    int code = resultBean.getCode();
                    if (code == 1) {
                        mAdapter.notifyDataSetChanged();
                        initData();
                        sumprice=0.0;
                        che_all.setChecked(false);
                        BigDecimal bd = new BigDecimal(sumprice);
                        sumprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        sum_goods_price.setText("合计：￥" + sumprice);
                    } else {
                        AppContext.showToast("购物车删除失败~");
                    }
                }
                mRefreshLayout.onLoadComplete();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };
}
