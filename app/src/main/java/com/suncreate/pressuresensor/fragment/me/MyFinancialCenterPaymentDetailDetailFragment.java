package com.suncreate.pressuresensor.fragment.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Pay;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 收支明细详情页
 */
public class MyFinancialCenterPaymentDetailDetailFragment extends BaseDetailFragment<Pay> {

    //公共部分
    @Bind(R.id.tv_in_out_amount)
    TextView tv_in_out_amount;
    @Bind(R.id.tv_amount)
    TextView tv_amount;
    @Bind(R.id.tv_type_level_1)
    TextView tv_type_level_1;
    @Bind(R.id.tv_time)
    TextView tv_time;
    @Bind(R.id.tv_ord_num)
    TextView tv_ord_num;
    @Bind(R.id.tv_type_level_2)
    TextView tv_type_level_2;
    //特殊部分，充值，提现 才显示
//    @Bind(R.id.status_box)
//    RelativeLayout status_box;
//    @Bind(R.id.tv_status)
//    TextView tv_status;
//    @Bind(R.id.tv_status_text)
//    TextView tv_status_text;
//    @Bind(R.id.account_box)
//    RelativeLayout account_box;
//    @Bind(R.id.tv_account)
//    TextView tv_account;
//    @Bind(R.id.tv_account_text)
//    TextView tv_account_text;

    //消费标识(0支出 1收入,默认全部)
    String mConsumeCost;
    //消费编号[订单:order,提现:cash,充值:rech]
    String mConsumeSn;
    //明细类型(0充值，1提现，2退款，3服务订单费，4工位订单费，5配件订单费，6管理员充值)
    String payType;
    String amount;
    String mAddtime;

    @Override
    public void initView(View view) {
        super.initView(view);

        Bundle args = getArguments();
        if (args != null) {
            mConsumeCost = args.getString("consumeCost");
            mConsumeSn = args.getString("consumeSn");
            payType = args.getString("payType");
            amount = args.getString("amount");
            mAddtime = args.getString("addtime");
        }

        initCommonView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_in_out_detail_detail;
    }

    @Override
    protected String getCacheKey() {
        return mId + "financialDetailDetail";
    }

    @Override
    protected void sendRequestDataForNet() {
        //根据消费编号请求不一样的消费编号[订单:order,提现:cash,充值:rech]
        //明细类型(0充值，1提现，2退款，3服务订单费，4工位订单费，5配件订单费，6管理员充值)
//        if (mConsumeSn.contains("order")) {
//
//        } else if (mConsumeSn.contains("cash")) {
//            MonkeyApi.viewWithdrawRecord(mConsumeSn, cashHandler);
//        } else if (mConsumeSn.contains("rech")) {
//            MonkeyApi.viewRechargeRecord(mConsumeSn, rechHandler);
//        }
    }

    protected TextHttpResponseHandler cashHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Pay> resultBean = AppContext.createGson().fromJson(responseString, getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Pay mPay = resultBean.getResult();
                    initWidthdrawDetailView(mPay);

                } else {
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    protected TextHttpResponseHandler rechHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {

        }
    };

    private void initWidthdrawDetailView(Pay mPay) {
        if (mPay != null) {

        }
    }

    private void initCommonView() {
        if (("1").equals(mConsumeCost)) {
            tv_in_out_amount.setText("入账金额");
            tv_type_level_1.setText("收入");
        } else if (mConsumeCost.equals("0")) {
            tv_in_out_amount.setText("出账金额");
            tv_type_level_1.setText("支出");
        }
        if (amount != null) {
            tv_amount.setText(amount);
        }
        if (mAddtime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long demand_addtimeL = Long.valueOf(mAddtime);
            String dateTime = sdf.format(new Date(demand_addtimeL));
            tv_time.setText(dateTime);
        }
        if (mConsumeSn != null) {
            tv_ord_num.setText(mConsumeSn);
        }
        //明细类型(0充值，1提现，2退款，3服务订单费，4工位订单费，5配件订单费)
        if (payType != null) {
            switch (payType) {
                case "0":
                    tv_type_level_2.setText("充值");
                    break;
                case "1":
                    tv_type_level_2.setText("提现");
                    break;
                case "2":
                    tv_type_level_2.setText("退款");
                    break;
                case "3":
                    tv_type_level_2.setText("服务订单费");
                    break;
                case "4":
                    tv_type_level_2.setText("工位订单费");
                    break;
                case "5":
                    tv_type_level_2.setText("配件订单费");
                    break;
            }
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Pay>>() {
        }.getType();
    }

}
