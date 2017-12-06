package com.suncreate.fireiot.adapter.general;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.user.Address;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AddressSelectListAdapter extends BaseListAdapter<Address> {

    public AddressSelectListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, Address item, int position) {
        ImageView address_check = vh.getView(R.id.address_check);
        if (Integer.parseInt(item.getDaddress()) == 0) {
            address_check.setVisibility(View.GONE);
        } else {
            address_check.setVisibility(View.VISIBLE);

        }
        vh.setText(R.id.address_trueName, item.getAddressTruename());
        vh.setText(R.id.address_mobile, item.getAddressMobile());
        vh.setText(R.id.address_info, item.getAddressInfo());
        vh.setText(R.id.area_id,item.getAreaId());
        TextView area_id=vh.getView(R.id.area_id);
        area_id.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId(int position, Address item) {
        return R.layout.fragment_item_address_select;
    }
}
