package com.suncreate.fireiot.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.util.ViewHolder;

import java.util.List;

/**
 * 通用的ViewHolder
 * <p/>
 * Created by 火蚁 on 15/4/8.
 */
@SuppressWarnings("unused")
public  class GoodsOrderItemListAdapter extends BaseAdapter {
    private List<ShoppingCar> list;
    private GoodsOrderItemListAdapter adapter;
    private Context context;
    private LayoutInflater inflater = null;
    private int resource;
    public GoodsOrderItemListAdapter(List<ShoppingCar> list, Context context){
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        if (convertView == null) {
            // 导入布局并赋值给convertview
//            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_item_order_fill_store, parent, false);
            convertView = inflater.inflate(R.layout.fragment_item_order_fill_goods,null);
        }
        ShoppingCar shoppingCar=list.get(position);
        TextView goods_name = ViewHolder.get(convertView, R.id.goods_name);
        goods_name.setText(shoppingCar.getGoodsName());
        TextView goods_price = ViewHolder.get(convertView, R.id.goods_price);
        goods_price.setText("￥"+shoppingCar.getGoodscartPrice());
        TextView goods_count = ViewHolder.get(convertView, R.id.goods_count);
        goods_count.setText("x"+shoppingCar.getGoodscartCount());
        return convertView;
    }
}
