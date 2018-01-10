package com.suncreate.pressuresensor.adapter.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.carBrand.CarModel;

import java.util.List;

/**
 * 车型选择 三级 排量列表adapter
 */
@SuppressWarnings("unused")
public class CarDisplacementListAdapter extends BaseAdapter {

    private List<CarModel> list;
    private Context context;
    private LayoutInflater inflater = null;

    public CarDisplacementListAdapter(List<CarModel> list, Context context) {
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
            convertView = inflater.inflate(R.layout.fragment_item_cardisplacement_list, null);
        }

        //排量
        TextView mDisplacement = com.suncreate.pressuresensor.util.ViewHolder.get(convertView, R.id.tv_displacement);
        CarModel carDisplacement = list.get(position);
        String carDisplacementStr = carDisplacement.getCarbrandDisplacement();
        if (carDisplacementStr != null){
            mDisplacement.setText(carDisplacementStr);
        }

        return convertView;
    }
}
