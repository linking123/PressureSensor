package com.suncreate.pressuresensor.adapter.general;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.ServicesOrder;
import com.suncreate.pressuresensor.fragment.order.ServicesOrderFragment;
import com.suncreate.pressuresensor.util.DatePro;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class FieldOrdersListAdapter extends BaseListAdapter<ServicesOrder> {

    private int actionPosition = 0;

    public void setActionPosition(int actionPosition) {
        this.actionPosition = actionPosition;
    }
    private Fragment mFragment;
    private int itemPosition = 0;
    private int flag;
    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public FieldOrdersListAdapter(Callback callback,Fragment fragment) {
        super(callback);
        mFragment=fragment;
    }

    @Override
    protected void convert(ViewHolder vh, ServicesOrder item, final int position) {
        final int crtRole = AppContext.getInstance().getCurrentRole();
        if (1 == crtRole) {
            vh.setImageForNet(R.id.iv_field_item_icon, MonkeyApi.getPartImgUrl(item.getImage()), R.drawable.widget_dface);
            vh.setText(R.id.tv_store_name, item.getStoreName());
        } else {
            vh.setImageForNet(R.id.iv_field_item_icon, MonkeyApi.getUserPortraitImgUrl(item.getUserId(),false), R.drawable.widget_dface);
            vh.setText(R.id.tv_store_name, item.getUserName());
        }
        vh.setText(R.id.tv_order_appointment,
                String.format(mCallback.getContext().getResources().getString(R.string.service_order_appointment),
                        DatePro.formatDay(item.getOrderAppointTimeStart(), "yyyy-MM-dd HH:mm")));
        final int orderStatus = item.getOrderStatus();
        final long orderId = item.getId();
        vh.setText(R.id.tv_order_status, Constants.convertServiceOrderStatus(orderStatus));
        if (orderStatus == 50) {
            vh.setTextColor(R.id.tv_order_status, ContextCompat.getColor(mCallback.getContext(), R.color.day_colorPrimary));
        } else if (orderStatus == 0) {
            vh.setTextColor(R.id.tv_order_status, ContextCompat.getColor(mCallback.getContext(), R.color.gray));
        } else {
            vh.setTextColor(R.id.tv_order_status, ContextCompat.getColor(mCallback.getContext(), R.color.orange_700));
        }
        Button btnOperate = vh.getView(R.id.btn_operate);
        switch (orderStatus) {
            //待报价
            case Constants.SERVICE_ORDER_TYPE.QUOTE:
                btnOperate.setText("取 消");
                break;
            //待确认
            case Constants.SERVICE_ORDER_TYPE.CONFIRM:
                if (crtRole == 1) {
                    btnOperate.setText("取 消");
                } else {
                    btnOperate.setVisibility(View.INVISIBLE);
                }
                break;
            //待结算
            case Constants.SERVICE_ORDER_TYPE.ACCOUNT:
                if (1 == crtRole) {
                    btnOperate.setText("去结算");
                } else {
                    btnOperate.setVisibility(View.INVISIBLE);
                }
                break;
            //待评价
            case Constants.SERVICE_ORDER_TYPE.EVALUATE:
                if (1 == crtRole) {
                    btnOperate.setText("去评价");
                } else {
                    btnOperate.setVisibility(View.INVISIBLE);
                }
                break;
            //已完成
            case Constants.SERVICE_ORDER_TYPE.COMPLETED:
//                btnOperate.setText("查 看");
                btnOperate.setVisibility(View.INVISIBLE);
                break;
            //已取消
            case Constants.SERVICE_ORDER_TYPE.CANCELD:
                btnOperate.setText("删 除");
                break;
            default:
//                btnOperate.setText("删 除");
                break;
        }
        vh.setOnClick(R.id.btn_operate, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16-12-21 更新订单状态
                setItemPosition(position);
                switch (orderStatus) {
                    //待报价，待确认，取消订单
//                    case Constants.SERVICE_ORDER_TYPE.QUOTE:
//                        break;
                    case Constants.SERVICE_ORDER_TYPE.CONFIRM:
                        itemPosition=position;
                        flag=0;
                        DialogHelp.getConfirmDialog(mCallback.getContext(), "提示", "确定取消该订单?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MonkeyApi.updateServiceOrder(String.valueOf(orderId), String.valueOf(flag), updateStatusHandler);
                            }
                        }).show();
                        break;
                    //去结算，结算订单
                    case Constants.SERVICE_ORDER_TYPE.ACCOUNT:
                        Bundle b = new Bundle();
                        b.putLong("id", orderId);
                        UIHelper.showSimpleBackForResult(mFragment, ServicesOrderFragment.REQUEST_CODE_SERVICE_ORDER_DETAIL, SimpleBackPage.FIELD_ORDER_DETAIL, b);
                        break;
                        //已完成，查看订单
                    case Constants.SERVICE_ORDER_TYPE.COMPLETED:
                        break;
                        //已取消，删除
                    case Constants.SERVICE_ORDER_TYPE.CANCELD:
                        itemPosition=position;
                        flag=-1;
                        DialogHelp.getConfirmDialog(mCallback.getContext(), "确定删除该订单?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MonkeyApi.updateServiceOrder(String.valueOf(orderId), "-1", updateStatusHandler);
                            }
                        }).show();
                        break;
                    //去评价，评价订单
                    case Constants.SERVICE_ORDER_TYPE.EVALUATE:
                        //TODO:去往评价页面
                        if (crtRole == 1) {
                            AppContext.showToast("暂未开通评价功能！");
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected int getLayoutId(int position, ServicesOrder item) {
        return R.layout.fragment_item_field_orders;
    }

    protected TextHttpResponseHandler updateStatusHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作失败!");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    getDatas().get(itemPosition).setOrderStatus(flag);
                    if(actionPosition!=0){
                        if(flag==0){
                            getDatas().remove(itemPosition);
                        }
                    }else{
                        if(flag==-1){
                            getDatas().remove(itemPosition);
                        }
                    }
                    notifyDataSetChanged();
                    AppContext.showToast("操作成功!");
                } else {
                    AppContext.showToast("操作失败!");
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };
}
