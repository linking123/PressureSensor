package com.suncreate.fireiot.fragment.me;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.general.CarmanagerListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.carBrand.CarManager;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * 车辆管理页面
 *
 */
public class MyCarManagerFragment extends BaseDetailFragment<CarManager> implements View.OnClickListener {

    public static final int REQUEST_CODE_ADD_CAR = 1;
    public static final int REQUEST_CODE_EDIT_CAR = 2;

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.btn_add_car)
    Button btnAddCar;

    private ConnectivityManager connectivityManager;

    List<CarManager> items;
    CarmanagerListAdapter adapter;

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
    public void initView(View view) {
        super.initView(view);
        btnAddCar.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_carmanager_list;
    }

    @Override
    protected String getCacheKey() {
        return "carManagerFragment_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getUserCarmodelList(mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(CarManager detail) {
        super.executeOnLoadDataSuccess(detail);

        items = detail.getItems();
        adapter = new CarmanagerListAdapter(items, getContext(), this);
        listView.setAdapter(adapter);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<CarManager>>() {
        }.getType();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_car:
                if (items.size() >= 5) {
                    AppContext.showToastShort("最多添加5辆爱车，请删除后添加");
                    return;
                }
                UIHelper.showSimpleBackForResult(MyCarManagerFragment.this, REQUEST_CODE_ADD_CAR, SimpleBackPage.MY_CAR_MANAGER_ADD);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE_ADD_CAR) {
            List<CarManager> listAdd = adapter.getmList();
            String mCarmodelId = data.getStringExtra("mCarmodelId");
            String mPlate = data.getStringExtra("mPlate");
            String mIsDefault = data.getStringExtra("mIsDefault");

            CarManager carManager = new CarManager();
            carManager.setCarmodelId(mCarmodelId);
            carManager.setPlate(mPlate);
            carManager.setIsDefault(mIsDefault);
            listAdd.add(0, carManager);
            refresh();
        }
        if (resultCode == REQUEST_CODE_EDIT_CAR) {
            List<CarManager> listAdd = adapter.getmList();
            String mCarmodelId = data.getStringExtra("mCarmodelId");
            String mPlate = data.getStringExtra("mPlate");
            String mIsDefault = data.getStringExtra("mIsDefault");

            CarManager carManager = new CarManager();
            carManager.setCarmodelId(mCarmodelId);
            carManager.setPlate(mPlate);
            carManager.setIsDefault(mIsDefault);
            listAdd.add(0, carManager);
            refresh();
        }
    }
}
