package com.suncreate.fireiot.fragment.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.FireiotApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.scan.User;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.StringUtils;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * @author linking
 *         date: 2017/10/24.
 *         description: 修改用户Email
 */
public class MyPersonInfoEditEmailFragment extends BaseFragment {

    //edittext
    @Bind(R.id.et_qq_edit_space)
    EditText mEditText;

    //类型
    String type;

    @Override
    protected void initData() {
        super.initData();
        Bundle b = getArguments();
        type = b.getString("type");
        String myName = b.getString("name");
        String myPhonenum = b.getString("phone");
        String myEmailnum = b.getString("email");
        if ("name".equals(type)) {
            mEditText.setText(myName);
        } else if ("phone".equals(type)) {
            mEditText.setText(myPhonenum);
        } else if ("email".equals(type)) {
            mEditText.setText(myEmailnum);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_person_info_edit;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.submit_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_send:
                if (StringUtils.isEmpty(mEditText.getText().toString())) {
                    mEditText.setError("修改不能为空");
                    mEditText.requestFocus();
                    break;
                }
                showWaitDialog(R.string.progress_submit);
                User user = AppContext.getInstance().getLoginUser();
                String userName = "", userPhone = "", userEmail = "";
                if ("name".equals(type)) {
                    userName = mEditText.getText().toString();
                } else if ("phone".equals(type)) {
                    userPhone = mEditText.getText().toString();
                } else if ("email".equals(type)) {
                    userEmail = mEditText.getText().toString();
                }
                FireiotApi.updateUser(String.valueOf(user.getUserId()), userName, userPhone, userEmail, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("更新失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.showToastShort("更新用户信息成功");
                                //发送更新用户信息的广播
                                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                                if ("name".equals(type)) {
                                    AppContext.getInstance().setProperty("user.name", mEditText.getText().toString());
                                } else if ("phone".equals(type)) {
                                    AppContext.getInstance().setProperty("user.phone", mEditText.getText().toString());
                                } else if ("email".equals(type)) {
                                    AppContext.getInstance().setProperty("user.email", mEditText.getText().toString());
                                }
                                getActivity().finish();
                            } else if (resultBean != null && resultBean.getMessage() != null) {
                                AppContext.showToast(resultBean.getMessage());
                            }
                        } catch (Exception e) {
                            onFailure(statusCode, headers, responseString, e);
                        }
                    }

                    @Override
                    public void onFinish() {
                        hideWaitDialog();
                        super.onFinish();
                    }
                });
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
