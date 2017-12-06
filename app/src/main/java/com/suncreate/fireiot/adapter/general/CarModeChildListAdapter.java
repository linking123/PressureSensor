package com.suncreate.fireiot.adapter.general;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.carBrand.CarModel;
import com.suncreate.fireiot.fragment.carBrand.CarModelFragment;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;

import java.util.List;

/**
 * 车型选择 二级 经销商下级的车型车系 列表 adapter
 */
public class CarModeChildListAdapter extends BaseAdapter {

    private List<CarModel> list;
    private CarModeChildListAdapter adapter;
    private Context context;
    private LayoutInflater inflater = null;
    private Fragment mFragment;
    private int resource;
    private String mCarBrandName;

    public CarModeChildListAdapter(List<CarModel> list, Context context, Fragment fragment, String carBrandName) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        mFragment = fragment;
        mCarBrandName = carBrandName;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CarModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_item_carmodel_child_list, null);
        }
        final CarModel mCarmodel = list.get(position);
        final TextView tvCarmodelName = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.carmodel_name);
        final LinearLayout llCarmodelName = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.ll_carmodel_line);

        String carmodelName = mCarmodel.getCarBrandName();

        if (!StringUtils.isEmpty(carmodelName)) {
            tvCarmodelName.setText(carmodelName);
        }

        llCarmodelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                String carmodelName = mCarmodel.getCarBrandName();
                String carbrandBrandType = mCarmodel.getCarbrandBrandType();
                //车品牌+车型
                String carBrandAndModel = mCarBrandName + "-" + carmodelName;
                b.putString("carBrandId", mCarmodel.getBrandId());
                b.putString("carmodelName", carmodelName);
                b.putString("carbrandBrandType", carbrandBrandType);
                b.putString("carBrandAndModel", carBrandAndModel);
                UIHelper.showSimpleBackForResult(mFragment, CarModelFragment.REQUEST_CODE_FOR_DISPLACEMENT, SimpleBackPage.CAR_DISPLACEMENT, b);
            }
        });

        return convertView;
    }

}
