package com.suncreate.pressuresensor.fragment.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.requirement.AccessoryRequirementPublishChooseSupplierAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.GoodsStore;
import com.suncreate.pressuresensor.fragment.base.BaseListFragment;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 选择供应商，最后一步，确认
 * <p>
 * desc
 */
public class AccessoryRequirementPublishChooseSupplierFragment extends BaseListFragment<GoodsStore> {

    public static final String TAG = "AccessoryRequirementPublishChooseSupplierFragment";

    @Bind(R.id.btn_publish)
    TextView mBtnPublish;
    @Bind(R.id.cb_choose_supplier_all)
    CheckBox mCbChooseSupplierAll;
    private String mBrandId;
    private String mDemandId;
    private StringBuffer mStoreIds;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (null != bundle) {
            mBrandId = bundle.getString("brandId");
            mDemandId = bundle.getString("demandId");
        }
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mBtnPublish.setOnClickListener(this);
        mCbChooseSupplierAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (GoodsStore gs : mAdapter.getDatas()) {
                    gs.setCkecked(mCbChooseSupplierAll.isChecked());
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_accessory_publish_choose_supplier;
    }

    @OnClick({R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish:
                if (TextUtils.isEmpty(mStoreIds)) {
                    AppContext.showToast("请选择配件商");
                    return;
                }
                String storeIds = mStoreIds.substring(1);
                showWaitDialog();
                MonkeyApi.putProductsDemand(mDemandId, storeIds, mSubmitHandler);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter.setmItemOnCheckListener(new BaseListAdapter.ItemOnCheckListener() {
            @Override
            public void noticeCheck(boolean isChecked) {
                int i = 0, j = 0;
                mStoreIds = new StringBuffer();
                for (GoodsStore gs : mAdapter.getDatas()) {
                    if (gs.isCkecked()) {
                        i++;
                        mStoreIds.append(",").append(gs.getId());
                    } else {
                        j++;
                    }
                }
                if (mAdapter.getDatas().size() == i) {
                    mCbChooseSupplierAll.setChecked(true);
                } else {
                    mCbChooseSupplierAll.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
    }

    @Override
    protected BaseListAdapter<GoodsStore> getListAdapter() {
        return new AccessoryRequirementPublishChooseSupplierAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        verifyCacheType();
        MonkeyApi.getGoodsStoreList(null, null, null, null, null, mBrandId, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<GoodsStore>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<GoodsStore>> resultBean) {
        verifyCacheType();
        super.setListData(resultBean);
    }

    /**
     * verify cache type
     */
    private void verifyCacheType() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
//        UIHelper.showSimpleBack(getActivity(), SimpleBackPage.ACCESSORY_REQUIREMENT_DETAIL);
    }

    protected TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("操作失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                JSONObject jsStr = (JSONObject) JSONObject.parse(responseString);
                if ("1".equals(jsStr.get("code").toString())) {
                    AppContext.showToast("配件需求发布成功，请等待商家报价。");
                    Intent intent = new Intent();
                    intent.putExtra("updateRequireCatalog", 1);
                    getActivity().setResult(2, intent);
                    getActivity().finish();
                } else {
                    AppContext.showToastShort("操作失败！");
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
}