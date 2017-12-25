package com.suncreate.pressuresensor.fragment.LoginRegister;

import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.AppManager;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.PhoneCheck;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.ui.LoginActivity;
import com.suncreate.pressuresensor.util.AreaHelper;
import com.suncreate.pressuresensor.util.CyptoUtils;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.TypeChk;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 注册页面
 *
 * @author linking
 *         created on 2017/12/25
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {

    protected static final String TAG = RegisterFragment.class.getSimpleName();
    public static final String REGISTER_PAGE = "REGISTER_PAGE";

    @Bind(R.id.reg_user_name)
    AutoCompleteTextView mUserName;
    @Bind(R.id.act_user_phone)
    AutoCompleteTextView mUserPhone;
    @Bind(R.id.reg_password)
    AutoCompleteTextView mUserPwd1;
    @Bind(R.id.reg_password_confirm)
    AutoCompleteTextView mUserPwd2;
    //提交注册按钮
    @Bind(R.id.btn_register)
    Button btn_register;

    private String mUsername;
    private String mPhoneNum;
    private String regPassword;
    private int num = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register3_usr_psd_confirm;
    }

    @Override
    protected void initWidget(View view) {
        super.initWidget(view);

        btn_register.setOnClickListener(this);

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

    //回调
    CallBackValue callBackValue;

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue = (RegisterFragment.CallBackValue) AppManager.getActivity(LoginActivity.class);
    }

    //定义一个回调接口，传值给activity
    public interface CallBackValue {
        void SendMessageValue(String strValue);
    }

    /**
     * 提交注册
     *
     * @param
     */
    private void handleRegister() {
//        hideWaitDialog();
//        AppContext.showToast("正在提交请求");
        //注册
        mPhoneNum = mUserPhone.getText().toString().trim();
        MonkeyApi.register("",mPhoneNum, 1, mUsername, regPassword, "", "", mSubmitHandler);
    }

    TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast(R.string.register_submit_send_failure);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    //传送手机号
                    callBackValue.SendMessageValue(mPhoneNum);
                    AppContext.showToastShort("注册成功，请登录！");
                    getActivity().finish();
                } else {
                    if (resultBean.getCode() == 1001202)
                        AppContext.showToast("此号码已被注册，您可尝试找回密码！");
                    else if (resultBean.getCode() == 1001201)
                        AppContext.showToastShort("验证码错误！");
                    else
                        AppContext.showToastShort("注册失败！");
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

}
