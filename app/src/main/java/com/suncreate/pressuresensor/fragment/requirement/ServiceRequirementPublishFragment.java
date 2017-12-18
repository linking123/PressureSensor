package com.suncreate.pressuresensor.fragment.requirement;

import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.meidia.TweetPicturesPreviewer;
import com.suncreate.pressuresensor.util.Car.CarUtils;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

import static org.kymjs.kjframe.utils.ImageUtils.getSmallImageFile;

/**
 * 发布服务需求
 */
public class ServiceRequirementPublishFragment extends BaseFragment
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private File demandImg1;
    private File demandImg2;
    private File demandImg3;

    //4个checkbox
    @Bind(R.id.checkbox_1)
    CheckBox checkbox_1;
    @Bind(R.id.checkbox_2)
    CheckBox checkbox_2;
    @Bind(R.id.checkbox_3)
    CheckBox checkbox_3;
    @Bind(R.id.checkbox_4)
    CheckBox checkbox_4;

    //请选择日期
    @Bind(R.id.tv_date_picker)
    TextView mDatePicker;
    //请选择时间
    @Bind(R.id.tv_time_picker)
    TextView mTimePicker;

    //服务位置
    @Bind(R.id.et_server_address)
    EditText et_server_address;
    //服务描述
    @Bind(R.id.et_demand_desc)
    EditText et_demand_desc;
    //照片容器
    @Bind(R.id.recycler_images)
    TweetPicturesPreviewer mLayImages;
    //照相机
    @Bind(R.id.upload_img)
    ImageButton mUpload_img;
    //立即发布
    @Bind(R.id.btn_publish)
    Button btn_publish;

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

    //需求类型
    private static final String DEMAND_TYPE = "1";
    private String noInternetAddressError = "网络定位失败，请手动填写";
    private String carModelId;
    private final Calendar calendar = Calendar.getInstance();

    //新建广播，接收默认车辆改变的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
    public void initWidget(View view) {
        super.initWidget(view);
        mUpload_img.setOnClickListener(this);
        btn_publish.setOnClickListener(this);
        mDatePicker.setOnClickListener(this);
        mTimePicker.setOnClickListener(this);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        carBrandBox.setOnClickListener(this);

        //地址是定位后带过来的
        if (StringUtils.isEmpty(AppContext.addressDetail)) {
            et_server_address.setHint(noInternetAddressError);
        } else {
            et_server_address.setText(AppContext.addressDetail);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_service_publish;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.car_brand_model_series_box:
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_CAR_MANAGER);
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
            case R.id.upload_img:
                mLayImages.onLoadMoreClick();
                break;
            case R.id.btn_publish:
                //服务类别
                String serivceType = "";
                if (checkbox_1.isChecked()) {
                    serivceType += "," + Constants.SERVICE_TYPE.MAINTENANCE;
                }
                if (checkbox_2.isChecked()) {
                    serivceType += "," + Constants.SERVICE_TYPE.CREPAIRS;
                }
                if (checkbox_3.isChecked()) {
                    serivceType += "," + Constants.SERVICE_TYPE.BDECORATION;
                }
                if (checkbox_4.isChecked()) {
                    serivceType += "," + Constants.SERVICE_TYPE.IALTERATION;
                }
                if (StringUtils.isEmpty(serivceType)) {
                    AppContext.showToastShort("请选择服务类别");
                    return;
                } else {
                    serivceType = serivceType.substring(1, serivceType.length());
                }

                //地址
                String demandAddress = et_server_address.getText().toString();
                if (demandAddress.equals(noInternetAddressError) || StringUtils.isEmpty(demandAddress)) {
                    AppContext.showToastShort(noInternetAddressError);
                    return;
                }
                String demandDesc = et_demand_desc.getText().toString();
                if (demandDesc.equals("需求描述越详细，技师更快更精准的提供服务") || demandDesc.equals("")) {
                    Toast.makeText(getContext(), "请填写服务需求描述！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String demandType = DEMAND_TYPE;

//                com.suncreate.pressuresensor.bean.user.User user = AppContext.getInstance().getLoginUserExt();
//                String demandPhone = user.getUserMobile();
//                String demandUsername = user.getUserName();
                //需求车型ID 应该是车辆全称
                carModelId = CarUtils.getmCarModelId();

                Double positionLat = AppContext.lat;
                Double positionLon = AppContext.lon;

                //预约时间
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
                String appointTimeStart = String.valueOf(ytime.getTime());

                try {
                    //图片上传
                    int imgsCount = mLayImages.getChildCount();
                    if (imgsCount == 0) {
                        Toast.makeText(getContext(), "请至少上传一张图片！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String[] imgPaths = mLayImages.getPaths();
                    int len = imgPaths.length;
                    if (len == 1) {
                        demandImg1 = getSmallImageFile(getContext(), imgPaths[0], 300, 200, true);
//                        demandImg1 = new File(imgPaths[0]);
                    } else if (len == 2) {
                        demandImg1 = getSmallImageFile(getContext(), imgPaths[0], 300, 200, true);
                        demandImg2 = getSmallImageFile(getContext(), imgPaths[1], 300, 200, true);
                    } else if (len == 3) {
                        demandImg1 = getSmallImageFile(getContext(), imgPaths[0], 300, 200, true);
                        demandImg2 = getSmallImageFile(getContext(), imgPaths[1], 300, 200, true);
                        demandImg3 = getSmallImageFile(getContext(), imgPaths[2], 300, 200, true);
                    }
                    showWaitDialog("需求发布中...");
                    //demandDesc demandType demandAddress carModelId demandUsername positionLat demandPhone positionLng
                    MonkeyApi.getServiceRequirementSubmit(demandDesc, demandType, demandAddress, carModelId, serivceType,
                            "123", "123", positionLat == null ? "" : positionLat.toString(),
                            positionLon == null ? "" : positionLon.toString(), appointTimeStart, demandImg1, demandImg2, demandImg3, mSubmitHeandler);

                } catch (IOException e) {
                    AppContext.showToast("图像不存在，上传失败");
                }
                break;
        }
    }

    protected TextHttpResponseHandler mSubmitHeandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("操作失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                JSONObject jsStr = (JSONObject) JSONObject.parse(responseString);
                if ("1".equals(jsStr.get("code").toString())) {
                    Toast.makeText(getContext(), "服务需求发布成功，请保持电话畅通，稍后会有技师与您取得联系。", Toast.LENGTH_LONG).show();
//                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.REQUIREMNET_VIEW_PAGE);
                    getActivity().finish();
                    //发送广播
                    getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_REQUIREMENT_NEW));
                } else {
                    AppContext.showToastShort("操作失败！");
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            hideWaitDialog();
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
