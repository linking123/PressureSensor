
package com.suncreate.fireiot.fragment.master;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.StoreListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Address;
import com.suncreate.fireiot.bean.user.FillGoodsOrder;
import com.suncreate.fireiot.bean.user.GoodsOrder;
import com.suncreate.fireiot.bean.user.GoodsOrderForTrans;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.bean.user.PriceByAddress;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.bean.user.ShoppingStore;
import com.suncreate.fireiot.bean.user.StorePriceByAddress;
import com.suncreate.fireiot.bean.user.SubmitGoodsOrder;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.util.Alipay.PayResult;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 订单详情
 * <p>
 * desc
 */
public class OrderFillDetailFragment extends BaseDetailFragment<FillGoodsOrder> implements View.OnClickListener {
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    public static final int REQUEST_CODE_DEFAULT_ADDRESS = 1;
    private Dialog dialog;
    private View inflate;
    private String ids;
    private String addressInfo;
    private String areaid;
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

    @Bind(R.id.listView)
    ListView mlistView;
    List<ShoppingStore> items;
    List<SubmitGoodsOrder> submitList;
    StoreListAdapter adapter;
    String orderid;
    boolean flag;
    Double TruePrices;
    Double balance;
    RelativeLayout layout2;
    RelativeLayout layout3;
    RelativeLayout layout4;
    TextView tv_price;
    TextView tv_balance;
    TextView tv_content;
    String paylist;
    String jsonTransCost;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_fill_detail;
    }

    @Override
    protected String getCacheKey() {
        return "";
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        String buyType = b.getString("buyType");
        if (buyType.equals("buy")) {//通过直接购买跳转
            String id = b.getString("id");
            String storeId = b.getString("storeId");
            String count = b.getString("count");
            if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(storeId) && !StringUtils.isEmpty(count)) {
                MonkeyApi.addGoodsCar(storeId, id, count, "1", carHandler);
            }
        } else if (buyType.equals("shoppingcar")) {//通过购物车结算跳转
            ids = b.getString("ids");
            if (!StringUtils.isEmpty(ids)) {
                MonkeyApi.buyShoppingCar(ids, mDetailHandler);
            }
        }
    }

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

        BigDecimal balanceprice = new BigDecimal(balance);//账户余额
        Double dbbalanceprice=balanceprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        tv_balance = (TextView) inflate.findViewById(R.id.tv_balance);
        tv_balance.setText(String.valueOf(dbbalanceprice) + "元");
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //处理监听事件
                getActivity().finish();
                Bundle bundle = new Bundle();
                bundle.putString("catalog", "1");
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_ORDER, bundle);
            }
        });
    }


    protected Type getShoppingCarType() {
        return new TypeToken<ResultBean<ShoppingCar>>() {
        }.getType();
    }

    protected Type getGoodsOrderType() {
        return new TypeToken<ResultBean<GoodsOrder>>() {
        }.getType();
    }

    protected Type getGoodsOrderForTransType() {
        return new TypeToken<ResultBean<GoodsOrderForTrans>>() {
        }.getType();
    }

    protected Type getAddressType() {
        return new TypeToken<ResultBean<Address>>() {
        }.getType();
    }

    protected Type getFillGoodsOrderType() {
        return new TypeToken<ResultBean<FillGoodsOrder>>() {
        }.getType();
    }

    protected TextHttpResponseHandler carHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("数据加载失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<ShoppingCar> resultBean = AppContext.createGson().fromJson(responseString, getShoppingCarType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    ids = resultBean.getResult().getIds();
                    MonkeyApi.buyShoppingCar(ids, mDetailHandler);
                } else {
                    if (resultBean.getCode() == 3) {
                        getActivity().finish();
                        UIHelper.showLoginActivity(getActivity());
                    }
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };


    protected TextHttpResponseHandler addressHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("收货地址加载失败~");
            flag = false;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Address> resultBean = AppContext.createGson().fromJson(responseString, getAddressType());
                if (null != resultBean && resultBean.getCode() == 1) {
                    order_name.setText(StringUtils.isEmpty(resultBean.getResult().getAddressTruename()) ? "" : resultBean.getResult().getAddressTruename());
                    order_phone.setText(StringUtils.isEmpty(resultBean.getResult().getAddressMobile()) ? "" : resultBean.getResult().getAddressMobile());
                    order_location.setText(StringUtils.isEmpty(resultBean.getResult().getAddressInfo()) ? "" : resultBean.getResult().getAddressInfo());
                    areaid = resultBean.getResult().getAreaId();

                    //获取json
                    jsonTransCost = JSON.toJSONString(getStorePriceByAddressList());
                    MonkeyApi.getPriceByAddress(jsonTransCost,transPriceHandler);
                    adapter.notifyDataSetChanged();
                    flag = true;
                } else {
                    order_phone.setText("暂无收货信息");
                    order_location.setText("请添加收货地址");
                    flag = false;
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    /**
     * 运费获取json构建
     * @return
     */
    private List<StorePriceByAddress> getStorePriceByAddressList() {
        List<StorePriceByAddress> storePriceByAddressList = new ArrayList<StorePriceByAddress>();
        for (ShoppingStore shoppingStore : items) {
            StorePriceByAddress storePriceByAddress = new StorePriceByAddress();
            storePriceByAddress.setStoreId(shoppingStore.getId().toString());
            storePriceByAddress.setAddressCode(areaid);
            List<PriceByAddress> priceByAddressList = new ArrayList<PriceByAddress>();
            List<ShoppingCar> shoppingCarList = shoppingStore.getItems();//获取store中的配件列表
            for (ShoppingCar shoppingCar : shoppingCarList) {//循环获取店铺中商品id,count 构造json
                PriceByAddress priceByAddress = new PriceByAddress();
                priceByAddress.setGoodsIds(shoppingCar.getGoodsId());
                priceByAddress.setGoodsCount(shoppingCar.getGoodscartCount());
                priceByAddressList.add(priceByAddress);
            }
            storePriceByAddress.setGoodsArray(priceByAddressList);
            storePriceByAddressList.add(storePriceByAddress);
        }
        return storePriceByAddressList;
    }

    protected TextHttpResponseHandler transPriceHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("运费获取失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                final ResultBean<GoodsOrderForTrans> resultBean = AppContext.createGson().fromJson(responseString, getGoodsOrderForTransType());
                if (resultBean != null && resultBean.getCode() == 1) {
                   List<SubmitGoodsOrder> submitGoodsOrdersList= resultBean.getResult().getItems();
                    TruePrices=0.0;
                    for(SubmitGoodsOrder item : submitGoodsOrdersList){
                        int i=0;
                        for(ShoppingStore shoppingStore : items) {
                            String storeId=String.valueOf(shoppingStore.getId());
                            if(storeId.equals(item.getStoreId()))
                            {
                                BigDecimal storetransprice = new BigDecimal(Double.valueOf(item.getTransPrices()));//运费
                                double dbtransprice=storetransprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                BigDecimal storegoodsprice = new BigDecimal(Double.valueOf(items.get(i).getGoodsPrices()));//商品金额
                                double dbgoodsprice=storegoodsprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                BigDecimal storeserviceprice = new BigDecimal(Double.valueOf(items.get(i).getServicePrices()));//平台服务费
                                double dbserviceprice=storeserviceprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                                Double truePrices=dbtransprice+dbgoodsprice+dbserviceprice;//获取运费后店铺实付款重新计算
                                items.get(i).setTransPrices(item.getTransPrices());//实体运费字段重新赋值
                                items.get(i).setTruePrices(truePrices.toString());//实体店铺实付款字段重新赋值

                                TruePrices+=truePrices;//所有订单加上运费，总价重新计算
                                BigDecimal trueprice = new BigDecimal(TruePrices);//平台服务费
                                TruePrices=trueprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                btn_contact_online.setText("合计：￥" + TruePrices);//页面重新渲染实付款
                            }
                            i++;
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };
    @Override
    public void initView(View view) {
        layout1.setOnClickListener(this);
        btn_appointment.setOnClickListener(this);
    }

    @Override
    protected void executeOnLoadDataSuccess(FillGoodsOrder detail) {
        super.executeOnLoadDataSuccess(detail);
        TruePrices = detail.getTruePrices();
        btn_contact_online.setText("合计：￥" + TruePrices);
        items = detail.getItems();
        adapter = new StoreListAdapter(items, getActivity());
        mlistView.setAdapter(adapter);
        showWaitDialog();
        MonkeyApi.getAddressItem("1", 0L, addressHandler);
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
                if (flag == true) {
                    UIHelper.showSimpleBackForResult(this, REQUEST_CODE_DEFAULT_ADDRESS, SimpleBackPage.ADDRESS_SELECT);
                } else {
                    UIHelper.showSimpleBackForResult(this, REQUEST_CODE_DEFAULT_ADDRESS, SimpleBackPage.ADDRESS_ADD_USE);
                }
                break;
            case R.id.layout2:
                if (orderid.equals("more")) {
                    DialogHelp.getMessageDialog(getContext(), "暂不支持支付宝多笔订单合并支付功能，请将订单分开支付", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                } else {
                    //支付宝支付
                    showWaitDialog(R.string.msg_paying);
                    doAlipy();
                }
                break;
            case R.id.layout3:
                if (balance < TruePrices) {
                    AppContext.showToast("余额不足，请选择其他方式支付");
                } else {
                    DialogHelp.getConfirmDialog(getContext(), "是否确定支付", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //新增余额支付
                            showWaitDialog(R.string.msg_paying);
                            MonkeyApi.addPayRecord(paylist, payHandler);//新增余额支付
                        }
                    }).show();
                }
                break;
            case R.id.layout4:
                AppContext.showToast("微信支付暂未开通");
                break;
            case R.id.btn_appointment:
                showWaitDialog();
                submitOrder();
                break;
            default:
                break;
        }
    }

    private void submitOrder(){
        if (!StringUtils.isEmpty(order_name.getText().toString())) {
            submitList = new ArrayList<SubmitGoodsOrder>();
            for (int i = 0; i < items.size(); i++) {
                SubmitGoodsOrder sgo = new SubmitGoodsOrder();
                List<ShoppingCar> carlist = items.get(i).getItems();
                String ids = "";
                for (ShoppingCar item : carlist) {
                    ids += item.getId() + ",";
                }
                if (ids.length() > 1) {
                    ids = ids.substring(0, ids.length() - 1);
                }
                sgo.setIds(ids);//购物车ID集合
                sgo.setOrderInvoice(StringUtils.isEmpty(items.get(i).getOrderInvoice()) ? "" : items.get(i).getOrderInvoice());//发票抬头
                sgo.setGoodsPrices(items.get(i).getGoodsPrices());//商品总金额
                sgo.setServicePrices(items.get(i).getServicePrices());//平台服务费
                sgo.setTransPrices(items.get(i).getTransPrices());//运费
                sgo.setTruePrices(items.get(i).getTruePrices());//总价
                String storeid = items.get(i).getId().toString();
                sgo.setStoreId(storeid);//店铺id
                addressInfo = order_name.getText() + " " + order_phone.getText() + " " + order_location.getText();
                sgo.setAddressID(addressInfo);//地址
                sgo.setAddressCode(areaid);//地址
                sgo.setOrderMsg("");//备注
                submitList.add(sgo);
            }
            String actuallist = JSON.toJSONString(submitList);
            actuallist = "{\"orders\":" + actuallist + "}";
            MonkeyApi.carOrder(actuallist, submitListHandler);
        } else {
            AppContext.showToast("收货地址未选择！");
        }
    }
    protected TextHttpResponseHandler payHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("购买商品失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                final ResultBean<GoodsOrder> resultBean = AppContext.createGson().fromJson(responseString, getGoodsOrderType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    getActivity().finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("catalog", "2");
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_ORDER, bundle);
                } else {
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    protected TextHttpResponseHandler submitListHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("购买商品失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<FillGoodsOrder> resultBean = AppContext.createGson().fromJson(responseString, getFillGoodsOrderType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    List<ShoppingStore> list = resultBean.getResult().getItems();
                    List<Pay> addPayList = new ArrayList<Pay>();
                    for (ShoppingStore shoppingStore : list) {
                        Pay pay = new Pay();
                        pay.setConsumeAmount(shoppingStore.getOrderTotalprice());
                        pay.setOrderId(shoppingStore.getId().toString());
                        pay.setConsumeMode(MonkeyApi.addPayRecord_consumeMode_goods);//配件订单
                        addPayList.add(pay);
                    }
                    if (list.size() == 1) {
                        for (ShoppingStore shoppingStore : list) {
                            orderid = shoppingStore.getOrderId();
                        }
                    } else {
                        orderid = "more";
                    }
                    paylist = JSON.toJSONString(addPayList);
                    Intent intent = new Intent();
                    getActivity().setResult(ShoppingCarFragment.DESTROY, intent);
                    MonkeyApi.getMyInformation(mUserInfoHandler);
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
            super.onFinish();
            hideWaitDialog();
        }
    };
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
                    balance  = user.getUserBalance() == null ? 0L : user.getUserBalance();
                    AppContext.getInstance().setProperty("user.userBalance", balance.toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_DEFAULT_ADDRESS) {
            String address = data.getStringExtra("address");
            String phone = data.getStringExtra("phone");
            String name = data.getStringExtra("name");
            String area = data.getStringExtra("area");
            order_location.setText(address);
            order_phone.setText(phone);
            order_name.setText(name);
            if(!areaid.equals(area)) {
                areaid = area;
                jsonTransCost = JSON.toJSONString(getStorePriceByAddressList());
                showWaitDialog();
                MonkeyApi.getPriceByAddress(jsonTransCost,transPriceHandler);
            }
            flag = true;
        }
    }

    public void doAlipy() {
        //订单名称
        String subject = "购买配件";
        //商品描述
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//        String goodInfo = AppContext.getInstance().getLoginUserExt().getUserName() + "在" + sdf2.format(new Date()) + " 购买配件 " + TruePrices + "元";
        //1.获取签名信息
//        MonkeyApi.getAlipaySignInfo(orderid, subject, TruePrices.toString(), goodInfo,
//                new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        System.out.print(responseString);
//                       AppContext.showToast("支付失败");
//                    }
//
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
//                        super.onFinish();
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
}
