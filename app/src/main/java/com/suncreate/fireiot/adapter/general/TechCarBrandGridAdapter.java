package com.suncreate.fireiot.adapter.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.util.Car.CarUtils;

/**
 * Created by JINWENLIN.
 * On 2016/12/29.
 */

public class TechCarBrandGridAdapter extends BaseAdapter {
    private String[] list;
    private LayoutInflater inflater = null;

    private Context context;

    public TechCarBrandGridAdapter(String[] list, Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String imagelogo;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_carbrand_techlist, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_carbrand_techlist);
            viewHolder.imagelogo = (TextView) convertView.findViewById(R.id.tv_logostr);
            imagelogo=list[position];
            viewHolder.imagelogo.setText(imagelogo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            imagelogo=viewHolder.imagelogo.getText().toString();
        }

        CarUtils.setCarBrandIcon(context, viewHolder.imageView, imagelogo);


        return convertView;
    }

    public static class ViewHolder {
        ImageView imageView;
        TextView imagelogo;
    }
}
