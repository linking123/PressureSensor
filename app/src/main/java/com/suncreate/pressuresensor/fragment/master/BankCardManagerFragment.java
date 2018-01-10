package com.suncreate.pressuresensor.fragment.master;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.general.BankCardManagerListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.BankCardManger;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * 银行卡列表
 */
public class BankCardManagerFragment extends BaseDetailFragment<BankCardManger> {

    public static final int REQUEST_CODE_ADD_BANK_CARD = 1;

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.btn_add_car)
    Button btnAddCar;

    private ConnectivityManager connectivityManager;

    List<BankCardManger> items;
    BankCardManagerListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bank_card_list;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        btnAddCar.setOnClickListener(this);
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getUserBankList(mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(BankCardManger detail) {
        super.executeOnLoadDataSuccess(detail);
        items = detail.getItems();
        adapter = new BankCardManagerListAdapter(items, getContext(), this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_car:
                if (items.size() >= 5) {
                    AppContext.showToastShort("最多添加5张银行卡，请删除后添加");
                    return;
                }
                UIHelper.showSimpleBackForResult(BankCardManagerFragment.this, REQUEST_CODE_ADD_BANK_CARD, SimpleBackPage.BANK_CARD_MANAGER_ADD);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE_ADD_BANK_CARD) {
            List<BankCardManger> listAdd = adapter.getmList();
            String mRealName = data.getStringExtra("mRealName");
            String mUserPhoneNum = data.getStringExtra("mUserPhoneNum");
            String mBankName = data.getStringExtra("mBankName");
            String mBankAccount = data.getStringExtra("mBankAccount");

            BankCardManger BankCardManger = new BankCardManger();
            BankCardManger.setRealname(mRealName);
            BankCardManger.setPhoneNumber(mUserPhoneNum);
            BankCardManger.setBankName(mBankName);
            BankCardManger.setBankCardNumber(mBankAccount);
            listAdd.add(0, BankCardManger);
            adapter.notifyDataSetChanged();
//            refresh();
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<BankCardManger>>() {
        }.getType();
    }

    @Override
    protected String getCacheKey() {
        return "bankCardManagerFragment_" + mId;
    }

}


