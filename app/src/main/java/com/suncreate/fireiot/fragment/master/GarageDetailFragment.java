package com.suncreate.fireiot.fragment.master;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.general.TechCarBrandGridAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Garage;
import com.suncreate.fireiot.ui.OSCPhotosActivity;
import com.suncreate.fireiot.util.ShowContactTell;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;

import butterknife.Bind;


/**
 * 快修站详情
 * <p>
 * desc
 */
public class GarageDetailFragment extends BaseDetailFragment<Garage> implements View.OnClickListener {
    private TechCarBrandGridAdapter gridAdapter;

    @Bind(R.id.btn_appointment)
    Button bAppointment;

    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;

    @Bind(R.id.tv_master_technician_name)
    TextView masterTechnicianName;

    @Bind(R.id.tv_master_technician_time)
    TextView masterTechnicianTime;

    @Bind(R.id.tv_master_repair_level)
    TextView masterRepairLevel;

    @Bind(R.id.icon_master_technician_car)
    ImageView masterTechnicianCar;

    @Bind(R.id.tv_master_technician_identity1)
    TextView masterTechnicianIdentityCar;

    @Bind(R.id.icon_master_technician_realstore)
    ImageView masterTechnicianRealstore;

    @Bind(R.id.tv_master_technician_identity2)
    TextView masterTechnicianIdentityRealstore;


    @Bind(R.id.icon_master_technician)
    ImageView masterTechnicianImage;

    @Bind(R.id.tv_master_technician_service1)
    TextView masterTechnicianStoreInfo;

    @Bind(R.id.tv_master_technician_service3)
    TextView masterTechnicianStoreTech;

    @Bind(R.id.garage_brand_name)
    GridView mbrandName;

    @Bind(R.id.tv_tech_brand_name)
    TextView tvbrandName;

    @Bind(R.id.tv_master_field_address)
    TextView mTvMasterFieldAddress;

    @Bind(R.id.tv_master_field_gps)
    TextView mTvMasterFieldGps;
    @Bind(R.id.tv_master_repair_order)
    TextView mTvMasterRepairOrder;
    @Bind(R.id.ll_contact_online)
    LinearLayout mLlContactOnline;
    @Override
    public void initView(View root) {
        masterTechnicianImage.setOnClickListener(this);
        bAppointment.setOnClickListener(this);
        mLlContactPhone.setOnClickListener(this);
        mTvMasterFieldGps.setOnClickListener(this);
        mLlContactOnline.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_garage_detail;
    }

    @Override
    protected String getCacheKey() {
        return "garage_" + mId;
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Garage>>() {
        }.getType();
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        String garageId = b.getString("id");
        if (!StringUtils.isEmpty(garageId)) {
            MonkeyApi.getGarageDetail(Long.parseLong(garageId), mDetailHandler);
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(Garage detail) {
        super.executeOnLoadDataSuccess(detail);
        String storeLogo = detail.getStoreLogoId();
        getImgLoader().load(MonkeyApi.getPartImgUrl(storeLogo)).error(R.drawable.logo).into(masterTechnicianImage);

        mTvMasterRepairOrder.setText(String.format(getString(R.string.str_tech_order_count),
                TextUtils.isEmpty(detail.getOrderCount()) ? "0" : detail.getOrderCount()));
        masterTechnicianName.setText(TextUtils.isEmpty(detail.getStoreName()) ? "" : detail.getStoreName());
        mTvMasterFieldAddress.setText(TextUtils.isEmpty(detail.getStoreAddress()) ? "无位置信息" : detail.getStoreAddress());
        masterTechnicianStoreInfo.setText(TextUtils.isEmpty(detail.getStoreInfo()) ? "" : detail.getStoreInfo());
        String level = detail.getStoreTechnicianLevel();
        if ("0".equals(level)) {
            masterRepairLevel.setText(R.string.str_community);
            masterRepairLevel.setBackgroundResource(R.drawable.btn_bg_primary);
        } else if ("1".equals(level)) {
            masterRepairLevel.setText(R.string.str_region);
            masterRepairLevel.setBackgroundResource(R.drawable.btn_bg_region);
        } else {
            masterRepairLevel.setVisibility(View.INVISIBLE);
        }
        masterTechnicianTime.setText(String.format(getString(R.string.str_tech_working), detail.getStoreTimeDesc()));
        if (!"1".equals(detail.getStoreCardApprove())) {
            masterTechnicianCar.setVisibility(View.GONE);
            masterTechnicianIdentityCar.setText("身份认证未审核");
        }
        if (!"1".equals(detail.getStoreRealstoreApprove())) {
            masterTechnicianRealstore.setVisibility(View.GONE);
            masterTechnicianIdentityRealstore.setText("资格认证未审核");
        }
        String store_carmodel = detail.getBrandIcon();//专修品牌

        if (!StringUtils.isEmpty(store_carmodel)) {
            String[] storeCarmodels;
            if (store_carmodel.contains(",")) {
                storeCarmodels = store_carmodel.split(",");
            } else {
                storeCarmodels = new String[]{store_carmodel};
            }
            gridAdapter = new TechCarBrandGridAdapter(storeCarmodels, getContext());
            mbrandName.setAdapter(gridAdapter);
        } else {
            mbrandName.setVisibility(View.GONE);
            tvbrandName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //预览店铺图片大图
            case R.id.icon_master_technician:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getStoreLogoId()));
                break;
            case R.id.tv_master_field_gps:
                UIHelper.showNavigateActivity(getActivity(), String.valueOf(mTvMasterFieldAddress.getText()));
                break;
            case R.id.ll_contact_online:
                AppContext.showToast("暂未开通在线沟通功能!");
                break;
            case R.id.ll_contact_phone:
                ShowContactTell.showContact(getContext(), mDetail.getStoreTelephone());
                break;
            case R.id.btn_appointment:
                if (!AppContext.getInstance().isLogin()) {
                    UIHelper.showLoginActivity(getActivity());
                } else {
                    Bundle b = getArguments();
                    String techId = b.getString("id");
                    b.putString("id", techId);
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GARAGE_APPOINTMENT, b);
                }
                break;
            default:
                break;
        }
    }
}
