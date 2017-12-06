package com.suncreate.fireiot.adapter.requirement;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.require.AccessoryRequireDetailStoreGoods;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AccessoryRequirementDetailGoodsListAdapter extends BaseListAdapter<AccessoryRequireDetailStoreGoods> {


    public AccessoryRequirementDetailGoodsListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, AccessoryRequireDetailStoreGoods item, int position) {
        vh.setText(R.id.tv_item_goods_name, item.getPartsName());
        vh.setText(R.id.tv_item_goods_type, String.format(mCallback.getContext().getString(R.string.txt_goods_type), item.getGcName()));
    }

    @Override
    protected int getLayoutId(int position, AccessoryRequireDetailStoreGoods item) {
        return R.layout.item_accessory_require_quote_goods;
    }

}
