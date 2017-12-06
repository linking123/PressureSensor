package com.suncreate.fireiot.fragment.requirement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.requirement.AccessoryRequirementSupplierGoodsListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.require.AccessoryRequireDetailStore;
import com.suncreate.fireiot.bean.require.AccessoryRequireDetailStoreGoods;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 配件商询价商品结算页面
 * <p>
 * desc
 */
public class AccessoryRequirementSupplierGoodsListFragment extends BaseFragment implements View.OnClickListener, BaseListAdapter.Callback {
    public static final String TAG = "AccessoryRequirementSupplierGoodsListFragment";
    @Bind(R.id.lv_supplier_goods_list)
    ListView mLvSupplierGoodsList;
    @Bind(R.id.cb_choose_goods_all)
    CheckBox mCbChooseGoodsAll;
    @Bind(R.id.btn_publish)
    Button mBtnPublish;

    private String mDemandId;
    private List<AccessoryRequireDetailStoreGoods> mGoodsList;
    private BaseListAdapter<AccessoryRequireDetailStoreGoods> mAdapter;
    private AccessoryRequireDetailStore mArds;

    private int mFlag;
    private StringBuffer mCarIds;

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (null != bundle) {
            mDemandId = bundle.getString("demandId");
            mArds = (AccessoryRequireDetailStore) bundle.getSerializable("ards");
        }
        mGoodsList = new ArrayList<>();
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mAdapter = new AccessoryRequirementSupplierGoodsListAdapter(this);
        mLvSupplierGoodsList.setAdapter(mAdapter);
        mBtnPublish.setOnClickListener(this);
        mCbChooseGoodsAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AccessoryRequireDetailStoreGoods ardsg : mAdapter.getDatas()) {
                    ardsg.setCkecked(mCbChooseGoodsAll.isChecked());
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_accessory_supplier_goods_list;
    }

    @OnClick({R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish:
                if (mGoodsList.size() < 1) {
                    AppContext.showToast("请选择结算商品");
                    return;
                }
                showWaitDialog();
                mFlag = 0;
                mCarIds = new StringBuffer();
                AccessoryRequireDetailStoreGoods goods = mGoodsList.get(mFlag);
                MonkeyApi.addGoodsCar(String.valueOf(mArds.getStroeId()), String.valueOf(goods.getGoodsId()), String.valueOf(goods.getPartsCount()), "1", mCarHandler);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if (null != mArds && null != mArds.getItems()) {
            mAdapter.addItem(mArds.getItems());
        }
        mAdapter.setmItemOnCheckListener(new BaseListAdapter.ItemOnCheckListener() {
            @Override
            public void noticeCheck(boolean isChecked) {
                int i = 0, j = 0;
                mGoodsList = new ArrayList<>();
                for (AccessoryRequireDetailStoreGoods ardsg : mAdapter.getDatas()) {
                    if (ardsg.isCkecked()) {
                        i++;
                        mGoodsList.add(ardsg);
                    } else {
                        j++;
                    }
                }
                if (mAdapter.getDatas().size() == i) {
                    mCbChooseGoodsAll.setChecked(true);
                } else {
                    mCbChooseGoodsAll.setChecked(false);
                }
            }
        });
    }

    protected TextHttpResponseHandler mCarHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("操作失败！");
            hideWaitDialog();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<ShoppingCar> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<ShoppingCar>>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    String carId = resultBean.getResult().getIds();
                    mCarIds.append(",").append(carId);
                    mFlag++;
                    if (mFlag < mGoodsList.size()) {
                        AccessoryRequireDetailStoreGoods goods = mGoodsList.get(mFlag);
                        MonkeyApi.addGoodsCar(String.valueOf(mArds.getStroeId()), String.valueOf(goods.getGoodsId()), String.valueOf(goods.getPartsCount()), "1", mCarHandler);
                    } else {
                        hideWaitDialog();
                        String carIds = mCarIds.substring(1);
                        Log.d("carIds", carIds);
                        Bundle b = new Bundle();
                        b.putString("ids", carIds);
                        b.putString("buyType", "shoppingcar");
                        UIHelper.showSimpleBack(getContext(), SimpleBackPage.ORDER_FILL_DETAIL, b);
                    }

                } else {
                    hideWaitDialog();
                    AppContext.showToastShort("操作失败！");
                }
            } catch (Exception e) {
                hideWaitDialog();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    protected TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("操作失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
//                JSONObject jsStr = (JSONObject) JSONObject.parse(responseString);
//                if ("1".equals(jsStr.get("code").toString())) {
//                    AppContext.showToast("需求发布成功，请等待商家报价。");
//                    Intent intent = new Intent();
//                    intent.putExtra("updateRequireCatalog", 1);
//                    getActivity().setResult(2, intent);
//                    getActivity().finish();
//                } else {
//                    AppContext.showToastShort("操作失败！");
//                }
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
    public Date getSystemTime() {
        return new Date();
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

