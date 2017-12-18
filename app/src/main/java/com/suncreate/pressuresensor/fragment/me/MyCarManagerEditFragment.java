package com.suncreate.pressuresensor.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.carBrand.CarManager;
import com.suncreate.pressuresensor.fragment.HomeFragment;
import com.suncreate.pressuresensor.util.Car.CarUtils;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 编辑车辆信息页面
 */
public class MyCarManagerEditFragment extends BaseDetailFragment<CarManager> implements View.OnClickListener {

    //发动机号
    @Bind(R.id.et_engine_num_text)
    EditText et_engine_num_text;
    //车架号
    @Bind(R.id.et_car_frame_num_text)
    EditText et_car_frame_num_text;
    //车牌号
    @Bind(R.id.et_car_num_text)
    EditText et_car_num_text;
    //车型选择
    @Bind(R.id.car_brand_model_series_box)
    LinearLayout carBrandBox;
    @Bind(R.id.brand_icon)
    ImageView brand_icon;
    @Bind(R.id.car_brand_text)
    TextView car_brand_text;
    @Bind(R.id.tv_choose_change)
    TextView tv_choose_change;

    //公里数
    @Bind(R.id.et_car_kms_text)
    EditText et_car_kms_text;
    //新车上路时间
    @Bind(R.id.tv_date_picker)
    TextView tv_date_picker;
    //完成编辑
    @Bind(R.id.btn_finish)
    Button btn_finish;

    private String carBrandId;//默认车辆品牌id
    private String isDefault = "";
    //用户车辆编号
    private String carId;
    private String carModelId;
    private String mEngineNum;
    private String mChassisNumber;
    private String mPlate;
    private String mCarBrand;
    private String mCarBrandIcon;
    private String mCarKMs;
    private String mCarRoadTime;
    private String mIsDefault;

    @Override
    public void initView(View view) {
        super.initView(view);

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        carBrandBox.setOnClickListener(this);

        Bundle b_carManager_edit = getArguments();
        carId = b_carManager_edit.getString("carId");
        isDefault = b_carManager_edit.getString("isDefault") == null ? "" : b_carManager_edit.getString("isDefault");

        if (!StringUtils.isEmpty(carId)) {
            sendRequestDataForNet();
        }

        btn_finish.setOnClickListener(this);
    }

    @Override
    protected void executeOnLoadDataSuccess(CarManager detail) {

        if (detail != null) {
            carModelId = detail.getCarmodelId();
            mEngineNum = detail.getEngineNumber();
            mChassisNumber = detail.getChassisNumber();
            mPlate = detail.getPlate();
            mCarBrand = detail.getCarmodelName();
            mCarBrandIcon = detail.getBrandIcon();
            mCarKMs = detail.getMileage();
            mCarRoadTime = detail.getRoadTime();
            mIsDefault = detail.getIsDefault();

            if (!StringUtils.isEmpty(mEngineNum)) {
                et_engine_num_text.setText(mEngineNum);
            }
            if (!StringUtils.isEmpty(mChassisNumber)) {
                et_car_frame_num_text.setText(mChassisNumber);
            }
            if (!StringUtils.isEmpty(mPlate)) {
                et_car_num_text.setText(mPlate);
            }
            if (!StringUtils.isEmpty(mCarBrand)) {
                car_brand_text.setText(mCarBrand);
            }
            if (!StringUtils.isEmpty(mCarBrandIcon)) {
                CarUtils.setCarBrandIcon(getContext(), brand_icon, mCarBrandIcon);
            }
            if (!StringUtils.isEmpty(mCarKMs)) {
                et_car_kms_text.setText(mCarKMs);
            }
            if (!StringUtils.isEmpty(mCarRoadTime)) {
                tv_date_picker.setText(mCarRoadTime);
            }
        }
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.doViewUserCar(isDefault, carId, mDetailHandler);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_car_manager_edit;
    }

    @Override
    protected String getCacheKey() {
        return "carmanageredit_" + mId;
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<CarManager>>() {
        }.getType();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.car_brand_model_series_box:
                Bundle b = new Bundle();
                b.putString("flag", "from_MyCarManagerEditFragment");
                UIHelper.showSimpleBackForResult(MyCarManagerEditFragment.this, HomeFragment.REQUEST_CODE_FOR_BRAND_CARMANAGEREDITFRAGMENT, SimpleBackPage.CAR_BRAND, b);
                break;
            case R.id.btn_finish:
                doFinishEditCar();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == HomeFragment.REQUEST_CODE_FOR_BRAND_CARMANAGEREDITFRAGMENT) {
            //选择带回来的是车型ID
            carModelId = data.getStringExtra("carId");
            carBrandId = data.getStringExtra("carBrandId");
            CarUtils.setCarBrandBox(getContext(), data, "carId", "carbrandIcon", brand_icon, "carBrandAndModelAndDisplacementAndYear", car_brand_text, tv_choose_change);
        }
    }

    private void doFinishEditCar() {

        mEngineNum = StringUtils.isEmpty(et_engine_num_text.getText().toString().trim()) ? "" : et_engine_num_text.getText().toString().trim();
        mChassisNumber = StringUtils.isEmpty(et_car_frame_num_text.getText().toString().trim()) ? "" : et_car_frame_num_text.getText().toString().trim();
        mPlate = StringUtils.isEmpty(et_car_num_text.getText().toString().trim()) ? "" : et_car_num_text.getText().toString().trim();
        mCarKMs = StringUtils.isEmpty(et_car_kms_text.getText().toString().trim()) ? "" : et_car_kms_text.getText().toString().trim();
        mCarRoadTime = StringUtils.isEmpty(tv_date_picker.getText().toString().trim()) ? "" : tv_date_picker.getText().toString().trim();

        MonkeyApi.doEditCar(carId, carModelId, mPlate, mEngineNum, mChassisNumber, mCarKMs, mCarRoadTime, mIsDefault, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Intent intent = new Intent();

                intent.putExtra("mCarmodelId", carModelId);
                intent.putExtra("mIsDefault", mIsDefault);
                if ("1".equals(mIsDefault)) {
                    CarUtils.setDefaultCar(carId, carModelId, carBrandId);
                    getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_DEFAULT_CAR));
                }
                intent.putExtra("mPlate", mPlate);

                getActivity().setResult(MyCarManagerFragment.REQUEST_CODE_EDIT_CAR, intent);
                getActivity().finish();
            }
        });
    }
}
