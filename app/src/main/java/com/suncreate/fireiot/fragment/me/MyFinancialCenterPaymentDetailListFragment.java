package com.suncreate.fireiot.fragment.me;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.MyFinancialCenterPaymentDetailListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseListFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * 收支明细列表
 */
public class MyFinancialCenterPaymentDetailListFragment extends GeneralListFragment<Pay> {

    /**
     * 收支明细的状态：总的分两大类，
     * 收入（下级分类有 充值，退款，服务订单费，工位订单费）
     * 与
     * 支出（下级分类有 提现，服务订单费，工位订单费，配件订单费），
     * 明细列表只以以上下级分类名称显示；
     * 在详情页面体现更细节的状态分类，体现订单号、时间、金额、种类；
     */
    public static final String TAG = "PaymentDetailListFragment";
    private static final String CACHE_KEY_PREFIX = "financial_center_inout_list_";
    private int mCatalog = 0;
    //消费标识(0支出 1收入,默认全部)
    String consumeCost = "";
    private ConnectivityManager connectivityManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getInt(BaseListFragment.BUNDLE_KEY_CATALOG, 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_financial_center_in_out_detail_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    /**
     * request local cache
     */
    @SuppressWarnings("unchecked")
    private void requestLocalCache() {
        mBean = (PageBean<Pay>) CacheManager.readObject(getActivity(), CACHE_NAME);
        if (mBean != null) {
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
            mRefreshLayout.setCanLoadMore();
        } else {
            mBean = new PageBean<>();
            mBean.setItems(new ArrayList<Pay>());
        }
    }

    @Override
    protected void initData() {
        CACHE_NAME = CACHE_KEY_PREFIX + mCatalog;
        super.initData();
    }

    @Override
    protected void onRequestError(int code) {
        super.onRequestError(code);
        requestLocalCache();
    }

    @Override
    protected void requestData() {
        super.requestData();
        MonkeyApi.getPayList(getPayTypeByCatalog(mCatalog), mBean != null ? mBean.getPageNum() + 1 : 1, AppContext.PAGE_SIZE, mHandler);
    }

    @Override
    protected void setListData(ResultBean<PageBean<Pay>> resultBean) {
        super.setListData(resultBean);
    }

    @Override
    protected BaseListAdapter<Pay> getListAdapter() {
        return new MyFinancialCenterPaymentDetailListAdapter(this);
    }

    private String getPayTypeByCatalog(int catagory) {
        switch (catagory) {
            case 0:
                consumeCost = "";
                break;
            case 1:
                consumeCost = "1";
                break;
            case 2:
                consumeCost = "0";
                break;
        }
        return consumeCost;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        Pay pay = mAdapter.getItem(position);
        if (pay != null) {
            Bundle b = new Bundle();
            b.putString("id", pay.getId());
            b.putString("addtime", pay.getAddtime());
            b.putString("amount", pay.getAmount());
            b.putString("payType", pay.getPayType());
            b.putString("consumeCost", pay.getConsumeCost());
            b.putString("consumeSn", pay.getConsumeSn());
            UIHelper.showSimpleBack(getActivity(), SimpleBackPage.MY_FINANCIAL_CENTER_PAYMENT_DETAIL_DETAIL, b);
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Pay>>>() {
        }.getType();
    }
}
