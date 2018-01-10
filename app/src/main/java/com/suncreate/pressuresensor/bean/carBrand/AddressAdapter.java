package com.suncreate.pressuresensor.bean.carBrand;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.user.Address;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    private List<Address> list = null;
    private Context mContext;
    private Fragment aFragment;

    public AddressAdapter(Context mContext, List<Address> list, Fragment fragment) {
        this.mContext = mContext;
        this.list = list;
        aFragment = fragment;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<Address> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public List<Address> getDatas() {
        return this.list;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder vh;
        Address mContent = list.get(position);
        if (view == null) {
            vh = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_address, null);
            vh.address_check = (ImageView) view.findViewById(R.id.address_check);
            vh.address_trueName1 = (TextView) view.findViewById(R.id.address_trueName1);
            vh.address_mobile1 = (TextView) view.findViewById(R.id.address_mobile1);
            vh.address_info1 = (TextView) view.findViewById(R.id.address_info1);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        if (null != mContent) {
            vh.address_trueName1.setText(mContent.getAddressTruename());
            if ("1".equals(mContent.getDaddress())) {
                vh.address_check.setVisibility(View.VISIBLE);
            } else {
                vh.address_check.setVisibility(View.INVISIBLE);
            }
            vh.address_mobile1.setText(mContent.getAddressMobile());
            vh.address_info1.setText(mContent.getAddressInfo());
        }
        return view;
    }

    final static class ViewHolder {
        TextView address_trueName1;
        TextView address_mobile1;
        ImageView address_check;
        TextView address_info1;
    }
}