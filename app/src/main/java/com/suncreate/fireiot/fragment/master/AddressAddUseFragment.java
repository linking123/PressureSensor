package com.suncreate.fireiot.fragment.master;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Address;
import com.suncreate.fireiot.util.AreaHelper;
import com.suncreate.fireiot.util.StringUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 地址
 * <p>
 * desc
 */
public class AddressAddUseFragment extends BaseDetailFragment<Address> implements View.OnClickListener {
    @Bind(R.id.layout1)
    RelativeLayout layout1;

    @Bind(R.id.user_name)
    EditText user_name;

    @Bind(R.id.user_phone)
    EditText user_phone;


    @Bind(R.id.user_area)
    TextView user_area;


    @Bind(R.id.user_address)
    EditText user_address;

    @Bind(R.id.btn_save_address_sure)
    Button btn_save_address_sure;

    OptionsPickerView addrOptions;

    private Address address;
    private String mAreaCode;
    private String userArea;
    @Override
    public void initView(View view) {
        addrOptions = AreaHelper.chooseAddress(addrOptions, this.getContext());
        layout1.setOnClickListener(this);
        addrOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                AreaHelper.onOptionsSelect(options1, option2, options3, user_area);
                userArea = user_area.getText().toString();
                if (!TextUtils.isEmpty(userArea)) {
                    String[] s = userArea.split(" ");
                    mAreaCode = AppContext.getInstance().getAreaCodeByName(s[s.length - 1], s[s.length - 2]);
                    return;
                }
            }
        });
        btn_save_address_sure.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_add_use;
    }


    @Override
    public void initData() {

    }
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }
    @Override
    protected String getCacheKey() {
        return "addressDetailuse_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getAddressItem("", 0L, mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(Address detail) {
        super.executeOnLoadDataSuccess(detail);
    }

    @Override
    protected java.lang.reflect.Type getType() {
        return new TypeToken<ResultBean<PageBean<Address>>>() {
        }.getType();
    }


    protected void executeOnLoadDataError() {
        AppContext.showToast("");
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout1:
                addrOptions.show();
                break;
            case R.id.btn_save_address_sure:
                address=new Address();
                address.setAddressInfo(userArea+" "+user_address.getText().toString());
                address.setAddressTruename(StringUtils.isEmpty(user_name.getText().toString()) ? "" : user_name.getText().toString());
                address.setAddressMobile(StringUtils.isEmpty(user_phone.getText().toString()) ? "" : user_phone.getText().toString());
                address.setAreaId(mAreaCode);
                address.setDaddress("1");
                showWaitDialog();
                MonkeyApi.getAddressAdd(address.getAddressInfo(),address.getAddressMobile(), "",
                        address.getAddressTruename(),"", address.getAreaId(),address.getDaddress(), mAddressHeandler);
                break;
            default:
                break;
        }
    }

    protected TextHttpResponseHandler mAddressHeandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("选取收货地址失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Address> resultBean = AppContext.createGson().fromJson(responseString, getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Intent intent = new Intent();
                    intent.putExtra("id",mId);
                    intent.putExtra("address",address.getAddressInfo());
                    intent.putExtra("phone",address.getAddressMobile());
                    intent.putExtra("name",address.getAddressTruename());
                    intent.putExtra("area",address.getAreaId());
                    getActivity().setResult(AddressSelectFragment.REQUEST_CODE_DEFAULT_ADDRESS, intent);
                    getActivity().finish();
                } else {
                    AppContext.showToast("选取收货地址失败");
                }
            } catch (Exception e) {
                AppContext.showToast(e.toString());
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };
}
