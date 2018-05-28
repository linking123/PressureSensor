package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.suncreate.pressuresensor.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by linking on 1/19/18.
 */
public class BluetoothActivity extends Activity{

    private Button button2;
    private ListView list;
    private ArrayAdapter mArrayAdapter;
    private BluetoothReceiver bluetoothReceiver;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private ArrayList<String> PairedMaclist;
    private TextView textView1;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetoothlist);


        list = (ListView)findViewById(R.id.listView);
        textView1 = findViewById(R.id.textView);

        //创建一个IntentFilter对象，将其action指定为BluetoothDevice.ACTION_FOUND,查找蓝牙
        IntentFilter intentFileter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothReceiver = new BluetoothReceiver();
        //注册广播接收器
        registerReceiver(bluetoothReceiver, intentFileter);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        button2 = findViewById(R.id.scanButton);
        button2.setOnClickListener(new ScanButtonListener());
        list.setOnItemClickListener(new ListViewListener());

        openBluetooth();

        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1);
        PairedMaclist=new ArrayList<String>();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bluetoothReceiver);
        super.onDestroy();
    }

    private void openBluetooth() {
        //bluetoothAdapter.enable();
        if(bluetoothAdapter != null){
            if(!bluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            }
            //得到所有已经被对的蓝牙适配器对象
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            if(devices.size() > 0){
                for(Iterator<BluetoothDevice> iterator = devices.iterator(); iterator.hasNext();){
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
                    //得到远程蓝牙设备的地址
                    System.out.println(bluetoothDevice.getAddress());
                }
            }
        }
        else {
            //System.out.println("没有蓝牙设备");
            Toast.makeText(BluetoothActivity.this, "没有蓝牙设备",Toast.LENGTH_SHORT).show();
        }
    }

    class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //得到intent里面的信息
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(BluetoothActivity.this, "发现蓝牙设备",Toast.LENGTH_SHORT).show();
                //System.out.println(device.getAddress());
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                PairedMaclist.add(device.getAddress());
                list.setAdapter(mArrayAdapter);
            }
        }

    }

    class ScanButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            mArrayAdapter.clear();

            bluetoothAdapter.startDiscovery();
            Toast.makeText(BluetoothActivity.this, "开始扫描",Toast.LENGTH_SHORT).show();
        }

    }

    class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long id) {
            String str = PairedMaclist.get(position);
            //System.out.println("Str-------------" + str);
//            Intent intent = new Intent(BluetoothActivity.this,BluetoothWaveform.class);
//            intent.putExtra("BluetoothMAC",str);
//            startActivity(intent);
        }

    }
}