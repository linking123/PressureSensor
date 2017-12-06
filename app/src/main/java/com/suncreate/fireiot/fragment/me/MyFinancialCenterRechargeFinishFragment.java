package com.suncreate.fireiot.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.fragment.base.BaseFragment;

import java.math.BigDecimal;

import butterknife.Bind;

/**
 * 充值完成页面
 * <p>
 * desc
 */
public class MyFinancialCenterRechargeFinishFragment extends BaseFragment implements View.OnClickListener {

    //充值金额
    String rechargeAmount;

    @Bind(R.id.tv_recharge_account_type)
    TextView tv_recharge_account_type;
    @Bind(R.id.tv_recharge_amount)
    TextView tv_recharge_amount;
    @Bind(R.id.btn_my_financial_center_recharge_finish)
    Button btn_finish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {
        //充值页面传参
        Bundle param = getArguments();
        //账户类型标识
        String rechargePayment = param.getString("rechargePayment");
        String rechargeType = "支付宝";
        if (rechargePayment == "alipay") {
            rechargeType = "支付宝";
        } else if (rechargePayment == "weixin") {
            rechargeType = "微信";
        } else if (rechargePayment == "chinabank") {
            rechargeType = "网银";
        }

        //充值金额
        rechargeAmount = param.getString("rechargeAmount");
        tv_recharge_account_type.setText(rechargeType);
        tv_recharge_amount.setText(rechargeAmount);
        btn_finish.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_recharge_finish;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_my_financial_center_recharge_finish:

//                User u = AppContext.getInstance().getLoginUserExt();
//                Double accountBalanceOld = u.getUserBalance();
//                Double accountBalanceNew = accountBalanceOld + Double.valueOf(rechargeAmount.trim());
//                BigDecimal bd = new BigDecimal(accountBalanceNew);
//                accountBalanceNew = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                AppContext.getInstance().setProperty("user.userBalance", String.valueOf(accountBalanceNew));

                getActivity().setResult(MyFinancialCenterRechargeFragment.REQUEST_CODE_FOR_FINISH_RECHARGE);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
