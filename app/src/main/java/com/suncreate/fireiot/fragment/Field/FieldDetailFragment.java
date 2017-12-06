package com.suncreate.fireiot.fragment.Field;

import android.app.Dialog;
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

import butterknife.Bind;

/**
 * 场地详情页
 */
public class FieldDetailFragment extends BaseDetailFragment<Garage> implements View.OnClickListener {

    private Dialog dialog;
    private View inflate;
    private TechCarBrandGridAdapter gridAdapter;

    //立即预约
    @Bind(R.id.btn_appointment_now)
    Button btn_appointment_now;
    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;
    @Bind(R.id.ll_contact_online)
    LinearLayout mLlContactOnline;
    @Bind(R.id.garage_brand_name)
    GridView mbrandName;
    //店铺图片
    @Bind(R.id.icon_master_field)
    ImageView icon_master_field;
    //店铺名称
    @Bind(R.id.tv_master_field_name)
    TextView tv_master_field_name;
    @Bind(R.id.tv_master_repair_level)
    TextView masterRepairLevel;
    //店铺工位数
    @Bind(R.id.tv_master_field_work)
    TextView tv_master_field_work;
    //店铺营业时间
    @Bind(R.id.tv_master_field_serviceTime)
    TextView masterTechnicianTime;
    //店铺地址
    @Bind(R.id.tv_master_field_address)
    TextView tv_master_field_address;
    @Bind(R.id.tv_tech_brand_name)
    TextView tvbrandName;
    //接单数
    @Bind(R.id.tv_master_field_orders)
    TextView tv_master_field_orders;
    //导航
    @Bind(R.id.tv_master_field_gps)
    TextView tv_master_field_gps;
    //场地说明
    @Bind(R.id.tv_master_field_tools_text)
    TextView tv_master_field_tools_text;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_field_detail;
    }

    @Override
    protected String getCacheKey() {
        return "field_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        String fieldId = b.getString("id");
        if (!StringUtils.isEmpty(fieldId)) {
            MonkeyApi.getGarageDetail(Long.parseLong(fieldId), mDetailHandler);
        }
    }

    @Override
    public void initView(View view) {
        //  mAppointment.setOnClickListener(this);
        icon_master_field.setOnClickListener(this);
        btn_appointment_now.setOnClickListener(this);
        tv_master_field_gps.setOnClickListener(this);
        mLlContactPhone.setOnClickListener(this);
        mLlContactOnline.setOnClickListener(this);
    }

    @Override
    protected void executeOnLoadDataSuccess(Garage detail) {
        super.executeOnLoadDataSuccess(detail);
        String storeLogo = null == detail.getJsRecommond() ? detail.getStoreLogoId() : detail.getJsRecommond();
        getImgLoader().load(MonkeyApi.getPartImgUrl(storeLogo)).error(R.drawable.widget_dface).into(icon_master_field);
        //店铺名称
        tv_master_field_name.setText(detail.getStoreName());

        String fieldWork = detail.getStationNum();//店铺工位数
        if (!StringUtils.isEmpty(fieldWork)) {
            if (fieldWork.equals("1")) {
                fieldWork = "1-3个";
            }
            if (fieldWork.equals("2")) {
                fieldWork = "4-6个";
            }
            if (fieldWork.equals("3")) {
                fieldWork = "7个以上";
            }
            tv_master_field_work.setText(fieldWork);
        }
        String store_carmodel = detail.getBrandIcon();//专修品牌
        if (!StringUtils.isEmpty(store_carmodel)) {
            mbrandName.setVisibility(View.VISIBLE);
            tvbrandName.setVisibility(View.GONE);
            if (store_carmodel.contains(",")) {
                String[] storeCarmodels = store_carmodel.split(",");
                gridAdapter = new TechCarBrandGridAdapter(storeCarmodels, getContext());
                mbrandName.setAdapter(gridAdapter);
            } else {
                String[] storeCarmodels = new String[]{store_carmodel};
                gridAdapter = new TechCarBrandGridAdapter(storeCarmodels, getContext());
                mbrandName.setAdapter(gridAdapter);
            }
        } else {
            mbrandName.setVisibility(View.GONE);
            tvbrandName.setVisibility(View.VISIBLE);
        }

        //接单数
        tv_master_field_orders.setText(String.format(getString(R.string.str_tech_order_count),
                TextUtils.isEmpty(detail.getOrderCount()) ? "0" : detail.getOrderCount()));

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
        String fieldAddress = detail.getStoreAddress();//店铺地址
        if (!StringUtils.isEmpty(fieldAddress)) {
            tv_master_field_address.setText(fieldAddress.trim());
        } else {
            tv_master_field_address.setText("无法显示该工位位置");
            tv_master_field_gps.setText("无法导航");
        }
        //工位说明
        String fieldInfo = detail.getSiteDesc();
        tv_master_field_tools_text.setText(fieldInfo);
    }

    @Override
    protected java.lang.reflect.Type getType() {
        return new TypeToken<ResultBean<Garage>>() {
        }.getType();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //预览店铺图片大图
            case R.id.icon_master_field:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getStoreLogoId()));
                break;
            case R.id.tv_master_field_gps:
                UIHelper.showNavigateActivity(getActivity(), String.valueOf(tv_master_field_address.getText()));
                break;
            case R.id.ll_contact_online:
                AppContext.showToast("暂未开通在线沟通功能!");
                break;
            case R.id.ll_contact_phone:
                ShowContactTell.showContact(getContext(), mDetail.getStoreTelephone());
                break;
            case R.id.btn_appointment_now:
                if (!AppContext.getInstance().isLogin()) {
                    UIHelper.showLoginActivity(getActivity());
                } else {
                    Bundle b = getArguments();
                    String techId = b.getString("id");
                    b.putString("id", techId);
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.FIELD_APPOINTMENT, b);
                }
                break;
        }
    }


}
