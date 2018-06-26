package com.suncreate.pressuresensor.fragment.LoginRegister;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.PhoneCheck;
import com.suncreate.pressuresensor.util.CyptoUtils;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.TypeChk;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 找回密码 发送验证码页面
 *
 * @author chenzhao
 *         created on 2015/12/22 9:37
 */
public class VerifyFragment extends BaseFragment {

    protected static final String TAG = VerifyFragment.class.getSimpleName();
    public static final String VERIFY_KEY = "VERIFY_KEY";

    @Bind(R.id.retrieve_verifyPhone)
    AutoCompleteTextView retrieve_verifyPhone;
    @Bind(R.id.retrieve_verifyCode)
    AutoCompleteTextView retrieve_verifyCode;
    @Bind(R.id.retrieve_btn_getVerifyCode)
    Button btnSendVerify;
    @Bind(R.id.retrieve_nextStep)
    Button btnNextStep;

    private String mUsername;
    private String mVerifyCode;
    private TimeCount time;
    private String verifykey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrieve_verifyphone, container,
                false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        Bundle args = getArguments();
        if (args != null) {
            String loginUserName = args.getString("loginUserName");
            retrieve_verifyPhone.setText(loginUserName);
        }
        btnSendVerify.setOnClickListener(this);
        btnNextStep.setOnClickListener(this);
    }

    @Override
    public void initData() {
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.retrieve_btn_getVerifyCode:
                handleSendVerify();
                break;
            case R.id.retrieve_nextStep:
                handleCheckVerify();
                break;
            default:
                break;
        }
    }

    private boolean prepareForSendVerify() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (retrieve_verifyPhone.length() == 0) {
            retrieve_verifyPhone.setError(getString(R.string.tip_please_input_phone));
            retrieve_verifyPhone.requestFocus();
            return true;
        } else {
            if (!TypeChk.isPhoneNum(retrieve_verifyPhone.getText().toString())) {
                retrieve_verifyPhone.setError(getString(R.string.tip_illegal_phone));
                retrieve_verifyPhone.requestFocus();
                return true;
            }
        }
        return false;
    }

    private boolean prepareForCheckVerify() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (retrieve_verifyPhone.length() == 0) {
            retrieve_verifyPhone.setError(getString(R.string.tip_please_input_phone));
            retrieve_verifyPhone.requestFocus();
            return true;
        } else {
            if (!TypeChk.isPhoneNum(retrieve_verifyPhone.getText().toString())) {
                retrieve_verifyPhone.setError(getString(R.string.tip_illegal_phone));
                retrieve_verifyPhone.requestFocus();
                return true;
            }
        }
        if (retrieve_verifyCode.length() == 0) {
            retrieve_verifyCode.setError(getString(R.string.tip_please_input_verify));
            retrieve_verifyCode.requestFocus();
            return true;
        }
        return false;
    }

    private void handleSendVerify() {
        if (prepareForSendVerify()) {
            return;
        }
        mUsername = retrieve_verifyPhone.getText().toString().trim();
        String sysKey = CyptoUtils.encode("SC_Monkey_Code", mUsername);
        showWaitDialog(R.string.progress_send);
        MonkeyApi.getPhoneCode(mUsername, Constants.VERIFY_TYPE.GET_BACK, sysKey, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AppContext.showToast(R.string.register_getVerifyCode_failure);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    ResultBean<PhoneCheck> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PhoneCheck>>() {
                    }.getType());
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getKey() != null) {
                        time.start();
                        hideWaitDialog();
                        AppContext.showToast(R.string.register_verifyCode_send);
                        verifykey = resultBean.getResult().getKey();
                    } else {
                        AppContext.showToast(R.string.register_getVerifyCode_failure);
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
        });
    }


    private void handleCheckVerify() {
        if (prepareForCheckVerify()) {
            return;
        }
        mUsername = retrieve_verifyPhone.getText().toString().trim();
        mVerifyCode = retrieve_verifyCode.getText().toString().trim();
        showWaitDialog(R.string.progress_submit);
        MonkeyApi.verficateCode(verifykey, mUsername, mVerifyCode, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AppContext.showToast(R.string.register_verifyCode_send_failure);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    ResultBean<PhoneCheck> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PhoneCheck>>() {
                    }.getType());
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getKey() != null) {
//                        handleRegister(resultBean.getResult().getKey());
                        verifykey = resultBean.getResult().getKey();
                        UIHelper.showRegisterRetrievePage(getActivity(), mUsername, verifykey, RegisterRetrieveFragment.REGET_PWD);
                    } else {
                        hideWaitDialog();
                        AppContext.showToast(resultBean.getCode());
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
        });
    }

     class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            btnSendVerify.setText(R.string.register_getVerifyCode);// 重获验证码
            btnSendVerify.setEnabled(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            btnSendVerify.setEnabled(false);
            if (isAdded()) {
                btnSendVerify.setText(getString(R.string.register_send_wait,
                        String.valueOf(millisUntilFinished / 1000)));
            }

        }
    }

}
