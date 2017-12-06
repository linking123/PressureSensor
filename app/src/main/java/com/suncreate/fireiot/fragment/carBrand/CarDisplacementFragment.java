package com.suncreate.fireiot.fragment.carBrand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.general.CarDisplacementListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.carBrand.CarModel;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * 选择车排量
 */
public class CarDisplacementFragment extends BaseDetailFragment<CarModel> {

    public static final int REQUEST_CODE_FOR_YEAR = 1;

    @Bind(R.id.tv_show_select)
    TextView tv_show_select;
    @Bind(R.id.car_list_view)
    ListView mlistView;

    //拼接的品牌（车型经销商）+车系
    String carBrandAndModelName;
    //车型名称 如 A3
    String carmodelName;
    //经销商名称 如 进口奥迪
    String carbrandBrandType;
    //车品牌id 如1
    String carBrandId;

    List<CarModel> items;
    CarDisplacementListAdapter adapter;

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
        carBrandAndModelName = b.getString("carBrandAndModel");

        tv_show_select.setText(carBrandAndModelName);
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
        if (carBrandId != null && carmodelName != null && carbrandBrandType != null) {
            MonkeyApi.getCarmodel(carBrandId, carmodelName, null, null, carbrandBrandType, "2", mDetailHandler);
        }
    }

    @Override
    protected void executeOnLoadDataSuccess(CarModel detail) {
        super.executeOnLoadDataSuccess(detail);

        items = detail.getItems();
        if (StringUtils.isEmpty(items.get(0).getCarbrandDisplacement())){
            //没有排量了,没有下一级的返回
            hideWaitDialog();
            Intent intent = new Intent();
            intent.putExtra("carId", "");
            intent.putExtra("carBrandAndModelAndDisplacementAndYear", carBrandAndModelName);

            getActivity().setResult(CarModelFragment.REQUEST_CODE_FOR_DISPLACEMENT, intent);
            getActivity().finish();
            return;
        }
        adapter = new CarDisplacementListAdapter(items, getActivity());
        mlistView.setAdapter(adapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarModel carDisplacement = items.get(position);
                if (carDisplacement != null) {
                    Bundle b = new Bundle();
                    String carbrandDisplacement = carDisplacement.getCarbrandDisplacement();
                    //拼接的品牌（车型经销商）+车系 + 排量
                    String carBrandAndModelAndDisplacement = carBrandAndModelName + "-" + carbrandDisplacement;
                    b.putString("carBrandId", carBrandId);
                    b.putString("carmodelName", carmodelName);
                    b.putString("carbrandBrandType", carbrandBrandType);
                    b.putString("carbrandDisplacement", carbrandDisplacement);
                    b.putString("carBrandAndModelAndDisplacement", carBrandAndModelAndDisplacement);
                    UIHelper.showSimpleBackForResult(CarDisplacementFragment.this, REQUEST_CODE_FOR_YEAR, SimpleBackPage.CAR_YEAR_MODEL, b);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_FOR_YEAR){
            String carId = data.getStringExtra("carId");
            String carBrandAndModelAndDisplacementAndYear = data.getStringExtra("carBrandAndModelAndDisplacementAndYear");

            Intent intent = new Intent();

            intent.putExtra("carId", carId);
            intent.putExtra("carBrandAndModelAndDisplacementAndYear", carBrandAndModelAndDisplacementAndYear);

            getActivity().setResult(CarModelFragment.REQUEST_CODE_FOR_DISPLACEMENT, intent);
            getActivity().finish();
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<CarModel>>() {
        }.getType();
    }

}
