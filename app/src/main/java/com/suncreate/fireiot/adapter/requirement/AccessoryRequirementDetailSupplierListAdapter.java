package com.suncreate.fireiot.adapter.requirement;

import android.support.v4.content.ContextCompat;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.require.AccessoryRequireDetailStore;
import com.suncreate.fireiot.util.DatePro;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AccessoryRequirementDetailSupplierListAdapter extends BaseListAdapter<AccessoryRequireDetailStore> {

    public AccessoryRequirementDetailSupplierListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, AccessoryRequireDetailStore item, int position) {
        vh.setText(R.id.tv_item_store_name, item.getStoreName());
        vh.setText(R.id.tv_item_store_quote_time, DatePro.formatDay(item.getAddTime(), "yyyy-MM-dd HH:mm"));
        switch (item.getStatus()) {
            case "1":
                vh.setTextColor(R.id.tv_item_store_quote_state, ContextCompat.getColor(mCallback.getContext(), R.color.orange_700));
                vh.setText(R.id.tv_item_store_quote_state, "待报价");
                break;
            case "2":
                vh.setTextColor(R.id.tv_item_store_quote_state, ContextCompat.getColor(mCallback.getContext(), R.color.day_colorPrimary));
                vh.setText(R.id.tv_item_store_quote_state, "已报价");
                break;
            default:
                vh.setTextColor(R.id.tv_item_store_quote_state, ContextCompat.getColor(mCallback.getContext(), R.color.gray));
                vh.setText(R.id.tv_item_store_quote_state, "未  知");
                break;
        }
    }

    @Override
    protected int getLayoutId(int position, AccessoryRequireDetailStore item) {
        return R.layout.item_accessory_require_quote_supplier;
    }

}
