package com.suncreate.pressuresensor.fragment.requirement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Address;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.fragment.master.OrderFillDetailFragment;
import com.suncreate.pressuresensor.meidia.TweetPicturesPreviewer;
import com.suncreate.pressuresensor.util.Car.CarUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

import static com.suncreate.pressuresensor.R.id.layout1;
import static org.kymjs.kjframe.utils.ImageUtils.getRandomFileName;
import static org.kymjs.kjframe.utils.ImageUtils.reduce;

/**
 * 配件发布第一页
 * <p>
 * desc
 */
public class AccessoryRequirementPublishFragment extends BaseFragment implements View.OnClickListener {

    //照片容器
    @Bind(R.id.recycler_images)
    TweetPicturesPreviewer mLayImages;
    //照相机
    @Bind(R.id.upload_img)
    ImageButton mUpload_img;
    //下一步
    @Bind(R.id.btn_next_step)
    Button btn_next_step;
    //从车辆管理的默认车型带过来车型,没有的话重新选择
    //选过车型，显示现有车型，也可选择更改
    @Bind(R.id.car_brand_model_series_box)
    LinearLayout carBrandBox;
    @Bind(R.id.brand_icon)
    ImageView brand_icon;
    @Bind(R.id.car_brand_text)
    TextView car_brand_text;
    @Bind(R.id.tv_choose_change)
    TextView tv_choose_change;
    @Bind(layout1)
    RelativeLayout mLayout1;
    @Bind(R.id.order_name)
    TextView order_name;
    @Bind(R.id.order_phone)
    TextView order_phone;
    @Bind(R.id.order_location)
    TextView order_location;
    //服务描述
    @Bind(R.id.et_demand_desc)
    EditText et_demand_desc;

    private String mBrandId;
    private String mCarModelId;
    private String mAreaCode;
    private String mDemandAddress;
    private String mDemandDesc;
    boolean flag;

    private File demandImg1;
    private File demandImg2;
    private File demandImg3;

    //新建广播，接收默认车辆改变的广播
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_DEFAULT_CAR:
                    CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_DEFAULT_CAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    protected void initWidget(View root) {

        //如果是没有选择过车型，就显示选择爱车，否则显示已选择过的车型
        CarUtils.initUserCarBox(getContext(), car_brand_text, tv_choose_change, brand_icon);
        MonkeyApi.getAddressItem("1", 0L, addressHandler);

        carBrandBox.setOnClickListener(this);
        mLayout1.setOnClickListener(this);
        mUpload_img.setOnClickListener(this);
        btn_next_step.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_requirement_accessory_publish;
    }

    @Override
    protected void initData() {
        mBrandId = CarUtils.getCarBrandId();
        mCarModelId = CarUtils.getmCarModelId();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.car_brand_model_series_box:
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_CAR_MANAGER);
                break;
            case R.id.layout1:
                if (flag == true) {
                    UIHelper.showSimpleBackForResult(this, OrderFillDetailFragment.REQUEST_CODE_DEFAULT_ADDRESS, SimpleBackPage.ADDRESS_SELECT);
                } else {
                    UIHelper.showSimpleBackForResult(this, OrderFillDetailFragment.REQUEST_CODE_DEFAULT_ADDRESS, SimpleBackPage.ADDRESS_ADD_USE);
                }
                break;
            case R.id.upload_img:
                mLayImages.onLoadMoreClick();
                break;
            case R.id.btn_next_step:
                String demandDesc = et_demand_desc.getText().toString();
                String orderLocation = order_location.getText().toString();
                if (TextUtils.isEmpty(mCarModelId)) {
                    AppContext.showToast("请选择车型");
                    return;
                }
                if (TextUtils.isEmpty(orderLocation)) {
                    AppContext.showToast("请选择收货地址");
                    return;
                }
                if (TextUtils.isEmpty(demandDesc)) {
                    AppContext.showToast("请填写采购描述！");
                    return;
                }
                try {
                    //图片上传
                    int imgsCount = mLayImages.getChildCount();
                    if (imgsCount == 0) {
                        Toast.makeText(getContext(), "请至少上传一张图片！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String[] imgPaths = mLayImages.getPaths();
                    int len = imgPaths.length;
                    if (len == 1) {
                        demandImg1 = getSmallImageFile(getContext(), imgPaths[0], 800, 480, true);
                    } else if (len == 2) {
                        demandImg1 = getSmallImageFile(getContext(), imgPaths[0], 800, 480, true);
                        demandImg2 = getSmallImageFile(getContext(), imgPaths[1], 800, 480, true);
                    } else if (len == 3) {
                        demandImg1 = getSmallImageFile(getContext(), imgPaths[0], 800, 480, true);
                        demandImg2 = getSmallImageFile(getContext(), imgPaths[1], 800, 480, true);
                        demandImg3 = getSmallImageFile(getContext(), imgPaths[2], 800, 480, true);
                    }
                    MonkeyApi.createProductsDemand(demandDesc, orderLocation, mAreaCode, mCarModelId,
                            AppContext.lon, AppContext.lat, demandImg1, demandImg2, demandImg3, mSubmitHandler);
                } catch (FileNotFoundException e) {
                    AppContext.showToast("所选图片不存在，请重新选择！");
                    return;
                }
                break;
            default:
                break;
        }
    }

    /**
     * TODO kjframe中的方法如果路径图片不存在会奔溃，在此重写，已提交pull request
     * @param cxt
     * @param filePath
     * @param width
     * @param height
     * @param isAdjust
     * @return
     */
    public static File getSmallImageFile(Context cxt, String filePath, int width, int height,
                                         boolean isAdjust) {
        BufferedOutputStream outputStream = null;
        File file = null;
        try {
            Bitmap bitmap = reduce(BitmapFactory.decodeFile(filePath), width, height, isAdjust);
            file = new File(getRandomFileName(cxt.getCacheDir().getPath()));
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    protected TextHttpResponseHandler mSubmitHandler = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToastShort("操作失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                JSONObject jsStr = (JSONObject) JSONObject.parse(responseString);
                if ("1".equals(jsStr.get("code").toString())) {
                    Bundle b = new Bundle();
                    b.putString("brandId", mBrandId);
                    b.putString("demandId", jsStr.get("result").toString());
                    UIHelper.showSimpleBackForResult(AccessoryRequirementPublishFragment.this, 1, SimpleBackPage.ACCESSORY_REQUIREMENT_PUBLISH_CHOOSE_ACCESSORY, b);
                } else {
                    AppContext.showToastShort("操作失败！");
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

    protected TextHttpResponseHandler addressHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("收货地址加载失败~");
            flag = false;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Address> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<Address>>() {
                }.getType());
                if (null != resultBean && resultBean.getCode() == 1) {
                    if (null != resultBean.getResult().getAddressTruename()) {
                        order_name.setText(resultBean.getResult().getAddressTruename());
                    }
                    if (null != resultBean.getResult().getAddressMobile()) {
                        order_phone.setText(resultBean.getResult().getAddressMobile());
                    }
                    if (null != resultBean.getResult().getAddressInfo()) {
                        order_location.setText(resultBean.getResult().getAddressInfo());
                        mAreaCode = resultBean.getResult().getAreaId();
                    }
                    flag = true;
                } else {
                    order_phone.setText("暂无收货信息");
                    order_location.setText("请添加收货地址");
                    flag = false;
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == OrderFillDetailFragment.REQUEST_CODE_DEFAULT_ADDRESS) {
            String address = data.getStringExtra("address");
            String phone = data.getStringExtra("phone");
            String name = data.getStringExtra("name");
            mAreaCode = data.getStringExtra("area");
            order_location.setText(address);
            order_phone.setText(phone);
            order_name.setText(name);
            flag = true;
        } else if (2 == resultCode) {
            getActivity().setResult(2, data);
            getActivity().finish();
        }
    }
}
