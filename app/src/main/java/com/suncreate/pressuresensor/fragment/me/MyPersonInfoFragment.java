package com.suncreate.pressuresensor.fragment.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.PressureSensorApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.scan.User;
import com.suncreate.pressuresensor.ui.LoginActivity;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;

import java.lang.reflect.Type;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 个人信息页面
 *
 * @author linking
 */
public class MyPersonInfoFragment extends BaseDetailFragment<com.suncreate.pressuresensor.bean.scan.User> implements View.OnClickListener {

    //整个页面
    @Bind(R.id.ll_my_info_page)
    View mInfoPage;
    //姓名区域
    @Bind(R.id.rl_my_name_box)
    RelativeLayout mMyName;
    @Bind(R.id.tv_my_name_who)
    TextView tv_my_name_who;
    //地址区域box
//    @Bind(R.id.rl_my_area_box)
//    RelativeLayout mAreaBox;
    //地址显示
//    @Bind(R.id.tv_my_address_where)
//    TextView user_area;
    //电话区域
    @Bind(R.id.rl_phone_area)
    RelativeLayout mPhone;
    @Bind(R.id.tv_my_phone_num)
    TextView tv_my_phone_num;
    //邮件区域
    @Bind(R.id.rl_email_box)
    RelativeLayout rl_email_box;
    //邮件显示
    @Bind(R.id.tv_my_email_num)
    TextView tv_my_email_num;

    //地址选择器
//    OptionsPickerView addrOptions;
    private User mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_person_info;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改用户信息广播监听
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_USER_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播接收
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void initView(View view) {
        super.initView(view);

        mMyName.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        rl_email_box.setOnClickListener(this);

        //选择地址
//        addrOptions= AreaHelper.chooseAddress(addrOptions,this.getContext());
//        //区域选择
//        mAreaBox.setOnClickListener(this);
//        addrOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int option2, int options3) {
//                // 返回的分别是三个级别的选中位置
//                String city = AreaHelper.provinceBeanList.get(options1).getPickerViewText();
//
//                // 如果是直辖市或者特别行政区只设置市和区/县
//                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
//                    AreaHelper.address = AreaHelper.provinceBeanList.get(options1).getPickerViewText()
//                            + " " + AreaHelper.districtList.get(options1).get(option2).get(options3);
//                } else {
//                    AreaHelper.address = AreaHelper.provinceBeanList.get(options1).getPickerViewText()
//                            + " " + AreaHelper.cityList.get(options1).get(option2)
//                            + " " + AreaHelper.districtList.get(options1).get(option2).get(options3);
//                }
//                user_area.setText(AreaHelper.address);
//            }
//        });
    }

    @Override
    protected String getCacheKey() {
        return "mypersoninfo" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        mUser = AppContext.getInstance().getLoginUser();
        if (mUser == null) {
            //没有登录
            AppContext.showToastShort("您尚未登录");
            Intent intent;
            intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }

        PressureSensorApi.getUserInfo(String.valueOf(AppContext.getInstance().getLoginUser().getUserId()), mUserInfoHandler);
    }

    private TextHttpResponseHandler mUserInfoHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("获取用户信息失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
            }.getType());
            if (resultBean != null && resultBean.isSuccess()) {
                User u = resultBean.getResult();
                initUser(u);
            }
        }
    };

    private void initUser(User u) {
        String userName = u.getUserName();
        if (!StringUtils.isEmpty(userName)) {
            tv_my_name_who.setText(userName);
        }
        String userRealPhone = u.getUserMobile();
        if (!StringUtils.isEmpty(userRealPhone)) {
            tv_my_phone_num.setText(userRealPhone.trim());
        }

        String userEmail = u.getUserEmail();
        if (!StringUtils.isEmpty(userEmail)) {
            tv_my_email_num.setText(userEmail.trim());
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<User>>() {
        }.getType();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_my_name_box:
                Bundle b = new Bundle();
                b.putString("type", "name");
                b.putString("name", tv_my_name_who.getText().toString());
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_PERSON_INFO_EDIT_NAME, b);
                break;
            case R.id.rl_phone_area:
                Bundle b1 = new Bundle();
                b1.putString("type", "phone");
                b1.putString("phone", tv_my_phone_num.getText().toString());
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_PERSON_INFO_EDIT_PHONE, b1);
                break;
            case R.id.rl_email_box:
                Bundle b2 = new Bundle();
                b2.putString("type", "email");
                b2.putString("email", tv_my_email_num.getText().toString());
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_PERSON_INFO_EDIT_EMAIL, b2);
                break;
//            case R.id.rl_my_area_box:
//                addrOptions.show();
//                break;
//            case R.id.rl_qq_box:
//                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_PERSON_INFO_EDIT_QQ);
//                break;
            default:
                break;
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                //修改用户信息广播
                case Constants.INTENT_ACTION_USER_CHANGE:
                    sendRequestDataForNet();
                    break;
            }
        }
    };

}
