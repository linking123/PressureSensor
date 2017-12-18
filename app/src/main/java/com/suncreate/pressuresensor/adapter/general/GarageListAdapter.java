package com.suncreate.pressuresensor.adapter.general;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.user.Garage;
import com.suncreate.pressuresensor.util.StringUtils;

import java.math.BigDecimal;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class GarageListAdapter extends BaseListAdapter<Garage> {

    TechCarBrandGridAdapter gridAdapter;

    public GarageListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, Garage item, int position) {
        String storeLogo = item.getStoreLogoId();
        if (!StringUtils.isEmpty(storeLogo)) {
            vh.setImageForNet(R.id.iv_tech_item_icon, MonkeyApi.getPartImgUrl(storeLogo), R.drawable.widget_dface);
        }

        //距离
        String distStr = item.getDist();
        if (!StringUtils.isEmpty(distStr)) {
            Double distDouble = Double.parseDouble(distStr.trim());
            if (distDouble > 1000) {
                distDouble = distDouble/1000;
                vh.setText(R.id.tv_tech_item_distance, String.format(mCallback.getContext().getString(R.string.str_distance_kilometer), distDouble));
            } else {
                vh.setText(R.id.tv_tech_item_distance, String.format(mCallback.getContext().getString(R.string.str_distance_meter), distDouble));
            }
        } else {
            vh.setText(R.id.tv_tech_item_distance, "--");
        }

        TextView tv_tech_item_name = vh.getView(R.id.tv_field_item_name);
        String userRealname = item.getStoreName();
        if (!StringUtils.isEmpty(userRealname)) {
            tv_tech_item_name.setText(userRealname.trim());
        }

        ImageView btn_tech_item_level = vh.getView(R.id.btn_tech_item_level);
        String level = item.getStoreTechnicianLevel();
        if (level.equals("1")) {
            btn_tech_item_level.setImageResource(R.drawable.one);
        } else if (level.equals("2")) {
            btn_tech_item_level.setImageResource(R.drawable.two);
        } else if (level.equals("3")) {
            btn_tech_item_level.setImageResource(R.drawable.three);
        } else {
            btn_tech_item_level.setVisibility(View.INVISIBLE);
        }

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
        GridView as = vh.getView(R.id.garage_brand_name);
        TextView textView = vh.getView(R.id.tv_carbrand_invisiable);
        if (!StringUtils.isEmpty(store_carmodel)) {
            String[] storeCarmodels;
            if (store_carmodel.contains(",")) {
                storeCarmodels = store_carmodel.split(",");

            }else {
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
    protected int getLayoutId(int position, Garage item) {
        return R.layout.fragment_item_garage;
    }
}
