package com.suncreate.pressuresensor.fragment.requirement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.GoodsTypeAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.GoodsType;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

public class GoodsTypeListFragment extends BaseFragment {

    @Bind(R.id.lv_goods_type)
    ListView mListView;

    private ArrayAdapter<GoodsType> mArrayAdapter;
    private List<GoodsType> mGoodsTypeList;
    private String mParentId;
    private String mLevel;

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        if (null != bundle) {
            mParentId = bundle.getString("mParentId");
            mLevel = bundle.getString("mLevel", "0");
        }
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mGoodsTypeList = new ArrayList<>();
        mArrayAdapter = new GoodsTypeAdapter(getContext(), R.layout.item_goods_type);
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsType gt = mArrayAdapter.getItem(position);
                if (null != gt) {
                    if ("2".equals(gt.getTotalLevel())) {
                        Intent intent = new Intent();
                        intent.putExtra("goodsTypeId", gt.getId());
                        intent.putExtra("goodsTypeName", gt.getTotalName());
                        getActivity().setResult(1, intent);
                        getActivity().finish();
                    } else {
                        Bundle b = new Bundle();
                        b.putString("mParentId", String.valueOf(gt.getId()));
                        b.putString("mLevel", String.valueOf(Integer.valueOf(gt.getTotalLevel()) + 1));
                        UIHelper.showSimpleBackForResult(GoodsTypeListFragment.this, 1, SimpleBackPage.GOODS_TYPE_LIST, b);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        showWaitDialog();
        MonkeyApi.getGoodsType(mParentId, null, null, mLevel, mHandler);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_goods_type;
    }

    private TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            DialogHelp.getTitleMessageDialog(getContext(), "提示", "暂时无法获取类别", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<GoodsType>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<GoodsType>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                        mGoodsTypeList = resultBean.getResult().getItems();
                        mArrayAdapter.clear();
                        mArrayAdapter.addAll(mGoodsTypeList);

                        mArrayAdapter.notifyDataSetChanged();
                    }
                } else {
                    onFailure(statusCode, headers, responseString, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            hideWaitDialog();
            super.onFinish();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == resultCode) {
            getActivity().setResult(1, data);
            getActivity().finish();
        }
    }
}
