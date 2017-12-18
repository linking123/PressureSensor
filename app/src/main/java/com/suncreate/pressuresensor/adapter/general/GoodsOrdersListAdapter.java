package com.suncreate.pressuresensor.adapter.general;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.base.GoodsListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.GoodsOrder;
import com.suncreate.pressuresensor.bean.user.ShoppingCar;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.util.ViewUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class GoodsOrdersListAdapter extends BaseListAdapter<GoodsOrder> {

    private int actionPosition = 0;
    private int flagposition = 0;
    private int catalog = 0;
    private List<ShoppingCar> items;
    private GoodsListAdapter adapter;
    private String flag;
    private String orderStatus;
    private Fragment mFragment;
    public void setActionPosition(int actionPosition) {
        this.actionPosition = actionPosition;
    }

    public GoodsOrdersListAdapter(Callback callback,Fragment fragment) {
        super(callback);
        mFragment = fragment;
    }

    @Override
    protected void convert(ViewHolder vh, final GoodsOrder item, final int position) {

        vh.setText(R.id.store_name,item.getStoreName());
        String count=item.getCount();
        String amount=item.getOrderTotalprice();
        String trans=item.getTransPrices();
        String orderPrice="共"+count+"件商品合计￥"+amount+"（含运费￥"+trans+"）";
        vh.setText(R.id.order_amount,orderPrice);

        TextView order_status=vh.getView(R.id.order_status);
        orderStatus=item.getOrderStatus();
        flag=item.getOrderStatus();
        //初始状态
        Button btn_cancel = vh.getView(R.id.btn_cancel);
        Button btn_submit = vh.getView(R.id.btn_submit);
        btn_cancel.setVisibility(View.GONE);
        btn_submit.setVisibility(View.VISIBLE);
        if(orderStatus.equals(MonkeyApi.goodsStatus_Obligations)) {
            order_status.setText("待付款");
            btn_cancel.setVisibility(View.VISIBLE);
            btn_cancel.setText("取消订单");
            btn_submit.setText("去付款");
            catalog=1;
        }else if(orderStatus.equals(MonkeyApi.goodsStatus_Shipped)){
            order_status.setText("待发货");
            btn_submit.setVisibility(View.GONE);
            catalog=2;
        }else if(orderStatus.equals(MonkeyApi.goodsStatus_Receipt)){
            order_status.setText("卖家已发货");
            btn_submit.setText("确认收货");
            catalog=3;
        }else if(orderStatus.equals(MonkeyApi.goodsStatus_Evaluated)){
            order_status.setText("已收货");
            btn_submit.setText("评价");
            catalog=4;
        }else if(orderStatus.equals(MonkeyApi.goodsStatus_Already_Evaluated)){
            order_status.setText("已评价");
            btn_submit.setVisibility(View.GONE);
        }else if(orderStatus.equals(MonkeyApi.goodsStatus_Cancel)){
            order_status.setText("交易取消");
            btn_submit.setText("删除");
            btn_submit.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.GONE);
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getOrderStatus().equals(MonkeyApi.goodsStatus_Cancel)) {
                    DialogHelp.getConfirmDialog(mCallback.getContext(), "确定删除该订单?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            flagposition = position;
                            flag=MonkeyApi.goodsStatus_Del;
                            MonkeyApi.updateServiceOrder(item.getOrderId(),MonkeyApi.goodsStatus_Del, updateListHandler);
                        }
                    }).show();
                }
                else {
                    Bundle b = new Bundle();
                    b.putString("id", item.getOrderId().toString());
                    b.putString("status", item.getOrderStatus().toString());
                    b.putString("catalog", String.valueOf(catalog));
                    UIHelper.showSimpleBackForResult(mFragment, 1, SimpleBackPage.ORDER_DETAIL, b);
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getOrderStatus().equals(MonkeyApi.goodsStatus_Obligations)) {
                    DialogHelp.getConfirmDialog(mCallback.getContext(), "确定取消该订单?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            flagposition=position;
                            flag=MonkeyApi.goodsStatus_Cancel;
                            MonkeyApi.updateServiceOrder(item.getOrderId(),MonkeyApi.goodsStatus_Cancel, updateListHandler);
                        }
                    }).show();

                }
            }
        });

        //配件列表
        ListView mlistView= vh.getView(R.id.listView);
        items=item.getItems();
        adapter = new GoodsListAdapter(items,mCallback.getContext());
        mlistView.setAdapter(adapter);
        //设置嵌套的listview中item不允许捕获焦点，这样点击时获取的是上层listview的焦点
        //三个条件缺一不可
        mlistView.setClickable(false);
        mlistView.setPressed(false);
        mlistView.setEnabled(false);
        ViewUtils.setListViewHeightBasedOnChildren(mlistView);
    }

    protected TextHttpResponseHandler updateListHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<GoodsOrder> resultBean = AppContext.createGson().fromJson(responseString, getType());
                if (resultBean != null && resultBean.getCode()==1) {
                    getDatas().get(flagposition).setOrderStatus(flag);
                    if(actionPosition!=0){
                        if(flag.equals("0")){
                            getDatas().remove(flagposition);
                        }
                    }else{
                        if(flag.equals("-1")){
                            getDatas().remove(flagposition);
                        }
                    }
                    notifyDataSetChanged();
                    AppContext.showToast("操作成功~");
                } else {
                    AppContext.showToast("操作失败~");
                }
            } catch (Exception e) {
                AppContext.showToast("操作失败~");
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };
    protected Type getType() {
        return new TypeToken<ResultBean<GoodsOrder>>() {
        }.getType();
    }
    protected int getLayoutId(int position, GoodsOrder item) {
        return R.layout.fragment_item_store_orders;
    }
}
