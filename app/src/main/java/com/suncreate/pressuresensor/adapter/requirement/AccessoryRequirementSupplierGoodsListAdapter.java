package com.suncreate.pressuresensor.adapter.requirement;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.bean.require.AccessoryRequireDetailStoreGoods;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AccessoryRequirementSupplierGoodsListAdapter extends BaseListAdapter<AccessoryRequireDetailStoreGoods> {

    public AccessoryRequirementSupplierGoodsListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, final AccessoryRequireDetailStoreGoods item, int position) {
        AccessoryRequireDetailStoreGoods goods = getDatas().get(position);
        vh.setText(R.id.tv_item_goods_name, item.getGoodsName());
        vh.setText(R.id.tv_item_goods_type, item.getGcName());
        final TextView tvItemGoodsCount = (TextView) vh.getView(R.id.ll_item_count).findViewById(R.id.tv_item_goods_count1);
        tvItemGoodsCount.setText(String.valueOf(item.getPartsCount()));
//        vh.setText(R.id.tv_item_goods_count1, item.getPartsCount());
        vh.setText(R.id.tv_item_goods_price, String.format(mCallback.getContext().getString(R.string.txt_price_rmb), item.getGoodsPrice()));

        final CheckBox cbItemGoods = vh.getView(R.id.cb_item_goods);
        cbItemGoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AccessoryRequireDetailStoreGoods ardsg = (AccessoryRequireDetailStoreGoods) cbItemGoods.getTag();
                ardsg.setCkecked(isChecked);
                mItemOnCheckListener.noticeCheck(isChecked);
            }
        });
        cbItemGoods.setTag(goods);
        cbItemGoods.setChecked(goods.isCkecked());

        vh.setOnClick(R.id.iv_item_count_minus, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(tvItemGoodsCount.getText().toString());
                if (count > 1) {
                    count--;
                    tvItemGoodsCount.setText(count.toString());
                    item.setPartsCount(count);
                }
            }
        });
        vh.setOnClick(R.id.iv_item_count_add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(tvItemGoodsCount.getText().toString());
                if (count < 99) {
                    count++;
                    tvItemGoodsCount.setText(count.toString());
                    item.setPartsCount(count);
                }
            }
        });

    }

    @Override
    protected int getLayoutId(int position, AccessoryRequireDetailStoreGoods item) {
        return R.layout.fragment_item_requirement_supplier_goods;
    }

}
