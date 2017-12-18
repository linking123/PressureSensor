package com.suncreate.pressuresensor.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import java.util.ArrayList;
import java.util.HashMap;


import com.suncreate.pressuresensor.R;

import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 通用的ViewHolder
 * <p/>
 * Created by 火蚁 on 15/4/8.
 */
@SuppressWarnings("unused")
public  class CarListAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private static HashMap<Integer,Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater = null;
    private int resource;
    public CarListAdapter(ArrayList<String> list,Context context){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    private void initDate(){
        for (int i =0;i<list.size();i++){
            getIsSelected().put(i,false);
        }
    }
    @Override
    public int getCount(){
        return list.size();
    }
    @Override
    public Object  getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_store_car, null);
            holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置list中TextView的显示
        holder.tv.setText(list.get(position));
        // 根据isSelected来设置checkbox的选中状况
        holder.cb.setChecked(getIsSelected().get(position));
        return convertView;

    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        CarListAdapter.isSelected = isSelected;
    }

    public static class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
}
