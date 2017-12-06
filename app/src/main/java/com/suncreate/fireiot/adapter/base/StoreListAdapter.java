package com.suncreate.fireiot.adapter.base;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.bean.user.ShoppingCar;
import com.suncreate.fireiot.bean.user.ShoppingStore;
import com.suncreate.fireiot.util.ViewUtils;
import com.suncreate.fireiot.widget.togglebutton.ToggleButton;

import java.util.List;

import static com.suncreate.fireiot.R.id.order_invoice;
import static com.suncreate.fireiot.R.id.tv_invoice;
import static com.suncreate.fireiot.R.id.tv_invoice_choose;
import static com.suncreate.fireiot.R.id.tv_master_technician_service;
import static com.suncreate.fireiot.R.id.user_default;

/**
 * 通用的ViewHolder
 * <p/>
 * Created by 火蚁 on 15/4/8.
 */
@SuppressWarnings("unused")
public class StoreListAdapter extends BaseAdapter {
    private List<ShoppingStore> list;
    private List<ShoppingCar> items;
    private GoodsListAdapter adapter;
    private Context context;
    private LayoutInflater inflater = null;
    private int resource;

    public StoreListAdapter(List<ShoppingStore> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        final ShoppingStore store = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fragment_item_order_fill_store, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.order_goods_price_show);
            viewHolder.tvServicePrice = (TextView) convertView.findViewById(R.id.order_service_price_show);
            viewHolder.tvTransPrice = (TextView) convertView.findViewById(R.id.order_trans_price_show);
            viewHolder.tvTruePrice = (TextView) convertView.findViewById(R.id.order_amount_price_show);
            viewHolder.listView = (ListView) convertView.findViewById(R.id.listView);
            viewHolder.tbUserDefault = (ToggleButton) convertView.findViewById(user_default);
            viewHolder.tvMasterTechnicianService = (TextView) convertView.findViewById(tv_master_technician_service);
            viewHolder.tvInvoiceChoose = (TextView) convertView.findViewById(tv_invoice_choose);
            viewHolder.tvInvoice = (TextView) convertView.findViewById(tv_invoice);
            viewHolder.etOrderInvoice = (EditText) convertView.findViewById(order_invoice);
            viewHolder.tbUserDefault.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    ShoppingStore info = (ShoppingStore) viewHolder.tbUserDefault.getTag();
                    if (on) {
                        info.setChecks(true);
                        viewHolder.tvMasterTechnicianService.setVisibility(View.GONE);
                        viewHolder.tvInvoiceChoose.setVisibility(View.GONE);
                        viewHolder.tvInvoice.setVisibility(View.VISIBLE);
                        viewHolder.etOrderInvoice.setVisibility(View.VISIBLE);

                    } else {
                        info.setChecks(false);
                        viewHolder.tvMasterTechnicianService.setVisibility(View.VISIBLE);
                        viewHolder.tvInvoiceChoose.setVisibility(View.VISIBLE);
                        viewHolder.tvInvoice.setVisibility(View.GONE);
                        viewHolder.etOrderInvoice.setVisibility(View.GONE);
                        viewHolder.etOrderInvoice.setText(null);
                        info.setOrderInvoice(null);
                    }
                }
            });
            viewHolder.etOrderInvoice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ShoppingStore info = (ShoppingStore) viewHolder.etOrderInvoice.getTag();
                    if (s.length() > 0) {
                        info.setOrderInvoice(viewHolder.etOrderInvoice.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            convertView.setTag(viewHolder);
            viewHolder.tbUserDefault.setTag(store);
            viewHolder.etOrderInvoice.setTag(store);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tbUserDefault.setTag(store);
            viewHolder.etOrderInvoice.setTag(store);
        }

        //店铺名称
        viewHolder.tvTitle.setText(store.getStoreName());
        //商品金额
        viewHolder.tvGoodsPrice.setText(store.getGoodsPrices());
        //平台服务费
        viewHolder.tvServicePrice.setText(store.getServicePrices());
        //运费
        viewHolder.tvTransPrice.setText(store.getTransPrices());
        //实付款
        viewHolder.tvTruePrice.setText(store.getTruePrices());

        if (null != store.getChecks()) {
            if (store.getChecks()) {
                viewHolder.tbUserDefault.setToggleOn();
                viewHolder.tvMasterTechnicianService.setVisibility(View.GONE);
                viewHolder.tvInvoiceChoose.setVisibility(View.GONE);
                viewHolder.tvInvoice.setVisibility(View.VISIBLE);
                viewHolder.etOrderInvoice.setVisibility(View.VISIBLE);
                viewHolder.etOrderInvoice.setText(store.getOrderInvoice());
            } else {
                viewHolder.tbUserDefault.setToggleOff();
                viewHolder.tvMasterTechnicianService.setVisibility(View.VISIBLE);
                viewHolder.tvInvoiceChoose.setVisibility(View.VISIBLE);
                viewHolder.tvInvoice.setVisibility(View.GONE);
                viewHolder.etOrderInvoice.setVisibility(View.GONE);
                viewHolder.etOrderInvoice.setText(null);
            }
        } else {
            viewHolder.tbUserDefault.setToggleOff();
            viewHolder.tvMasterTechnicianService.setVisibility(View.VISIBLE);
            viewHolder.tvInvoiceChoose.setVisibility(View.VISIBLE);
            viewHolder.tvInvoice.setVisibility(View.GONE);
            viewHolder.etOrderInvoice.setVisibility(View.GONE);
            viewHolder.etOrderInvoice.setText(null);
        }

        items = store.getItems();
        adapter = new GoodsListAdapter(items, context);
        viewHolder.listView.setAdapter(adapter);
        //防editText焦点丢失
        viewHolder.listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        ViewUtils.setListViewHeightBasedOnChildren(viewHolder.listView);
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvGoodsPrice;
        TextView tvServicePrice;
        TextView tvTransPrice;
        TextView tvTruePrice;
        ListView listView;
        ToggleButton tbUserDefault;
        TextView tvMasterTechnicianService;
        TextView tvInvoiceChoose;
        TextView tvInvoice;
        EditText etOrderInvoice;
    }
}
