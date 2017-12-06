package com.suncreate.fireiot.fragment.me;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.Alipay.PayResult;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 财务中心 充值页面
 * <p>
 * desc
 */
public class MyFinancialCenterRechargeFragment extends BaseFragment implements View.OnClickListener {

    public static int REQUEST_CODE_FOR_FINISH_RECHARGE = 1;

    private int num = 0;

    //支付标识
    private static final int SDK_PAY_FLAG = 1;
    //订单信息
    private String orderInfo = "";
    //预存款金额
    private String rechargeAmount = "0";
    //支付状态:0等待支付，1等待审核，2支付完成，3关闭支付
    private String rechargePaySatus;
    //支付标识（从数据字典取alipay支付宝，chinabank网银在线，tenpay财付通，bill快钱，outline线下支付，微信weixin）
    private String rechargePayment = "alipay";
    //预存款编号
    private String rechargeSn;

    //选择充值方式
    @Bind(R.id.tv_financial_center_recharge_type_choose)
    TextView tv_financial_center_recharge_type_choose;
    //充值金额
    @Bind(R.id.et_recharge_amount)
    EditText et_recharge_amount;
    //下一步
    @Bind(R.id.btn_financial_center_nextstep)
    Button nextStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {
        tv_financial_center_recharge_type_choose.setOnClickListener(this);
        et_recharge_amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)}); //最多输入5位整数，1-4位整数可带两位小数
        nextStep.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_recharge;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_financial_center_recharge_type_choose:
                showPay();
                break;
            case R.id.btn_financial_center_nextstep:
                dopay();
                break;
            default:
                break;
        }
    }

    public void showPay() {
        new AlertDialog.Builder(getActivity()).setTitle("请选择支付方式")
                .setIcon(android.R.drawable.ic_input_add)
                .setSingleChoiceItems(new String[]{"支付宝", "微信"}, num,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                num = which;
                                if (num == 0) {
                                    tv_financial_center_recharge_type_choose.setText("支付宝");
                                    rechargePayment = "alipay";

                                } else if (num != 0) {
                                    tv_financial_center_recharge_type_choose.setText("微信");
                                    rechargePayment = "weixin";
                                }
                            }
                        }
                )
                .setPositiveButton("确定", null)
                .show();
    }

    //限制输入两位小数
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            //正则过滤
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    private void dopay() {
        String rechargeType = tv_financial_center_recharge_type_choose.getText().toString();
        if (rechargeType.trim().equals("支付宝")) {
            doAlipy();
        } else if (rechargeType.trim().equals("微信")) {
            doWXpay();
        }
    }

    private void doWXpay() {
        Toast.makeText(getContext(), "功能开发中，敬请期待！", Toast.LENGTH_SHORT).show();
        return;
    }

    protected Type getPayType() {
        return new TypeToken<ResultBean<Pay>>() {
        }.getType();
    }

    /**
     * 支付宝支付业务
     */
    public void doAlipy() {
        //订单名称
        final String subject = "充值";
        //付款金额，double字符串
        final String totalFee = et_recharge_amount.getText().toString();
        if (StringUtils.isEmpty(totalFee) || "0".compareTo(totalFee) == 0) {
            AppContext.showToastShort("请填写正确的充值金额");
            return;
        }
        //商品描述
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
//        final String goodInfo = AppContext.getInstance().getLoginUserExt().getUserName() + "在" + sdf2.format(new Date()) + " 充值 " + totalFee + "元";
        //1.增加充值记录start
        rechargeAmount = et_recharge_amount.getText().toString();
        //支付状态，充值后是待支付，返回支付结果后在后端更新状态
        rechargePaySatus = "0";
        MonkeyApi.addRechargeRecord(rechargeAmount, rechargePaySatus, rechargePayment, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                //静默增加充值记录
                ResultBean<Pay> resultBean = AppContext.createGson().fromJson(responseString, getPayType());
                String rechargesn = resultBean.getResult().getRechargeSn();
                if (!StringUtils.isEmpty(rechargesn)) {
                    //2.获取签名信息
//                    MonkeyApi.getAlipaySignInfo(rechargesn, subject, totalFee, goodInfo,
//                            new TextHttpResponseHandler() {
//                                @Override
//                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//                                }
//
//                                @Override
//                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                                    JSONObject jsonobject = (JSONObject) JSONObject.parse(responseString);
//                                    orderInfo = jsonobject.getJSONObject("result").get("orderInfo").toString();
//                                    Runnable payRunnable = new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            PayTask alipay = new PayTask(getActivity());
//                                            Map<String, String> result = alipay.payV2(orderInfo, true);
//                                            Log.i("msp", result.toString());
//
//                                            Message msg = new Message();
//                                            msg.what = SDK_PAY_FLAG;
//                                            msg.obj = result;
//                                            mHandler.sendMessage(msg);
//                                        }
//                                    };
//                                    Thread payThread = new Thread(payRunnable);
//                                    payThread.start();
//                                }
//                            });
                } else {
                    AppContext.showToast("生成充值记录失败!");
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //充值成功，转到完成页面
                        Bundle bundle = new Bundle();
                        bundle.putString("rechargePayment", rechargePayment);
                        bundle.putString("rechargeAmount", rechargeAmount);
                        UIHelper.showSimpleBackForResult(MyFinancialCenterRechargeFragment.this, REQUEST_CODE_FOR_FINISH_RECHARGE, SimpleBackPage.MY_FINANCIAL_CENTER_RECHARGE_FINISH, bundle);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_FOR_FINISH_RECHARGE) {
            getActivity().finish();
        }
    }
}
