package com.suncreate.fireiot.fragment.LoginRegister;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.PhoneCheck;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.ui.LoginActivity;
import com.suncreate.fireiot.util.AreaHelper;
import com.suncreate.fireiot.util.CyptoUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.TDevice;
import com.suncreate.fireiot.util.TypeChk;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 注册页面
 *
 * @author chenzhao
 *         created on 2015/12/22 9:37
 */
public class RegisterFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener,
        TextWatcher {

    protected static final String TAG = RegisterFragment.class.getSimpleName();
    public static final String REGISTER_PAGE = "REGISTER_PAGE";

    //RadioGroup，实现选项切换
    @Bind(R.id.reg_radioGroup)
    RadioGroup radioGroup;
    //车主
    @Bind(R.id.reg_carOwner)
    RadioButton reg_carOwner;
    //技师
    @Bind(R.id.reg_technician)
    RadioButton reg_technician;
    //快修站
    @Bind(R.id.reg_repairShop)
    RadioButton reg_repairShop;
    //地址选择box
    @Bind(R.id.reg_addressBox)
    View reg_addressBox;
    //选择地址
    @Bind(R.id.reg_user_area)
    TextView reg_user_area;
    //详细地址输入框
    @Bind(R.id.reg_detailAddress)
    EditText reg_detailAddress;
    //技师姓名区域
    @Bind(R.id.reg_tech_name_box)
    View reg_tech_name_box;
    //真实姓名
    @Bind(R.id.reg_tech_true_name)
    EditText reg_tech_name;
    //快修站名称区域
    @Bind(R.id.reg_garage_name_box)
    View reg_garage_name_box;
    //    //快修站名称
//    @Bind(R.id.reg_garage_true_name)
//    EditText reg_garage_name;
    //输入密码
    @Bind(R.id.reg_password)
    EditText reg_password;
    //电话号码
    @Bind(R.id.register_verifyPhone)
    EditText register_phoneNum;
    //验证码
    @Bind(R.id.register_verifyCode)
    EditText register_verifyCode;
    //获取验证码按钮
    @Bind(R.id.register_getVerifyCode)
    Button btn_register_getVerifyCode;
    //协议
    @Bind(R.id.reg_protocol)
    CheckBox reg_protocol;
    //提交注册按钮
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.btn_password)
    Button btn_password;

    private int regType = 1;
    OptionsPickerView addrOptions;

    private Activity activity;
    private String mUsername;
    private String garageName;
    private String detailAddress;
    private String region;
    private String mPhoneNum;
    private String regPassword;
    private String mVerifyCode;
    private RegisterFragment.TimeCount time;
    private int num = 2;
    private String verifykey;
    private String mAreaCode = "";  //区域编码

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }


    @Override
    protected void initWidget(View view) {
        super.initWidget(view);
        //初始显示车主注册界面
        showCarOwnerPage();

        btn_register_getVerifyCode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        btn_password.setOnClickListener(this);
        register_phoneNum.addTextChangedListener(this);
        register_verifyCode.addTextChangedListener(this);
        reg_detailAddress.addTextChangedListener(this);
        reg_tech_name.addTextChangedListener(this);
        //reg_garage_name.addTextChangedListener(this);
        reg_password.addTextChangedListener(this);
        btn_password.setOnClickListener(this);
        addrOptions = AreaHelper.chooseAddress(addrOptions, this.getContext());

        reg_user_area.setOnClickListener(this);
        addrOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                AreaHelper.onOptionsSelect(options1, option2, options3, reg_user_area);
                String mUserArea = reg_user_area.getText().toString();
                if (!TextUtils.isEmpty(mUserArea)) {
                    String[] s = mUserArea.split(" ");
                    mAreaCode = AppContext.getInstance().getAreaCodeByName(s[s.length - 1], s[s.length - 2]);
                }
            }
        });

        reg_protocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    veriryInputNotEmpty();
                } else {
                    btn_register.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void initData() {
        time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.reg_user_area:
                ((InputMethodManager) mContext.getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                addrOptions.show();
                break;
            case R.id.register_getVerifyCode:
                handleSendVerify();
                break;
            case R.id.btn_register:
                handleCheckVerify();
                break;
            case R.id.btn_password:
                openPassword();
                break;
            default:
                break;
        }
    }

    public void openPassword() {
        if (num % 2 == 0) {
            reg_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            btn_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.visiable1));
            num++;
        } else if (num % 2 != 0) {
            reg_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btn_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.visiable));
            num++;
        }
    }

    //默认显示车主页面
    public void showCarOwnerPage() {
        reg_addressBox.setVisibility(View.GONE);
        reg_tech_name_box.setVisibility(View.GONE);
        reg_garage_name_box.setVisibility(View.GONE);
        setBluetext(1, "会员注册协议");
    }

    //切换RadioButton
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //选择我是车主
        if (checkedId == R.id.reg_carOwner) {
            regType = 1;
            showWhoChoosed(1);
            setBluetext(1, "会员注册协议");
        } else if (checkedId == R.id.reg_technician) {
            regType = 2;
            //选择我是技师
            showWhoChoosed(2);
            setBluetext(2, "技师入驻协议");
        } else {
            regType = 3;
            //选择我是快修站
            showWhoChoosed(3);
            setBluetext(3, "快修站入驻协议");
        }
    }

    //选择RadioButton时切换显示效果
    public void showWhoChoosed(int who) {
        switch (who) {
            case 1:
                reg_addressBox.setVisibility(View.GONE);
                reg_tech_name_box.setVisibility(View.GONE);
                reg_garage_name_box.setVisibility(View.GONE);
                break;
            case 2:
                reg_addressBox.setVisibility(View.VISIBLE);
                reg_tech_name_box.setVisibility(View.VISIBLE);
                reg_garage_name_box.setVisibility(View.GONE);
                break;
            case 3:
                reg_addressBox.setVisibility(View.VISIBLE);
                reg_tech_name_box.setVisibility(View.GONE);
                reg_garage_name_box.setVisibility(View.VISIBLE);
        }

        register_phoneNum.requestFocus();
        register_phoneNum.setText("");
        register_verifyCode.setText("");
        reg_user_area.setText("");
        reg_detailAddress.setText("");
        reg_tech_name.setText("");
        //reg_garage_name.setText("");
        reg_password.setText("");
        btn_register.setEnabled(false);
    }

    /**
     * 显示不同的协议，并可点击显示协议内容
     * 设置蓝色链接，点击可跳转
     */
    public void setBluetext(int tag, String bluetext) {

        String remindlongWords = "同意《" + bluetext + "》";//显示的全部字符串

        //需要监听click的范围 start end
        int start = 3;
        int end = start + bluetext.length();

        //1.必须要的新建一个Span
        SpannableString spStr = new SpannableString(remindlongWords);
        //2.NoLineClickSpan  写好了制定位置的颜色和click事件
        NoLineClickSpan clickSpan = new NoLineClickSpan(tag, Color.RED);
        //3.span帮顶下click span
        spStr.setSpan(clickSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //4.需要设置下str
        reg_protocol.setText(spStr);

        //5.设置TextView可以点击
        reg_protocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //监听文本框输入，输入前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    //监听文本框输入，输入时，按钮颜色状态改变
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (register_phoneNum.getText().toString().isEmpty()) {
            btn_register_getVerifyCode.setEnabled(false);
        } else {
            btn_register_getVerifyCode.setEnabled(true);
        }

        if (reg_protocol.isChecked()) {
            veriryInputNotEmpty();
        } else {
            btn_register.setEnabled(false);
        }
    }

    //验证输入框都不为空，提交按钮才能变为可选，颜色改变
    private void veriryInputNotEmpty() {

        if (reg_carOwner.isChecked()
                && !register_phoneNum.getText().toString().isEmpty()
                && !register_verifyCode.getText().toString().isEmpty()
                && !reg_password.getText().toString().isEmpty()) {
            btn_register.setEnabled(true);
        } else if (reg_technician.isChecked()
                && !register_phoneNum.getText().toString().isEmpty()
                && !register_verifyCode.getText().toString().isEmpty()
                && !reg_password.getText().toString().isEmpty()
                && !reg_tech_name.getText().toString().isEmpty()
                && !reg_detailAddress.getText().toString().isEmpty()) {
            btn_register.setEnabled(true);
        } else if (reg_repairShop.isChecked()
                && !register_phoneNum.getText().toString().isEmpty()
                && !register_verifyCode.getText().toString().isEmpty()
                && !reg_password.getText().toString().isEmpty()
//                &&!reg_garage_name.getText().toString().isEmpty()
                && !reg_detailAddress.getText().toString().isEmpty()) {
            btn_register.setEnabled(true);
        } else {
            btn_register.setEnabled(false);
        }
    }

    //监听文本框输入，输入后
    @Override
    public void afterTextChanged(Editable s) {
    }

    /**
     * 没有下划线的可点击链接
     * NoLineClickSpan是一个继承于CLickSpan的类，重写了颜色和click事件，代码如下：
     */
    public class NoLineClickSpan extends ClickableSpan {
        int type;
        int showcolor;

        public NoLineClickSpan(int type, int cl) {
            super();
            this.type = type;
            showcolor = cl;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(showcolor);
            ds.setUnderlineText(false); //去掉下划线
        }

        @Override
        public void onClick(View v) {
            switch (type) {
//                case 1:
//                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.USER_PROTOCAL);
//                    break;
//                case 2:
//                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.USER_PROTOCAL);
//                    break;
//                case 3:
//                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.USER_PROTOCAL);
//                    break;
                default:
                    break;
            }
        }
    }

    private boolean prepareForSendVerify() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }
        if (register_phoneNum.length() == 0) {
            register_phoneNum.setError(getString(R.string.tip_please_input_phone));
            register_phoneNum.requestFocus();
            return true;
        } else {
            if (!TypeChk.isPhoneNum(register_phoneNum.getText().toString())) {
                register_phoneNum.setError(getString(R.string.tip_illegal_phone));
                register_phoneNum.requestFocus();
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
        if (register_phoneNum.length() == 0) {
            register_phoneNum.setError(getString(R.string.tip_please_input_phone));
            register_phoneNum.requestFocus();
            return true;
        } else {
            if (!TypeChk.isPhoneNum(register_phoneNum.getText().toString())) {
                register_phoneNum.setError(getString(R.string.tip_illegal_phone));
                register_phoneNum.requestFocus();
                return true;
            }
        }
        if (register_verifyCode.length() == 0) {
            register_verifyCode.setError(getString(R.string.tip_please_input_verify));
            register_verifyCode.requestFocus();
            return true;
        }
        if (regType == 2) {
            if (StringUtils.isEmpty(reg_tech_name.getText().toString())) {
                reg_tech_name.setError("请填写真实姓名");
                reg_tech_name.requestFocus();
                return true;
            }
        }
        if (regType == 2 || regType == 3) {
            if (StringUtils.isEmpty(reg_user_area.getText().toString())) {
                reg_user_area.setError("请填写区域");
                reg_user_area.requestFocus();
                return true;
            }
            if (StringUtils.isEmpty(reg_detailAddress.getText().toString())) {
                reg_detailAddress.setError("请填写详细地址");
                reg_detailAddress.requestFocus();
                return true;
            }
        }
        return false;
    }

    private void handleSendVerify() {
        if (prepareForSendVerify()) {
            return;
        }
        mPhoneNum = register_phoneNum.getText().toString().trim();
        String sysKey = CyptoUtils.encode("SC_Monkey_Code", mPhoneNum);
        showWaitDialog(R.string.progress_send);
        MonkeyApi.getPhoneCode(mPhoneNum, Constants.VERIFY_TYPE.REGISTER, sysKey, mSendHandler);
    }


    private void handleCheckVerify() {
        if (prepareForCheckVerify()) {
            return;
        }
        mPhoneNum = register_phoneNum.getText().toString().trim();
        mVerifyCode = register_verifyCode.getText().toString().trim();
        showWaitDialog(R.string.progress_submit);
        //校验验证码接口
        MonkeyApi.verficateCode(verifykey, mPhoneNum, mVerifyCode, mCheckHandler);
    }

    TextHttpResponseHandler mSendHandler = new TextHttpResponseHandler() {
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
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };


    TextHttpResponseHandler mCheckHandler = new TextHttpResponseHandler() {
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
                    handleRegister(resultBean.getResult().getKey());
                } else {
                    hideWaitDialog();
                    AppContext.showToast(resultBean.getCode());
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

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
     * @param verifykey
     */
    private void handleRegister(String verifykey) {
//        hideWaitDialog();
//        AppContext.showToast("正在提交请求");
        //注册
        mPhoneNum = register_phoneNum.getText().toString().trim();
        mVerifyCode = register_verifyCode.getText().toString().trim();
        regPassword = reg_password.getText().toString().trim();
        if (regType == 2 || regType == 3) {
            //区域
            if (StringUtils.isEmpty(mAreaCode)) {
                AppContext.showToastShort("请选择区域信息！");
                return;
            }
            region = mAreaCode;
            //详细地址
            detailAddress = reg_detailAddress.getText().toString();
            //真实姓名
            mUsername = reg_tech_name.getText().toString();
        }
        MonkeyApi.register(verifykey, mPhoneNum, regType, mUsername, regPassword, region, detailAddress, mSubmitHandler);
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

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (btn_register_getVerifyCode != null) {
                btn_register_getVerifyCode.setEnabled(true);
                btn_register_getVerifyCode.setText(R.string.register_getVerifyCode);// 重获验证码
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            if (btn_register_getVerifyCode != null) {
                btn_register_getVerifyCode.setEnabled(false);

                if (isAdded()) {
                    btn_register_getVerifyCode.setText(getString(R.string.register_send_wait,
                            String.valueOf(millisUntilFinished / 1000)));
                }
            }
        }
    }
}
