package com.suncreate.fireiot.fragment.master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.adapter.general.AddressSelectListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Address;
import com.suncreate.fireiot.fragment.general.GeneralListFragment;

import java.lang.reflect.Type;

/**
 * 地址列表
 * <p>
 * desc
 */
public class AddressSelectFragment extends GeneralListFragment<Address> {

    public static final int REQUEST_CODE_DEFAULT_ADDRESS = 1;
    public static final String ITEM_TECH = "item_address";
    protected int mId;
    private int catalog = 1;
    private int[] positions = {1, 0, 0, 0, 0, 0, 0, 0};
    private ConnectivityManager connectivityManager;
    private Address address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        return R.layout.fragment_address_list_select;
    }

    @Override
    protected void initData() {
        super.initData();
        MonkeyApi.getAddressList(mHandler);
    }

    @Override
    protected BaseListAdapter<Address> getListAdapter() {
        return new AddressSelectListAdapter(this);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Address>>>() {
        }.getType();
    }

    @Override
    protected void setListData(ResultBean<PageBean<Address>> resultBean) {
        super.setListData(resultBean);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        address = mAdapter.getItem(position);
        if (address != null) {
            Intent intent = new Intent();
            intent.putExtra("id", mId);
            intent.putExtra("address", address.getAddressInfo());
            intent.putExtra("phone", address.getAddressMobile());
            intent.putExtra("name", address.getAddressTruename());
            intent.putExtra("area", address.getAreaId());
            getActivity().setResult(AddressSelectFragment.REQUEST_CODE_DEFAULT_ADDRESS, intent);
            getActivity().finish();
        }
    }
}
