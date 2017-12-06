package com.suncreate.fireiot.adapter.general;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.ViewHolder;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.bean.user.ShoppingStore;
import com.suncreate.fireiot.util.ViewUtils;
import com.suncreate.fireiot.widget.togglebutton.ToggleButton;

import java.util.List;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class OrderFillStoreListAdapter extends BaseListAdapter<ShoppingStore> {

    //总金额
    private double sumprice = 0.0;
    //所有选中id
    private String ids="";
    public OrderFillStoreListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(final ViewHolder vh, final ShoppingStore item, final int position) {
        TextView store_name=vh.getView(R.id.title);
        ToggleButton user_default=vh.getView(R.id.user_default);
        final TextView tv_invoice_choose=vh.getView(R.id.tv_invoice_choose);
        final TextView tv_invoice=vh.getView(R.id.tv_invoice);
        final EditText order_invoice=vh.getView(R.id.order_invoice);
        String storename = item.getStoreName();
        ListView lv=vh.getView(R.id.listView);

        List<ShoppingCar> items=item.getItems();
        ViewUtils.setListViewHeightBasedOnChildren(lv);
        user_default.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    tv_invoice_choose.setVisibility(View.GONE);
                    tv_invoice.setVisibility(View.VISIBLE);
                    order_invoice.setVisibility(View.VISIBLE);
                } else {
                    tv_invoice_choose.setVisibility(View.VISIBLE);
                    tv_invoice.setVisibility(View.GONE);
                    order_invoice.setVisibility(View.GONE);
                    order_invoice.setText("");
                }
            }
        });
    }
    @Override
    protected int getLayoutId(int position, ShoppingStore item) {
        return R.layout.fragment_item_shoppingcar_store;
    }



}
