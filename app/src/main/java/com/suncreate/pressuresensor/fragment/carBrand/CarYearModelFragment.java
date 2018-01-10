package com.suncreate.pressuresensor.fragment.carBrand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.general.CarYearModelListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.carBrand.CarModel;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * 选择车系
 */
public class CarYearModelFragment extends BaseDetailFragment<CarModel> {

    @Bind(R.id.tv_show_select)
    TextView tv_show_select;
    @Bind(R.id.car_list_view)
    ListView mlistView;

    //拼接的品牌（车型经销商）+车系
    String carBrandAndModelAndDisplacement;
    //车型名称 如 A3
    String carmodelName;
    //经销商名称 如 进口奥迪
    String carbrandBrandType;
    //车品牌id 如1
    String carBrandId;
    //排量
    String carbrandDisplacement;

    List<CarModel> items;
    CarYearModelListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        Bundle b = getArguments();
        carBrandId = b.getString("carBrandId");
        carbrandBrandType = b.getString("carbrandBrandType");
        carmodelName = b.getString("carmodelName");
        carbrandDisplacement = b.getString("carbrandDisplacement");
        carBrandAndModelAndDisplacement = b.getString("carBrandAndModelAndDisplacement");

        tv_show_select.setText(carBrandAndModelAndDisplacement);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.car_series_choose_box;
    }

    @Override
    protected String getCacheKey() {
        return "carDisplacement_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        if (carBrandId != null && carmodelName != null && carbrandDisplacement != null && carbrandBrandType != null) {
            MonkeyApi.getCarmodel(carBrandId, carmodelName, null, carbrandDisplacement, carbrandBrandType, "3", mDetailHandler);
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(CarModel detail) {
        super.executeOnLoadDataSuccess(detail);

        items = detail.getItems();
        adapter = new CarYearModelListAdapter(items, getActivity());
        mlistView.setAdapter(adapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarModel carYear = items.get(position);
                if (carYear != null) {
                    Intent intent = new Intent();
                    String carId = carYear.getId();
                    String yearStr = carYear.getCarbrandYear();
                    intent.putExtra("carId", carId);
                    intent.putExtra("carBrandAndModelAndDisplacementAndYear", carBrandAndModelAndDisplacement + "-" + yearStr);

                    getActivity().setResult(CarDisplacementFragment.REQUEST_CODE_FOR_YEAR, intent);
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<CarModel>>() {
        }.getType();
    }


}
