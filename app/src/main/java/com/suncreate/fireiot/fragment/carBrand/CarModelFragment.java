package com.suncreate.fireiot.fragment.carBrand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.general.CarModelListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.carBrand.CarModel;
import com.suncreate.fireiot.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * 第二步，选择车型
 */
public class CarModelFragment extends BaseDetailFragment<CarModel> {

    public static final int REQUEST_CODE_FOR_DISPLACEMENT = 1;

    @Bind(R.id.tv_show_select)
    TextView tv_show_select;
    @Bind(R.id.listView)
    ListView mlistView;

    //从第一步选择带过来品牌
    private String carBrandName;
    private String carBrandId;

    private String carIdd;

    List<CarModel> items;
    CarModelListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        Bundle b = getArguments();
        carBrandId = b.getString("carBrandId");
        carBrandName = b.getString("carBrandName");

        tv_show_select.setText(carBrandName);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<CarModel>>() {
        }.getType();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.car_model_choose_box;
    }

    @Override
    protected String getCacheKey() {
        return "carmodel_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        if (!StringUtils.isEmpty(carBrandId)) {
            MonkeyApi.getCarmodel(carBrandId, null, null, null, null, "1", mDetailHandler);
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(CarModel detail) {
        super.executeOnLoadDataSuccess(detail);

        items = detail.getItems();
        adapter = new CarModelListAdapter(items, getActivity(), this, carBrandName);
        adapter.setCarBrandName(carBrandName);
        adapter.setCarBrandId(carBrandId);
        mlistView.setAdapter(adapter);
    }

    @Override
    protected void executeOnLoadDataError() {
        super.executeOnLoadDataError();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_FOR_DISPLACEMENT) {
            String carId = data.getStringExtra("carId");
            if (StringUtils.isEmpty(carId)) {
                carId = adapter.getmCarId();
            }
            String carBrandAndModelAndDisplacementAndYear = data.getStringExtra("carBrandAndModelAndDisplacementAndYear");

            Intent intent = new Intent();

            intent.putExtra("carId", carId);
            intent.putExtra("carBrandAndModelAndDisplacementAndYear", carBrandAndModelAndDisplacementAndYear);

            getActivity().setResult(CarBrandFragment.REQUEST_CODE_FOR_MODEL, intent);
            getActivity().finish();
        }
    }
}
