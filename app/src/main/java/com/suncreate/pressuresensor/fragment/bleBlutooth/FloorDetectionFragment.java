package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * 盆底检测
 *
 * @author linking
 *         created on 2018/5/5 22:31
 */
public class FloorDetectionFragment extends BaseFragment {
    protected static final String TAG = FloorDetectionFragment.class.getSimpleName();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ps_floor_detection;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    public void initData() {
    }

    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_register:
                break;
            default:
                break;
        }
    }
}
