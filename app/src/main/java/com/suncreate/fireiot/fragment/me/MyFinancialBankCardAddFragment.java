package com.suncreate.fireiot.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.BankCardManger;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.fragment.master.BankCardManagerFragment;
import com.suncreate.fireiot.util.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 增加银行卡页面
 */
public class MyFinancialBankCardAddFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.et_true_name)
    EditText et_true_name;
    @Bind(R.id.et_bank_account_num)
    EditText et_bank_account_num;
    @Bind(R.id.et_bank_name)
    EditText et_bank_name;
    @Bind(R.id.et_user_card_id)
    EditText et_user_card_id;
    @Bind(R.id.et_user_phone_num)
    EditText et_user_phone_num;
    @Bind(R.id.btn_finish)
    Button btn_finish;

    private String mRealName;
    private String mBankAccount;
    private String mBankName;
    private String mUserCardId;
    private String mUserPhoneNum;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {

        et_bank_account_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入银行卡号结束后，自动获取开户行名称并填写
                mBankAccount = et_bank_account_num.getText().toString().trim();
                MonkeyApi.getUserBankName(mBankAccount, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                        }.getType());
                        if (resultBean != null && resultBean.getCode() == 1) {
                            String bankNameStr = resultBean.getResult().toString();

                            if (!StringUtils.isEmpty(bankNameStr))
                                et_bank_name.setText(bankNameStr);
                        }
                    }
                });
            }
        });

        btn_finish.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_bankcard_manager_add;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_finish:
                doAddCar();
                break;
            default:
                break;
        }

    }

    public void doAddCar() {
        mRealName = et_true_name.getText().toString().trim();
        if (StringUtils.isEmpty(mRealName)) {
            AppContext.showToastShort("请填写持卡人真实姓名");
            return;
        }
        mBankAccount = et_bank_account_num.getText().toString().trim();
        if (StringUtils.isEmpty(mBankAccount)) {
            AppContext.showToastShort("请填写正确的银行卡号");
            return;
        }
        mBankName = et_bank_name.getText().toString().trim();
        if (StringUtils.isEmpty(mBankName)) {
            AppContext.showToastShort("请填写开户银行名称");
            return;
        }
        mUserCardId = et_user_card_id.getText().toString().trim();
        if (StringUtils.isEmpty(mUserCardId)) {
            AppContext.showToastShort("请填写您的身份证号");
            return;
        }
        mUserPhoneNum = et_user_phone_num.getText().toString().trim();
        if (StringUtils.isEmpty(mUserPhoneNum)) {
            AppContext.showToastShort("请填写真实的手机号，以便我们联系您");
            return;
        }

        MonkeyApi.addUserBankCard(mRealName, mBankAccount, mBankName, mUserCardId, mUserPhoneNum, handler);

    }

    protected AsyncHttpResponseHandler handler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            //完成之后需要提醒列表页刷新
            try {
                ResultBean<BankCardManger> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<BankCardManger>>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("mRealName", mRealName);
                    intent.putExtra("mUserPhoneNum", mUserPhoneNum);
                    intent.putExtra("mBankName", mBankName);
                    intent.putExtra("mBankAccount", mBankAccount);
                    getActivity().setResult(BankCardManagerFragment.REQUEST_CODE_ADD_BANK_CARD, intent);
                    getActivity().finish();
                } else {
                    AppContext.showToast(resultBean.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
