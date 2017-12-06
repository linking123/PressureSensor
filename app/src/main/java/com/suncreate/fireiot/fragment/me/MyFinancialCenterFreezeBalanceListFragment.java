package com.suncreate.fireiot.fragment.me;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.FreezeBalanceOrderListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.FreezeBalanceOrder;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;

/**
 * 冻结金额 订单列表信息
 */
public class MyFinancialCenterFreezeBalanceListFragment extends GeneralListFragment<FreezeBalanceOrder> {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_freeze_balance_order_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
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
    protected void requestData() {
        super.requestData();
        MonkeyApi.getFreezeOrderList(mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected void setListData(ResultBean<PageBean<FreezeBalanceOrder>> resultBean) {
        super.setListData(resultBean);
    }

    @Override
    protected BaseListAdapter<FreezeBalanceOrder> getListAdapter() {
        return new FreezeBalanceOrderListAdapter(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        FreezeBalanceOrder freezeBalanceOrder = mAdapter.getItem(position);
        String orderClass = freezeBalanceOrder.getOrderClass(); //0配件订单 1抢单订单 2服务订单 3询价订单 4场地订单
        switch (orderClass) {
            case "0":
                Bundle b0 = new Bundle();
                b0.putString("id", freezeBalanceOrder.getId());
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.ORDER_DETAIL, b0);
                break;
            case "1":
                break;
            case "2":
                Bundle b2 = new Bundle();
                b2.putLong("id", Long.parseLong(freezeBalanceOrder.getId()));
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.SERVICE_ORDER_DETAIL, b2);
                break;
            case "3":
                break;
            case "4":
                Bundle b4 = new Bundle();
                b4.putLong("id", Long.parseLong(freezeBalanceOrder.getId()));
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.FIELD_ORDER_DETAIL, b4);
                break;
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<FreezeBalanceOrder>>>() {
        }.getType();
    }
}
