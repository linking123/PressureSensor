package com.suncreate.pressuresensor.adapter.general;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qindachang.bluetoothle.BluetoothLe;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.util.StringUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;


/**
 * desc: 蓝牙列表 adapter
 */
public class ConnectBleListAdapter extends BaseAdapter {

    private List<BluetoothDevice> items;
    private Context context;
    private LayoutInflater layoutInflater = null;
    private BluetoothLe mBlueToothLe;

    public ConnectBleListAdapter(List<BluetoothDevice> list, Context context, BluetoothLe blueToothLe) {
        this.items = list;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.mBlueToothLe = blueToothLe;
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
        final BluetoothDevice ble = items.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.fragment_item_connect_ble, null);
            viewHolder.imageView = convertView.findViewById(R.id.civ_connect_ble_img);
            viewHolder.bleName = convertView.findViewById(R.id.tv_connect_ble_name);
            viewHolder.status = convertView.findViewById(R.id.ble_connect_status);
            viewHolder.btnConn = convertView.findViewById(R.id.fb_mode_introduce);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setImageResource(R.drawable.technology_blutooth);
        viewHolder.bleName.setText(StringUtils.isEmpty(ble.getName()) ? "未知名称" : ble.getName());

        int status = ble.getBondState();
        //BOND_NONE = 10;BOND_BONDING = 11;BOND_BONDED = 12
        if (10 == status) {
            viewHolder.status.setText("未连接");
        } else if (11 == status) {
            viewHolder.status.setText("连接中");
        } else if (12 == status) {
            viewHolder.status.setText("已连接");
        }

        viewHolder.btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlueToothLe.startConnect(true, ble);
            }
        });

        return convertView;
    }

    final static class ViewHolder {
        CircleImageView imageView;
        TextView bleName;
        TextView status;
        FButton btnConn;
    }

}