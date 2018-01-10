package com.suncreate.pressuresensor.adapter.general;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.bean.carBrand.AddressAdapter;
import com.suncreate.pressuresensor.bean.user.Address;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AddressListAdapter extends BaseListAdapter<Address> {

    public static final int REQUEST_CODE_DEFAULT_ADDRESS = 1;
    private AddressAdapter mAdapter;
    private LayoutInflater inflater = null;
    private Fragment mFragment;
    public AddressListAdapter(Callback callback) {
        super(callback);
    }


//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            // 导入布局并赋值给convertview
//            convertView = inflater.inflate(R.layout.fragment_item_address, null);
//
//        }
//
//
//        //点击item中的编辑按钮
//        com.suncreate.pressuresensor.util.ViewHolder.get(convertView, R.id.tv_arrow_right2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Address address = ((Address) mAdapter.getItem(position));
//                if (address != null) {
//                    Bundle b = new Bundle();
//                    b.putSerializable("address", address);
//                    UIHelper.showSimpleBackForResult((mFragment,1, SimpleBackPage.ADDRESS_DETAIL, b);
//                }
//            });
//        }
//    }

    @Override
    protected void convert(ViewHolder vh, Address item, int position) {
        //    id	long	收货地址编号1
//    address_addtime	long	添加时间
//    address_status	int	删除标识：锁定1，正常01
//    address_info	string	详细地址1
//    address_mobile	string	手机号1
//    address_telephone	string	电话1
//    address_truename	string	真实姓名1
//    address_zip	string	邮编1
//    area_name	long	区域地址
//    user_id	long	会员编号
//    daddress	int	1 代表默认  0为一般（一个用户只有一个默认地址）1
        ImageView address_check = vh.getView(R.id.address_check);
        if (Integer.parseInt(item.getDaddress()) == 0) {
            address_check.setVisibility(View.GONE);
        } else {
            address_check.setVisibility(View.VISIBLE);

        }
        TextView address_trueName1 = vh.getView(R.id.address_trueName1);
        String address_truename = item.getAddressTruename();
        //  if (!StringUtils.isEmpty(address_truename)) {
        address_trueName1.setText(address_truename.trim());
        //  }
        TextView address_mobile1 = vh.getView(R.id.address_mobile1);
        String address_mobile = item.getAddressMobile();
        //  if (!StringUtils.isEmpty(address_mobile)) {
        address_mobile1.setText(address_mobile.trim());
        // }

        TextView address_info1 = vh.getView(R.id.address_info1);
        String address_info = item.getAddressInfo();

        address_info1.setText(address_info.trim());
//        Button aButton = vh.getView(R.id.tv_arrow_right2);
//        aButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Address address = ((Address) mAdapter.getItem(m
//        });


//        com.suncreate.pressuresensor.util.ViewHolder.get(, R.id.tv_arrow_right2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Address address = ((Address) mAdapter.getItem(position));
//                if (address != null) {
//                    Bundle b = new Bundle();
//                    b.putSerializable("address", address);
//                    UIHelper.showSimpleBackForResult((mFragment,1, SimpleBackPage.ADDRESS_DETAIL, b);
//                }
//            });
//        }


    }

    @Override
    protected int getLayoutId(int position, Address item) {
        return R.layout.fragment_item_address;
    }
}
