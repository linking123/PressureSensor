package com.suncreate.pressuresensor.adapter.general;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.bean.carBrand.CarBrand;

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
