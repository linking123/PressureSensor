package com.suncreate.fireiot.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.carBrand.CarManager;
import com.suncreate.fireiot.fragment.HomeFragment;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.LicenseKeyboardUtil;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.widget.togglebutton.ToggleButton;
import com.loopj.android.http.TextHttpResponseHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 增加车辆页面
 */
public class MyCarManagerAddFragment extends BaseFragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    public static final String INPUT_LICENSE_COMPLETE = "me.kevingo.licensekeyboard.input.comp";
    public static final String INPUT_LICENSE_KEY = "LICENSE";

    @Bind(R.id.car_brand_model_series_box)
    View carBrandBox;
    @Bind(R.id.brand_icon)
    ImageView brand_icon;
    @Bind(R.id.car_brand_text)
    TextView car_brand_text;
    @Bind(R.id.tv_choose_change)
    TextView tv_choose_change;

    @Bind(R.id.et_inputbox1)
    EditText inputbox1;
    @Bind(R.id.et_inputbox2)
    EditText inputbox2;
    @Bind(R.id.et_inputbox3)
    EditText inputbox3;
    @Bind(R.id.et_inputbox4)
    EditText inputbox4;
    @Bind(R.id.et_inputbox5)
    EditText inputbox5;
    @Bind(R.id.et_inputbox6)
    EditText inputbox6;
    @Bind(R.id.et_inputbox7)
    EditText inputbox7;
    @Bind(R.id.et_engine_num_text)
    EditText et_engine_num;
    @Bind(R.id.et_car_frame_num_text)
    EditText et_car_frame_num_text;
    @Bind(R.id.et_car_kms_text)
    EditText et_car_kms_text;
    @Bind(R.id.btn_finish)
    Button btn_finish;
    //请选择日期
    @Bind(R.id.tv_date_picker)
    TextView mDatePicker;
    @Bind(R.id.tb_default_love_car)
    ToggleButton tb_default_love_car;

    private LicenseKeyboardUtil keyboardUtil;
    private final Calendar calendar = Calendar.getInstance();

    private CarManager carManager;
    //ShoppingUserCarmodel(carmodelId,plate,engineNumber,chassisNumber,mileage,roadTime,isDefault)
    private String mCarmodelId;  // 车型ID
    private String carBrandId;  //车品牌ID
    private String mPlate;  //车牌号
    private String mEngineNum;  //发动机号
    private String mChassisNumber;  //车架号 vin
    private String mMileage;  //公里数
    private String mRoadTime;  //上路时间
    private String mIsDefault;  //1 代表默认  0为一般

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {

        inputbox1.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
        inputbox2.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
        inputbox3.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
        inputbox4.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
        inputbox5.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
        inputbox6.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出
        inputbox7.setInputType(InputType.TYPE_NULL);// 让系统键盘不弹出

        carBrandBox.setOnClickListener(this);
        inputbox1.setOnClickListener(this);
        inputbox2.setOnClickListener(this);
        inputbox3.setOnClickListener(this);
        inputbox4.setOnClickListener(this);
        inputbox5.setOnClickListener(this);
        inputbox6.setOnClickListener(this);
        inputbox7.setOnClickListener(this);
        et_engine_num.setOnClickListener(this);
        et_car_frame_num_text.setOnClickListener(this);
        et_car_kms_text.setOnClickListener(this);
        mDatePicker.setOnClickListener(this);
        btn_finish.setOnClickListener(this);

        tb_default_love_car.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    mIsDefault = "1";
                } else {
                    mIsDefault = "0";
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_car_manager_add;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.car_brand_model_series_box:
                Bundle b = new Bundle();
                b.putString("flag", "from_MyCarManageAddFragment");
                UIHelper.showSimpleBackForResult(MyCarManagerAddFragment.this, HomeFragment.REQUEST_CODE_FOR_BRAND_CARMANAGERADDFRAGMENT, SimpleBackPage.CAR_BRAND, b);
                break;
            case R.id.et_inputbox1:
            case R.id.et_inputbox2:
            case R.id.et_inputbox3:
            case R.id.et_inputbox4:
            case R.id.et_inputbox5:
            case R.id.et_inputbox6:
            case R.id.et_inputbox7:
//                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(getContext().INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //隐藏系统软键盘
                ((InputMethodManager) mContext.getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                keyboardUtil = new LicenseKeyboardUtil(getContext(), new EditText[]{inputbox1, inputbox2, inputbox3, inputbox4, inputbox5, inputbox6, inputbox7});
                keyboardUtil.showKeyboard();
                break;
            case R.id.et_engine_num_text:
            case R.id.et_car_frame_num_text:
            case R.id.et_car_kms_text:
                keyboardUtil.hideKeyboard();
                break;
            case R.id.tv_date_picker:
                final DatePickerDialog datePickerDialog = DatePickerDialog
                        .newInstance(this, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.setVibrate(false);
                datePickerDialog.show(getFragmentManager(), "datepicker");
                break;
            case R.id.btn_finish:
                doAddCar();
                break;
            default:
                break;
        }

    }

    public void doAddCar() {

        //ShoppingUserCarmodel(carmodelId,plate,engineNumber,chassisNumber,mileage,roadTime,isDefault)

        //车型;这个应该是车型选择后返回的id值;carBrandBox返回的车型ID;
//        mCarmodelId = "2";
        if (StringUtils.isEmpty(mCarmodelId)) {
            AppContext.showToastShort("请选择爱车");
            return;
        }
        //车牌号
        mPlate = inputbox1.getText().toString() + inputbox2.getText().toString() + inputbox3.getText().toString() +
                inputbox4.getText().toString() + inputbox5.getText().toString() + inputbox6.getText().toString() + inputbox7.getText().toString();
        //发动机号
        mEngineNum = et_engine_num.getText().toString();
        //车架号
        mChassisNumber = et_car_frame_num_text.getText().toString().trim();
        if (mChassisNumber.length() != 0 && mChassisNumber.length() != 17) {
            AppContext.showToastShort("请填写正确格式的车架号");
            return;
        }
        //公里数
        mMileage = et_car_kms_text.getText().toString();
        //新车上路时间
        mRoadTime = mDatePicker.getText().toString();
        String mRoadTime13 = null;
        if ("请选择上路时间".equals(mRoadTime)) {
            mRoadTime13 = "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date mRoadTimeDate = null;
            try {
                mRoadTimeDate = sdf.parse(mRoadTime + " " + "00:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mRoadTime13 = String.valueOf(mRoadTimeDate.getTime());
        }

        //构造carmanager对象
        carManager = new CarManager();
        carManager.setCarmodelId(mCarmodelId);
        carManager.setPlate(StringUtils.isEmpty(mPlate) ? "" : mPlate);
        carManager.setEngineNumber(StringUtils.isEmpty(mEngineNum) ? "" : mEngineNum);
        carManager.setChassisNumber(StringUtils.isEmpty(mChassisNumber) ? "" : mChassisNumber);
        carManager.setMileage(StringUtils.isEmpty(mMileage) ? "" : mMileage);
        carManager.setRoadTime(StringUtils.isEmpty(mRoadTime13) ? "" : mRoadTime13);
        carManager.setIsDefault(StringUtils.isEmpty(mIsDefault) ? "0" : mIsDefault);
        MonkeyApi.doAddCar(carManager.getCarmodelId(), carManager.getPlate(), carManager.getEngineNumber(),
                carManager.getChassisNumber(), carManager.getMileage(), carManager.getRoadTime(), carManager.getIsDefault(), mHandler);
    }

    protected TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("添加失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {

            //如果选择了默认车辆，则更改用户表中车辆id
            if ("1".equals(mIsDefault)) {
                CarUtils.setDefaultCar("", mCarmodelId, carBrandId);
                //如果新增时设置为默认车辆了，通知需要更改车型的控件刷新
                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_DEFAULT_CAR));
            }

            //完成之后需要提醒列表页刷新，用resultBack
            try {
                ResultBean<CarManager> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<CarManager>>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("mCarmodelId", mCarmodelId);
                    intent.putExtra("mIsDefault", mIsDefault);
                    intent.putExtra("mPlate", mPlate);
                    getActivity().setResult(MyCarManagerFragment.REQUEST_CODE_ADD_CAR, intent);
                    getActivity().finish();
                } else {
                    AppContext.showToast(resultBean.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //回调，显示选择的车型
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == HomeFragment.REQUEST_CODE_FOR_BRAND_CARMANAGERADDFRAGMENT) {
            String carId = data.getStringExtra("carId");
            carBrandId = data.getStringExtra("carBrandId");
            //暂存用户车型ID
            mCarmodelId = carId;

            CarUtils.setCarBrandBox(getContext(), data, "carId", "carbrandIcon", brand_icon, "carBrandAndModelAndDisplacementAndYear", car_brand_text, tv_choose_change);

        }

    }

    //回调，显示日期
    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mDatePicker.setText(year + "-" + (month + 1) + "-" + day);
        mDatePicker.setCompoundDrawables(null, null, null, null);
    }
}
