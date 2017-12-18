package com.suncreate.pressuresensor.fragment.requirement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseActionAdapter;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.requirement.ServiceRequirementListAdater;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.require.Requirement;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;

import butterknife.Bind;

/**
 * 服务需求
 * <p>
 * desc
 */
public class ServiceRequirementFragment extends GeneralListFragment<Requirement> {

    public static final String TAG = "ServiceRequirement";

    private int mCatalog = 0;
    private BaseActionAdapter mBaseActionAdapter;
    private int[] positions = {1, 0, 0};

    private String mOrderState = "";  //初始进入显示全部，0新发布，1已接单, 2已取消

    @Bind(R.id.gv_ques)
    GridView mGridView;

    @Bind(R.id.tv_requirement)
    TextView tv_requirement;

    //新建广播，接收发布成功，刷新列表
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_REQUIREMENT_NEW:
                    onRefreshing();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册监听，发布完成后，返回刷新列表
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_REQUIREMENT_NEW);
        getActivity().registerReceiver(mReceiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        tv_requirement.setText("发布服务需求");
        tv_requirement.setOnClickListener(this);
        mBaseActionAdapter = new BaseActionAdapter(getActivity(), R.array.service_requirement_item, positions);
        mGridView.setAdapter(mBaseActionAdapter);
        int size = positions.length;
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int itemWidth = (screenWidth - 20) / size;
        mGridView.setColumnWidth(itemWidth);
        mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mGridView.setNumColumns(size);
        mGridView.setItemChecked(mCatalog, true);
        updateAction(mCatalog);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCatalog = position;
                ((ServiceRequirementListAdater) mAdapter).setActionPosition(position + 1);
                if (!mIsRefresh) {
                    mIsRefresh = true;
                }
                updateAction(position);
                switch (position) {
                    case 0:
                        mOrderState = "";
                        break;
                    case 1:
                        mOrderState = "0";  //新发布
                        break;
                    case 2:
                        mOrderState = "1";  //已接单
                        break;
                    default:
                        break;
                }
                onRefreshing();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_require_fragment;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            //发布按钮被点击
            case R.id.tv_requirement:
                if (AppContext.getInstance().isLogin()) {
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.SERVICE_REQUIREMENT_PUBLISH);
                } else {
                    UIHelper.showLoginActivity(getContext());
                }
                break;
            default:
                break;
        }
    }

    /**
     * notify action data
     *
     * @param position position
     */
    private void updateAction(int position) {
        int len = positions.length;
        for (int i = 0; i < len; i++) {
            if (i != position) {
                positions[i] = 0;
            } else {
                positions[i] = 1;
            }
        }
        mBaseActionAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onRequestError(int code) {
        Log.i(TAG, "请求失败");
    }

    @Override
    protected BaseListAdapter<Requirement> getListAdapter() {
        return new ServiceRequirementListAdater(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        MonkeyApi.getServiceRequirementList(mOrderState, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Requirement>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Requirement>> resultBean) {
        super.setListData(resultBean);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Requirement requirement = mAdapter.getItem(position);
        if (requirement != null) {
            Bundle b = new Bundle();
            String requirementId = requirement.getId();
            b.putString("requirementId", requirementId);
            UIHelper.showSimpleBackForResult(this, 1, SimpleBackPage.SERVICE_REQUIREMENT_DETAIL, b);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            String requirementId = data.getStringExtra("requirementId");
            for (int i = 0; i < mAdapter.getDatas().size(); i++) {
                if (mAdapter.getDatas().get(i).getId().equals(requirementId)) {
                    mAdapter.getDatas().remove(mAdapter.getDatas().get(i));
                    break;
                }
            }
            mAdapter.notifyDataSetChanged();
            onRefreshing();
        }
    }
}
