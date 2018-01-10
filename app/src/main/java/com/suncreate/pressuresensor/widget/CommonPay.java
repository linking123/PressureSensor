package com.suncreate.pressuresensor.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;

import java.math.BigDecimal;

/**
 * Created by chenzhao on 16-12-26.
 */

public class CommonPay {

    public static void showCommonPay(Context context, Double price, Double balance, View.OnClickListener aliPay, View.OnClickListener balancePay, View.OnClickListener wechatPay) {
        Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_common_pay, null);
        TextView tvPrice = (TextView) inflate.findViewById(R.id.tv_price);
        TextView tvBalance = (TextView) inflate.findViewById(R.id.tv_balance);
        RelativeLayout rlItemAlipay = (RelativeLayout) inflate.findViewById(R.id.rl_item_alipay);
        RelativeLayout rlItemBalance = (RelativeLayout) inflate.findViewById(R.id.rl_item_balance);
        RelativeLayout rlItemWechatpay = (RelativeLayout) inflate.findViewById(R.id.rl_item_wechatpay);

        tvPrice.setText(null == price ? "0.00元" : price + "元");

        BigDecimal balanceprice = new BigDecimal(balance);//账户余额
        Double dbbalanceprice=balanceprice.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        tvBalance.setText(null == dbbalanceprice ? "0.00元" : dbbalanceprice + "元");
        rlItemAlipay.setOnClickListener(aliPay);
        rlItemBalance.setOnClickListener(balancePay);
        rlItemWechatpay.setOnClickListener(wechatPay);

        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
