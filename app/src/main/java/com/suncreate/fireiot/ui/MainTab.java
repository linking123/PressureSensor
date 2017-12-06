package com.suncreate.fireiot.ui;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.fragment.HomeFragment;
import com.suncreate.fireiot.fragment.MyInformationFragment;
import com.suncreate.fireiot.fragment.msg.MsgContactCustomerFragment;
import com.suncreate.fireiot.viewpagerfragment.RequirementViewPagerFragment;

public enum MainTab {

    HOME(0, R.string.main_tab_name_news, R.drawable.tab_icon_home,
            HomeFragment.class),

    REQUIRE(1, R.string.main_tab_name_tweet, R.drawable.tab_icon_order,
            RequirementViewPagerFragment.class),

    MESSAGE(2, R.string.main_tab_name_explore, R.drawable.tab_icon_msg,
            MsgContactCustomerFragment.class),

    ME(3, R.string.main_tab_name_my, R.drawable.tab_icon_me,
            MyInformationFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
