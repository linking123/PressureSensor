package com.suncreate.fireiot.fragment.order;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.bean.user.ServicesOrderDetail;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.util.Alipay.PayResult;
import com.suncreate.fireiot.util.DatePro;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.ShowContactTell;
import com.suncreate.fireiot.util.UIHelper;
import com.suncreate.fireiot.widget.CommonPay;
import com.suncreate.fireiot.widget.togglebutton.ToggleButton;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.suncreate.fireiot.fragment.order.ServicesOrderFragment.RESULT_CODE_SERVICE_ORDER_DETAIL;


/**
 * 订单详情页
 */
public class ServiceOrderDetailFragment extends BaseDetailFragment<ServicesOrderDetail> implements View.OnClickListener {
    public static final String TAG = "ServiceOrderDetailFragment";

    public static final int ORDER_VIEW = 0;//查看
    public static final int ORDER_ACTION = 1;//操作

    public static final int REQUEST_CODE_QUOTATION = 1;
    public static final int RESULT_CODE_QUOTATION = 2;
    public static final int REQUEST_CODE_PHOTO_PRE = 3;
    public static final int RESULT_CODE_PHOTO_PRE = 4;
    public static final int REQUEST_CODE_PHOTO_POST = 5;
    public static final int RESULT_CODE_PHOTO_POST = 6;
    public static final int REQUEST_CODE_SETTLE = 7;
    public static final int RESULT_CODE_SETTLE = 8;

    @Bind(R.id.tv_order_id)
    TextView mTvOrderId;
    @Bind(R.id.tv_service_order_stat)
    TextView tvServiceOrderStat;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.tv_service_type)
    TextView mTvServiceType;
    @Bind(R.id.tv_service_model)
    TextView mTvServiceModel;
    @Bind(R.id.tv_service_time)
    TextView mTvServiceTime;
    @Bind(R.id.tv_service_desc)
    TextView mTvServiceDesc;
    @Bind(R.id.tv_store_name)
    TextView mTvStoreName;
    @Bind(R.id.rl_order_quotation)
    RelativeLayout mRlOrderQuotation;
    @Bind(R.id.tv_order_quotation_tip)
    TextView mTvOrderQuotationTip;
    @Bind(R.id.rl_service_photo_pre)
    RelativeLayout mRlServicePhotoPre;
    @Bind(R.id.tv_service_photo_pre_tip)
    TextView mTvServicePhotoPreTip;
    @Bind(R.id.rl_service_photo_post)
    RelativeLayout mRlServicePhotoPost;
    @Bind(R.id.tv_service_photo_post_tip)
    TextView mTvServicePhotoPostTip;
    @Bind(R.id.rl_order_settle)
    RelativeLayout mRlOrderSettle;
    @Bind(R.id.tv_order_settle_tip)
    TextView mTvOrderSettleTip;
    @Bind(R.id.tv_order_goods_price)
    TextView mTvOrderGoodsPrice;
    @Bind(R.id.tv_order_accessory_price)
    TextView mTvOrderAccessoryPrice;
    @Bind(R.id.tv_order_field_price)
    TextView mTvOrderFieldPrice;
    @Bind(R.id.tv_order_work_price)
    TextView mTvOrderWorkPrice;
    @Bind(R.id.tv_order_amount_price)
    TextView mTvOrderAmountPrice;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.tv_flag_open_close)
    TextView tvFlagOpenClose;
    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;
    @Bind(R.id.ll_contact_online)
    LinearLayout mLlContactOnline;
    @Bind(R.id.btn_settle_account)
    Button mBtnSettleAccount;
    @Bind(R.id.layout8)
    RelativeLayout layout8;
    @Bind(R.id.layout3)
    RelativeLayout layout3;
    @Bind(R.id.layout9)
    RelativeLayout layout9;
    @Bind(R.id.user_default)
    ToggleButton tbUserDefault;
    int mCurrentRole;
    private Integer mOrderStatus;
    private Integer mPhotoStatus;
    private String mContactPhone;
    private Double mTotalPrice;
    private Double mUserBalance;
    String paylist;
    String strServiceType = "";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void initView(View view) {
        mRlOrderQuotation.setOnClickListener(this);
        mRlServicePhotoPre.setOnClickListener(this);
        mRlServicePhotoPost.setOnClickListener(this);
        mRlOrderSettle.setOnClickListener(this);
        mLlContactPhone.setOnClickListener(this);
        mLlContactOnline.setOnClickListener(this);
        mBtnSettleAccount.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mCurrentRole = AppContext.getInstance().getCurrentRole();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service_order_detail;
    }

    @Override
    protected String getCacheKey() {
        return "service_order_detail_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getServiceOrderDetail(String.valueOf(mDetailId), mDetailHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<ServicesOrderDetail>>() {
        }.getType();
    }

    @Override
    protected void executeOnLoadDataSuccess(ServicesOrderDetail detail) {
        super.executeOnLoadDataSuccess(detail);
//        detail.setOrderStatus(20);
//        detail.setPhotoStatus(1);

        //支付参数构建
        List<Pay> addPayList = new ArrayList<Pay>();
        Pay pay = new Pay();
        mTotalPrice = Double.valueOf(detail.getOrderTotalprice());
        pay.setConsumeAmount(String.valueOf(detail.getOrderTotalprice()));
        pay.setOrderId(detail.getId().toString());
        pay.setConsumeMode(MonkeyApi.addPayRecord_consumeMode_service);//服务订单
        addPayList.add(pay);
        paylist = JSON.toJSONString(addPayList);
        mOrderStatus = detail.getOrderStatus();
        mPhotoStatus = detail.getPhotoStatus();
        int mCrtRole = AppContext.getInstance().getCurrentRole();

        if (1 == mCrtRole) {//车主隐藏说明文字
            mContactPhone = detail.getStoreMobile();
            layout8.setVisibility(View.GONE);
        } else {
            mContactPhone = detail.getUserMobile();
        }
        tvServiceOrderStat.setText(Constants.convertServiceOrderStatus(mOrderStatus));
        if (null == mOrderStatus) {
            tvServiceOrderStat.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_disabled));
            mBtnSettleAccount.setText("未 知");
            mBtnSettleAccount.setEnabled(false);
            mBtnSettleAccount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btn_disabled));
        } else {
            switch (mOrderStatus) {
                case 10://待报价
                    if (mCrtRole == 1) {
                        mRlOrderQuotation.setEnabled(false);
                        mTvOrderQuotationTip.setText("等待技师报价");
                    } else {
                        mRlOrderQuotation.setEnabled(true);
                        mTvOrderQuotationTip.setText("填写确认报价单");
                    }
                    mRlServicePhotoPre.setEnabled(false);
                    mTvServicePhotoPreTip.setText("服务尚未开始");
                    mRlServicePhotoPost.setEnabled(false);
                    mTvServicePhotoPostTip.setText("服务尚未结束");
                    mRlOrderSettle.setEnabled(false);
                    mTvOrderSettleTip.setText("尚未结算");
                    mBtnSettleAccount.setEnabled(false);
                    mBtnSettleAccount.setText("待报价");
                    break;
                case 20://待确认:
                    mRlServicePhotoPre.setEnabled(false);
                    mTvServicePhotoPreTip.setText("服务尚未开始");
                    mRlServicePhotoPost.setEnabled(false);
                    mTvServicePhotoPostTip.setText("服务尚未结束");
                    mRlOrderSettle.setEnabled(false);
                    mTvOrderSettleTip.setText("尚未结算");
                    mRlOrderQuotation.setEnabled(true);
                    if (mCrtRole == 1) {
                        if (mPhotoStatus == 1) {
                            mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 技师已报价, 点击查看");
                            mBtnSettleAccount.setEnabled(true);
                            mBtnSettleAccount.setText("确认报价");
                        } else {
                            mBtnSettleAccount.setEnabled(false);
                            mBtnSettleAccount.setText("确认报价");
                        }
                    } else {
                        mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 已报价，等待车主确认");
                        mBtnSettleAccount.setEnabled(false);
                        mBtnSettleAccount.setText("待确认");
                    }
                    break;
                case 30://待结算/服务中
                    mRlOrderQuotation.setEnabled(true);
//                    mTvOrderQuotationTip.setText("查看");
                    if (mPhotoStatus == 1) {//服务前照片未上传
                        mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        if (mCrtRole == 1) {
                            mRlServicePhotoPre.setEnabled(false);
                            mTvServicePhotoPreTip.setText("尚未开始拍照");
                        } else {
                            mRlServicePhotoPre.setEnabled(true);
                            mTvServicePhotoPreTip.setText("服务前请务必先上传照片");
                        }
                        mRlServicePhotoPost.setEnabled(false);
                        mTvServicePhotoPostTip.setText("服务尚未结束");
                        mRlOrderSettle.setEnabled(false);
                        mTvOrderSettleTip.setText("尚未结算");
                        mBtnSettleAccount.setEnabled(false);
                        mBtnSettleAccount.setText("服务中");
                    } else if (mPhotoStatus == 2) {//服务前照片已上传，服务后照片未上传
                        mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        mRlServicePhotoPre.setEnabled(true);
                        mTvServicePhotoPreTip.setText(DatePro.formatDay(detail.getOrderBeforeTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        if (mCrtRole == 1) {
                            mRlServicePhotoPost.setEnabled(false);
                            mTvServicePhotoPostTip.setText("尚未开始拍照");
                        } else {
                            mRlServicePhotoPost.setEnabled(true);
                            mTvServicePhotoPostTip.setText("上传服务后照片");
                        }
                        mRlOrderSettle.setEnabled(false);
                        mTvOrderSettleTip.setText("尚未结算");
                        mBtnSettleAccount.setEnabled(false);
                        mBtnSettleAccount.setText("服务中");
                    } else if (mPhotoStatus == 3) {//服务前后照片已上传，结算单未上传
                        mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        mRlServicePhotoPre.setEnabled(true);
                        mTvServicePhotoPreTip.setText(DatePro.formatDay(detail.getOrderBeforeTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        mRlServicePhotoPost.setEnabled(true);
                        mTvServicePhotoPostTip.setText(DatePro.formatDay(detail.getOrderAfterTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        if (mCrtRole == 1) {
                            mRlOrderSettle.setEnabled(false);
                            mTvOrderSettleTip.setText("尚未上传结算单");
                        } else {
                            mRlOrderSettle.setEnabled(true);
                            mTvOrderSettleTip.setText("填写确认结算单");
                        }
                        mBtnSettleAccount.setEnabled(false);
                        mBtnSettleAccount.setText("服务中");
                    } else if (mPhotoStatus == 4) {//结算单已上传
                        mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        mRlServicePhotoPre.setEnabled(true);
                        mTvServicePhotoPreTip.setText(DatePro.formatDay(detail.getOrderBeforeTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        mRlServicePhotoPost.setEnabled(true);
                        mTvServicePhotoPostTip.setText(DatePro.formatDay(detail.getOrderAfterTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        mRlOrderSettle.setEnabled(true);
                        mTvOrderSettleTip.setText(DatePro.formatDay(detail.getOrderSettlementTime(), "yyyy-MM-dd HH:mm") + " 查看");
                        if (mCrtRole == 1) {
                            mBtnSettleAccount.setText("结 算");
                            mBtnSettleAccount.setEnabled(true);
                        } else {
                            mBtnSettleAccount.setText("等待结算");
                            mBtnSettleAccount.setEnabled(false);
                        }
                    }
                    break;
                case 40://待评价
                    mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mRlServicePhotoPre.setEnabled(true);
                    mTvServicePhotoPreTip.setText(DatePro.formatDay(detail.getOrderBeforeTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mRlServicePhotoPost.setEnabled(true);
                    mTvServicePhotoPostTip.setText(DatePro.formatDay(detail.getOrderAfterTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mRlOrderSettle.setEnabled(true);
                    mTvOrderSettleTip.setText(DatePro.formatDay(detail.getOrderSettlementTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    if (mCrtRole == 1) {
                        mBtnSettleAccount.setText("去评价");
                        mBtnSettleAccount.setEnabled(true);
                    } else {
                        mBtnSettleAccount.setText("待评价");
                        mBtnSettleAccount.setEnabled(false);
                    }
                    break;
                case 50://已完成:
                    mTvOrderQuotationTip.setText(DatePro.formatDay(detail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mRlServicePhotoPre.setEnabled(true);
                    mTvServicePhotoPreTip.setText(DatePro.formatDay(detail.getOrderBeforeTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mRlServicePhotoPost.setEnabled(true);
                    mTvServicePhotoPostTip.setText(DatePro.formatDay(detail.getOrderAfterTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mRlOrderSettle.setEnabled(true);
                    mTvOrderSettleTip.setText(DatePro.formatDay(detail.getOrderSettlementTime(), "yyyy-MM-dd HH:mm") + " 查看");
                    mBtnSettleAccount.setText("已完成");
                    mBtnSettleAccount.setEnabled(false);
                    mBtnSettleAccount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.day_colorPrimary));
                    break;
                case 0://已取消
                    mBtnSettleAccount.setText("已取消");
                    mBtnSettleAccount.setEnabled(false);
                    mTvOrderQuotationTip.setText("");
                    mRlOrderQuotation.setEnabled(false);
                    mTvServicePhotoPreTip.setText("");
                    mRlServicePhotoPre.setEnabled(false);
                    mTvServicePhotoPostTip.setText("");
                    mRlServicePhotoPost.setEnabled(false);
                    mTvOrderSettleTip.setText("");
                    mRlOrderSettle.setEnabled(false);
                    mBtnSettleAccount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btn_disabled));
                    break;
                default://未知
                    mBtnSettleAccount.setText("未 知");
                    mBtnSettleAccount.setEnabled(false);
                    mBtnSettleAccount.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btn_disabled));
                    break;
            }
            if (mOrderStatus == 50) {
                tvServiceOrderStat.setTextColor(ContextCompat.getColor(getContext(), R.color.green_a400));
            } else if (mOrderStatus == 0) {
                tvServiceOrderStat.setTextColor(ContextCompat.getColor(getContext(), R.color.btn_disabled));
            } else {
                tvServiceOrderStat.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_700));
            }
        }


        //TODO:美容装饰不需要报价等。
        if (!TextUtils.isEmpty(detail.getStoreSerivceType())) {
            strServiceType = "";
            String[] arrServiceType = detail.getStoreSerivceType().split(",");
            for (String st : arrServiceType) {
                st = Constants.converServiceType(st);
                strServiceType += "," + st;
            }
            strServiceType = strServiceType.substring(1);
            mTvServiceType.setText(String.format(getString(R.string.service_order_type), strServiceType));
            if (strServiceType.equals("美容装饰")) {//如果是美容装饰则由开关控制是否需要报价拍照，默认开关关闭
                //如果商家未开启报价拍照功能，车主端隐藏，默认情况下关闭状态
                if (null == mDetail.getOrderQuotationTime()) {//通过商家报价时间null获得按钮状态为关闭
                    tbUserDefault.setToggleOff();
                    tvFlagOpenClose.setText("关闭");
                    layout9.setVisibility(View.GONE);
                    if (mCrtRole == 1 && null == mDetail.getOrderSettlementTime()) {//如果车主端，并且服务商未填写结算单
                        mRlOrderSettle.setEnabled(false);
                    } else {
                        if (null != mDetail.getOrderSettlementTime()) {//如果服务商已经填写过结算单，则不可操作
                            tbUserDefault.setEnabled(false);
                        }
                        mRlOrderSettle.setEnabled(true);
                    }
                    if (mOrderStatus == 10) {
                        mBtnSettleAccount.setText("待结算");
                    }
                } else {//如果已经报价，说明商家开启，开启并报价后不可关闭
                    tbUserDefault.setToggleOn();
                    tbUserDefault.setEnabled(false);
                    tvFlagOpenClose.setText("开启");
                    layout9.setVisibility(View.VISIBLE);
                }
                if (mCrtRole == 1) {//如果是车主，则隐藏控制开关
                    layout3.setVisibility(View.GONE);
                }

            } else {//如果不是美容装饰，隐藏控制按钮
                layout3.setVisibility(View.GONE);
            }
        }
        tbUserDefault.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    layout9.setVisibility(View.VISIBLE);
                    tvFlagOpenClose.setText("开启");
                    mBtnSettleAccount.setText("待报价");
                    mRlOrderSettle.setEnabled(false);
                } else {
                    layout9.setVisibility(View.GONE);
                    tvFlagOpenClose.setText("关闭");
                    mBtnSettleAccount.setText("待结算");
                    mRlOrderSettle.setEnabled(true);
                }
            }
        });

        mTvOrderId.setText(String.format(getString(R.string.service_order_detail_id), detail.getOrderId()));
        mTvUserName.setText(detail.getUserMobile());
        mTvServiceModel.setText(String.format(getString(R.string.service_order_model), TextUtils.isEmpty(detail.getOrderCarType()) ? "" : detail.getOrderCarType()));
        mTvServiceTime.setText(String.format(getString(R.string.service_order_time),
                DatePro.formatDay(detail.getOrderAppointTimeStart(), "yyyy-MM-dd HH:mm")));
        mTvServiceDesc.setText(String.format(getString(R.string.service_order_desc), TextUtils.isEmpty(detail.getOrderMsg()) ? "" : detail.getOrderMsg()));
        mTvStoreName.setText(detail.getStoreName());
        mTvOrderGoodsPrice.setText(String.format(getString(R.string.format_price_double), detail.getOrderPartsAmount()));
        mTvOrderFieldPrice.setText(String.format(getString(R.string.format_price_double), detail.getPlaceAmount()));
        mTvOrderWorkPrice.setText(String.format(getString(R.string.format_price_double), detail.getOrderWorkingAmount()));
        mTvOrderAccessoryPrice.setText(String.format(getString(R.string.format_price_double), detail.getAccessoriesAmount()));
        double orderAmountPrice = detail.getOrderPartsAmount() + detail.getPlaceAmount() + detail.getOrderWorkingAmount() + detail.getAccessoriesAmount();
        mTvOrderAmountPrice.setText(String.format(getString(R.string.format_price_double), orderAmountPrice));
        tvOrderTime.setText(String.format(getContext().getResources().getString(R.string.service_order_appointment),
                DatePro.formatDay(detail.getOrderAppointTimeStart(), "yyyy-MM-dd HH:mm")));
    }

    public void onClick(View v) {
        Bundle b = new Bundle();
        b.putSerializable("serviceOrder", mDetail);
        switch (v.getId()) {
            case R.id.ll_contact_online:
                AppContext.showToast("暂未开通在线沟通功能!");
                break;
            //联系对方
            case R.id.ll_contact_phone:
                ShowContactTell.showContact(getContext(), mContactPhone);
                break;
            //报价单
            case R.id.rl_order_quotation:
                if (1 == mCurrentRole) {
                    b.putInt("action", ORDER_VIEW);//查看报价单
                } else {
                    //车主未确认前，报价单可以修改,确认后不可以
                    if (mOrderStatus > 20) {
                        b.putInt("action", ORDER_VIEW);//查看报价单
                    } else {
                        b.putInt("action", ORDER_ACTION);//技师报价
                    }
                }
                UIHelper.showSimpleBackForResult(this, REQUEST_CODE_QUOTATION, SimpleBackPage.SERVICE_ORDER_QUOTATION_ACTION, b);
                break;
            //服务前照片
            case R.id.rl_service_photo_pre:
                //未上传服务前照片且是技师，才能上传
                if (1 == mCurrentRole) {
                    b.putInt("action", ORDER_VIEW);//查看报价单
                } else {
                    if (mPhotoStatus == 1) {
                        b.putInt("action", ORDER_ACTION);//技师报价
                    } else {
                        b.putInt("action", ORDER_VIEW);//查看报价单
                    }
                }
                UIHelper.showSimpleBackForResult(this, REQUEST_CODE_PHOTO_PRE, SimpleBackPage.SERVICE_ORDER_PHOTO_BEFORE_ACTION, b);
                break;
            //服务后照片
            case R.id.rl_service_photo_post:
                //未上传服务后照片且是技师，才能上传
                if (1 == mCurrentRole) {
                    b.putInt("action", ORDER_VIEW);//查看报价单
                } else {
                    if (mPhotoStatus == 2) {
                        b.putInt("action", ORDER_ACTION);//技师报价
                    } else {
                        b.putInt("action", ORDER_VIEW);//查看报价单
                    }
                }
                UIHelper.showSimpleBackForResult(this, REQUEST_CODE_PHOTO_POST, SimpleBackPage.SERVICE_ORDER_PHOTO_AFTER_ACTION, b);
                break;
            //结算单
            case R.id.rl_order_settle:
                if (1 == mCurrentRole) {
                    b.putInt("action", ORDER_VIEW);//查看报价单
                    //如果是美容装饰类，并且关闭报价拍照功能，则直接进入结算环节
                    if (strServiceType.equals("美容装饰")) {//如果是美容装饰
                        if (null == mDetail.getOrderQuotationTime()) {//且按钮状态为关闭
                            b.putString("flag", "close");
                        }
                    }
                } else {
                    //车主未结算前，结算单都可以修改,结算后不可修改
                    if (mOrderStatus < 40) {
                        b.putInt("action", ORDER_ACTION);//技师报价
                    } else {
                        b.putInt("action", ORDER_VIEW);//查看报价单
                    }
                    //如果是美容装饰类，并且关闭报价拍照功能，则直接进入结算环节
                    if (strServiceType.equals("美容装饰")) {//如果是美容装饰
                        if (null == mDetail.getOrderQuotationTime()) {//且按钮状态为关闭
                            b.putString("flag", "close");
                        }
                    }
                }
                UIHelper.showSimpleBackForResult(this, REQUEST_CODE_SETTLE, SimpleBackPage.SERVICE_ORDER_SETTLE_ACTION, b);
                break;
            //操作按钮
            case R.id.btn_settle_account:
                if (mOrderStatus == 20) {
                    DialogHelp.getConfirmDialog(getContext(), "确认报价", "请仔细核对报价单,是否确认报价？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认报价,更新订单状态
                            MonkeyApi.updateServiceOrder(String.valueOf(mDetail.getId()), "30", updateStatusHandler);
                        }
                    }).show();
                } else if (mOrderStatus == 30) {
                    DialogHelp.getConfirmDialog(getContext(), "确认结算", "请仔细核对结算单,是否确认结算？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认结算,先获取用户账户信息，跳转支付，服务端更新订单状态
                            showWaitDialog();
                            MonkeyApi.getMyInformation(mUserInfoHandler);
                        }
                    }).show();
                } else if (mOrderStatus == 40) {
                    // TODO: 12/26/16 评价订单
                    //TODO:去往评价页面
                    if (mCurrentRole == 1) {
                        AppContext.showToast("暂未开通评价功能!");
                    }
                }
                break;
            default:
                break;
        }
    }

    protected TextHttpResponseHandler updateStatusHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作状态失败!");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("操作成功!");
                    Intent intent = new Intent();
                    intent.putExtra("updateOrderStatus", 30);
                    getActivity().setResult(RESULT_CODE_SERVICE_ORDER_DETAIL, intent);
                    getActivity().finish();
                } else {
                    onFailure(statusCode, headers, responseString, null);
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    private TextHttpResponseHandler mUserInfoHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("获取用户信息失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    User user = resultBean.getResult();
                    mUserBalance = user.getUserBalance() == null ? 0L : user.getUserBalance();
                    AppContext.getInstance().setProperty("user.userBalance", mUserBalance.toString());
                    CommonPay.showCommonPay(getActivity(), mTotalPrice, mUserBalance, aliPay, balancePay, wechatPay);
                } else {
                    onFailure(statusCode, headers, responseString, null);
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
    private View.OnClickListener wechatPay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AppContext.showToast("暂未开通微信支付!");
        }
    };
    private View.OnClickListener aliPay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //支付宝支付
            showWaitDialog(R.string.msg_paying);
            doAlipay();
        }
    };

    private View.OnClickListener balancePay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserBalance < mTotalPrice) {
                AppContext.showToastShort("余额不足，请选择其他方式支付");
            } else {
                DialogHelp.getConfirmDialog(getContext(), "是否确定支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //新增余额支付
                        showWaitDialog(R.string.msg_paying);
                        MonkeyApi.addPayRecord(paylist, mPayHandler);
                    }
                }).show();
            }
        }
    };

    private TextHttpResponseHandler mPayHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("余额付款失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    //扣除客户端余额
                    double remainPrice = mUserBalance - mTotalPrice;
                    BigDecimal bd = new BigDecimal(remainPrice);
                    remainPrice = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    AppContext.getInstance().setProperty("user.userBalance", String.valueOf(remainPrice));
                    AppContext.showToast("操作成功!");
                    Intent intent = new Intent();
                    intent.putExtra("updateOrderStatus", 40);
                    getActivity().setResult(RESULT_CODE_SERVICE_ORDER_DETAIL, intent);
                    getActivity().finish();
                } else {
                    onFailure(statusCode, headers, responseString, null);
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

    public void doAlipay() {
        //订单名称
        String subject = "服务订单";
        //商品描述
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        StringBuffer sb = new StringBuffer(256);
//        sb.append(AppContext.getInstance().getLoginUserExt().getUserName()).append("在").append(sdf2.format(new Date()))
//                .append("向服务订单").append(mDetail.getOrderId()).append("支付").append(mTotalPrice).append("元");
//
//        //1.获取签名信息
//        MonkeyApi.getAlipaySignInfo(mDetail.getOrderId(), subject, mTotalPrice.toString(), sb.toString(),
//                new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        AppContext.showToast("支付宝付款失败！");
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                        try {
//                            JSONObject jsonobject = (JSONObject) JSONObject.parse(responseString);
//                            final String orderInfo = jsonobject.getJSONObject("result").get("orderInfo").toString();
//                            Runnable payRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                    PayTask alipay = new PayTask(getActivity());
//                                    Map<String, String> result = alipay.payV2(orderInfo, true);
//                                    Log.i("msp", result.toString());
//                                    Message msg = new Message();
//                                    msg.what = 1;
//                                    msg.obj = result;
//                                    mHandler.sendMessage(msg);
//                                }
//                            };
//                            Thread payThread = new Thread(payRunnable);
//                            payThread.start();
//                            //增加充值记录end
//                        } catch (Exception e) {
//                            onFailure(statusCode, headers, responseString, e);
//                        }
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        hideWaitDialog();
//                    }
//                });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case 1: {
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            AppContext.showToast("支付成功");
                            Intent intent = new Intent();
                            intent.putExtra("updateOrderStatus", 40);
                            getActivity().setResult(RESULT_CODE_SERVICE_ORDER_DETAIL, intent);
                            getActivity().finish();

                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            AppContext.showToast("支付宝支付失败");
                        }
                        break;
                    }
                    default:
                        break;
                }
            } catch (Exception e) {
                AppContext.showToast("支付宝支付失败");
            }
        }
    };

    /**
     * 是否已上传服务照片
     *
     * @param detail
     * @return
     */
    private boolean isNotUploadServicePhoto(ServicesOrderDetail detail) {
        return (detail.getBeforePhoto1() == null
                || detail.getBeforePhoto2() == null
                || detail.getBeforePhoto3() == null
                || detail.getAfterPhoto1() == null
                || detail.getAfterPhoto2() == null
                || detail.getAfterPhoto2() == null);
    }

    /**
     * 是否已上传服务照片
     *
     * @param detail
     * @return
     */
    private boolean isNotUploadServicePhotoPre(ServicesOrderDetail detail) {
        return (detail.getBeforePhoto1() == null
                || detail.getBeforePhoto2() == null
                || detail.getBeforePhoto3() == null);
    }

    /**
     * 是否已上传服务照片
     *
     * @param detail
     * @return
     */
    private boolean isNotUploadServicePhotoPost(ServicesOrderDetail detail) {
        return (detail.getAfterPhoto1() == null
                || detail.getAfterPhoto2() == null
                || detail.getAfterPhoto2() == null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE_QUOTATION && resultCode == RESULT_CODE_QUOTATION) {
        showWaitDialog();
        sendRequestDataForNet();
//        }
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
}
