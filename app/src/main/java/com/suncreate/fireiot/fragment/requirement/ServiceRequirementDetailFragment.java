package com.suncreate.fireiot.fragment.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Garage;
import com.suncreate.fireiot.bean.require.Requirement;
import com.suncreate.fireiot.bean.user.ServicesOrder;
import com.suncreate.fireiot.bean.user.Technician;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.ui.OSCPhotosActivity;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

import static com.suncreate.fireiot.fragment.order.ServicesOrderFragment.REQUEST_CODE_SERVICE_ORDER_DETAIL;

/**
 * 需求详情
 * <p>
 * desc
 */
public class ServiceRequirementDetailFragment extends BaseDetailFragment<Requirement> implements View.OnClickListener {

    public static final String TAG = "ServiceRequireDetail";

    @Bind(R.id.tv_order_state)
    TextView tv_order_state;
    @Bind(R.id.tv_taked_order)
    TextView tv_taked_order;
    @Bind(R.id.publish_time_box)
    LinearLayout publish_time_box;
    @Bind(R.id.publish_time)
    TextView publish_time;
    @Bind(R.id.ll_order_take_time_box)
    LinearLayout tv_order_take_time_box;
    @Bind(R.id.tv_order_take_time)
    TextView tv_order_take_time;
    @Bind(R.id.ll_who_take_box)
    LinearLayout ll_who_take_box;
    @Bind(R.id.tv_who_take)
    TextView tv_who_take;
    @Bind(R.id.car_brand_text)
    TextView tv_car_model;
    @Bind(R.id.tv_server_appoint_time)
    TextView tv_server_appoint_time;
    @Bind(R.id.tv_address)
    TextView tv_address;
    @Bind(R.id.tv_demand_desc)
    TextView tv_demand_desc;
    @Bind(R.id.tv_server_kinds)
    TextView tv_server_kinds;
    @Bind(R.id.imageView1)
    ImageView imageView1;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.imageView3)
    ImageView imageView3;
    @Bind(R.id.btn_grab_sure)
    Button btn_grab_sure;

    //需求编号
    String demandId;
    //需求订单编号
    String demandOrderId;
    String currentRole;
    String requirementId;
    Long userId;
    Long storeId;
    boolean flag = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_service_detail;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        if (AppContext.getInstance().isLogin()) {
//            User user = AppContext.getInstance().getLoginUserExt();
            //1会员中心 2技师中心 3商家中心 4代理商中心
//            currentRole = user.getCurrentRole();
//            userId = user.getId();
//            storeId = user.getUserStoreId();
            if (userId != 0) {
                if (("2").equals(currentRole)) {//技师店铺状态获取
                    MonkeyApi.getTechDetail(userId, tecHandler);
                }
                if (("3").equals(currentRole)) {//快修站店铺状态获取
                    MonkeyApi.getGarageDetail(storeId, garHandler);
                }
            }
            if ("1".equals(currentRole)) {
                btn_grab_sure.setVisibility(View.GONE);
            } else {
                btn_grab_sure.setVisibility(View.VISIBLE);
            }
            btn_grab_sure.setOnClickListener(this);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
            tv_taked_order.setOnClickListener(this);
        } else {
            UIHelper.showLoginActivity(getActivity());
        }
    }

    TextHttpResponseHandler tecHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("店铺未审核通过，无法抢单！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Garage> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<Garage>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (("2").equals(resultBean.getResult().getStoreStatus())) {
                        flag = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };
    TextHttpResponseHandler garHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("店铺未审核通过，无法抢单！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Technician> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<Technician>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (("2").equals(resultBean.getResult().getStoreStatus())) {
                        flag = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    @Override
    protected void sendRequestDataForNet() {
        Bundle b = getArguments();
        requirementId = b.getString("requirementId");
        if (!StringUtils.isEmpty(requirementId)) {
            MonkeyApi.getServiceRequirementDetail(requirementId, mDetailHandler);
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(final Requirement detail) {
        super.executeOnLoadDataSuccess(detail);
        //需求编号
        demandId = detail.getId();
        //需求订单编号
        demandOrderId = detail.getOrderId();
        //需求状态
        String orderState = detail.getOrderState();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //MAINTENANCE-保养修护 CREPAIRS-汽车维修 BDECORATION-美容装饰 IALTERATION-安装改装
        String serviceType = detail.getSerivceType();
        if (null != serviceType) {
            serviceType = serviceType.replace("MAINTENANCE", "保养修护").replace("CREPAIRS", "汽车维修").replace("BDECORATION", "美容装饰").replace("IALTERATION", "安装改装").replace(",", " ").trim();
            tv_server_kinds.setText(serviceType);
        }
        //发布时间
        String demand_addtime = detail.getDemandAddtime();
        if (!StringUtils.isEmpty(demand_addtime)) {
            long demand_addtimeL = Long.valueOf(demand_addtime);
            demand_addtime = sdf.format(new Date(demand_addtimeL));
            publish_time.setText(demand_addtime);
        }
        //接单时间
        String takers_date = detail.getTakersDate();
        //接单人
        String mTakersUserName = detail.getTakersUserName();
        if (!StringUtils.isEmpty(orderState)) {
            if (orderState.equals("1")) {
                tv_order_state.setText("已接单");
                if (StringUtils.isEmpty(demandOrderId)) {
                    tv_taked_order.setVisibility(View.GONE);
                }
                if (!StringUtils.isEmpty(takers_date)) {
                    long takers_dateL = Long.valueOf(takers_date);
                    takers_date = sdf.format(new Date(takers_dateL));
                    tv_order_take_time.setText(takers_date);
                } else {
                    tv_order_take_time.setText("没有记录");
                }
                if (!StringUtils.isEmpty(mTakersUserName)) {
                    tv_who_take.setText(mTakersUserName);
                } else {
                    tv_who_take.setText("没有记录");
                }
            } else if (orderState.equals("0")) {
                tv_order_state.setText("新发布");
                tv_order_take_time_box.setVisibility(View.GONE);
                tv_taked_order.setVisibility(View.GONE);
                ll_who_take_box.setVisibility(View.GONE);
            }
        }

        //预约时间
        String mAppointTime = detail.getAppointTimeStart();
        if (!StringUtils.isEmpty(mAppointTime)) {
            long mAppointTimeL = Long.valueOf(mAppointTime);
            mAppointTime = sdf.format(new Date(mAppointTimeL));
            tv_server_appoint_time.setText(mAppointTime);
        } else {
            tv_server_appoint_time.setText("没有记录");
        }

        //车品牌名称
        String carbrand_name = detail.getCarbrandName();
        if (!StringUtils.isEmpty(carbrand_name)) {
            tv_car_model.setText(carbrand_name);
        }
        //位置
        String demand_address = detail.getDemandAddress();
        if (!StringUtils.isEmpty(demand_address)) {
            tv_address.setText(demand_address);
        }
        //需求描述
        String demand_desc = detail.getDemandDesc();
        if (!StringUtils.isEmpty(demand_desc)) {
            tv_demand_desc.setText(demand_desc);
        }
        //需求图片1
        String demand_imgId1 = detail.getDemandImg1();
        if (!StringUtils.isEmpty(demand_imgId1)) {
            try {
                MonkeyApi.getPartImg(demand_imgId1, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            imageView1.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        imageView1.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageView1.setVisibility(View.GONE);
        }
        //需求图片2
        String demand_imgId2 = detail.getDemandImg2();
        if (!StringUtils.isEmpty(demand_imgId2)) {
            try {
                MonkeyApi.getPartImg(demand_imgId2, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            imageView2.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        imageView2.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageView2.setVisibility(View.GONE);
        }
        //需求图片3
        String demand_imgId3 = detail.getDemandImg3();
        if (!StringUtils.isEmpty(demand_imgId3)) {
            try {
                MonkeyApi.getPartImg(demand_imgId3, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            imageView3.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        imageView3.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageView3.setVisibility(View.GONE);
        }
    }

    @Override
    protected void executeOnLoadDataError() {
        super.executeOnLoadDataError();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //预览大图
            case R.id.imageView1:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getDemandImg1()));
                break;
            case R.id.imageView2:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getDemandImg2()));
                break;
            case R.id.imageView3:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getDemandImg3()));
                break;
            case R.id.btn_grab_sure:
                if (flag) {
                    if ("2".equals(currentRole) || "3".equals(currentRole)) {
                        DialogHelp.getConfirmDialog(getContext(), "是否确定抢单?", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MonkeyApi.grabOrder(demandId, new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        AppContext.showToast("抢单失败!");
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        final ResultBean<ServicesOrder> resultBean = AppContext.createGson().fromJson(responseString, getOrderType());
                                        if (resultBean != null && resultBean.isSuccess()) {
                                            resultBean.getResult().getId();
                                            DialogHelp.getMessageDialog(getContext(), "抢单成功！请进入订单处理环节！", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Bundle b = new Bundle();
                                                    b.putLong("id", resultBean.getResult().getId());
                                                    UIHelper.showSimpleBackForResult(ServiceRequirementDetailFragment.this, REQUEST_CODE_SERVICE_ORDER_DETAIL, SimpleBackPage.SERVICE_ORDER_DETAIL, b);
                                                }
                                            }).show();
                                        } else {
                                            onFailure(statusCode, headers, responseString, null);
                                        }
                                    }
                                });
                            }
                        }).show();
                    }
                } else {
                    DialogHelp.getMessageDialog(getContext(), "店铺暂未审核通过,不能进行抢单操作！", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
                break;
            case R.id.tv_taked_order:
                if (!StringUtils.isEmpty(demandOrderId)) {
                    Bundle b = new Bundle();
                    b.putLong("id", Long.parseLong(demandOrderId));
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.SERVICE_ORDER_DETAIL, b);
                }
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        intent.putExtra("requirementId", requirementId);
        getActivity().setResult(1, intent);
        getActivity().finish();
    }

    protected Type getOrderType() {
        return new TypeToken<ResultBean<ServicesOrder>>() {
        }.getType();
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Requirement>>() {
        }.getType();
    }

    @Override
    protected String getCacheKey() {
        return "service_require_" + mId;
    }

}