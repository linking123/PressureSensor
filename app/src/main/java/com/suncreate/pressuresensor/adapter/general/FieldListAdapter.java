package com.suncreate.pressuresensor.adapter.general;

import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.user.Garage;
import com.suncreate.pressuresensor.util.StringUtils;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class FieldListAdapter extends BaseListAdapter<Garage> {

    public FieldListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, Garage item, int position) {
        String storeLogo = item.getStoreLogoId();
        if (!StringUtils.isEmpty(storeLogo)) {
            vh.setImageForNet(R.id.iv_field_item_icon, MonkeyApi.getPartImgUrl(storeLogo), R.drawable.widget_dface);
        }
        TextView tv_field_item_name = vh.getView(R.id.tv_field_item_name);
        String fieldName = item.getStoreName();
        if (!StringUtils.isEmpty(fieldName)) {
            tv_field_item_name.setText(fieldName.trim());
        }

        //距离
        String distStr = item.getDist();
        if (!StringUtils.isEmpty(distStr)) {
            Double distDouble = Double.parseDouble(distStr.trim());
            if (distDouble > 1000) {
                distDouble = distDouble / 1000;
                vh.setText(R.id.tv_field_item_distance, String.format(mCallback.getContext().getString(R.string.str_distance_kilometer), distDouble));
            } else {
                vh.setText(R.id.tv_field_item_distance, String.format(mCallback.getContext().getString(R.string.str_distance_meter), distDouble));
            }
        } else {
            vh.setText(R.id.tv_field_item_distance, "--");
        }

        TextView tv_address = vh.getView(R.id.tv_address1);
        String fieldAddress = item.getStoreAddress();
        if (!StringUtils.isEmpty(fieldAddress)) {
            tv_address.setText(fieldAddress.trim());
        }

        //工位数
        TextView field_num = vh.getView(R.id.tv_field_item_station);
        String fieldNum = item.getStationNum();
        //工位数；1-3是1；4-7是2；7个以上是3
        if (!StringUtils.isEmpty(fieldNum)) {
            if ("1".equals(fieldNum)) {
                field_num.setText("工位数：1~3个");
            } else if ("2".equals(fieldNum)) {
                field_num.setText("工位数：4~6个");
            } else if ("3".equals(fieldNum)) {
                field_num.setText("工位数：7个以上");
            }
        } else {
            field_num.setText("工位数: --");
        }
    }

    @Override
    protected int getLayoutId(int position, Garage item) {
        return R.layout.fragment_item_field;
    }

}
