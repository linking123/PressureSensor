package com.suncreate.pressuresensor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.user.GoodsType;

/**
 * Created by chenzhao on 17-1-4.
 */

public class GoodsTypeAdapter extends ArrayAdapter<GoodsType> {

    public GoodsTypeAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GoodsType goodsType = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_goods_type, parent, false);
        }
        // Lookup view for data population
        TextView tvGoodsTypeName = (TextView) convertView.findViewById(R.id.tv_item_goods_type);
        // Populate the data into the template view using the data object
        tvGoodsTypeName.setText(goodsType.getTotalName());
        // Return the completed view to render on screen
        return convertView;

    }
}
