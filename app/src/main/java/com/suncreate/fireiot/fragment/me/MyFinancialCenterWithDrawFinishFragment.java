package com.suncreate.fireiot.fragment.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.fragment.base.BaseFragment;

import butterknife.Bind;

/**
 * 提现完成页面
 * <p>
 * desc
 */
public class MyFinancialCenterWithDrawFinishFragment extends BaseFragment implements View.OnClickListener {

    //哪家银行
    @Bind(R.id.tv_account_type)
    TextView tv_account_type;
    //银行账号
    @Bind(R.id.tv_account_num)
    TextView tv_account_num;
    //提现金额
    @Bind(R.id.tv_amount)
    TextView tv_amount;
    @Bind(R.id.btn_my_financial_center_withdraw_finish)
    Button btn_finish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {
        Bundle param = getArguments();
        //哪家银行
        String mBankName = param.getString("mBankName");
        //银行账号
        String mBankAccountNum = param.getString("mBankAccountNum");
        //提现金额
        String withdrawAmount = param.getString("withdrawAmount");
        tv_account_type.setText(mBankName);
        tv_account_num.setText(mBankAccountNum);
        tv_amount.setText("￥ " + withdrawAmount);
        btn_finish.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_withdraw_finish;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_my_financial_center_withdraw_finish:
                getActivity().setResult(MyFinancialCenterWithDrawFragment.REQUEST_CODE_FOR_FINISH_WITHDRAW);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
