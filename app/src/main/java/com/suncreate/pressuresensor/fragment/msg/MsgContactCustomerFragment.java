package com.suncreate.pressuresensor.fragment.msg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.util.ShowContactTell;

import butterknife.Bind;

/**
 * 客服电话页面
 * <p>
 * desc
 */
public class MsgContactCustomerFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.tv_contact_server)
    LinearLayout mContactSever;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initWidget(View root) {
        mContactSever.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_contact_server:
                ShowContactTell.showContact(getContext(), "400-805-5110");
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ImageView iv_search=(ImageView)getActivity().findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
    }
}
