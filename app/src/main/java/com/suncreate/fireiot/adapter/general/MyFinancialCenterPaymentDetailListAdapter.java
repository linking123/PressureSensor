package com.suncreate.fireiot.adapter.general;

import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.user.Pay;
import com.suncreate.fireiot.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收支明细列表adapter
 */
public class MyFinancialCenterPaymentDetailListAdapter extends BaseListAdapter<Pay> {

    public MyFinancialCenterPaymentDetailListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, Pay item, int position) {

        //consumeCost 消费标识(0支出1收入)，即一级类型
        String consumeCost = item.getConsumeCost();
        //明细类型，收支一级类型下的二级类型
        //明细类型(0充值，1提现，2退款，3服务订单费，4工位订单费，5配件订单费)
        TextView tvPayType = vh.getView(R.id.tv_pay_type);
        String mPayType = item.getPayType();
        if (!StringUtils.isEmpty(mPayType)) {
            switch (mPayType) {
                case "0":
                    tvPayType.setText("充值");
                    break;
                case "1":
                    tvPayType.setText("提现");
                    break;
                case "2":
                    tvPayType.setText("退款");
                    break;
                case "3":
                    tvPayType.setText("服务订单费");
                    break;
                case "4":
                    tvPayType.setText("工位订单费");
                    break;
                case "5":
                    tvPayType.setText("配件订单费");
                    break;
                case "6":
                    tvPayType.setText("管理员充值");
                    break;
            }
        }

        //发生时间
        TextView tvTime = vh.getView(R.id.tv_time);
        String mAddtime = item.getAddtime();
        if (!mAddtime.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long demand_addtimeL = Long.valueOf(mAddtime);
            String dateTime = sdf.format(new Date(demand_addtimeL));
            tvTime.setText(dateTime);
        }

        //金额
        TextView tvAmount = vh.getView(R.id.tv_amount);
        String mAmount = item.getAmount();
        if (!mAmount.isEmpty()) {
            if (consumeCost.equals("0")) {
                tvAmount.setText(mAmount);
            } else if (consumeCost.equals("1")) {
                String amountStr = "+ " + mAmount;
                tvAmount.setText(amountStr);
            }
        }

    }

    @Override
    protected int getLayoutId(int position, Pay item) {
        return R.layout.fragment_item_financial_center_in_out;
    }
}
