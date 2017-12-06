package com.suncreate.fireiot.fragment.master;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.GoodsListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.FillGoodsOrder;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.bean.user.ShoppingStore;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.util.ViewUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * 订单详情
 * <p>
 * desc
 */
public class OrderViewDetailFragment extends BaseDetailFragment<ShoppingStore> implements View.OnClickListener {
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    public static final int REQUEST_CODE_DEFAULT_ADDRESS = 1;
    private Dialog dialog;
    private View inflate;
    private String ids;
    private String addressId;
    @Bind(R.id.layout1)
    RelativeLayout layout1;
    @Bind(R.id.order_name)
    TextView order_name;
    @Bind(R.id.order_phone)
    TextView order_phone;
    @Bind(R.id.order_location)
    TextView order_location;

    @Bind(R.id.btn_contact_online)
    TextView btn_contact_online;
    @Bind(R.id.btn_appointment)
    TextView btn_appointment;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.order_goods_price_show)
    TextView order_goods_price_show;
    @Bind(R.id.order_service_price_show)
    TextView order_service_price_show;
    @Bind(R.id.order_trans_price_show)
    TextView order_trans_price_show;
    @Bind(R.id.order_amount_price_show)
    TextView order_amount_price_show;

    @Bind(R.id.listView)
    ListView mlistView;
    List<ShoppingCar> items;
    GoodsListAdapter adapter;
    String address;
    boolean flag;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_view_detail;
    }

    @Override
    protected String getCacheKey() {
        return "";
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        String id=b.getString("id");
        MonkeyApi.getPartsOrderView(id, mDetailHandler);
    }
    @Override
    public void initView(View view) {
        layout1.setOnClickListener(this);
        btn_appointment.setOnClickListener(this);
    }

    @Override
    protected void executeOnLoadDataSuccess(ShoppingStore detail) {
        super.executeOnLoadDataSuccess(detail);
        String truePrices = String.valueOf(detail.getTruePrices());
        if (!StringUtils.isEmpty(truePrices)) {
            btn_contact_online.setText("合计：￥"+truePrices.trim());
        }

        //店铺名称
        String getStoreName = String.valueOf(detail.getStoreName());
        if (!StringUtils.isEmpty(getStoreName)) {
            title.setText(getStoreName.trim());
        }
        //商品金额
        String getGoodsPrice = String.valueOf(detail.getOrderGoodsAmount());
        if (!StringUtils.isEmpty(getGoodsPrice)) {
            order_goods_price_show.setText(getGoodsPrice.trim());
        }
        //平台服务费
        String getServicePrice= String.valueOf(detail.getOrderGoodsAmount());
        if (!StringUtils.isEmpty(getServicePrice)) {
            order_service_price_show.setText(getServicePrice.trim());
        }
        //运费
        String getTransPrice= String.valueOf(detail.getTransPrices());
        if (!StringUtils.isEmpty(getTransPrice)) {
            order_trans_price_show.setText(getTransPrice.trim());
        }
        //实付款
        String getTotalPrice= String.valueOf(detail.getOrderTotalprice());
        if (!StringUtils.isEmpty(getTotalPrice)) {
            order_amount_price_show.setText(getTotalPrice.trim());
        }

        items=detail.getItems();
        adapter = new GoodsListAdapter(items,getActivity());
        mlistView.setAdapter(adapter);
        ViewUtils.setListViewHeightBasedOnChildren(mlistView);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<FillGoodsOrder>>() {
        }.getType();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout1:
                if(flag==true) {
                    UIHelper.showSimpleBackForResult(this, REQUEST_CODE_DEFAULT_ADDRESS, SimpleBackPage.ADDRESS_SELECT);
                }
                else{
                    UIHelper.showSimpleBackForResult(this,REQUEST_CODE_DEFAULT_ADDRESS,SimpleBackPage.ADDRESS_ADD_USE);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_DEFAULT_ADDRESS){
            String address = data.getStringExtra("address");
            String phone = data.getStringExtra("phone");
            String name = data.getStringExtra("name");
            String area = data.getStringExtra("area");
            order_location.setText(area+" "+address);
            order_phone.setText(phone);
            order_name.setText(name);

            flag=true;
        }
    }

}
