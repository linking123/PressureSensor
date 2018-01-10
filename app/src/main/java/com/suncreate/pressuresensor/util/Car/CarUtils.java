package com.suncreate.pressuresensor.util.Car;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.carBrand.CarModel;
import com.suncreate.pressuresensor.util.StringUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by JINWENLIN.
 * On 2016/12/25.
 */

public class CarUtils {

    private static String mCarId; //用户车辆编号
    private static String mCarName; //用户车辆全称 车品牌+车型+排量+年份
    private static String mCarModelId;  //用户车型编号
    private static String mCarBrandId;  //用户车品牌编号

    //公用的设置车标方法
    public static void setCarBrandIcon(Context context, ImageView carbrandImageView, String brandIconId) {
        String iconUrl = "carbrand/" + brandIconId + ".png";

        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(iconUrl);
            Bitmap imap = BitmapFactory.decodeStream(inputStream);
            carbrandImageView.setImageBitmap(imap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置车型选择box,接收来自于页面返回并更改控件，并设置全局默认车辆；
     * 适用于需要保存车辆id，且不需要已登录状态的场景；如首页选择车型
     *
     * @param context                                上下文
     * @param data                                   onActivityResult中data参数
     * @param carId                                  车辆id
     * @param carbrandIcon                           车辆图标 id
     * @param brand_icon                             ImageView 车辆图标布局
     * @param carBrandAndModelAndDisplacementAndYear 车辆名称：大众奥迪-A3-3.0L-2016
     * @param car_brand_text                         TextView 车辆名称显示布局
     * @param tv_choose_change                       TextView 选择或编辑区域
     */
    public static void setCarBrandBox(Context context, Intent data, String carId, String carbrandIcon, ImageView brand_icon, String carBrandAndModelAndDisplacementAndYear, TextView car_brand_text, TextView tv_choose_change) {
        String mCarId = data.getStringExtra(carId);
        //存储全局的车辆ID
//        setDefaultCar(mCarId);
        //车标Id
        String mCarbrandIcon = data.getStringExtra(carbrandIcon);
        //品牌（经销商）+车型+排量+年款
        String mCarBrandAndModelAndDisplacementAndYear = data.getStringExtra(carBrandAndModelAndDisplacementAndYear);

        if (!StringUtils.isEmpty(mCarbrandIcon)) {
            setCarBrandIcon(context, brand_icon, mCarbrandIcon);
        }

        car_brand_text.setText(mCarBrandAndModelAndDisplacementAndYear);
        if (AppContext.getInstance().isLogin()) {
            tv_choose_change.setText("编辑>");
        } else {
            tv_choose_change.setText("请选择>");
        }
    }

    /**
     * 设置车型选择box,接收来自于页面返回并更改控件；
     * 适用于不需要保存车辆id，且需要已登录状态的场景；如发布需求的车型
     *
     * @param context                                上下文
     * @param data                                   onActivityResult中data参数
     * @param carbrandIcon                           车辆图标 id
     * @param brand_icon                             ImageView 车辆图标布局
     * @param carBrandAndModelAndDisplacementAndYear 车辆名称：大众奥迪-A3-3.0L-2016
     * @param car_brand_text                         TextView 车辆名称显示布局
     * @param tv_choose_change                       TextView 选择或编辑区域
     */
    public static void setCarBrandBoxNeedLogin(Context context, Intent data, String carbrandIcon, ImageView brand_icon, String carBrandAndModelAndDisplacementAndYear, TextView car_brand_text, TextView tv_choose_change) {
        //车标Id
        String mCarbrandIcon = data.getStringExtra(carbrandIcon);
        //品牌（经销商）+车型+排量+年款
        String mCarBrandAndModelAndDisplacementAndYear = data.getStringExtra(carBrandAndModelAndDisplacementAndYear);

        if (!StringUtils.isEmpty(mCarbrandIcon) && !StringUtils.isEmpty(mCarBrandAndModelAndDisplacementAndYear)) {
            setCarBrandIcon(context, brand_icon, mCarbrandIcon);
            car_brand_text.setText(mCarBrandAndModelAndDisplacementAndYear);
            tv_choose_change.setText("编辑>");
        }
    }

    /**
     * 首页等页面顶部用到的车型选择,页面加载时初始化控件
     * <p>
     * 只适用于不需要接受广播的fragment
     * </p>
     *
     * @param context          上下文
     * @param car_brand_text   TextView 车辆名称显示布局
     * @param tv_choose_change TextView 选择或编辑区域
     * @param brand_icon       ImageView 车辆图标布局
     */
    public static void initUserCarBox(final Context context, final TextView car_brand_text, final TextView tv_choose_change, final ImageView brand_icon) {
        if (AppContext.getInstance().isLogin()) {
            MonkeyApi.doViewUserCar("1", "", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        ResultBean<CarModel> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<CarModel>>() {
                        }.getType());
                        if (resultBean != null && resultBean.isSuccess()) {
                            CarModel carModel = resultBean.getResult();

                            mCarName = carModel.getCarModelName();
                            car_brand_text.setText(mCarName);
                            tv_choose_change.setText("编辑");

                            String carbrnadIcon = carModel.getBrandIcon();
                            setCarBrandIcon(context, brand_icon, carbrnadIcon);

                            //从icon中截取数字，如bc9，得到品牌编号9。
                            mCarBrandId = getNumbers(carbrnadIcon);

                            //用户车辆编号
                            mCarId = carModel.getId();
                            //用户车型ID
                            mCarModelId = carModel.getCarmodelId();

                        } else {
                            car_brand_text.setText("当前无爱车");
                            tv_choose_change.setText("请选择>");
                        }
                    } catch (Exception e) {
                        onFailure(statusCode, headers, responseString, e);
                    }
                }
            });
        }
    }

    //用户车辆全称 车品牌+车型+排量+年份
    public static String getmCarName() {
        if (!StringUtils.isEmpty(mCarName) && AppContext.getInstance().isLogin()) {
            return mCarName;
        }
        return "";
    }

    //获取用户车辆ID
    public static String getCarId() {
        if (!StringUtils.isEmpty(mCarId) && AppContext.getInstance().isLogin()) {
            return mCarId;
        }
        return "";
    }

    //获取车辆的品牌ID
    public static String getCarBrandId() {
        if (!StringUtils.isEmpty(mCarBrandId) && AppContext.getInstance().isLogin()) {
            return mCarBrandId;
        }
        return "";
    }

    //获取车型ID
    public static String getmCarModelId() {
        if (!StringUtils.isEmpty(mCarModelId) && AppContext.getInstance().isLogin()) {
            return mCarModelId;
        }
        return "";
    }

    /**
     * 设置默认爱车
     *
     * @param defaultCarId      用户车辆编号 ID
     * @param defaultCarBrandId 用户车辆品牌 ID
     * @param defaultCarModelId 用户车辆车型 ID shopping_user_carmodel主键
     */

    public static void setDefaultCar(String defaultCarId, String defaultCarModelId, String defaultCarBrandId) {
        if (!StringUtils.isEmpty(defaultCarId)) {
            AppContext.getInstance().setProperty("user.userCarid", defaultCarId);
        }
        if (!StringUtils.isEmpty(defaultCarModelId)) {
            AppContext.getInstance().setProperty("user.userModelid", defaultCarModelId);
        }
        if (!StringUtils.isEmpty(defaultCarBrandId)) {
            AppContext.getInstance().setProperty("user.userBrandid", defaultCarBrandId);
        }
    }

    //截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

}
