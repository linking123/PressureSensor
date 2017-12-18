package com.suncreate.pressuresensor.fragment.master;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.AppManager;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.Constants.SERVICE_TYPE;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.TechnicianOrder;
import com.suncreate.pressuresensor.bean.user.User;
import com.suncreate.pressuresensor.ui.SimpleBackActivity;
import com.suncreate.pressuresensor.util.Car.CarUtils;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * 技师预约
 * <p>
 * desc
 */
public class TechnicianAppointmentFragment extends BaseDetailFragment<TechnicianOrder> implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @Bind(R.id.btn_appointment)
    Button bAppointment;
    //请选择日期
    @Bind(R.id.tv_date_picker)
    TextView mDatePicker;
    //请选择时间
    @Bind(R.id.tv_time_picker)
    TextView mTimePicker;

    //汽车维修
    @Bind(R.id.checkBox1)
    CheckBox mCheckBox1;
    //保养维修
    @Bind(R.id.checkBox2)
    CheckBox mCheckBox2;
    //美容装饰
    @Bind(R.id.checkBox3)
    CheckBox mCheckBox3;
    //安装改装
    @Bind(R.id.checkBox4)
    CheckBox mCheckBox4;

    @Bind(R.id.tv_appointment_workTime)
    TextView mTvAppointmentWorkTime;
    //需求描述
    @Bind(R.id.tv_master_technician_describe)
    TextView masterTechnicianDescribe;
    //从车辆管理的默认车型带过来车型,没有的话重新选择
    //选过车型，显示现有车型，也可选择更改
    @Bind(R.id.car_brand_model_series_box)
    LinearLayout carBrandBox;
    @Bind(R.id.brand_icon)
    ImageView brand_icon;
    @Bind(R.id.car_brand_text)
    TextView car_brand_text;
    @Bind(R.id.tv_choose_change)
    TextView tv_choose_change;
    @Bind(R.id.ll_service_order_types)
    LinearLayout mLlServiceOrderTypes;
    private long mStoreId;
    private String mStoreStatus;
    private String mUserStatus;
    private User mUser;
    String mCarFromBroadcase;

    private final Calendar calendar = Calendar.getInstance();

    //新建广播，接收默认车辆改变的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    mCarFromBroadcase = intent.getStringExtra("mCarFromBroadcase");
                    CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_DEFAULT_CAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void initView(View view) {
        bAppointment.setOnClickListener(this);
        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);
        carBrandBox.setOnClickListener(this);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        mCarFromBroadcase = CarUtils.getmCarName();
    }

    @Override
    public void initData() {
//        mUser = AppContext.getInstance().getLoginUserExt();
        super.initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_technician_detail_appointment;
    }

    @Override
    protected String getCacheKey() {
        return "techOrder_" + mDetailId;
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getTechOrder(mDetailId, mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(TechnicianOrder detail) {
        super.executeOnLoadDataSuccess(detail);
        String storeSerivceType = detail.getStoreSerivceType();
        try {
            if (!TextUtils.isEmpty(storeSerivceType)) {
                //CREPAIRS-汽车维修 MAINTENANCE-保养修护 BDECORATION-美容装饰 IALTERATION-安装改装
                if (storeSerivceType.contains(SERVICE_TYPE.CREPAIRS)) {
                    mCheckBox1.setVisibility(View.VISIBLE);
                }
                if (storeSerivceType.contains(SERVICE_TYPE.MAINTENANCE)) {
                    mCheckBox2.setVisibility(View.VISIBLE);
                }
                if (storeSerivceType.contains(SERVICE_TYPE.BDECORATION)) {
                    mCheckBox3.setVisibility(View.VISIBLE);
                }
                if (storeSerivceType.contains(SERVICE_TYPE.IALTERATION)) {
                    mCheckBox4.setVisibility(View.VISIBLE);
                }
            } else {
                mCheckBox1.setVisibility(View.VISIBLE);
                mCheckBox2.setVisibility(View.VISIBLE);
                mCheckBox3.setVisibility(View.VISIBLE);
                mCheckBox4.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            mCheckBox1.setVisibility(View.VISIBLE);
            mCheckBox2.setVisibility(View.VISIBLE);
            mCheckBox3.setVisibility(View.VISIBLE);
            mCheckBox4.setVisibility(View.VISIBLE);
        }
        mStoreId = detail.getStoreId();
        mStoreStatus = detail.getStoreStatus();
        mUserStatus = detail.getUserStatus();
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<TechnicianOrder>>() {
        }.getType();
    }

    protected TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Toast.makeText(getContext(), "操作失败！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    DialogHelp.getTitleMessageDialog(getContext(), "预约成功", "您已成功预约服务技师，具体事宜稍后该技师会与您取得联系，请保持电话畅通。\n点击确定查看服务订单!",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().finish();
                                    AppManager.getAppManager().finishActivity(SimpleBackActivity.class);
                                    Bundle b = new Bundle();
                                    b.putInt("mCatalog", 1);//预约成功后跳转到服务订单，状态：待报价。
                                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SERVICE_ORDER, b);
                                }
                            }).show();
                } else {
                    Toast.makeText(getContext(), "操作失败！", Toast.LENGTH_LONG).show();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_appointment:
                //获取服务类型checkbox值
                //CREPAIRS-汽车维修 MAINTENANCE-保养修护 BDECORATION-美容装饰 IALTERATION-安装改装
                String storeSerivceType = "";
                if (mCheckBox1.isChecked()) {
                    storeSerivceType += "," + SERVICE_TYPE.CREPAIRS;
                }
                if (mCheckBox2.isChecked()) {
                    storeSerivceType += "," + SERVICE_TYPE.MAINTENANCE;
                }
                if (mCheckBox3.isChecked()) {
                    storeSerivceType += "," + SERVICE_TYPE.BDECORATION;
                }
                if (mCheckBox4.isChecked()) {
                    storeSerivceType += "," + SERVICE_TYPE.IALTERATION;
                }
                if (StringUtils.isEmpty(storeSerivceType)) {
                    Toast.makeText(getContext(), "请选择预约服务类型", Toast.LENGTH_LONG).show();
                    return;
                }
                String date = mDatePicker.getText().toString();
                String time = mTimePicker.getText().toString();
                if (StringUtils.isEmpty(date) || "请选择日期".equals(date)) {
                    Toast.makeText(getContext(), "请选择预约日期", Toast.LENGTH_LONG).show();
                    return;
                }
                if (StringUtils.isEmpty(time) || "请选择时间".equals(date)) {
                    Toast.makeText(getContext(), "请选择预约时间", Toast.LENGTH_LONG).show();
                    return;
                }

                //预约车型,存储用户车型表中的主键ID，
//                String carmodelId = AppContext.getInstance().getProperty("user.userModelid");
                //预约时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date ytime = null;
                try {
                    ytime = sdf.parse(date + " " + time + ":00");
                } catch (ParseException e) {
                }
                showWaitDialog();
                MonkeyApi.getTechOrderSubmit(storeSerivceType.substring(1), mCarFromBroadcase,
                        masterTechnicianDescribe.getText().toString(), mStoreId, ytime.getTime(), mSubmitHandler);
                break;
            case R.id.tv_date_picker:
                final DatePickerDialog datePickerDialog = DatePickerDialog
                        .newInstance(this, calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.setVibrate(false);
                datePickerDialog.show(getFragmentManager(), "datepicker");
                break;
            case R.id.tv_time_picker:
                new TimePickerDialog(getContext(), this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true).show();
                break;
            case R.id.car_brand_model_series_box:
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_CAR_MANAGER);
                break;
            default:
                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mDatePicker.setText(year + "-" + (month + 1) + "-" + day);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTimePicker.setText(hourOfDay + ":" + minute);
    }
}
