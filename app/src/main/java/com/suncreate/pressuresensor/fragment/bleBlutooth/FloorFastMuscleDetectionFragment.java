package com.suncreate.pressuresensor.fragment.bleBlutooth;

import android.os.Bundle;
import android.view.View;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.widget.CardiographView;
import com.suncreate.pressuresensor.widget.PathView;

import butterknife.Bind;

/**
 * 盆底检测-快肌检测
 *
 * @author linking
 *         created on 2018/5/5 22:31
 */
public class FloorFastMuscleDetectionFragment extends BaseFragment implements View.OnClickListener {
    protected static final String TAG = FloorFastMuscleDetectionFragment.class.getSimpleName();

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
