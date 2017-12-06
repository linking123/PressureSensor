package com.suncreate.fireiot.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by chenzhao on 16-12-23.
 * 主题属性工具类
 */
public class ThemeAttrUtil {

    public static int getThemeAttr(Context context, int resId) {
        TypedValue tv = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resId, tv, true);
        return tv.data;
    }

    public static int getThemeAttrRes(Context context, int resId) {
        TypedValue tv = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resId, tv, true);
        return tv.resourceId;
    }

}
