package com.suncreate.fireiot.fragment.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseActivity;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.QuoteSettle;
import com.suncreate.fireiot.bean.user.QuoteSettleProject;
import com.suncreate.fireiot.bean.user.ServicesOrderDetail;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.meidia.TweetPicturesPreviewer;
import com.suncreate.fireiot.ui.OSCPhotosActivity;
import com.suncreate.fireiot.util.DatePro;
import com.suncreate.fireiot.util.DialogHelp;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.text.InputType.TYPE_NULL;
import static com.suncreate.fireiot.R.id.btn_submit_report;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.ORDER_ACTION;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.ORDER_VIEW;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.RESULT_CODE_QUOTATION;
import static java.util.regex.Pattern.compile;
import static org.kymjs.kjframe.utils.ImageUtils.getSmallImageFile;

/**
 * 技师填写报价单
 */
public class ServiceOrderQuotationFragment extends BaseFragment implements TextWatcher {

    @Bind(R.id.tv_order_id)
    TextView mTvOrderId;
    @Bind(R.id.tv_take_photo)
    TextView mTvTakePhoto;
    @Bind(R.id.iv_report_photo)
    ImageView mIvReportPhoto;
    @Bind(R.id.recycler_images)
    TweetPicturesPreviewer mLayImages;
    @Bind(R.id.et_parts_amount)
    EditText mEtPartsAmount;
    @Bind(R.id.et_working_amount)
    EditText mEtWorkingAmount;
    @Bind(R.id.et_place_amount)
    EditText mEtPlaceAmount;
    @Bind(R.id.et_accessory_amount)
    EditText mEtAccessoryAmount;
    @Bind(R.id.tv_total_amount)
    TextView mTvTotalAmount;
    @Bind(btn_submit_report)
    Button mBtnSubmitReport;
    @Bind(R.id.tv_title_photo)
    TextView mTvTitlePhoto;
    @Bind(R.id.tv_title_amount)
    TextView mTvTitleAmount;
    @Bind(R.id.tv_upload_tip)
    TextView mTvUploadTip;

    int mCurrentRole;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_title_time)
    LinearLayout llTitleTime;
    private ServicesOrderDetail mDetail;
    private QuoteSettle mQuoteSettle;
    private File mReportImg;
    private double mTotalAmount;
    private double[] mAmountArray = {0, 0, 0, 0};
    private int mAction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (null != bundle) {
            mAction = bundle.getInt("action", 0);
            if (mAction == ORDER_ACTION) {
                ((BaseActivity) getActivity()).setActionBarTitle(R.string.service_order_quotation_action);
            } else {
                ((BaseActivity) getActivity()).setActionBarTitle(R.string.service_order_quotation_view);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service_order_quotation;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mEtPartsAmount.addTextChangedListener(this);
        mEtWorkingAmount.addTextChangedListener(this);
        mEtPlaceAmount.addTextChangedListener(this);
        mEtAccessoryAmount.addTextChangedListener(this);
        mTvTitlePhoto.setText(getString(R.string.str_title_photo_quote));
        mTvTitleAmount.setText(getString(R.string.str_title_amount_quote));
    }

    @Override
    protected void initData() {
        super.initData();
        mCurrentRole = AppContext.getInstance().getCurrentRole();
        mDetail = getBundleSerializable("serviceOrder");
        if(null==mDetail.getOrderQuotationTime()){
            llTitleTime.setVisibility(View.GONE);
        }else {
            tvTime.setText("填写报价单时间：" + DatePro.formatDay(mDetail.getOrderQuotationTime(), "yyyy-MM-dd HH:mm"));
        }
        if (mDetail != null) {
            mTvOrderId.setText(mDetail.getOrderId());
            if (mAction == ORDER_VIEW) {
                showWaitDialog();
                MonkeyApi.repairReport(String.valueOf(mDetail.getId()), Constants.QUOTE_TYPE.QUOTE, mDetailHandler);
                mEtPartsAmount.setEnabled(false);
                mEtPartsAmount.setInputType(TYPE_NULL);
                mEtWorkingAmount.setEnabled(false);
                mEtWorkingAmount.setInputType(TYPE_NULL);
                mEtPlaceAmount.setEnabled(false);
                mEtPlaceAmount.setInputType(TYPE_NULL);
                mEtAccessoryAmount.setEnabled(false);
                mEtAccessoryAmount.setInputType(TYPE_NULL);
                mLayImages.setVisibility(View.GONE);
                mTvTakePhoto.setVisibility(View.GONE);
                mIvReportPhoto.setVisibility(View.VISIBLE);
                if (mCurrentRole == 1) {
                    if (mDetail.getOrderStatus() == 20) {
                        mBtnSubmitReport.setText("确认报价");
                    } else {
                        mBtnSubmitReport.setVisibility(View.GONE);
                    }
                } else {
                    mBtnSubmitReport.setVisibility(View.GONE);
                }
            } else {
//                llTitleQuotationTime.setVisibility(View.GONE);
                if (mDetail.getPhotoStatus() > 0) {//已上传报价单,加载照片
                    MonkeyApi.repairReport(String.valueOf(mDetail.getId()), Constants.QUOTE_TYPE.QUOTE, mDetailHandler);
                    mTvUploadTip.setVisibility(View.VISIBLE);
                }
                if (mDetail.getOrderStatus() > 20) {//确认后，隐藏报价单提交按钮
                    mBtnSubmitReport.setVisibility(View.GONE);
                }
//                mIvReportPhoto.setVisibility(View.GONE);//TODO：上传控件无法加载已上传的图片
                mBtnSubmitReport.setText(getString(R.string.btn_submit));
            }
        } else {
            AppContext.showToast("加载失败");
        }
    }

    @OnClick({R.id.iv_report_photo, R.id.tv_take_photo, R.id.btn_submit_report})
    void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_report_photo:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto4()));//TODO:报价单照片
                break;
            case R.id.tv_take_photo:
                mLayImages.onLoadMoreClick1Img();
                break;
            case R.id.btn_submit_report:
                if (mCurrentRole == 1) {//车主确认报价
                    DialogHelp.getConfirmDialog(getContext(), "确认报价", "请仔细核对报价单,是否确认报价？", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //确认报价,更新订单状态
                            MonkeyApi.updateServiceOrder(String.valueOf(mDetail.getId()), "30", updateStatusHandler);
                        }
                    }).show();
                } else {//技师提交报价
                    try {
                        String orderId = mTvOrderId.getText().toString();
                        String partsAmount = mEtPartsAmount.getText().toString();
                        String workingAmount = mEtWorkingAmount.getText().toString();
                        String placeAmount = mEtPlaceAmount.getText().toString();
                        String accessoryAmount = mEtAccessoryAmount.getText().toString();
                        Pattern pattern = compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
                        if (TextUtils.isEmpty(orderId)) {
                            AppContext.showToast("订单信息有误");
                            return;
                        }
                        if (!pattern.matcher(partsAmount).matches()) {
                            AppContext.showToast("配件费用输入有误！");
                            return;
                        }
                        if (!pattern.matcher(workingAmount).matches()) {
                            AppContext.showToast("工时费用输入有误！");
                            return;
                        }
                        if (!pattern.matcher(placeAmount).matches()) {
                            AppContext.showToast("场地费用输入有误！");
                            return;
                        }
                        if (!pattern.matcher(accessoryAmount).matches()) {
                            AppContext.showToast("辅料费用输入有误！");
                            return;
                        }
                        //图片上传
                        int imgCount = mLayImages.getChildCount();
                        if (imgCount == 0) {
                            AppContext.showToast("请上传一张图片！");
                            return;
                        } else if (imgCount > 1) {
                            AppContext.showToast("最多上传一张图片！");
                            return;
                        }
                        String[] imgPaths = mLayImages.getPaths();
                        int len = imgPaths.length;
                        if (len == 1) {
//                            mReportImg = new File(imgPaths[0]);
                            mReportImg = getSmallImageFile(getContext(), imgPaths[0], 300, 200, true);
                        }
                        //TODO:一期技师报价结算只添加默认项目
                        QuoteSettle qs = new QuoteSettle();
                        List<QuoteSettleProject> list = new ArrayList<>();
                        qs.setId(String.valueOf(mDetail.getId()));
                        qs.setType(Constants.QUOTE_TYPE.QUOTE);
                        qs.setPlaceAmount(placeAmount);
                        qs.setAccessoriesAmount(accessoryAmount);
                        qs.setGoodsAmount(partsAmount);
                        qs.setWorkingAmount(workingAmount);
                        qs.setTotalAmount(String.valueOf(mTotalAmount));
                        qs.setItems(list);
                        showWaitDialog(R.string.progress_submit);
                        MonkeyApi.techOffer(JSON.toJSONString(qs), mReportImg, mSubmitHandler);
                    } catch (FileNotFoundException e) {
                        AppContext.showToast("图像不存在，上传失败");
                    }
                }
                break;
        }
    }

    protected TextHttpResponseHandler mDetailHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("加载失败!");
            mEtPartsAmount.setText("0.00");
            mEtWorkingAmount.setText("0.00");
            mEtPlaceAmount.setText("0.00");
            mEtAccessoryAmount.setText("0.00");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<QuoteSettle> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<QuoteSettle>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    mQuoteSettle = resultBean.getResult();
                    mEtPartsAmount.setText(TextUtils.isEmpty(mQuoteSettle.getGoodsAmount()) ? "0.00" : mQuoteSettle.getGoodsAmount());
                    mEtWorkingAmount.setText(TextUtils.isEmpty(mQuoteSettle.getWorkingAmount()) ? "0.00" : mQuoteSettle.getWorkingAmount());
                    mEtPlaceAmount.setText(TextUtils.isEmpty(mQuoteSettle.getPlaceAmount()) ? "0.00" : mQuoteSettle.getPlaceAmount());
                    mEtAccessoryAmount.setText(TextUtils.isEmpty(mQuoteSettle.getAccessoriesAmount()) ? "0.00" : mQuoteSettle.getAccessoriesAmount());
                    setImageFromNet(R.id.iv_report_photo, MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto4()), R.drawable.logo);
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

    protected TextHttpResponseHandler updateStatusHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作失败!");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("操作成功!");
                    Intent intent = new Intent();
                    getActivity().setResult(RESULT_CODE_QUOTATION, intent);
                    getActivity().finish();
                } else {
                    onFailure(statusCode, headers, responseString, null);
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    private TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("提交报价单失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("提交报价单成功");
                    Intent intent = new Intent();
                    getActivity().setResult(RESULT_CODE_QUOTATION, intent);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTotalAmount = 0;
        mAmountArray[0] = TextUtils.isEmpty(mEtPartsAmount.getText()) ? 0.00 : Double.parseDouble(mEtPartsAmount.getText().toString());
        mAmountArray[1] = TextUtils.isEmpty(mEtWorkingAmount.getText()) ? 0.00 : Double.parseDouble(mEtWorkingAmount.getText().toString());
        mAmountArray[2] = TextUtils.isEmpty(mEtPlaceAmount.getText()) ? 0.00 : Double.parseDouble(mEtPlaceAmount.getText().toString());
        mAmountArray[3] = TextUtils.isEmpty(mEtAccessoryAmount.getText()) ? 0.00 : Double.parseDouble(mEtAccessoryAmount.getText().toString());
        for (double a : mAmountArray) {
            mTotalAmount += a;
        }
        mTvTotalAmount.setText(String.format(getString(R.string.report_order_total_price), mTotalAmount));
    }

    @Override
    public void afterTextChanged(Editable s) {

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

