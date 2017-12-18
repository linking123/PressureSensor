package com.suncreate.pressuresensor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.BaseFragment;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.UpdateManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author linking
 */
public class AboutFireIotFragment extends BaseFragment {

    @Bind(R.id.tv_version)
    TextView mTvVersionStatus;

    @Bind(R.id.tv_version_name)
    TextView mTvVersionName;

    @Bind(R.id.rl_about)
    RelativeLayout mAbout;

    @Bind(R.id.rl_user_protocal)
    RelativeLayout mUserProtocal;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.rl_check_update).setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mUserProtocal.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTvVersionName.setText("V " + TDevice.getVersionName());
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.rl_check_update:
                onClickUpdate();
                break;
//            case R.id.rl_about:
//                UIHelper.showSimpleBack(getContext(), SimpleBackPage.INTRODUCTION_OF_fireiot);
////                UIHelper.openBrowser(getContext(), "https://manage.fireiot.com/web/admin/ShoppingArticle/showH5?id=196615");
//                break;
//            case R.id.rl_user_protocal:
//                UIHelper.showSimpleBack(getContext(), SimpleBackPage.USER_PROTOCAL);
//                break;
//            case R.id.rl_statement:
//                UIHelper.showSimpleBack(getContext(), SimpleBackPage.STATEMENT_OF_fireiot);
//                break;
            default:
                break;
        }
    }

    private void onClickUpdate() {
        new UpdateManager(getActivity(), true).checkUpdate();
    }
}
