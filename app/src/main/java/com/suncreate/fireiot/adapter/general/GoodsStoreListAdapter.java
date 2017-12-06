package com.suncreate.fireiot.adapter.general;

import android.widget.RatingBar;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.user.GoodsStore;
import com.suncreate.fireiot.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by Panzhaoxuan on 2016/12/26.
 * desc:
 */
public class GoodsStoreListAdapter extends BaseListAdapter<GoodsStore> {


    public GoodsStoreListAdapter(Callback callback) {
        super(callback);
    }


    protected void convert(ViewHolder vh, final GoodsStore item, int position) {
        //店铺logo
        String storeLogo = item.getStoreLogoId();
        if (!StringUtils.isEmpty(storeLogo)) {
            storeLogo = MonkeyApi.getPartImgUrl(storeLogo);
        }
        vh.setImageForNet(R.id.iv_store_item_icon, storeLogo, R.drawable.widget_dface);
        //店铺名称
        TextView tv_tech_item_name = vh.getView(R.id.tv_store_item_name);

        String goods_name = item.getStoreName();
        if (goods_name != null) {
            tv_tech_item_name.setText(goods_name.trim());
        }


        //店铺评分
        RatingBar rb_tech_item_rating = vh.getView(R.id.rb_tech_item_rating);
        Double dp = 5.0;
        Double sp = 5.0;
        Double pp = 5.0;
        if (!StringUtils.isEmpty(item.getStoreDescriptionPoint())) {
            dp = Double.valueOf(item.getStoreDescriptionPoint());
        }
        if (!StringUtils.isEmpty(item.getStoreServicePoint())) {
            sp = Double.valueOf(item.getStoreServicePoint());
        }
        if (!StringUtils.isEmpty(item.getStoreShipPoint())) {
            pp = Double.valueOf(item.getStoreShipPoint());
        }
        Double point = (dp + sp + pp) / 3;
        BigDecimal b = new BigDecimal(point);
        point = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        rb_tech_item_rating.setRating(Float.valueOf(point.toString()));
        TextView btn_tech_item_score = vh.getView(R.id.btn_tech_item_score);
        btn_tech_item_score.setText(point.toString());
        //店铺地址
        TextView tv_goods_area = vh.getView(R.id.tv_goods_area);
        tv_goods_area.setText(item.getStoreAddress());
        //店铺联系电话
        TextView tv_goods_phone = vh.getView(R.id.tv_goods_phone);
        tv_goods_phone.setText(item.getStoreTelephone());

    }


    protected int getLayoutId(int position, GoodsStore item) {
        return R.layout.fragment_item_goods_store;
    }
}