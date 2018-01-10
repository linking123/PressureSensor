package com.suncreate.pressuresensor.fragment.general;

import com.suncreate.pressuresensor.fragment.base.BaseListFragment;
import com.suncreate.pressuresensor.interf.OnTabReselectListener;

/**
 * Created by JuQiu
 * on 16/6/6.
 */

public abstract class ShoppingCarlListFragment<T> extends BaseListFragment<T> implements OnTabReselectListener {
    @Override
    public void onTabReselect() {
        mListView.setSelection(0);
        mRefreshLayout.setRefreshing(true);
        onRefreshing();
    }
}
