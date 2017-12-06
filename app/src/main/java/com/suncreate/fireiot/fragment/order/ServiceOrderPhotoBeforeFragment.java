package com.suncreate.fireiot.fragment.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseActivity;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.ServicesOrderDetail;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.meidia.TweetPicturesPreviewer;
import com.suncreate.fireiot.ui.OSCPhotosActivity;
import com.suncreate.fireiot.util.DatePro;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.ORDER_ACTION;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.ORDER_VIEW;
import static com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment.RESULT_CODE_PHOTO_PRE;
import static org.kymjs.kjframe.utils.ImageUtils.getSmallImageFile;

/**
 * 服务前拍照
 */
public class ServiceOrderPhotoBeforeFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.btn_submit_report)
    Button mBtnSubmitReport;
    @Bind(R.id.tv_order_id)
    TextView mTvOrderId;
    @Bind(R.id.tv_take_photo_1)
    TextView mTvTakePhoto1;
    @Bind(R.id.iv_report_photo_1)
    ImageView mIvReportPhoto1;
    @Bind(R.id.recycler_images_1)
    TweetPicturesPreviewer mRecyclerImages1;
    @Bind(R.id.tv_take_photo_2)
    TextView mTvTakePhoto2;
    @Bind(R.id.iv_report_photo_2)
    ImageView mIvReportPhoto2;
    @Bind(R.id.recycler_images_2)
    TweetPicturesPreviewer mRecyclerImages2;
    @Bind(R.id.tv_take_photo_3)
    TextView mTvTakePhoto3;
    @Bind(R.id.iv_report_photo_3)
    ImageView mIvReportPhoto3;
    @Bind(R.id.recycler_images_3)
    TweetPicturesPreviewer mRecyclerImages3;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.ll_title_time)
    LinearLayout llTitleTime;
    private ServicesOrderDetail mDetail;
    private int mAction;
    private File[] mImgBefore;


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
                ((BaseActivity) getActivity()).setActionBarTitle(R.string.service_order_photo_before_view);
            } else {
                ((BaseActivity) getActivity()).setActionBarTitle(R.string.service_order_photo_before_view);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_service_order_take_photo_before;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mTvTakePhoto1.setOnClickListener(this);
        mTvTakePhoto2.setOnClickListener(this);
        mTvTakePhoto3.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mDetail = getBundleSerializable("serviceOrder");
        if(null==mDetail.getOrderBeforeTime()){
            llTitleTime.setVisibility(View.GONE);
        }else {
            tvTime.setText("服务前拍照时间：" + DatePro.formatDay(mDetail.getOrderBeforeTime(), "yyyy-MM-dd HH:mm"));
        }
        if (mDetail != null) {
            mTvOrderId.setText(mDetail.getOrderId());
            // 已报价或者非技师，只能查看报价单
            if (mAction == ORDER_VIEW) {
                mTvTakePhoto1.setVisibility(View.GONE);
                mRecyclerImages1.setVisibility(View.GONE);
                mTvTakePhoto2.setVisibility(View.GONE);
                mRecyclerImages2.setVisibility(View.GONE);
                mTvTakePhoto3.setVisibility(View.GONE);
                mRecyclerImages3.setVisibility(View.GONE);
                mIvReportPhoto1.setVisibility(View.VISIBLE);
                mIvReportPhoto2.setVisibility(View.VISIBLE);
                mIvReportPhoto3.setVisibility(View.VISIBLE);
                mBtnSubmitReport.setVisibility(View.GONE);
                setImageFromNet(R.id.iv_report_photo_1, MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto1()), R.drawable.logo);
                setImageFromNet(R.id.iv_report_photo_2, MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto2()), R.drawable.logo);
                setImageFromNet(R.id.iv_report_photo_3, MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto3()), R.drawable.logo);
            }
        } else {
            AppContext.showToast("加载失败");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_take_photo_1, R.id.iv_report_photo_1, R.id.tv_take_photo_2, R.id.iv_report_photo_2, R.id.tv_take_photo_3, R.id.iv_report_photo_3, R.id.btn_submit_report})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_take_photo_1:
                mRecyclerImages1.onLoadMoreClick1Img();
                break;
            case R.id.iv_report_photo_1:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto1()));
                break;
            case R.id.tv_take_photo_2:
                mRecyclerImages2.onLoadMoreClick1Img();
                break;
            case R.id.iv_report_photo_2:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto2()));
                break;
            case R.id.tv_take_photo_3:
                mRecyclerImages3.onLoadMoreClick1Img();
                break;
            case R.id.iv_report_photo_3:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getBeforePhoto3()));
                break;
            case R.id.btn_submit_report:
                try {
                    //图片上传
                    int imgCount1 = mRecyclerImages1.getChildCount();
                    int imgCount2 = mRecyclerImages2.getChildCount();
                    int imgCount3 = mRecyclerImages3.getChildCount();
                    if (imgCount1 == 0 || imgCount2 == 0 || imgCount3 == 0) {
                        AppContext.showToast("还有照片没上传！");
                        return;
                    } else if (imgCount1 > 1 || imgCount2 > 1 || imgCount3 > 1) {
                        AppContext.showToast("每项最多上传一张图片！");
                        return;
                    }
                    mImgBefore = new File[3];
                    String[] imgPaths1 = mRecyclerImages1.getPaths();
                    int len1 = imgPaths1.length;
                    if (len1 == 1) {
//                        mImgBefore[0] = new File(imgPaths1[0]);
                        mImgBefore[0] = getSmallImageFile(getContext(), imgPaths1[0], 300, 200, true);
                    }
                    String[] imgPaths2 = mRecyclerImages2.getPaths();
                    int len2 = imgPaths2.length;
                    if (len2 == 1) {
//                        mImgBefore[1] = new File(imgPaths2[0]);
                        mImgBefore[1] = getSmallImageFile(getContext(), imgPaths2[0], 300, 200, true);
                    }
                    String[] imgPaths3 = mRecyclerImages3.getPaths();
                    int len3 = imgPaths1.length;
                    if (len3 == 1) {
//                        mImgBefore[2] = new File(imgPaths3[0]);
                        mImgBefore[2] = getSmallImageFile(getContext(), imgPaths3[0], 300, 200, true);
                    }
                    showWaitDialog(R.string.progress_submit);
                    MonkeyApi.uploadServiceOrderImagePre(mDetail.getId(), mImgBefore, mSubmitHandler);
                } catch (FileNotFoundException e) {
                    AppContext.showToast("图像不存在，上传失败");
                }
                break;
        }
    }

    private TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("上传失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("上传成功");
                    Intent intent = new Intent();
                    getActivity().setResult(RESULT_CODE_PHOTO_PRE, intent);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}

