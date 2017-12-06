package com.suncreate.fireiot.fragment.order;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseActivity;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.bean.user.QuoteSettle;
import com.suncreate.fireiot.bean.user.QuoteSettleProject;
import com.suncreate.fireiot.bean.user.ServicesOrderDetail;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.meidia.TweetPicturesPreviewer;
import com.suncreate.fireiot.util.Alipay.PayResult;
import com.suncreate.fireiot.util.DatePro;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.widget.CommonPay;
import com.loopj.android.http.TextHttpResponseHandler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.text.InputType.TYPE_NULL;
import static com.suncreate.fireiot.R.id.btn_submit_report;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.ORDER_ACTION;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.ORDER_VIEW;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.RESULT_CODE_SETTLE;
import static com.suncreate.fireiot.fragment.order.ServicesOrderFragment.RESULT_CODE_SERVICE_ORDER_DETAIL;
import static java.util.regex.Pattern.compile;

/**
 * 技师填写结算单
 */
public class FieldOrderSettleFragment extends BaseFragment implements TextWatcher {

    @Bind(R.id.tv_order_id)
    TextView mTvOrderId;
    @Bind(R.id.tv_take_photo)
    TextView mTvTakePhoto;
    @Bind(R.id.iv_report_photo)
    ImageView mIvReportPhoto;
    @Bind(R.id.recycler_images)
    TweetPicturesPreviewer mLayImages;
    @Bind(R.id.et_parts_amount)
    EditText mEtPartsAmount;
    @Bind(R.id.et_working_amount)
    EditText mEtWorkingAmount;
    @Bind(R.id.et_place_amount)
    EditText mEtPlaceAmount;
    @Bind(R.id.et_accessory_amount)
    EditText mEtAccessoryAmount;
    @Bind(R.id.tv_total_amount)
    TextView mTvTotalAmount;
    @Bind(btn_submit_report)
    Button mBtnSubmitReport;
    @Bind(R.id.tv_title_photo)
    TextView mTvTitlePhoto;
    @Bind(R.id.tv_title_amount)
    TextView mTvTitleAmount;
    @Bind(R.id.ll_title_photo)
    LinearLayout mLlTitlePhoto;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_title_time)
    LinearLayout llTitleTime;
    private ServicesOrderDetail mDetail;
    private QuoteSettle mQuoteSettle;
    int mCurrentRole;
    private Double mUserBalance;
    private Double mTotalPrice;
    private double mTotalAmount;
    private double[] mAmountArray = {0, 0, 0, 0};
    private int mAction;
    String paylist;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (null != bundle) {
            mAction = bundle.getInt("action", 0);
            if (mAction == ORDER_ACTION) {
                ((BaseActivity) getActivity()).setActionBarTitle(R.string.service_order_settle_action);
            } else {
                ((BaseActivity) getActivity()).setActionBarTitle(R.string.service_order_settle_view);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_field_order_quotation;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mEtPartsAmount.addTextChangedListener(this);
        mEtWorkingAmount.addTextChangedListener(this);
        mEtPlaceAmount.addTextChangedListener(this);
        mEtAccessoryAmount.addTextChangedListener(this);
        mTvTitlePhoto.setText(getString(R.string.str_title_photo_settle));
        mTvTitleAmount.setText(getString(R.string.str_title_amount_settle));
    }

    @Override
    protected void initData() {
        super.initData();
        mCurrentRole = AppContext.getInstance().getCurrentRole();
        //结算单不需要上传照片
        mLlTitlePhoto.setVisibility(View.GONE);
        mTvTakePhoto.setVisibility(View.GONE);
        mLayImages.setVisibility(View.GONE);
        mIvReportPhoto.setVisibility(View.GONE);
        mDetail = getBundleSerializable("fieldOrder");

        List<Pay> addPayList = new ArrayList<Pay>();
        Pay pay = new Pay();
        mTotalPrice=mDetail.getOrderTotalprice();
        pay.setConsumeAmount(String.valueOf(mDetail.getOrderTotalprice()));
        pay.setOrderId(mDetail.getId().toString());
        pay.setConsumeMode(MonkeyApi.addPayRecord_consumeMode_service);//服务订单
        addPayList.add(pay);
        paylist = JSON.toJSONString(addPayList);

        if(null==mDetail.getOrderSettlementTime()){
            llTitleTime.setVisibility(View.GONE);
        }else {
            tvTime.setText("填写结算单时间：" + DatePro.formatDay(mDetail.getOrderSettlementTime(), "yyyy-MM-dd HH:mm"));
        }
        if (mDetail != null) {
            mTvOrderId.setText(mDetail.getOrderId());
            // 已报价或者非技师，只能查看报价单
            if (mAction == ORDER_VIEW) {
                MonkeyApi.repairReport(String.valueOf(mDetail.getId()), Constants.QUOTE_TYPE.SETTLE, mDetailHandler);
                showWaitDialog();
                mEtPartsAmount.setEnabled(false);
                mEtPartsAmount.setInputType(TYPE_NULL);
                mEtWorkingAmount.setEnabled(false);
                mEtWorkingAmount.setInputType(TYPE_NULL);
                mEtPlaceAmount.setEnabled(false);
                mEtPlaceAmount.setInputType(TYPE_NULL);
                mEtAccessoryAmount.setEnabled(false);
                mEtAccessoryAmount.setInputType(TYPE_NULL);
                if (mCurrentRole == 1) {//如果是车主
                    if (mDetail.getOrderStatus().equals(30)) {//已经填写过结算单,未结算时
                        mBtnSubmitReport.setText("确认结算");
                    } else {
                        mBtnSubmitReport.setVisibility(View.GONE);//已结算
                    }
                } else {
                    mBtnSubmitReport.setVisibility(View.GONE);
                }
            } else {
                if (mDetail.getPhotoStatus() == 4) {//已上传结算单
                    MonkeyApi.repairReport(String.valueOf(mDetail.getId()), Constants.QUOTE_TYPE.SETTLE, mDetailHandler);
                } else {//未上传，直接显示
                    mEtPartsAmount.setText("0.00");
                    mEtWorkingAmount.setText("0.00");
                    mEtPlaceAmount.setText("0.00");
                    mEtAccessoryAmount.setText("0.00");
                }
                mBtnSubmitReport.setVisibility(View.VISIBLE);
            }
        } else {
            AppContext.showToast("加载失败");
        }
    }
    private View.OnClickListener wechatPay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppContext.showToast("暂未开通微信支付!");
        }
    };
    private View.OnClickListener aliPay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doAlipay();
        }
    };
    public void doAlipay() {
        //订单名称
        String subject = "服务订单";
        //商品描述
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        StringBuffer sb = new StringBuffer(256);
//        sb.append(AppContext.getInstance().getLoginUserExt().getUserName()).append("在").append(sdf2.format(new Date()))
//                .append("向服务订单").append(mDetail.getOrderId()).append("支付").append(mTotalPrice).append("元");
//
//        //1.获取签名信息
//        MonkeyApi.getAlipaySignInfo(mDetail.getOrderId(), subject, mTotalPrice.toString(), sb.toString(),
//                new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        AppContext.showToast("支付宝付款失败！");
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                        try {
//                            JSONObject jsonobject = (JSONObject) JSONObject.parse(responseString);
//                            final String orderInfo = jsonobject.getJSONObject("result").get("orderInfo").toString();
//                            Runnable payRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    PayTask alipay = new PayTask(getActivity());
//                                    Map<String, String> result = alipay.payV2(orderInfo, true);
//                                    Log.i("msp", result.toString());
//                                    Message msg = new Message();
//                                    msg.what = 1;
//                                    msg.obj = result;
//                                    mHandler.sendMessage(msg);
//                                }
//                            };
//                            Thread payThread = new Thread(payRunnable);
//                            payThread.start();
//                            //增加充值记录end
//                        } catch (Exception e) {
//                            onFailure(statusCode, headers, responseString, e);
//                        }
//
//                    }
//                });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            try {
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
                            Intent intent = new Intent();
                            intent.putExtra("updateOrderStatus", 40);
                            getActivity().setResult(RESULT_CODE_SERVICE_ORDER_DETAIL, intent);
                            getActivity().finish();

                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            AppContext.showToast("支付宝支付失败");
                        }
                        break;
                    }
                    default:
                        break;
                }
            } catch (Exception e) {
                AppContext.showToast("支付宝支付失败");
            }
        }
    };

    private View.OnClickListener balancePay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserBalance < mTotalPrice) {
                AppContext.showToast("余额不足，请选择其他方式支付");
            } else {
                DialogHelp.getConfirmDialog(getContext(), "是否确定支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //新增余额支付
                        showWaitDialog(R.string.msg_paying);
                        MonkeyApi.addPayRecord(paylist, mPayHandler);
                    }
                }).show();
            }
        }
    };

    private TextHttpResponseHandler mPayHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("余额付款失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    //扣除客户端余额
                    double remainPrice = mUserBalance - mTotalPrice;
                    BigDecimal bd = new BigDecimal(remainPrice);
                    remainPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    AppContext.getInstance().setProperty("user.userBalance", String.valueOf(remainPrice));
                    AppContext.showToast("操作成功!");
                    Intent intent = new Intent();
                    intent.putExtra("updateOrderStatus", 40);
                    getActivity().setResult(RESULT_CODE_SERVICE_ORDER_DETAIL, intent);
                    getActivity().finish();
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

    @OnClick({R.id.btn_submit_report})
    void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_submit_report:
                if (mCurrentRole == 1) {//车主确认报价
                    DialogHelp.getConfirmDialog(getContext(), "确认结算", "请仔细核对结算单,是否确认结算？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认结算,先获取用户账户信息，跳转支付，服务端更新订单状态
                            showWaitDialog();
                            MonkeyApi.getMyInformation(mUserInfoHandler);
                        }
                    }).show();
                } else {
                    String orderId = mTvOrderId.getText().toString();
                    String partsAmount = mEtPartsAmount.getText().toString();
                    String workingAmount = mEtWorkingAmount.getText().toString();
                    String placeAmount = mEtPlaceAmount.getText().toString();
                    String accessoryAmount = mEtAccessoryAmount.getText().toString();
                    Pattern pattern = compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
                    if (TextUtils.isEmpty(orderId)) {
                        AppContext.showToast("订单信息有误");
                        return;
                    }
                    if (!pattern.matcher(partsAmount).matches()) {
                        AppContext.showToast("配件费用输入有误！");
                        return;
                    }
                    if (!pattern.matcher(workingAmount).matches()) {
                        AppContext.showToast("工时费用输入有误！");
                        return;
                    }
                    if (!pattern.matcher(placeAmount).matches()) {
                        AppContext.showToast("场地费用输入有误！");
                        return;
                    }
                    if (!pattern.matcher(accessoryAmount).matches()) {
                        AppContext.showToast("辅料费用输入有误！");
                        return;
                    }
                    //TODO:一期技师报价结算只添加默认项目
                    QuoteSettle qs = new QuoteSettle();
                    List<QuoteSettleProject> list = new ArrayList<>();
                    qs.setId(String.valueOf(mDetail.getId()));
                    qs.setType(Constants.QUOTE_TYPE.SETTLE);
                    qs.setPlaceAmount(placeAmount);
                    qs.setAccessoriesAmount(accessoryAmount);
                    qs.setGoodsAmount(partsAmount);
                    qs.setWorkingAmount(workingAmount);
                    qs.setTotalAmount(String.valueOf(mTotalAmount));
                    qs.setItems(list);
                    showWaitDialog(R.string.progress_submit);
                    MonkeyApi.techOfferSSettle(JSON.toJSONString(qs), mSubmitHandler);
                }
                break;
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
                    mUserBalance = user.getUserBalance();
                    CommonPay.showCommonPay(getActivity(), mTotalPrice, mUserBalance, aliPay, balancePay, wechatPay);
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
    protected TextHttpResponseHandler mDetailHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("加载失败!");
            mEtPartsAmount.setText("0.00");
            mEtWorkingAmount.setText("0.00");
            mEtPlaceAmount.setText("0.00");
            mEtAccessoryAmount.setText("0.00");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<QuoteSettle> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<QuoteSettle>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    mQuoteSettle = resultBean.getResult();
                    mEtPartsAmount.setText(TextUtils.isEmpty(mQuoteSettle.getGoodsAmount()) ? "0.00" : mQuoteSettle.getGoodsAmount());
                    mEtWorkingAmount.setText(TextUtils.isEmpty(mQuoteSettle.getWorkingAmount()) ? "0.00" : mQuoteSettle.getWorkingAmount());
                    mEtPlaceAmount.setText(TextUtils.isEmpty(mQuoteSettle.getPlaceAmount()) ? "0.00" : mQuoteSettle.getPlaceAmount());
                    mEtAccessoryAmount.setText(TextUtils.isEmpty(mQuoteSettle.getAccessoriesAmount()) ? "0.00" : mQuoteSettle.getAccessoriesAmount());
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


    private TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("提交结算单失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("提交结算单成功");
                    Intent intent = new Intent();
                    getActivity().setResult(RESULT_CODE_SETTLE, intent);
                    getActivity().finish();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTotalAmount = 0;
        mAmountArray[0] = TextUtils.isEmpty(mEtPartsAmount.getText()) ? 0.00 : Double.parseDouble(mEtPartsAmount.getText().toString());
        mAmountArray[1] = TextUtils.isEmpty(mEtWorkingAmount.getText()) ? 0.00 : Double.parseDouble(mEtWorkingAmount.getText().toString());
        mAmountArray[2] = TextUtils.isEmpty(mEtPlaceAmount.getText()) ? 0.00 : Double.parseDouble(mEtPlaceAmount.getText().toString());
        mAmountArray[3] = TextUtils.isEmpty(mEtAccessoryAmount.getText()) ? 0.00 : Double.parseDouble(mEtAccessoryAmount.getText().toString());
        for (double a : mAmountArray) {
            mTotalAmount += a;
        }
        mTvTotalAmount.setText(String.format(getString(R.string.report_order_total_price), mTotalAmount));
    }

    @Override
    public void afterTextChanged(Editable s) {

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

