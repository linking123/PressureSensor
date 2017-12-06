package com.suncreate.fireiot.adapter.general;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.user.FreezeBalanceOrder;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class FreezeBalanceOrderListAdapter extends BaseListAdapter<FreezeBalanceOrder> {

//    "id": 1384,
//    "orderFreezeTime": 1483519826198,
//    "orderClass": 2,
//    "orderFreezeBlance": 0.04,
//    "orderId": "order20161228164139307"

    public FreezeBalanceOrderListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, FreezeBalanceOrder item, int position) {

        //冻结时间
        String freezeTime = item.getOrderFreezeTime();
        if (freezeTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long demand_addtimeL = Long.valueOf(freezeTime);
            String dateTime = sdf.format(new Date(demand_addtimeL));
            vh.setText(R.id.tv_order_freeze_time, dateTime);
        }

        //冻结金额
        vh.setText(R.id.tv_order_freeze_fee, "￥ " + item.getOrderFreezeBlance() + "元");

        //订单类型
        //0配件订单 1抢单订单 2服务订单 3询价订单 4场地订单
        String orderClass = item.getOrderClass();
        if (!orderClass.isEmpty()) {
            String orderClassStr = "";
            switch (orderClass) {
                case "0":
                    orderClassStr = "配件订单";
                    break;
                case "1":
                    orderClassStr = "抢单订单";
                    break;
                case "2":
                    orderClassStr = "服务订单";
                    break;
                case "3":
                    orderClassStr = "询价订单";
                    break;
                case "4":
                    orderClassStr = "场地订单";
                    break;
            }
            vh.setText(R.id.tv_order_type, orderClassStr);
        }
    }

    @Override
    protected int getLayoutId(int position, FreezeBalanceOrder item) {
        return R.layout.fragment_item_freeze_balance_order;
    }
}
