package com.suncreate.fireiot.fragment.master;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.suncreate.fireiot.bean.user.Technician;
import com.suncreate.fireiot.ui.OSCPhotosActivity;
import com.suncreate.fireiot.util.ShowContactTell;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * 技师详情
 * <p>
 * desc
 */
public class TechnicianDetailFragment extends BaseDetailFragment<Technician> implements View.OnClickListener {

    @Bind(R.id.btn_appointment)
    Button mAppointment;

    @Bind(R.id.icon_master_technician)
    ImageView masterIconTechnician;

    @Bind(R.id.tv_master_technician_name)
    TextView masterTechnicianName;

    @Bind(R.id.tv_master_technician_year)
    TextView masterTechnicianYear;

    @Bind(R.id.tv_master_technician_order)
    TextView masterTechnicianOrder;

    @Bind(R.id.tv_master_technician_distance)
    TextView mTechnicianDistance;

    @Bind(R.id.tv_technician_gps)
    TextView mTvTechnicianGps;
    @Bind(R.id.tv_master_technician_address)
    TextView mTvTechnicianAddress;

    @Bind(R.id.tv_master_technician_time)
    TextView masterTechnicianTime;

    @Bind(R.id.tv_master_technician_level)
    TextView masterTechnicianLevel;

    @Bind(R.id.icon_master_technician_car)
    ImageView masterTechnicianCar;

    @Bind(R.id.tv_master_technician_identity1)
    TextView masterTechnicianIdentityCar;

    @Bind(R.id.icon_master_technician_realstore)
    ImageView masterTechnicianRealstore;

    @Bind(R.id.icon_master_technician_recommond)
    ImageView masterTechnicianRecommond;

    @Bind(R.id.tv_master_technician_identity2)
    TextView masterTechnicianIdentityRealstore;

    @Bind(R.id.tv_master_technician_service1)
    TextView masterTechnicianStoreInfo;

    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;
    @Bind(R.id.ll_contact_online)
    LinearLayout mLlContactOnline;
    @Bind(R.id.tech_brand_name)
    GridView mGvBrandName;
    @Bind(R.id.tv_tech_brand_name)
    TextView tvBrandName;

    private TechCarBrandGridAdapter gridAdapter;
    private String dist;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_technician_detail;
    }

    @Override
    protected String getCacheKey() {
        return "tech_" + mDetailId;
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getTechDetail(mDetailId, mDetailHandler);
    }

    @Override
    public void initView(View view) {
        Bundle b = getArguments();
        dist = b.getString("dist");
        mTvTechnicianGps.setOnClickListener(this);
        mAppointment.setOnClickListener(this);
        mLlContactPhone.setOnClickListener(this);
        mLlContactOnline.setOnClickListener(this);
        masterIconTechnician.setOnClickListener(this);
        masterTechnicianRecommond.setOnClickListener(this);
    }

    @Override
    protected void executeOnLoadDataSuccess(Technician detail) {
        super.executeOnLoadDataSuccess(detail);
        //头像
        getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getStoreLogoId())).error(R.drawable.widget_dface).into(masterIconTechnician);
        //技师简介
        getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getJsRecommondId())).error(R.drawable.widget_dface).into(masterTechnicianRecommond);
        //技师店铺地址  距离  导航
        if (!StringUtils.isEmpty(detail.getStoreLng()) && !StringUtils.isEmpty(detail.getStoreLat())) {
            Geocoder geocoder = new Geocoder(getContext(), Locale.CHINA);
            try {
                List<Address> addressList = geocoder.getFromLocation(Double.valueOf(detail.getStoreLat()), Double.valueOf(detail.getStoreLng()), 2);
                mTvTechnicianAddress.setText(addressList.get(0).getAddressLine(0));
            } catch (IOException e) {
                getLocationFromFailue();
                e.printStackTrace();
            }
        } else {
            getLocationFromFailue();
        }
        //距离
        if (!StringUtils.isEmpty(dist)) {
            Double distDouble = Double.parseDouble(dist);
            if (distDouble > 1000) {
                distDouble = distDouble / 1000;
                mTechnicianDistance.setText(String.format(getContext().getString(R.string.str_tech_distance_kilometer_with_dist), distDouble));
            } else {
                mTechnicianDistance.setText(String.format(getContext().getString(R.string.str_distance_meter_with_dist), distDouble));
            }
        } else {
            mTechnicianDistance.setText(getContext().getString(R.string.str_distance_empty));
        }
        //级别
        masterTechnicianName.setText(detail.getUserRealname());
        String level = detail.getStoreTechnicianLevel();
        if ("1".equals(level)) {
            masterTechnicianLevel.setText("初级");
            masterTechnicianLevel.setBackgroundResource(R.drawable.btn_bg_primary);
        } else if ("2".equals(level)) {
            masterTechnicianLevel.setText("中级");
            masterTechnicianLevel.setBackgroundResource(R.drawable.btn_bg_middle);
        } else if ("3".equals(level)) {
            masterTechnicianLevel.setText("高级");
            masterTechnicianLevel.setBackgroundResource(R.drawable.btn_bg_high);
        } else {
            masterTechnicianLevel.setVisibility(View.INVISIBLE);
        }
        //修龄
        if (!StringUtils.isEmpty(detail.getStoreTechnicianYear())) {
            masterTechnicianYear.setText(String.format(getString(R.string.str_tech_year), detail.getStoreTechnicianYear()));
        }
        //接单数
        masterTechnicianOrder.setText(String.format(getString(R.string.str_tech_order_count), StringUtils.isEmpty(detail.getOrderCount()) ? "0" : detail.getOrderCount()));
        if (!"1".equals(detail.getStoreCardApprove())) {
            masterTechnicianCar.setVisibility(View.GONE);
            masterTechnicianIdentityCar.setText("身份认证未审核");
        }
        if (!"1".equals(detail.getStoreRealstoreApprove())) {
            masterTechnicianRealstore.setVisibility(View.GONE);
            masterTechnicianIdentityRealstore.setText("资格认证未审核");
        }
        //工作时间
        masterTechnicianTime.setText(String.format(getString(R.string.str_tech_working), StringUtils.isEmpty(detail.getStoreTimeDesc()) ? "节假日不休息" : detail.getStoreTimeDesc()));
        masterTechnicianStoreInfo.setText(StringUtils.isEmpty(mDetail.getStoreInfo()) ? "专修主流品牌，请放心选择" : mDetail.getStoreInfo());
        //专修品牌
        String store_carmodel = detail.getBrandIcon();
        if (!StringUtils.isEmpty(store_carmodel)) {
            if (store_carmodel.contains(",")) {
                String[] storeCarmodels = store_carmodel.split(",");
                gridAdapter = new TechCarBrandGridAdapter(storeCarmodels, getContext());
                mGvBrandName.setAdapter(gridAdapter);
            } else {
                String[] storeCarmodels = new String[]{store_carmodel};
                gridAdapter = new TechCarBrandGridAdapter(storeCarmodels, getContext());
                mGvBrandName.setAdapter(gridAdapter);
            }
        } else {
            mGvBrandName.setVisibility(View.INVISIBLE);
            tvBrandName.setVisibility(View.VISIBLE);
        }
    }

    private void getLocationFromFailue() {
        mTvTechnicianAddress.setText("该技师无法定位");
        mTvTechnicianGps.setText("无法导航");
        mTvTechnicianGps.setClickable(false);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Technician>>() {
        }.getType();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //预览技师头像大图
            case R.id.icon_master_technician:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getStoreLogoId()));
                break;
            //预览技师简介大图
            case R.id.icon_master_technician_recommond:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getJsRecommondId()));
                break;
            //开始导航
            case R.id.tv_technician_gps:
                UIHelper.showLonLatNavigateActivity(getActivity(), mDetail.getStoreLat(), mDetail.getStoreLng());
                break;
            case R.id.ll_contact_online:
                AppContext.showToast("暂未开通在线沟通功能!");
                break;
            case R.id.ll_contact_phone:
                ShowContactTell.showContact(getContext(), mDetail.getUserMobile());
                break;
            case R.id.btn_appointment:
                if (!AppContext.getInstance().isLogin()) {
                    UIHelper.showLoginActivity(getActivity());
                } else {
                    Bundle b = new Bundle();
                    b.putLong("id", mDetailId);
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.TECHNICIAN_APPOINTMENT, b);
                }
                break;
            default:
                break;
        }
    }

}
