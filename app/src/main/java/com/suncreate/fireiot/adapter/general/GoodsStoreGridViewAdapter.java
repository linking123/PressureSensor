package com.suncreate.fireiot.adapter.general;


import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.user.Goods;
import com.suncreate.fireiot.util.StringUtils;

/**
 * Created by JINWENLIN.
 * On 2016/12/14.
 */

public class GoodsStoreGridViewAdapter extends BaseListAdapter<Goods> {

    public GoodsStoreGridViewAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, Goods item, int position) {

        //配件照片
        String storeLogo = item.getGoodsPhoto();
        if (!StringUtils.isEmpty(storeLogo)) {
            storeLogo=MonkeyApi.getPartImgUrl(storeLogo);
        }
        vh.setImageForNet(R.id.goods_img,storeLogo, R.drawable.error);

        //配件名称goods_description
        TextView goodsDescription = vh.getView(R.id.goods_description);
        String goodsName = item.getGoodsName();
        if (goodsName != null){
            goodsDescription.setText(goodsName);
        }
        //配件价格goods_price
        TextView goods_price = vh.getView(R.id.goods_price);
        String goodsPrice = item.getGoodsCurrentPrice();
        if (goodsPrice!=null){
            goods_price.setText(goodsPrice);
        }
    }

    @Override
    protected int getLayoutId(int position, Goods item) {
        return R.layout.fragment_goods_supplier_shop_gridview_item;
    }
}
