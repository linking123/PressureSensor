package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.widget.CardiographView;
import com.suncreate.pressuresensor.widget.PathView;

import butterknife.Bind;

/**
 * 盆底检测
 *
 * @author linking
 *         created on 2018/5/5 22:31
 */
public class FloorDetectionFragment extends BaseFragment implements View.OnClickListener {
    protected static final String TAG = FloorDetectionFragment.class.getSimpleName();

    @Bind(R.id.cgv_card_view)
    CardiographView cardiographView;
    @Bind(R.id.pv_path_view)
    PathView pathView;

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
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
//        btn_moderate.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
//            case R.id.btn_moderate:
//
//                break;
            default:
                break;
        }
    }
}
