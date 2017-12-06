package com.suncreate.fireiot.fragment.master;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.ui.MainActivity;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 角色切换
 */
public class RoleChangeFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.layout1)
    RelativeLayout layout1;
    @Bind(R.id.layout2)
    RelativeLayout layout2;
    @Bind(R.id.layout3)
    RelativeLayout layout3;
    @Bind(R.id.img_role_car_hook)
    ImageView imgRoleCarHook;
    @Bind(R.id.img_role_technician_hook)
    ImageView imgRoleTechnicianHook;
    @Bind(R.id.img_role_repair_hook)
    ImageView imgRoleRepairHook;

    @Override
    protected void initWidget(View root) {
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);

//        String userRole = AppContext.getInstance().getLoginUserExt().getUserRole();
//        String currentRole = AppContext.getInstance().getLoginUserExt().getCurrentRole();
//        if ("1".equals(userRole)) {
//            layout1.setVisibility(View.VISIBLE);
//            layout2.setVisibility(View.GONE);
//            layout3.setVisibility(View.GONE);
//        } else if ("2".equals(userRole)) {
//            layout1.setVisibility(View.VISIBLE);
//            layout2.setVisibility(View.VISIBLE);
//            layout3.setVisibility(View.GONE);
//        } else if ("3".equals(userRole)) {
//            layout1.setVisibility(View.VISIBLE);
//            layout2.setVisibility(View.GONE);
//            layout3.setVisibility(View.VISIBLE);
//        } else {
//            layout1.setVisibility(View.VISIBLE);
//            layout2.setVisibility(View.GONE);
//            layout3.setVisibility(View.GONE);
//        }
//        if ("1".equals(currentRole)) {
//            layout1.setClickable(false);
//            imgRoleCarHook.setVisibility(View.VISIBLE);
//            imgRoleTechnicianHook.setVisibility(View.GONE);
//            imgRoleRepairHook.setVisibility(View.GONE);
//        } else if ("2".equals(currentRole)) {
//            layout2.setClickable(false);
//            imgRoleCarHook.setVisibility(View.GONE);
//            imgRoleTechnicianHook.setVisibility(View.VISIBLE);
//            imgRoleRepairHook.setVisibility(View.GONE);
//        } else if ("3".equals(currentRole)) {
//            layout3.setClickable(false);
//            imgRoleCarHook.setVisibility(View.GONE);
//            imgRoleTechnicianHook.setVisibility(View.GONE);
//            imgRoleRepairHook.setVisibility(View.VISIBLE);
//        } else {
            layout1.setClickable(false);
            imgRoleCarHook.setVisibility(View.VISIBLE);
            imgRoleTechnicianHook.setVisibility(View.GONE);
            imgRoleRepairHook.setVisibility(View.GONE);
        }

//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_role_change;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        int crtRole = AppContext.getInstance().getCurrentRole();
        switch (id) {
            case R.id.layout1:
                MonkeyApi.changeRole("1", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("切换角色失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.getInstance().setProperty("user.currentRole", "1");
                                AppManager.getAppManager().finishActivity(MainActivity.class);
                                getActivity().finish();
                                Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.layout2:
                MonkeyApi.changeRole("2", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("切换角色失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.getInstance().setProperty("user.currentRole", "2");
                                AppManager.getAppManager().finishActivity(MainActivity.class);
                                getActivity().finish();
                                Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                break;
            case R.id.layout3:
                MonkeyApi.changeRole("3", new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("切换角色失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.getInstance().setProperty("user.currentRole", "3");
                                AppManager.getAppManager().finishActivity(MainActivity.class);
                                getActivity().finish();
                                Intent intent = new Intent(AppContext.getInstance(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                break;
        }
    }
}
