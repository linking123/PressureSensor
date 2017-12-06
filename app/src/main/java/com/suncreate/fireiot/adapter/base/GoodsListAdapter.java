package com.suncreate.fireiot.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.util.ViewHolder;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 通用的ViewHolder
 * <p/>
 * Created by 火蚁 on 15/4/8.
 */
@SuppressWarnings("unused")
public class GoodsListAdapter extends BaseAdapter {
    private List<ShoppingCar> list;
    private GoodsListAdapter adapter;
    private Context context;
    private LayoutInflater inflater = null;
    private int resource;

    public GoodsListAdapter(List<ShoppingCar> list, Context context) {
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
            convertView = inflater.inflate(R.layout.fragment_item_order_fill_goods, null);
        }
        ShoppingCar shoppingCar = list.get(position);
        final ImageView imageView = ViewHolder.get(convertView, R.id.goods_id);
        try {
            String id = shoppingCar.getGoodsPhoto();
            MonkeyApi.getPartImg(id, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (responseBody != null) {
                        Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                       if(null!=imap) {
                           imageView.setImageBitmap(imap);
                       }else {
                           imageView.setImageResource(R.drawable.error);
                       }
                    }else {
                        imageView.setImageResource(R.drawable.error);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    imageView.setImageResource(R.drawable.error);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView goods_name = ViewHolder.get(convertView, R.id.goods_name);
        goods_name.setText(shoppingCar.getGoodsName());
        TextView goods_price = ViewHolder.get(convertView, R.id.goods_price);
        goods_price.setText("￥" + shoppingCar.getGoodscartPrice());
        TextView goods_count = ViewHolder.get(convertView, R.id.goods_count);
        goods_count.setText("x" + shoppingCar.getGoodscartCount());
        return convertView;
    }
}
