package com.suncreate.fireiot.adapter.general;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.carBrand.CarModel;
import com.suncreate.fireiot.util.ViewUtils;

import java.util.List;

/**
 * 车型选择 一级 经销商列表adapter
 */
@SuppressWarnings("unused")
public class CarModelListAdapter extends BaseAdapter {

    private List<CarModel> list;
    private List<CarModel> items;
    private CarModeChildListAdapter adapter;
    private Context context;
    private LayoutInflater inflater = null;
    private int resource;
    private String carBrandName;
    private String carBrandId;
    private Fragment mFragment;
    private String mCarId;
    private String mCarBrandName;

    public CarModelListAdapter(List<CarModel> list, Context context, Fragment fragment, String carBrandName) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        mFragment = fragment;
        mCarBrandName = carBrandName;
    }

    public void setCarBrandName(String carBrandName) {
        this.carBrandName = carBrandName;
    }

    public void setCarBrandId(String carBrandId) {
        this.carBrandId = carBrandId;
    }

    public String getCarBrandId() {
        return carBrandId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //设置carId
    public void setmCarId(String mId) {
        this.mCarId = mId;
    }

    public String getmCarId() {
        return mCarId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final CarModel carmodel = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_carmodel_list, null);
            //车型经销商
            viewHolder.tv_car_model_supplier = (TextView) convertView.findViewById(R.id.tv_car_model_supplier);
            viewHolder.mlistView = (ListView) convertView.findViewById(R.id.listView);
            convertView.setTag(viewHolder);
//            viewHolder.mlistView.setTag(carmodel);
//            viewHolder.tv_car_model_supplier.setTag(carmodel);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
//            viewHolder.mlistView.setTag(carmodel);
//            viewHolder.tv_car_model_supplier.setTag(carmodel);
        }

        //车型经销商
        final String dealers = carmodel.getBrandName();
        viewHolder.tv_car_model_supplier.setText(dealers);

        items = carmodel.getItems();
        adapter = new CarModeChildListAdapter(items, context, mFragment, mCarBrandName);
        viewHolder.mlistView.setAdapter(adapter);

        String carId = items.get(position).getId();
        setmCarId(carId);

        ViewUtils.setListViewHeightBasedOnChildren(viewHolder.mlistView);
        return convertView;
    }

    final static class ViewHolder {
        TextView tv_car_model_supplier;
        ListView mlistView;
    }
}
