package com.suncreate.fireiot.fragment.order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.GoodsListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.GoodsOrder;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.util.Alipay.PayResult;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.ShowContactTell;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.util.ViewUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 配件订单详情页
 */
public class GoodsOrderDetailFragment extends BaseDetailFragment<GoodsOrder> implements View.OnClickListener {
    @Bind(R.id.orderid)
    TextView tvorderid;
    @Bind(R.id.order_location)
    TextView order_location;
    @Bind(R.id.tv_service_order_stat)
    TextView tvServiceOrderStat;
    @Bind(R.id.tv_invoice_choose)
    TextView tv_invoice_choose;
    @Bind(R.id.btn_appointment)
    Button btn_appointment;
    @Bind(R.id.store_name)
    TextView store_name;
    @Bind(R.id.order_goods_price_show)
    TextView order_goods_price_show;
    @Bind(R.id.order_service_price_show)
    TextView order_service_price_show;
    @Bind(R.id.order_trans_price_show)
    TextView order_trans_price_show;
    @Bind(R.id.order_amount_price_show)
    TextView order_amount_price_show;
    @Bind(R.id.order_time)
    TextView order_time;
    @Bind(R.id.layout8)
    LinearLayout layout8;
    @Bind(R.id.layout9)
    RelativeLayout layout9;

    @Bind(R.id.listView)
    ListView mlistView;
    List<ShoppingCar> items;
    private GoodsListAdapter goodsadapter;
    private String id;
    private String flag;
    private String catalog;
    private String orderid;
    private Dialog dialog;
    private View inflate;
    private String status;
    private Integer mOrderStatus;

    Double TruePrices;
    Double balance;
    RelativeLayout layout2;
    RelativeLayout layout3;
    RelativeLayout layout4;
    TextView tv_price;
    TextView tv_balance;
    TextView tv_content;
    String paylist;
    String storeTelephone;
    String storeId;
    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;
    @Bind(R.id.ll_contact_online)
    LinearLayout mLlContactOnline;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void initView(View view) {
        btn_appointment.setOnClickListener(this);
        mLlContactPhone.setOnClickListener(this);
        mLlContactOnline.setOnClickListener(this);
        layout9.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_detail;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected String getCacheKey() {
        return "";
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        String id = b.getString("id");
//        status = b.getString("status");
//        catalog = b.getString("catalog");
        MonkeyApi.getPartsOrderView(id, mDetailHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<GoodsOrder>>() {
        }.getType();
    }

    @Override
    protected void executeOnLoadDataSuccess(GoodsOrder detail) {
        //支付参数构建
        List<Pay> addPayList=new ArrayList<Pay>();
        Pay pay=new Pay();
        TruePrices=Double.valueOf(detail.getOrderTotalprice());
        pay.setConsumeAmount(detail.getOrderTotalprice());
        pay.setOrderId(detail.getId().toString());
        pay.setConsumeMode(MonkeyApi.addPayRecord_consumeMode_goods);//配件订单
        addPayList.add(pay);
        paylist = JSON.toJSONString(addPayList);

        super.executeOnLoadDataSuccess(detail);
        status=detail.getOrderStatus();
        mOrderStatus = Integer.valueOf(status);
        tvServiceOrderStat.setText(Constants.convertGoodsOrderStatus(mOrderStatus));
        id=String.valueOf(detail.getId());
        storeId=detail.getStoreId();
        tvorderid.setText(String.format(getString(R.string.goods_order_id), detail.getOrderId()));
        orderid=detail.getOrderId();
        storeTelephone=detail.getStoreTelephone();
        store_name.setText(detail.getStoreName());
        order_location.setText(detail.getAddress());
        tv_invoice_choose.setText(StringUtils.isEmpty(detail.getOrderInvoice())?"未开发票":String.format(getString(R.string.goods_order_invoices),detail.getOrderInvoice() ));
        order_goods_price_show.setText(detail.getOrderGoodsAmount());
        order_service_price_show.setText(detail.getOrderServicePrice());
        order_trans_price_show.setText(detail.getTransPrices());
        order_amount_price_show.setText(detail.getOrderTotalprice());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long addTimeL = Long.valueOf(detail.getAddtime());
        String demand_addtime = transferLongToDate(addTimeL);
        order_time.setText(String.format(getString(R.string.goods_order_time),demand_addtime));
        if(status.equals(MonkeyApi.goodsStatus_Cancel)){
            btn_appointment.setText("删除订单");
        }else if(status.equals(MonkeyApi.goodsStatus_Obligations)){
            btn_appointment.setText("去结算");
        }else if(status.equals(MonkeyApi.goodsStatus_Shipped)){
            layout8.setVisibility(View.GONE);
        }else if(status.equals(MonkeyApi.goodsStatus_Receipt)){
            btn_appointment.setText("确认收货");
        }else if(status.equals(MonkeyApi.goodsStatus_Evaluated)){
            btn_appointment.setText("评 价");
        }
        items = detail.getItems();
        goodsadapter = new GoodsListAdapter(items, getActivity());
        mlistView.setAdapter(goodsadapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ShoppingCar shoppingCar = items.get(position);
                Bundle b = new Bundle();
                b.putLong("id",  Long.parseLong(shoppingCar.getGoodsId()));
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_DETAIL, b);
            }
        });

        ViewUtils.setListViewHeightBasedOnChildren(mlistView);
    }

    private static String transferLongToDate(Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date date= new Date(millSec);
        return sdf.format(date);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_contact_online:
                AppContext.showToast("暂未开通在线沟通功能!");
                break;
            case R.id.ll_contact_phone:
                ShowContactTell.showContact(getContext(), storeTelephone);
                break;
            case R.id.layout2:
                showWaitDialog("支付处理中...");
                doAlipy();
                break;
            case R.id.layout3:
                if (balance < TruePrices) {
                    AppContext.showToast("余额不足，请选择其他方式支付");
                } else {
                    DialogHelp.getConfirmDialog(getContext(),"是否确定支付", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showWaitDialog("支付处理中...");
                            MonkeyApi.addPayRecord(paylist, payHandler);//新增余额支付
                        }
                    }).show();
                }
                break;
            case R.id.layout4:
                AppContext.showToast("微信支付暂未开通");
                break;
            case R.id.layout9:
                Bundle b = new Bundle();
                b.putString("storeId", storeId);
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_SUPPLIER_SHOP, b);
                break;
            case R.id.btn_appointment:
                String mes="";
                if(status.equals(MonkeyApi.goodsStatus_Cancel)){
                    mes="确定删除该订单?";
                    flag=MonkeyApi.goodsStatus_Del;
                }else if(status.equals(MonkeyApi.goodsStatus_Obligations)){
                    mes="是否确认付款?";
                    flag=MonkeyApi.goodsStatus_Shipped;
                    MonkeyApi.getMyInformation(mUserInfoHandler);
                    break;
                }
                else if(status.equals(MonkeyApi.goodsStatus_Receipt)){
                    mes="是否确认收货?";
                    flag=MonkeyApi.goodsStatus_Evaluated;
                    DialogHelp.getConfirmDialog(getContext(),mes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MonkeyApi.confirmOrder(id, updateListHandler);
                        }
                    }).show();
                    break;
                }else if(status.equals(MonkeyApi.goodsStatus_Evaluated)){
                    mes="暂未开通评价功能";
                    DialogHelp.getMessageDialog(getContext(),mes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                    break;
                }
                DialogHelp.getConfirmDialog(getContext(),mes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MonkeyApi.updateServiceOrder(id,flag, updateListHandler);
                    }
                }).show();
                break;
            default: break;
        }
    }
    private TextHttpResponseHandler mUserInfoHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("获取用户信息失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    User user = resultBean.getResult();
                    balance = user.getUserBalance();
                    show();
                } else {
                    onFailure(statusCode, headers, responseString, null);
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }

        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };
    public void doAlipy() {
        //订单名称
        String subject = "购买配件";
        //商品描述
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//        String goodInfo = AppContext.getInstance().getLoginUserExt().getUserName() + "在" + sdf2.format(new Date()) + " 购买配件 " + TruePrices + "元";
//        //1.获取签名信息
//        MonkeyApi.getAlipaySignInfo(orderid, subject, TruePrices.toString(), goodInfo,
//                new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//                    }
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                        JSONObject jsonobject = (JSONObject) JSONObject.parse(responseString);
//                        final String orderInfo = jsonobject.getJSONObject("result").get("orderInfo").toString();
//                        Runnable payRunnable = new Runnable() {
//                            @Override
//                            public void run() {
//                                PayTask alipay = new PayTask(getActivity());
//                                Map<String, String> result = alipay.payV2(orderInfo, true);
//                                Log.i("msp", result.toString());
//                                Message msg = new Message();
//                                msg.what = 1;
//                                msg.obj = result;
//                                mHandler.sendMessage(msg);
//                            }
//                        };
//                        Thread payThread = new Thread(payRunnable);
//                        payThread.start();
//                        //增加充值记录end
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        hideWaitDialog();
//                    }
//                });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        AppContext.showToast("支付成功");
                        //5.更新充值记录完成，转到完成页面
                        getActivity().finish();
                        Bundle bundle = new Bundle();
                        bundle.putString("catalog", "2");
                        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_ORDER, bundle);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        AppContext.showToast("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    protected Type getGoodsOrderType() {
        return new TypeToken<ResultBean<GoodsOrder>>() {
        }.getType();
    }
    protected TextHttpResponseHandler payHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("购买商品失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<GoodsOrder> resultBean = AppContext.createGson().fromJson(responseString, getGoodsOrderType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    //扣除客户端余额
                    double sumprice=balance-TruePrices;
                    BigDecimal bd = new BigDecimal(sumprice);
                    sumprice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    AppContext.getInstance().setProperty("user.userBalance",String.valueOf(sumprice));
                    Intent intent = new Intent();
                    intent.putExtra("flag",flag);
                    intent.putExtra("id",id);
                    intent.putExtra("catalog","2");
                    getActivity().setResult(1, intent);
                    getActivity().finish();

                } else {
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }
        @Override
        public void onFinish(){
            hideWaitDialog();
        }

    };

    public void show() {
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_goods_pay, null);
        //初始化控件
        layout2 = (RelativeLayout) inflate.findViewById(R.id.layout2);
        layout3 = (RelativeLayout) inflate.findViewById(R.id.layout3);
        layout4 = (RelativeLayout) inflate.findViewById(R.id.layout4);
//        balance = AppContext.getInstance().getLoginUserExt().getUserBalance();
        tv_price = (TextView) inflate.findViewById(R.id.tv_price);
        tv_price.setText(TruePrices.toString() + "元");
        tv_balance = (TextView) inflate.findViewById(R.id.tv_balance);
        tv_balance.setText(balance.toString() + "元");
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
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
                    Intent intent = new Intent();
                    intent.putExtra("flag",flag);
                    intent.putExtra("id",id);
                    intent.putExtra("catalog","4");
                    getActivity().setResult(1, intent);
                    getActivity().finish();
                } else {
                    AppContext.showToast("操作失败~");
                }
            } catch (Exception e) {
                AppContext.showToast("操作失败~");
                onFailure(statusCode, headers, responseString, e);
            }
        }
        @Override
        public void onFinish() {
            hideWaitDialog();
        }
    };

    @Override
    public void onDestroy() {
        Intent intent = new Intent();
        intent.putExtra("he", "HEHE");
        getActivity().setResult(3, intent);
        super.onDestroy();
    }
}
