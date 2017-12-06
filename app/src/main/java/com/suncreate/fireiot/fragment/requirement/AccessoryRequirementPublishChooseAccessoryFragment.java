package com.suncreate.fireiot.fragment.requirement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.requirement.AccessoryRequirePublishChooseGoodsAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.user.GoodsRequire;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 选择配件
 * <p>
 * desc
 */
public class AccessoryRequirementPublishChooseAccessoryFragment extends BaseFragment implements View.OnClickListener, BaseListAdapter.Callback {


    @Bind(R.id.tv_accessory_kind_input)
    TextView mTvAccessoryKindInput;
    @Bind(R.id.et_accessory_name)
    EditText mEtAccessoryName;
    @Bind(R.id.btn_goods_add)
    Button mBtnGoodsAdd;
    //    @Bind(R.id.checkbox_choose_all)
//    CheckBox mCheckboxChooseAll;
//    @Bind(R.id.btn_goods_del)
//    ImageButton mBtnGoodsDel;
    @Bind(R.id.btn_choose_supplier)
    Button mBtnChooseSupplier;
    @Bind(R.id.lv_publish_require_accessory)
    ListView mLvPublishRequireAccessory;

    private Long mCurrentGoodsTypeId;
    private String mCurrentGoodsTypeName;
    private ArrayList<GoodsRequire> mGoodsRequireList;
    private String mDemandId;

    private BaseListAdapter<GoodsRequire> mAdapter;

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (null != bundle) {
            mDemandId = bundle.getString("demandId");
        }
    }

    @Override
    protected void initWidget(View root) {
        mBtnGoodsAdd.setOnClickListener(this);
//        mBtnGoodsDel.setOnClickListener(this);
        mBtnChooseSupplier.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_accessory_publish_choose_accessory;
    }

    @Override
    protected void initData() {
        mAdapter = new AccessoryRequirePublishChooseGoodsAdapter(this);
        mLvPublishRequireAccessory.setAdapter(mAdapter);
        mGoodsRequireList = new ArrayList<>();
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

    @OnClick({R.id.tv_accessory_kind_input, R.id.btn_goods_add, R.id.btn_choose_supplier})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_accessory_kind_input:
                Bundle b = new Bundle();
                b.putString("parentLevel", "0");
                UIHelper.showSimpleBackForResult(this, 0, SimpleBackPage.GOODS_TYPE_LIST, b);
                break;
            case R.id.btn_goods_add:
                //隐藏系统软键盘
                ((InputMethodManager) mContext.getSystemService(getContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String currentGoodsName = mEtAccessoryName.getText().toString();
//                for (GoodsRequire g : mGoodsRequireList) {
//                    if (currentGoodsName.equals(g.getPartsName())) {
//                        DialogHelp.getMessageDialog(getContext(), "配件名称重复").show();
//                        return;
//                    }
//                }
                if (null != mCurrentGoodsTypeId && !TextUtils.isEmpty(mCurrentGoodsTypeName) && !TextUtils.isEmpty(currentGoodsName)) {
                    GoodsRequire gr = new GoodsRequire();
                    gr.setGcId(String.valueOf(mCurrentGoodsTypeId));
                    gr.setGcName(mCurrentGoodsTypeName);
                    gr.setPartsName(currentGoodsName);
                    if (TextUtils.isEmpty(gr.getPartsCount())) {
                        gr.setPartsCount("1");
                    }
                    mGoodsRequireList.add(gr);
                    mAdapter.addItem(gr);
//                    mCurrentGoodsTypeId = null;
//                    mCurrentGoodsTypeName = null;
//                    mTvAccessoryKindInput.setText("");
                    mEtAccessoryName.setText("");
                } else {
                    if (null == mCurrentGoodsTypeId || TextUtils.isEmpty(mCurrentGoodsTypeName)) {
                        AppContext.showToast("请选择配件类别");
                        return;
                    }
                    if (TextUtils.isEmpty(currentGoodsName)) {
                        AppContext.showToast("请填写配件名称");
                        return;
                    }
                }
                break;
            case R.id.btn_choose_supplier:
                //需求发布成功，提醒后，自动跳转回到配件需求列表，并刷新列表，本次发布状态为新发布；但目前requirementViewpageFragment中怎么定tab不知道；
                if (TextUtils.isEmpty(mDemandId)) {
                    AppContext.showToast("需求信息为空");
                    return;
                }
                if (mGoodsRequireList.size() == 0) {
                    AppContext.showToast("请至少选择一个配件");
                    return;
                }
                String detailsJson = JSON.toJSONString(mGoodsRequireList);
                showWaitDialog();
                MonkeyApi.supplementProductsDemand(mDemandId, detailsJson, mSubmitHandler);
                break;
        }
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
                    Bundle b = getArguments();
                    UIHelper.showSimpleBackForResult(AccessoryRequirementPublishChooseAccessoryFragment.this, 1, SimpleBackPage.ACCESSORY_REQUIREMENT_PUBLISH_CHOOSE_SUPPLIER, getArguments());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == resultCode) {
            mCurrentGoodsTypeId = data.getExtras().getLong("goodsTypeId");
            mCurrentGoodsTypeName = data.getExtras().getString("goodsTypeName");
            mTvAccessoryKindInput.setText(mCurrentGoodsTypeName);
        } else if (2 == resultCode) {
            getActivity().setResult(2, data);
            getActivity().finish();
        }
    }

    @Override
    public Date getSystemTime() {
        return new Date();
    }
}
