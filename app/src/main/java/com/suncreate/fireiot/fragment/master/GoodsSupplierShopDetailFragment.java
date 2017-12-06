package com.suncreate.fireiot.fragment.master;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.ShoppingStore;
import com.suncreate.fireiot.ui.OSCPhotosActivity;
import com.suncreate.fireiot.util.StringUtils;

import java.lang.reflect.Type;

import butterknife.Bind;

/**
 * 配件商门店简介
 * 详情
 * <p>
 * desc
 */
public class GoodsSupplierShopDetailFragment extends BaseDetailFragment<ShoppingStore> {

    private String storeId;

    @Bind(R.id.iv_store_img)
    ImageView iv_store_img;
    @Bind(R.id.tv_store_name)
    TextView tv_store_name;
    @Bind(R.id.tv_store_address)
    TextView tv_store_address;
    @Bind(R.id.tv_store_owner)
    TextView tv_store_owner;
    @Bind(R.id.tv_telephone)
    TextView tv_telephone;
    @Bind(R.id.tv_store_brief_introduce)
    TextView tv_store_brief_introduce;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_supplier_shop_detail;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        iv_store_img.setOnClickListener(this);
    }

    @Override
    protected String getCacheKey() {
        return "goods_supplier_shop_introduce" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        Bundle bbb = getArguments();
        storeId = bbb.getString("id");
        MonkeyApi.viewStoreBriefIntroduce(storeId, mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(ShoppingStore detail) {
        super.executeOnLoadDataSuccess(detail);
        if (detail != null) {
            //店铺Logo图片
            String storeImgId = detail.getStoreLogoId();
            getImgLoader().load(MonkeyApi.getPartImgUrl(storeImgId)).error(R.drawable.error).into(iv_store_img);

            //店铺名称
            String storeName = detail.getStoreName();
            if (!StringUtils.isEmpty(storeName)) {
                tv_store_name.setText(storeName);
            }
            //店铺地址
            String storeAddress = detail.getAddress();
            if (!StringUtils.isEmpty(storeAddress)) {
                tv_store_address.setText(storeAddress);
            }
            //联系人
            String storeOwner = detail.getUserName();
            if (!StringUtils.isEmpty(storeOwner)) {
                tv_store_owner.setText(storeOwner);
            }
            //电话
            String storePhone = detail.getPhone();
            if (!StringUtils.isEmpty(storePhone)) {
                tv_telephone.setText(storePhone);
            }
            //简介
            String storeInfo = detail.getInfo();
            if (!StringUtils.isEmpty(storeInfo)) {
                tv_store_brief_introduce.setText(storeInfo);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //预览logo大图
            case R.id.iv_store_img:
                OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getPartImgUrl(mDetail.getStoreLogoId()));
                break;
        }
    }

    @Override
    protected void executeOnLoadDataError() {
        super.executeOnLoadDataError();
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<ShoppingStore>>() {
        }.getType();
    }

}
