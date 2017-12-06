package com.suncreate.fireiot.adapter.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.carBrand.CarModel;

import java.util.List;

/**
 * 车型选择 三级 排量列表adapter
 */
@SuppressWarnings("unused")
public class CarYearModelListAdapter extends BaseAdapter {

    private List<CarModel> list;
    private Context context;
    private LayoutInflater inflater = null;

    public CarYearModelListAdapter(List<CarModel> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_caryear_list, null);
        }

        //年份
        TextView mYear = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.tv_year);
        CarModel carYear = list.get(position);
        String carYearStr = carYear.getCarbrandYear();
        if (carYearStr != null){
            mYear.setText(carYearStr);
        }

        return convertView;
    }
}
