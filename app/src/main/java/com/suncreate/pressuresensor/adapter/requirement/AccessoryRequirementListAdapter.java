package com.suncreate.pressuresensor.adapter.requirement;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.require.Requirement;
import com.suncreate.pressuresensor.util.DatePro;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.StringUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class AccessoryRequirementListAdapter extends BaseListAdapter<Requirement> {

    public AccessoryRequirementListAdapter(Callback callback) {
        super(callback);
    }

    private int actionPosition = 0;

    public void setActionPosition(int actionPosition) {
        this.actionPosition = actionPosition;
    }

    private int itemPosition = 0;

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    @Override
    protected void convert(ViewHolder vh, Requirement item, final int position) {
        final String orderState = item.getOrderState();
        final String demandId = item.getId();
        if (null != item) {
            vh.setText(R.id.tv_accessory_requirement_time, DatePro.formatDay(StringUtils.toLong(item.getDemandAddtime()), "yyyy-MM-dd HH:mm"));
            switch (orderState) {
                case "1":
                    vh.setTextColor(R.id.tv_accessory_requirement_state, ContextCompat.getColor(mCallback.getContext(), R.color.orange_700));
                    vh.setText(R.id.tv_accessory_requirement_state, "新发布");
                    break;
                case "2":
                    vh.setTextColor(R.id.tv_accessory_requirement_state, ContextCompat.getColor(mCallback.getContext(), R.color.day_colorPrimary));
                    vh.setText(R.id.tv_accessory_requirement_state, "已报价");
                    break;
//                case "3":
//                    vh.setTextColor(R.id.tv_accessory_requirement_state, ContextCompat.getColor(mCallback.getContext(), R.color.day_colorPrimary));
//                    vh.setText(R.id.tv_accessory_requirement_state, "已确认");
//                    break;
                default:
                    vh.setTextColor(R.id.tv_accessory_requirement_state, ContextCompat.getColor(mCallback.getContext(), R.color.gray));
                    vh.setText(R.id.tv_accessory_requirement_state, "未  知");
                    break;
            }
            //TODO 配件类
            vh.setText(R.id.tv_requirement_desc, item.getDemandDesc());
            vh.setText(R.id.tv_car_model, item.getCarbrandName());
            vh.setText(R.id.tv_master_location, item.getDemandAddress());
            vh.setOnClick(R.id.btn_operate, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemPosition(position);
                    // TODO: 17-1-4 哪些状态可以取消
                    DialogHelp.getConfirmDialog(mCallback.getContext(), "提示", "确定取消该订单?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MonkeyApi.deleteDemand(demandId, updateStatusHandler);
                        }
                    }).show();

                }
            });
        }
    }

    protected TextHttpResponseHandler updateStatusHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作失败!");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    getDatas().remove(itemPosition);
                    notifyDataSetChanged();
                    AppContext.showToast("操作成功!");
                } else {
                    AppContext.showToast("操作失败!");
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };


    @Override
    protected int getLayoutId(int position, Requirement item) {
        return R.layout.fragment_item_requirement_accessory;
    }

}
