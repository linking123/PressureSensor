package com.suncreate.fireiot.adapter.general;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.carBrand.CarBrand;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class CarBrandListAdapter extends BaseListAdapter<CarBrand> {

    public CarBrandListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, CarBrand item, int position) {

    }


    @Override
    protected int getLayoutId(int position, CarBrand item) {
        return R.layout.fragment_item_technician;
    }

}
