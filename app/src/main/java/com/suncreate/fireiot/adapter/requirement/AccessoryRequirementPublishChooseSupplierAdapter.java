package com.suncreate.fireiot.adapter.requirement;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.user.GoodsStore;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AccessoryRequirementPublishChooseSupplierAdapter extends BaseListAdapter<GoodsStore> {

    public AccessoryRequirementPublishChooseSupplierAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(final ViewHolder vh, final GoodsStore item, int position) {
        GoodsStore store = getDatas().get(position);
        vh.setText(R.id.tv_item_store_name, item.getStoreName());
        vh.setText(R.id.tv_item_store_address, item.getStoreAddress());
        final CheckBox cbItemStore = vh.getView(R.id.cb_item_store);
        cbItemStore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GoodsStore gs = (GoodsStore) cbItemStore.getTag();
                gs.setCkecked(isChecked);
                mItemOnCheckListener.noticeCheck(isChecked);
            }
        });
        cbItemStore.setTag(store);
        cbItemStore.setChecked(store.isCkecked());
    }

    @Override
    protected int getLayoutId(int position, GoodsStore item) {
        return R.layout.fragment_item_requirement_accessory_choose_supplier;
    }

}
