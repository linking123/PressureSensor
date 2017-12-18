package com.suncreate.pressuresensor.fragment.master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.adapter.general.TechServersListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.require.Requirement;
import com.suncreate.pressuresensor.fragment.general.GeneralListFragment;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;

/**
 * 附近服务需求
 */
public class TechServersListFragment extends GeneralListFragment<Requirement> {

    public static final String TAG = "TechServersListFragment";

    private ConnectivityManager connectivityManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tech_servers_list;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
    }

    @Override
    protected BaseListAdapter<Requirement> getListAdapter() {
        return new TechServersListAdapter(this);
    }

    @Override
    protected void requestData() {
        super.requestData();
        MonkeyApi.getAllNearServiceRequirementList(AppContext.lon, AppContext.lat, mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
//        MonkeyApi.getAllNearServiceRequirementList("104.48060937499996", "36.30556423523153", mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
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
        if (null != requirement) {
            if (AppContext.getInstance().isLogin()) {
                Bundle b = new Bundle();
                b.putString("requirementId", requirement.getId());
                UIHelper.showSimpleBackForResult(this, 1, SimpleBackPage.SERVICE_REQUIREMENT_DETAIL, b);
            } else {
                UIHelper.showLoginActivity(getContext());
            }
        } else {
            AppContext.showToast("该服务需求不存在！");
        }
    }

    @Override
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
