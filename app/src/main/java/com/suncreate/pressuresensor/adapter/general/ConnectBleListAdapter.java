package com.suncreate.pressuresensor.adapter.general;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * desc: 蓝牙列表 adapter
 */
public class ConnectBleListAdapter extends BaseAdapter {

    private List<BluetoothDevice> items;
    private Context context;
    private LayoutInflater layoutInflater = null;

    public ConnectBleListAdapter(List<BluetoothDevice> list, Context context) {
        this.items = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public List<BluetoothDevice> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        BluetoothDevice ble = items.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.fragment_item_connect_ble, null);
            viewHolder.imageView = convertView.findViewById(R.id.civ_connect_ble_img);
            viewHolder.bleName = convertView.findViewById(R.id.tv_connect_ble_name);
            viewHolder.status = convertView.findViewById(R.id.ble_connect_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(R.drawable.technology_blutooth);
        viewHolder.bleName.setText(ble.getName());

        int status = ble.getBondState();
        if (0 == status) {
            viewHolder.status.setText(ble.getBondState());
        } else if (1 == status) {
            viewHolder.status.setText(ble.getBondState());
        } else {
            viewHolder.status.setText(ble.getBondState());
        }

        return convertView;
    }

    final static class ViewHolder {
        CircleImageView imageView;
        TextView bleName;
        TextView status;
    }

}