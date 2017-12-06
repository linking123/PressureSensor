package com.suncreate.fireiot.adapter.requirement;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.require.Requirement;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.StringUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.suncreate.fireiot.R.id.catalog;

/**
 * desc: 服务需求列表,每个item含有5条信息（状态更新时间，状态，问题描述，车型，地址）
 */
public class ServiceRequirementListAdater extends BaseListAdapter<Requirement> {

    public ServiceRequirementListAdater(Callback callback) {
        super(callback);
    }

    private int actionPosition = 0;
    private String flag;
    public void setActionPosition(int actionPosition) {
        this.actionPosition = actionPosition;
    }

    @Override
    protected void convert(ViewHolder vh, final Requirement item, final int position) {
        //需求状态
        TextView service_requirement_state = vh.getView(R.id.service_requirement_state);
        String orderState = item.getOrderState();
        flag=item.getOrderState();
        Button btn_cancel = vh.getView(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getOrderState().equals("0")) {
                    DialogHelp.getConfirmDialog(mCallback.getContext(), "确定取消该订单?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            actionPosition=position;
                            flag="2";
                            MonkeyApi.deleteDemand(item.getId(), updateListHandler);
                        }
                    }).show();
                }
            }
        });
        if (!StringUtils.isEmpty(orderState)) {
            if (orderState.equals("0")) {
                service_requirement_state.setText("新发布");
                btn_cancel.setVisibility(View.VISIBLE);
            } else {
                service_requirement_state.setText("已接单");
                btn_cancel.setVisibility(View.GONE);
            }
        }
        //时间
        TextView tv_update_time = vh.getView(R.id.tv_update_time);
        String addTime = item.getDemandAddtime(); //发布时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!StringUtils.isEmpty(addTime)) {
            long addTimeL = Long.valueOf(addTime);
            addTime = sdf.format(new Date(addTimeL));
            tv_update_time.setText(addTime);
        }
        //需求描述
        TextView tv_demand_desc = vh.getView(R.id.tv_demand_desc);
        String demandDesc = item.getDemandDesc();
        if (!StringUtils.isEmpty(demandDesc)) {
            tv_demand_desc.setText(demandDesc);
        }
        //车型
        TextView tv_car_model = vh.getView(R.id.tv_car_model);
        String carbrand_name = item.getCarbrandName();
        if (!StringUtils.isEmpty(carbrand_name)) {
            tv_car_model.setText(carbrand_name);
        }
        //地址
        TextView tv_address = vh.getView(R.id.tv_address);
        String demand_address = item.getDemandAddress();
        if (!StringUtils.isEmpty(demand_address)) {
            tv_address.setText(demand_address);
        }


    }

    protected Type getType(){
        return new TypeToken<ResultBean<Requirement>>(){
        }.getType();
    }

    protected TextHttpResponseHandler updateListHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Requirement> resultBean = AppContext.createGson().fromJson(responseString, getType());
                if (resultBean != null && resultBean.getCode()==1) {
                    if(catalog!=0){
                        getDatas().get(actionPosition).setOrderState(flag);
                        getDatas().remove(actionPosition);
                        notifyDataSetChanged();
                    }else{
                        getDatas().remove(actionPosition);
                        notifyDataSetChanged();
                    }
                    AppContext.showToast("操作成功~");
                } else {
                    AppContext.showToast("操作失败~");
                }
            } catch (Exception e) {
                AppContext.showToast("操作失败~");
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };
    @Override
    protected int getLayoutId(int position, Requirement item) {
        return R.layout.fragment_item_requirement_service;
    }

}
