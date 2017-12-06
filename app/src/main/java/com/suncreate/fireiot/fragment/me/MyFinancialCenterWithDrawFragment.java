package com.suncreate.fireiot.fragment.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Store;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 提现页面
 * <p>
 * 该页面还需加入银行卡管理
 * 不要删除注释掉的代码
 * 20160110 jin
 */
public class MyFinancialCenterWithDrawFragment extends BaseFragment implements View.OnClickListener {

    public static int REQUEST_CODE_FOR_FINISH_WITHDRAW = 1;

    private int num;
    //提取标识（从数据字典取alipay支付宝，chinabank网银在线，tenpay财付通，bill快钱，outline线下支付，weixin微信）
    private String withdrawPayment = "outline";
    //账户名称
    private String mAccountName;
    //开户银行
    private String mBankName;
    //银行账号
    private String mBankAccountNum;
    //提现金额
    private String withdrawAmount;
    //提现备注
    private String withdrawInfo;
    //余额
    private String accountBalance;

    @Bind(R.id.btn_financial_center_withdraw)
    Button btn_withdraw;
    @Bind(R.id.tv_financial_center_withdraw_account_choose)
    TextView tv_financial_center_withdraw_account_choose;
    @Bind(R.id.et_account_name)
    EditText et_account_name;
    @Bind(R.id.et_bank_name)
    EditText et_bank_name;
    @Bind(R.id.et_bank_account_num)
    EditText et_bank_account_num;
    @Bind(R.id.et_withdraw_amount)
    EditText et_withdraw_amount;
    //    @Bind(R.id.Btn_select)
//    ImageView Btn_select;
    //余额
    @Bind(R.id.tv_balance)
    TextView mBalance;
    //全部提现按钮
    @Bind(R.id.tv_withdraw_all)
    TextView tv_withdraw_all;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //银行卡管理入口，暂时关闭
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.bank_manager_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.public_menu_manager:
//                UIHelper.showSimpleBack(getContext(), SimpleBackPage.BANK_CARD_MANAGER);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void initWidget(View root) {

        //获取账户余额
        Bundle b = getArguments();
        accountBalance = b.getString("accountBalance");
        mBalance.setText("当前可提现余额：" + accountBalance + "元，");

        tv_withdraw_all.setOnClickListener(this);
//        Btn_select.setOnClickListener(this);
        btn_withdraw.setOnClickListener(this);
        //设置提现金额格式
        et_withdraw_amount.setFilters(new InputFilter[]{new MyFinancialCenterWithDrawFragment.DecimalDigitsInputFilter(5, 2)}); //最多输入5位整数，1-4位整数可带两位小数;

        /** 用户当前角色:1会员中心 2技师中心 3修理厂 */
        String userRole = AppContext.getInstance().getUserRole();
        if ("1".equals(userRole)) {
            initListener();
        } else {
            MonkeyApi.getStoreInfo(mHandler);
        }
    }

    String beforeStr = "";
    String afterStr = "";
    String changeStr = "";
    int index = 0;
    boolean changeIndex = true;

    public void initListener() {
        //点击开户行，自动填写
        et_bank_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, final boolean hasFocus) {
                if (hasFocus) {
                    //输入银行卡号结束后，点击开户银行获取焦点后，自动获取开户行名称并填入
                    mBankAccountNum = et_bank_account_num.getText().toString().trim();
                    MonkeyApi.getUserBankName(mBankAccountNum.replace(" ", ""), new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            AppContext.showToastShort("银行卡号无效，请重新填写！");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                String bankNameStr = resultBean.getResult().toString();
                                if (!StringUtils.isEmpty(bankNameStr)) {
                                    et_bank_name.setText(bankNameStr);
                                }
                            } else {
                                onFailure(statusCode, headers, responseString, null);
                            }
                        }
                    });
                }
            }
        });
        //银行卡间隔4位
        et_bank_account_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                afterStr = s.toString();
                if (changeIndex)
                    index = et_bank_account_num.getSelectionStart();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString()) || s.toString() == null || beforeStr.equals(afterStr)) {
                    changeIndex = true;
                    return;
                }
                changeIndex = false;
                char c[] = s.toString().replace(" ", "").toCharArray();
                changeStr = "";
                for (int i = 0; i < c.length; i++) {
                    changeStr = changeStr + c[i] + ((i + 1) % 4 == 0 && i + 1 != c.length ? " " : "");
                }
                if (afterStr.length() > beforeStr.length()) {
                    if (changeStr.length() == index + 1) {
                        index = changeStr.length() - afterStr.length() + index;
                    }
                    if (index % 5 == 0 && changeStr.length() > index + 1) {
                        index++;
                    }
                } else if (afterStr.length() < beforeStr.length()) {
                    if ((index + 1) % 5 == 0 && index > 0 && changeStr.length() > index + 1) {
                        //  index--;
                    } else {
                        index = changeStr.length() - afterStr.length() + index;
                        if (afterStr.length() % 5 == 0 && changeStr.length() > index + 1) {
                            index++;
                        }
                    }
                }
                et_bank_account_num.setText(changeStr);
                et_bank_account_num.setSelection(index);
            }
        });
    }

    protected TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("请到店铺设置中设置银行卡信息");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Store> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<Store>>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Store store = resultBean.getResult();
                    //账户名称
                    String storeAccountName = store.getStoreAccountname();
                    //银行账号
                    String storeBankaccount = store.getStoreBankaccount();
                    //开户银行
                    String storeBankname = store.getStoreBankname();
                    if (StringUtils.isEmpty(storeAccountName) || StringUtils.isEmpty(storeBankaccount) || StringUtils.isEmpty(storeBankname)) {
                        AppContext.showToastShort("请到店铺设置中设置银行卡信息");
                    } else {
                        et_account_name.setText(storeAccountName);
                        et_bank_name.setText(storeBankname);
                        et_bank_account_num.setText(storeBankaccount);
                    }
                } else {
                    onFailure(statusCode, headers, responseString, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_withdraw;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//            case R.id.Btn_select:
            //暂时不给选择支付宝和微信，只用银行卡
//                showPay();
//                break;
            case R.id.tv_withdraw_all:
                et_withdraw_amount.setText(accountBalance.replace(",", ""));
                break;
            case R.id.btn_financial_center_withdraw:
                dopay();
                break;
            default:
                break;
        }
    }

    public void showPay() {
        new AlertDialog.Builder(getActivity()).setTitle("请选择支付方式")

                .setIcon(android.R.drawable.ic_input_add)
                .setSingleChoiceItems(new String[]{"银行卡", "支付宝", "微信"}, num,
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                num = which;
                                if (num == 0) {
                                    tv_financial_center_withdraw_account_choose.setText("银行卡");
                                    withdrawPayment = "chinabank";
                                } else if (num == 1) {
                                    AppContext.showToastShort("暂未开通此功能");
                                    return;
//                                    tv_financial_center_withdraw_account_choose.setText("支付宝");
//                                    withdrawPayment = "alipay";
                                } else {
                                    AppContext.showToastShort("暂未开通此功能");
                                    return;
//                                    tv_financial_center_withdraw_account_choose.setText("微信");
//                                    withdrawPayment = "weixin";
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
        //账户名称
        mAccountName = et_account_name.getText().toString();
        //开户银行
        mBankName = et_bank_name.getText().toString();
        //银行账号
        mBankAccountNum = et_bank_account_num.getText().toString();
        //提现金额
        withdrawAmount = et_withdraw_amount.getText().toString();
        //提现备注
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        withdrawInfo = AppContext.getInstance().getLoginUserExt().getUserName() + "在" + sdf.format(new Date()) + "提现" + withdrawAmount + "元";

        if (StringUtils.isEmpty(mAccountName)) {
            AppContext.showToastShort("账户名称不能为空！");
            return;
        }
        if (StringUtils.isEmpty(mBankName)) {
            AppContext.showToastShort("开户银行不能为空！");
            return;
        }
        if (StringUtils.isEmpty(mBankAccountNum)) {
            AppContext.showToastShort("银行账号不能为空！");
            return;
        }
        if (StringUtils.isEmpty(withdrawAmount)) {
            AppContext.showToastShort("提现金额不能为空！");
            return;
        }
        if (Double.parseDouble(et_withdraw_amount.getText().toString().trim()) > Double.parseDouble(accountBalance) || "0.00".equals(accountBalance) || "0.0".equals(accountBalance)) {
            AppContext.showToastShort("账户余额不足！");
            return;
        }
        MonkeyApi.addWithdrawRecord(withdrawAmount, mAccountName, mBankName, mBankAccountNum.replace(" ", ""), withdrawInfo, withdrawPayment, handler);
    }

    TextHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("提交申请失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mBankName", mBankName);
                    bundle.putString("mBankAccountNum", mBankAccountNum);
                    bundle.putString("withdrawAmount", withdrawAmount);
                    AppContext.showToast("申请提交成功，请保持手机畅通，悟空车服网稍后会联系您打款！");
                    UIHelper.showSimpleBackForResult(MyFinancialCenterWithDrawFragment.this, REQUEST_CODE_FOR_FINISH_WITHDRAW, SimpleBackPage.MY_FINANCIAL_CENTER_WITHDRAW_FINISH, bundle);
                } else if ((resultBean != null ? resultBean.getCode() : 0) == 1001010 && resultBean.getMessage().contains("withdrawAccount")) {
                    AppContext.showToastShort(resultBean.getMessage().replace("参数[withdrawAccount]", "") + "，请重新填写！");
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
        if (resultCode == REQUEST_CODE_FOR_FINISH_WITHDRAW) {
            getActivity().finish();
        }
    }
}
