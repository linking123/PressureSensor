package com.suncreate.fireiot.adapter.general;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.carBrand.CarManager;
import com.suncreate.fireiot.bean.user.User;
import com.suncreate.fireiot.fragment.me.MyCarManagerFragment;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CarmanagerListAdapter extends BaseAdapter {

    private List<CarManager> mList;
    private Fragment mFragment;
    private Context context;
    private LayoutInflater inflater = null;

    public CarmanagerListAdapter(List<CarManager> list, Context context, Fragment fragment) {
        this.context = context;
        this.mList = list;
        inflater = LayoutInflater.from(context);
        mFragment = fragment;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public List<CarManager> getmList() {
        return mList;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_car_manager, null);
        }
        final CarManager thisCar = mList.get(position);

        //车标
        ImageView carbrand = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.car_brand);
        final String brandIconId = thisCar.getBrandIcon();
        if (!StringUtils.isEmpty(brandIconId)) {
            CarUtils.setCarBrandIcon(context, carbrand, brandIconId);
        }

        //车品牌+车型+排量+年份
        final String carbrandDetail = thisCar.getCarmodelName();
        //车品牌名称
        TextView carbrand_name = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.tv_car_brand_name);
        String carbrandName = "";
        if (!StringUtils.isEmpty(carbrandDetail)) {
            carbrandName = carbrandDetail.split(" ")[0];
        }
        if (!StringUtils.isEmpty(carbrandName)) {
            carbrand_name.setText(carbrandName);
        }

        //车牌号
        TextView carbrand_num = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.tv_car_num);
        String carbrandNum = thisCar.getPlate();
        if (!StringUtils.isEmpty(carbrandNum)) {
            carbrand_num.setText(carbrandNum);
        }

        //车品牌+车型+排量+年份
        TextView tv_car_brand_detail = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.tv_car_brand_detail);
        if (!StringUtils.isEmpty(carbrandDetail)) {
            tv_car_brand_detail.setText(carbrandDetail);
        }

        //默认爱车
        final CheckBox cb_select_as_lovecar = com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.cb_select_as_lovecar);
        String mIsDefaultLovecar = thisCar.getIsDefault();
        if ("1".equals(mIsDefaultLovecar)) {
            cb_select_as_lovecar.setChecked(true);
        } else {
            cb_select_as_lovecar.setChecked(false);
        }
        //更换默认爱车
        cb_select_as_lovecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_select_as_lovecar.isChecked()) {

                    final String carmodelId = thisCar.getCarmodelId();
                    MonkeyApi.doEditCar(thisCar.getId(), carmodelId, thisCar.getPlate(), thisCar.getEngineNumber(),
                            thisCar.getChassisNumber(), thisCar.getMileage(), thisCar.getRoadTime(), "1", new TextHttpResponseHandler() {
                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                    //把默认车辆存起来
//                                    User user = AppContext.getInstance().getLoginUserExt();
//                                    user.setUserBrandid(Long.parseLong(carmodelId));

                                    for (int i = 0; i < mList.size(); i++) {
                                        if ("1".equals(mList.get(i).getIsDefault())) {
                                            mList.get(i).setIsDefault("0");
                                        }
                                        if (thisCar.getId().equals(mList.get(i).getId())) {
                                            mList.get(i).setIsDefault("1");
                                            String brandIcon = thisCar.getBrandIcon();
                                            String carBrandId = CarUtils.getNumbers(brandIcon);
                                            CarUtils.setDefaultCar(thisCar.getId(), carmodelId, carBrandId);
                                        }
                                    }
                                    notifyDataSetChanged();

                                    //通知全局更改车型，如首页
                                    Intent intent = new Intent();
                                    intent.putExtra("mCarFromBroadcase", carbrandDetail);
                                    intent.putExtra("carbrnadIconFromBroadcase", brandIconId);
                                    intent.setAction(Constants.INTENT_ACTION_DEFAULT_CAR);
                                    context.sendBroadcast(intent);

                                }
                            });
                } else {
                    cb_select_as_lovecar.setChecked(true);
                    AppContext.showToastShort("该车已设为默认爱车");
                }
            }
        });

        //点击item中的编辑按钮
        com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("carId", thisCar.getId());
                b.putString("isDefault", thisCar.getIsDefault());
                UIHelper.showSimpleBackForResult(mFragment, MyCarManagerFragment.REQUEST_CODE_EDIT_CAR, SimpleBackPage.MY_CAR_MANAGER_EDIT, b);
            }
        });

        //点击item中的删除按钮
        final CarManager carManager = mList.get(position);
        com.suncreate.fireiot.util.ViewHolder.get(convertView, R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionDel(context, carManager, position);
            }
        });

        return convertView;
    }

    private void optionDel(Context context, final CarManager carManager, final int position) {
        DialogHelp.getConfirmDialog(context, "确定删除吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MonkeyApi.doDelCar(carManager.getId(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        mList.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    }
                });
            }
        }).show();
    }
}