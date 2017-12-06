package com.suncreate.fireiot.fragment.general;

import com.suncreate.fireiot.fragment.base.BaseListFragment;
import com.suncreate.fireiot.interf.OnTabReselectListener;

/**
 * Created by JuQiu
 * on 16/6/6.
 */

public abstract class GeneralListFragment<T> extends BaseListFragment<T> implements OnTabReselectListener {
    @Override
    public void onTabReselect() {
        mListView.setSelection(0);
       mRefreshLayout.setRefreshing(true);
       onRefreshing();
    }
}
