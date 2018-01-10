package com.suncreate.pressuresensor.adapter.general;

import android.widget.RatingBar;
import android.widget.TextView;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.user.Goods;
import com.suncreate.pressuresensor.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class GoodsListAdapter extends BaseListAdapter<Goods> {
    public GoodsListAdapter(Callback callback) {
        super(callback);
    }

    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss:S");
    protected void convert(ViewHolder vh,final Goods item, int position) {
//        System.out.println(item.getGoodsName()+"开始时间--------------"+sdf1.format(new Date()));
        String storeLogo = item.getGoodsPhoto();
        if (!StringUtils.isEmpty(storeLogo)) {
            storeLogo=MonkeyApi.getPartImgUrl(storeLogo);
        }
        vh.setImageForNet(R.id.iv_goods_item_icon,storeLogo, R.drawable.error);

        TextView tv_tech_item_name = vh.getView(R.id.tv_field_item_name1);

        String goods_name =item.getGoodsName() ;
        if (goods_name != null) {
            tv_tech_item_name.setText(goods_name.trim());
        }

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

        TextView tv_price = vh.getView(R.id.tv_price);
        tv_price.setText("￥"+String.valueOf(item.getGoodsCurrentPrice()));

        TextView tv_goods_area = vh.getView(R.id.tv_goods_area);
        tv_goods_area.setText(AppContext.getInstance().getAreaProCityNameByCode(item.getXzqhName()));

        TextView tv_sale_num = vh.getView(R.id.tv_sale_num);

        tv_sale_num.setText("已售" + String.valueOf(item.getGoodsSalenum()));
//        System.out.println(item.getGoodsName()+"分页数据结束时间--------------"+sdf1.format(new Date()));
    }



    protected int getLayoutId(int position, Goods item) {
        return R.layout.fragment_item_goods;
    }

}