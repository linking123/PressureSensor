package com.suncreate.pressuresensor.fragment.LoginRegister;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseActivity;
import com.suncreate.pressuresensor.base.BaseFragment;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.ui.LoginActivity;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.TDevice;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 找回密码 输入密码页面
 *
 * @author chenzhao
 *         created on 2015/12/22 16:04
 */
public class RegisterRetrieveFragment extends BaseFragment {
    protected static final String TAG = RegisterRetrieveFragment.class.getSimpleName();
    public static final String VERIFY_USER_NAME = "VERIFY_USER_NAME";

    public static final String PWD_TYPE = "PWD_TYPE";//设置密码方式

    public static final int REGET_PWD = 1;//找回密码
    public static final int REPAIR_PWD = 2;//修改密码

    private int verifyType;

    @Bind(R.id.et_password)
    EditText mEtPassword;
    @Bind(R.id.et_password_confirm)
    EditText mEtPasswordConfirm;
    @Bind(R.id.btn_register)
    Button mBtnRegister;

    private Activity activity;
    private String mUsername;
    private String mPassword;
    private String verifykey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            verifyType = args.getInt(PWD_TYPE, 1);
            mUsername = args.getString(VERIFY_USER_NAME);
            verifykey = args.getString(VerifyFragment.VERIFY_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retrieve_newpsw, container,
                false);
        ButterKnife.bind(this, view);
        activity = getActivity();
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        mBtnRegister.setOnClickListener(this);
        switch (verifyType) {
            case REGET_PWD:
                ((BaseActivity) activity).setActionBarTitle(R.string.login_retrieve_password);
                break;
            case REPAIR_PWD:
                ((BaseActivity) activity).setActionBarTitle(R.string.login_ret_password);
                break;
            default:
                ((BaseActivity) activity).setActionBarTitle(R.string.login_retrieve_password);
                break;
        }
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_register:
                handleRegister();
                break;
            default:
                break;
        }
    }

    private boolean prepareForRegister() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (mEtPassword.length() < 6) {
            mEtPassword.setError(getString(R.string.verify_password_short));
            mEtPassword.requestFocus();
            return true;
        }
        if (mEtPasswordConfirm.length() < 6) {
            mEtPasswordConfirm.setError(getString(R.string.verify_password_short));
            mEtPasswordConfirm.requestFocus();
            return true;
        }
        if (!mEtPassword.getText().toString().equals(mEtPasswordConfirm.getText().toString())) {
            AppContext.showToastShort(R.string.verify_password_diff);
            mEtPassword.requestFocus();
            return true;
        }
        return false;
    }

    private void handleRegister() {
        if (prepareForRegister()) {
            return;
        }
        mPassword = mEtPassword.getText().toString().trim();
        showWaitDialog(R.string.progress_submit);
        if (verifyType == REPAIR_PWD) {
            MonkeyApi.password(mPassword, mRepairHandler);
        } else if (verifyType == REGET_PWD) {
            MonkeyApi.resetPassword(mUsername, mPassword, verifykey, mRegetPwdHandler);
        } else {
            hideWaitDialog();
            AppContext.showToast("参数错误");
        }
    }

    AsyncHttpResponseHandler mRepairHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast(R.string.msg_operate_failure);

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("修改成功！");
                    getActivity().finish();
                } else {
                    AppContext.showToast(R.string.msg_operate_failure);
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

    private final AsyncHttpResponseHandler mRegetPwdHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast(R.string.msg_operate_failure);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    handleRegisterResult();
                } else {
                    AppContext.showToast(R.string.msg_operate_failure);
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

    private void handleRegisterResult() {
        DialogHelp.getMessageDialog(activity, getString(R.string.verify_modify_success), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//LoginActivity以上activity全部出栈
                activity.startActivity(intent);
            }
        }).show();
    }

}
