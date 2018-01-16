package com.suncreate.pressuresensor.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.User;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.util.StrPro;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.math.BigDecimal;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 财务中心
 */
public class MyFinancialCenterFragment extends BaseFragment implements View.OnClickListener {


    public static int REQUEST_CODE_FOR_RECHARGE = 1;
    public static int REQUEST_CODE_FOR_WITHDRAW = 2;

    //账户余额
    private String accountBalance;
    //账户冻结金额
    private String accountFreezeBalance;

    //账户余额ui
    @Bind(R.id.tv_account_balance)
    TextView tv_account_balance;
    //中间竖线，需要控制显隐
    @Bind(R.id.v_line)
    View v_line;
    //账户冻结金额ui
    @Bind(R.id.tv_account_balance_freeze)
    TextView tv_account_freeze_balance;
    //账户冻结金额Box，大区域，控制显隐
    @Bind(R.id.rl_freeze_balance_box)
    RelativeLayout rl_freeze_balance_box;
    //账户冻结金额Box,可点击区域
    @Bind(R.id.rl_freeze_balance_box_in)
    RelativeLayout rl_freeze_balance_box_in;
    //充值按钮
    @Bind(R.id.btn_my_financial_center_recharge)
    Button mRecharge;
    //提现按钮
    @Bind(R.id.btn_my_financial_center_withdraw)
    Button mWithdraw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initWidget(View root) {
        mRecharge.setOnClickListener(this);
        mWithdraw.setOnClickListener(this);
        rl_freeze_balance_box_in.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getBalance();
    }

    //取一次余额
    private void getBalance() {

        MonkeyApi.getMyInformation(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    User u = resultBean.getResult();
                    //当前角色  /** 用户当前角色:1会员中心 2技师中心 3商家中心 4代理商中心 */
                    String uCurrentRole = u.getCurrentRole().trim();
                    //可用余额
                    if (null != u.getUserBalance()) {
                        BigDecimal storetransprice = new BigDecimal(u.getUserBalance());
                        Double dbtransprice = storetransprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        accountBalance = String.valueOf(dbtransprice);
                    } else {
                        accountBalance = "0.00";
                    }
                    String accountBalanceText = "￥ " + accountBalance;
                    tv_account_balance.setText(accountBalanceText);
                    if ("1".equals(uCurrentRole)) {
                        rl_freeze_balance_box.setVisibility(View.GONE);
                        v_line.setVisibility(View.GONE);
                    } else {
                        rl_freeze_balance_box.setVisibility(View.VISIBLE);
                        v_line.setVisibility(View.VISIBLE);
                        //冻结金额
                        accountFreezeBalance = u.getUserFreezeblance() == 0.0 ? "0.00" : StrPro.moneyFormat(u.getUserFreezeblance());
                        String accountFreezeBalanceText = "￥ " + accountFreezeBalance;
                        tv_account_freeze_balance.setText(accountFreezeBalanceText);
                    }
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_my_financial_center_recharge:
                UIHelper.showSimpleBackForResult(MyFinancialCenterFragment.this, REQUEST_CODE_FOR_RECHARGE, SimpleBackPage.MY_FINANCIAL_CENTER_RECHARGE);
                break;
            case R.id.btn_my_financial_center_withdraw:
                Bundle b = new Bundle();
                b.putString("accountBalance", accountBalance);
                UIHelper.showSimpleBackForResult(MyFinancialCenterFragment.this, REQUEST_CODE_FOR_WITHDRAW, SimpleBackPage.MY_FINANCIAL_CENTER_WITHDRAW, b);
                break;
            case R.id.rl_freeze_balance_box_in:
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FINANCIAL_CENTER_FREEZE_BALANCE_ORDER);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_FOR_RECHARGE) {
            getBalance();
        }
        if (resultCode == REQUEST_CODE_FOR_WITHDRAW) {

        }
    }

    //menu收支明细
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_activity_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            //收支明细menu
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
