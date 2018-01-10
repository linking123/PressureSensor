package com.suncreate.pressuresensor.fragment.master;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.general.GoodsStoreGridViewAdapter;
import com.suncreate.pressuresensor.adapter.general.GoodsTypeAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Goods;
import com.suncreate.pressuresensor.bean.user.GoodsType;
import com.suncreate.pressuresensor.bean.user.ShoppingStore;
import com.suncreate.pressuresensor.fragment.base.BaseGridFragment;
import com.suncreate.pressuresensor.util.ShowContactTell;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 配件商门店列表
 * 展示配件
 * <p>
 * desc
 */
public class GoodsSupplierShopFragment extends BaseGridFragment<Goods> implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    @Bind(R.id.btn_shop_brief_introduce)
    Button mShopBriefIntroduce;
    @Bind(R.id.btn_goods_category)
    Button mGoodsCategory;
    @Bind(R.id.btn_contact_supplier)
    LinearLayout mContactSupplier;
    private String storeId;
    private List<Map<String, Object>> listitemForClass;
    private List<GoodsType> goodsClassItems;
    private String storePhone;
    private List<Map<String, Object>> listitem1;
    private ListView mlistView1;
    private GoodsTypeAdapter myAdapter1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listitem1 = new ArrayList<Map<String, Object>>();

        //接收传值
        Bundle bbb = getArguments();
        storeId = bbb.getString("storeId");
        MonkeyApi.viewStoreBriefIntroduce(storeId, mDetailHandler);
        MonkeyApi.getUserGoodsClass(storeId, null, classHandler);
    }

    protected TextHttpResponseHandler mDetailHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("网络异常，加载失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<ShoppingStore> resultBean = AppContext.createGson().fromJson(responseString, getStoreType());
                if (resultBean != null && resultBean.isSuccess()) {
                    storePhone = resultBean.getResult().getPhone();
                } else {
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mShopBriefIntroduce.setOnClickListener(this);
        mGoodsCategory.setOnClickListener(this);
        mContactSupplier.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_supplier_shop;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void requestData() {
        super.requestData();
        if (storeId != null) {
            MonkeyApi.getGoodsList(null, null, null, null, storeId, null, null, null, null,
                    mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Goods goods = mAdapter.getItem(position);
        Bundle b = new Bundle();
        b.putLong("id", Long.parseLong(goods.getId()));
        UIHelper.showSimpleBack(getContext(), SimpleBackPage.GOODS_DETAIL, b);
    }

    @Override
    protected BaseListAdapter<Goods> getListAdapter() {
        return new GoodsStoreGridViewAdapter(this);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Goods>>>() {
        }.getType();
    }

    protected Type getStoreType() {
        return new TypeToken<ResultBean<ShoppingStore>>() {
        }.getType();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_goods_category:
                if (goodsClassItems.size() > 0) {
                    showDialog();
                } else {
                    AppContext.showToast("店主未设置类别");
                }
                break;
            case R.id.btn_shop_brief_introduce:
                Bundle b2d = new Bundle();
                b2d.putString("id", storeId);
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.GOODS_SUPPLIER_SHOP_DETAIL, b2d);
                break;
            case R.id.btn_contact_supplier:
                if (null != storePhone) {
                    ShowContactTell.showContact(getContext(), storePhone);
                } else {
                    AppContext.showToast("店主有点懒，没有留下联系方式！");
                }
                break;
        }
    }

    private void initFirstListView() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("id1", "-1");
        map1.put("name1", "所有类型");
        listitem1.add(map1);
        if (goodsClassItems.size() > 1) {
            for (GoodsType goodstype : goodsClassItems) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id1", goodstype.getId());
                map.put("name1", goodstype.getTotalName());
                listitem1.add(map);
            }
        }
        myAdapter1 = new GoodsTypeAdapter(this.mContext, listitem1);
        myAdapter1.setSelectPosition(0);
        mlistView1.setAdapter(myAdapter1);
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
//                    mbtn_type.setText(text1);
                    myAdapter1.setSelectPosition(0);
                    mAdapter.notifyDataSetChanged();
                    onRefreshing();
                    return;
                }
            }
        });
    }


    TextHttpResponseHandler classHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<GoodsType>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<GoodsType>>>() {
                }.getType());
                if (resultBean != null) {
                    if (resultBean.getResult().getItems() != null) {
                        goodsClassItems = resultBean.getResult().getItems();
                    }
                } else {
                    mRefreshLayout.setNoMoreData();
                }
//                mRefreshLayout.onLoadComplete();
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    private void showDialog() {
        View inflate;
        Dialog dialog;
        dialog = new Dialog(getContext(), R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_goods_category, null);
        mlistView1 = (ListView) inflate.findViewById(R.id.listview);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        int bottonHeight = mGoodsCategory.getHeight();
        dialogWindow.getDecorView().setPadding(0, 0, 0, bottonHeight);
        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        initFirstListView();
        dialog.show();
    }
}
