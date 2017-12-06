package com.suncreate.fireiot.fragment.Field;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.GarageOrder;
import com.suncreate.fireiot.ui.SimpleBackActivity;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.ShowContactTell;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 工位预约
 * <p>
 * desc
 */
public class FieldAppointmentFragment extends BaseDetailFragment<GarageOrder> implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private Dialog dialog;
    private View inflate;
    @Bind(R.id.btn_appointment)
    Button bAppointment;
    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;
    @Bind(R.id.ll_contact_online)
    LinearLayout mLlContactOnline;
    //需求描述
    @Bind(R.id.tv_master_technician_describe)
    TextView masterTechnicianDescribe;
    //请选择日期
    @Bind(R.id.tv_date_picker)
    TextView mDatePicker;
    //请选择时间
    @Bind(R.id.tv_time_picker)
    TextView mTimePicker;

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

    private final Calendar calendar = Calendar.getInstance();
    private long mStoreId;

    String mCarFromBroadcase;
    private String mContactPhone;
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
    public void initView(View root) {
        bAppointment.setOnClickListener(this);
        mLlContactPhone.setOnClickListener(this);
        mLlContactOnline.setOnClickListener(this);
        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型,编辑
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        mCarFromBroadcase = CarUtils.getmCarName();
        carBrandBox.setOnClickListener(this);
    }

    @Override
    protected String getCacheKey() {
        return "fieldOrder_" + mId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_field_detail_appointment;
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<GarageOrder>>() {
        }.getType();
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        if (!StringUtils.isEmpty(b.getString("id"))) {
            mStoreId = Long.valueOf(b.getString("id"));
            MonkeyApi.getGarageOrder(mStoreId, mDetailHandler);
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(GarageOrder detail) {
        super.executeOnLoadDataSuccess(detail);
        String carmodelName = detail.getCarmodelName();
        if (!StringUtils.isEmpty(carmodelName)) {
//            masterTechnicianCarType.setText(carmodelName.trim());
        } else {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_contact_online:
                AppContext.showToast("暂未开通在线沟通功能!");
                break;
            case R.id.ll_contact_phone:
                ShowContactTell.showContact(getContext(), "0551-1122331");
                break;
            case R.id.btn_appointment:
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date ytime = null;
                try {
                    ytime = sdf.parse(date + " " + time + ":00");
                } catch (ParseException e) {
                }
                String timee = String.valueOf(ytime.getTime());
                MonkeyApi.getGarageOrderSubmit("1",null,mCarFromBroadcase, masterTechnicianDescribe.getText().toString(), String.valueOf(mStoreId),
                        timee, mSubmitHeandler);
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

    protected TextHttpResponseHandler mSubmitHeandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Toast.makeText(getContext(), "操作失败！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                JSONObject jsStr = (JSONObject) JSONObject.parse(responseString);
                if (jsStr != null && "1".equals(jsStr.get("code").toString())) {
                    DialogHelp.getMessageDialog(getContext(),getString(R.string.actionbar_title_field_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getActivity().finish();
                            AppManager.getAppManager().finishActivity(SimpleBackActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("mCatalog", 1);//预约成功后跳转到服务订单，状态：待确认。
                            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FIELD_ORDER, b);
                        }
                    }).show();
                } else {
                    Toast.makeText(getContext(), "操作失败！", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        mDatePicker.setText(year + "-" + (month + 1) + "-" + day);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTimePicker.setText(hourOfDay + ":" + minute);
    }
}

