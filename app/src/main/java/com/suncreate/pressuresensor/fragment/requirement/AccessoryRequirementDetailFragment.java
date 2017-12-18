package com.suncreate.pressuresensor.fragment.requirement;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.requirement.AccessoryRequirementDetailGoodsListAdapter;
import com.suncreate.pressuresensor.adapter.requirement.AccessoryRequirementDetailSupplierListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.require.AccessoryRequireDetail;
import com.suncreate.pressuresensor.bean.require.AccessoryRequireDetailStore;
import com.suncreate.pressuresensor.bean.require.AccessoryRequireDetailStoreGoods;
import com.suncreate.pressuresensor.ui.OSCPhotosActivity;
import com.suncreate.pressuresensor.util.DatePro;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.util.ViewUtils;

import java.lang.reflect.Type;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 配件需求详情
 */
public class AccessoryRequirementDetailFragment extends BaseDetailFragment<AccessoryRequireDetail> implements BaseListAdapter.Callback {


    @Bind(R.id.tv_order_state)
    TextView mTvOrderState;
    @Bind(R.id.tv_publish_time)
    TextView mTvPublishTime;
    @Bind(R.id.tv_car_model)
    TextView mTvCarModel;
    @Bind(R.id.tv_receiver_address)
    TextView mTvReceiverAddress;
    @Bind(R.id.tv_demand_desc)
    TextView mTvDemandDesc;
    @Bind(R.id.iv_demand_img_1)
    ImageView mIvDemandImg1;
    @Bind(R.id.iv_demand_img_2)
    ImageView mIvDemandImg2;
    @Bind(R.id.iv_demand_img_3)
    ImageView mIvDemandImg3;
    @Bind(R.id.lv_goods_list)
    ListView mLvGoodsList;
    @Bind(R.id.lv_store_list)
    ListView mLvStoreList;

    private BaseListAdapter<AccessoryRequireDetailStoreGoods> mGoodsAdapter;
    private BaseListAdapter<AccessoryRequireDetailStore> mStoreAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_accessory_detail;
    }

    @Override
    public void initData() {
        super.initData();
        mGoodsAdapter = new AccessoryRequirementDetailGoodsListAdapter(this);
        mStoreAdapter = new AccessoryRequirementDetailSupplierListAdapter(this);
        mLvGoodsList.setAdapter(mGoodsAdapter);
        mLvStoreList.setAdapter(mStoreAdapter);
        mLvStoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccessoryRequireDetailStore ards = mStoreAdapter.getItem(position);
                if ("2".equals(ards.getStatus())) {
                    Bundle b = new Bundle();
                    b.putSerializable("ards", ards);
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.ACCESSORY_REQUIRE_SUPPLIER_GOODS_LIST, b);
                } else {
                    AppContext.showToastShort("配件商尚未报价！");
                }
            }
        });
    }

    @Override
    protected String getCacheKey() {
        return "service_order_detail_" + mDetailId;
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.viewProductsDemand(String.valueOf(mDetailId), mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(AccessoryRequireDetail detail) {
        super.executeOnLoadDataSuccess(detail);
        switch (detail.getOrderState()) {
            case 1:
                mTvOrderState.setTextColor(ContextCompat.getColor(getContext(), R.color.orange_700));
                mTvOrderState.setText("新发布");
                break;
            case 2:
                mTvOrderState.setTextColor(ContextCompat.getColor(getContext(), R.color.day_colorPrimary));
                mTvOrderState.setText("已报价");
                break;
            default:
                mTvOrderState.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                mTvOrderState.setText("未  知");
                break;
        }
        mTvPublishTime.setText(DatePro.formatDay(detail.getDemandAddtime(), "yyyy-MM-dd HH:mm"));
        mTvCarModel.setText(detail.getCarbrandName());
        mTvReceiverAddress.setText(detail.getDemandAddress());
        mTvDemandDesc.setText(detail.getDemandDesc());
        if (!TextUtils.isEmpty(detail.getDemandImg1())) {
            mIvDemandImg1.setVisibility(View.VISIBLE);
            getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getDemandImg1())).error(R.drawable.logo).into(mIvDemandImg1);
        }
        if (!TextUtils.isEmpty(detail.getDemandImg2())) {
            mIvDemandImg2.setVisibility(View.VISIBLE);
            getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getDemandImg2())).error(R.drawable.logo).into(mIvDemandImg2);
        }
        if (!TextUtils.isEmpty(detail.getDemandImg3())) {
            mIvDemandImg3.setVisibility(View.VISIBLE);
            getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getDemandImg3())).error(R.drawable.logo).into(mIvDemandImg3);
        }
        mGoodsAdapter.addItem(detail.getXjItems());
        mStoreAdapter.addItem(detail.getItems());
        ViewUtils.setListViewHeightBasedOnChildren(mLvGoodsList);
        ViewUtils.setListViewHeightBasedOnChildren(mLvStoreList);
    }


    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<AccessoryRequireDetail>>() {
        }.getType();
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

    @Override
    public Date getSystemTime() {
        return new Date();
    }

    @OnClick({R.id.iv_demand_img_1, R.id.iv_demand_img_2, R.id.iv_demand_img_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_demand_img_1:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getDemandImg1()));
                break;
            case R.id.iv_demand_img_2:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getDemandImg2()));
                break;
            case R.id.iv_demand_img_3:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getDemandImg3()));
                break;
        }
    }
}
