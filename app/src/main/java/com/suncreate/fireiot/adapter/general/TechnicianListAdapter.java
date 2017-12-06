package com.suncreate.fireiot.adapter.general;

import android.view.View;
import android.widget.GridView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.user.Technician;
import com.suncreate.fireiot.util.StringUtils;

import java.math.BigDecimal;


/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class TechnicianListAdapter extends BaseListAdapter<Technician> {

    TechCarBrandGridAdapter gridAdapter;

    public TechnicianListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, Technician item, int position) {

        vh.setImageForNet(R.id.iv_tech_item_icon, MonkeyApi.getPartImgUrl(item.getStoreLogoId()), R.drawable.widget_dface);

        String distStr = item.getDist();
        if (!StringUtils.isEmpty(distStr)) {
            Double distDouble = Double.parseDouble(distStr.trim());
            if (distDouble > 1000) {
                distDouble = distDouble / 1000;
                vh.setText(R.id.tv_tech_item_distance, String.format(mCallback.getContext().getString(R.string.str_distance_kilometer), distDouble));
            } else {
                vh.setText(R.id.tv_tech_item_distance, String.format(mCallback.getContext().getString(R.string.str_distance_meter), distDouble));
            }
        } else {
            vh.setText(R.id.tv_tech_item_distance, "--km");
        }

        vh.setText(R.id.tv_field_item_name, item.getStoreName());

        String level = item.getStoreTechnicianLevel();
        TextView techLevel = vh.getView(R.id.btn_tech_item_level);
        if (level.equals("1")) {
            //初级：#82caea 中级：#5cb0d5 高级：#389ac5
            techLevel.setText("初级");
            techLevel.setBackgroundResource(R.drawable.btn_bg_primary);
        } else if (level.equals("2")) {
            techLevel.setText("中级");
            techLevel.setBackgroundResource(R.drawable.btn_bg_middle);
        } else if (level.equals("3")) {
            techLevel.setText("高级");
            techLevel.setBackgroundResource(R.drawable.btn_bg_high);
        }

        vh.setText(R.id.btn_tech_item_year, mCallback.getContext().getString(R.string.str_tech_year, item.getStoreTechnicianYear()));

        Float store_ship_point = Float.parseFloat(StringUtils.isEmpty(item.getStoreShipPoint()) ? "0" : item.getStoreShipPoint());
        Float store_description_point = Float.parseFloat(StringUtils.isEmpty(item.getStoreDescriptionPoint()) ? "0" : item.getStoreDescriptionPoint());
        Float store_service_point = Float.parseFloat(StringUtils.isEmpty(item.getStoreServicePoint()) ? "0" : item.getStoreServicePoint());
        Float sumStore = store_ship_point + store_description_point + store_service_point;
        Float avgStore = sumStore == 0 ? 0 : sumStore / 3;
        // Random ra =new Random();
        //avgStore = ra.nextFloat()*5;
        BigDecimal bd = new BigDecimal(avgStore);
        Float showStore = bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

        //求平均分
        TextView btnTechItemScore = vh.getView(R.id.btn_tech_item_score);
        btnTechItemScore.setText(showStore.toString());
        RatingBar rb_tech_item_rating = vh.getView(R.id.rb_tech_item_rating);
        rb_tech_item_rating.setRating(showStore);

        String store_carmodel = item.getBrandIcon();//专修品牌
        GridView as = vh.getView(R.id.tech_brand_name);
        TextView textView = vh.getView(R.id.tv_carbrand_invisiable);
        if (!StringUtils.isEmpty(store_carmodel)) {
            String[] storeCarmodels;
            if (store_carmodel.contains(",")) {
                storeCarmodels = store_carmodel.split(",");
            } else {
                storeCarmodels = new String[]{store_carmodel};
            }
            gridAdapter = new TechCarBrandGridAdapter(storeCarmodels, mCallback.getContext());
            as.setVisibility(View.VISIBLE);
            as.setAdapter(gridAdapter);
            textView.setVisibility(View.GONE);
        } else {
            as.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutId(int position, Technician item) {
        return R.layout.fragment_item_technician;
    }
}
