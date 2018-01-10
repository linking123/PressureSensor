package com.suncreate.pressuresensor.adapter.general;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.ShoppingCar;
import com.suncreate.pressuresensor.fragment.master.ShoppingCarFragment;
import com.suncreate.pressuresensor.util.StringUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class ShoppingCarListAdapter extends BaseListAdapter<ShoppingCar> {

    private static Long amount;

    public ShoppingCarListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, final ShoppingCar item, int position) {
        vh.setImageForNet(R.id.goods_image, MonkeyApi.getPartImgUrl(item.getGoodsPhoto()), R.drawable.error);
        TextView goods_name = vh.getView(R.id.goods_name);
        goods_name.setText(item.getGoodsName());
        TextView goods_price = vh.getView(R.id.goods_price);
        goods_price.setText(item.getGoodscartPrice());
        final TextView goods_count = vh.getView(R.id.goods_count);
        final String goodscount = item.getGoodscartCount();
        amount = Long.valueOf(goodscount);
        if (!StringUtils.isEmpty(goodscount)) {
            goods_count.setText(goodscount.trim());
        }
        goods_count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                MonkeyApi.updateGoodsCar(item.getId().toString(), goods_count.getText().toString(), countHandler);
            }
        });
        Button selector_del = vh.getView(R.id.selector_del);
        selector_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                amount = Long.valueOf(item.getGoodscartCount());
                if (amount > 1L) {
                    amount--;
                    goods_count.setText(amount.toString());
                    item.setGoodscartCount(amount.toString());
                    mItemOnCheckListener.noticeCheck(true);
                }
                goods_count.clearFocus();
            }
        });
        Button selector_add = vh.getView(R.id.selector_add);
        selector_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                amount = Long.valueOf(item.getGoodscartCount());
                if (amount < 100L) {
                    amount++;
                    goods_count.setText(amount.toString());
                    item.setGoodscartCount(amount.toString());
                    mItemOnCheckListener.noticeCheck(true);
                }
                goods_count.clearFocus();
            }
        });
        CheckBox goods_id = vh.getView(R.id.goods_id);

        if (null != item.getChecks()) {
            if (item.getChecks() && !goods_id.isChecked()) {
                goods_id.setChecked(true);
            } else if (!item.getChecks() && goods_id.isChecked()) {
                goods_id.setChecked(false);
            }
            ShoppingCarFragment.belowType = "below";
        } else {
            item.setChecks(false);
        }

        goods_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setChecks(isChecked);
                if (!isChecked) {
                    ShoppingCarFragment.topType = "";
                    ShoppingCarFragment.middleType = "";
                }
                ShoppingCarFragment.belowType = "below";
                if (ShoppingCarFragment.belowType.equals("below")) {
                    mItemOnCheckListener.noticeCheck(isChecked);
                    ShoppingCarFragment.belowType = "";
                }
            }
        });
    }

    protected TextHttpResponseHandler countHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("操作失败~");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<ShoppingCar> resultBean = AppContext.createGson().fromJson(responseString, getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                } else {
                    AppContext.showToast("操作失败~");
                }
            } catch (Exception e) {
                AppContext.showToast("操作失败~");
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    protected Type getType() {
        return new TypeToken<ResultBean<ShoppingCar>>() {
        }.getType();
    }

    @Override
    protected int getLayoutId(int position, ShoppingCar item) {
        return R.layout.fragment_item_shoppingcar_goods;
    }
}
