package com.suncreate.pressuresensor.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;

/**
 * Created by Macintosh on 9/26/16.
 */

public class BaseSpinnerAdapter extends BaseAdapter {

    private final String datas[];

    private final Context context;

    private int selectIndex = 0;

    public void setSelectIndex(int index) {
        this.selectIndex = index;
    }

    public BaseSpinnerAdapter(Context context, String datas[]) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.spinner_layout_head, null);
        }
        ((TextView) convertView).setText(getItem(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_cell_team, null, false);
        }
        String name = getItem(position);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
        if (name != null) {
            tv.setText(name);
        }
//        if (selectIndex != position) {
//            tv.setTextColor(Color.parseColor("#acd4b3"));
//        } else {
//            tv.setTextColor(Color.parseColor("#6baf77"));
//        }
        return convertView;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public String getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
