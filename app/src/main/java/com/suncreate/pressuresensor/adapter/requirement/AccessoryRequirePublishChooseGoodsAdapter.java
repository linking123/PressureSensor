package com.suncreate.pressuresensor.adapter.requirement;

import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.bean.user.GoodsRequire;
import com.suncreate.pressuresensor.util.DialogHelp;

/**
 * Created by chenzhao on 17-1-5.
 */
public class AccessoryRequirePublishChooseGoodsAdapter extends BaseListAdapter<GoodsRequire> {

    private int itemPosition = 0;

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public AccessoryRequirePublishChooseGoodsAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(final ViewHolder vh, final GoodsRequire item, final int position) {
        final TextView tvItemGoodsCount = vh.getView(R.id.tv_item_goods_count);
        final TextView tvItemGoodsType = vh.getView(R.id.tv_item_goods_type);
        final TextView tvItemGoodsName = vh.getView(R.id.tv_item_goods_name);

//        final int goodsCount = vh.getView(R.id.tv_item_goods_count)
        vh.setText(R.id.tv_item_goods_type, item.getGcName());
        vh.setText(R.id.tv_item_goods_name, item.getPartsName());
        vh.setOnClick(R.id.iv_item_count_minus, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer count = Integer.valueOf(tvItemGoodsCount.getText().toString());
                if (count > 1) {
                    count--;
                    tvItemGoodsCount.setText(count.toString());
                    item.setPartsCount(String.valueOf(count));
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
                    item.setPartsCount(String.valueOf(count));
                }
            }
        });
        vh.setOnClick(R.id.ib_item_goods_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemPosition(position);
                DialogHelp.getConfirmDialog(mCallback.getContext(), "提示", "确认删除该商品?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDatas().remove(itemPosition);
                        notifyDataSetChanged();
                    }
                }).show();
            }
        });
        vh.setOnClick(R.id.tv_item_goods_type, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelp.getTitleMessageDialog(mCallback.getContext(), "配件类别", tvItemGoodsType.getText().toString(), null).show();
            }
        });
        vh.setOnClick(R.id.tv_item_goods_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHelp.getTitleMessageDialog(mCallback.getContext(), "配件名称", tvItemGoodsName.getText().toString(), null).show();
            }
        });

    }

    @Override
    protected int getLayoutId(int position, GoodsRequire item) {
        return R.layout.item_publish_require_accessory;
    }

}
