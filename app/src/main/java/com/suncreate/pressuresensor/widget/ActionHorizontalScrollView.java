package com.suncreate.pressuresensor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by Macintosh on 10/2/16.
 */

public class ActionHorizontalScrollView extends HorizontalScrollView {
    public ActionHorizontalScrollView(Context context) {
        super(context);
    }

    public ActionHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    通知父层ViewGroup，你不能截获
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

}
