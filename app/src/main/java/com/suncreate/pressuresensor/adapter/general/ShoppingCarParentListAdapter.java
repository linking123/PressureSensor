package com.suncreate.pressuresensor.adapter.general;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.ViewHolder;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.bean.user.ShoppingCar;
import com.suncreate.pressuresensor.bean.user.ShoppingStore;
import com.suncreate.pressuresensor.fragment.master.ShoppingCarFragment;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.ViewUtils;

import java.util.List;

/**
 * Created by fei on 2016/5/24.
 * desc:
 */
public class ShoppingCarParentListAdapter extends BaseListAdapter<ShoppingStore> {

    //总金额
    private double sumprice = 0.0;

    //所有选中id
    private String ids = "";

    public ShoppingCarParentListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(final ViewHolder vh, final ShoppingStore item, final int position) {
        TextView store_name = vh.getView(R.id.storename);
        String storename = item.getStoreName();
        if (!StringUtils.isEmpty(storename)) {
            store_name.setText(storename.trim());
        }
        final CheckBox store_id = vh.getView(R.id.storeid);
        //初始化需要将监听设为null，否则将会出现adapter数据乱跳的现象
        store_id.setOnCheckedChangeListener(null);
        ListView lv = vh.getView(R.id.listView);
        List<ShoppingCar> items = item.getItems();
        final BaseListAdapter<ShoppingCar> adapter = new ShoppingCarListAdapter(mCallback);
        lv.setAdapter(adapter);
        if (null == item.getChecks()) {
            item.setChecks(false);
            adapter.addItem(items);
        } else {
            adapter.addItem(items);
            //设置当checkbox状态发生变化，则触发监听
            if (item.getChecks() && !store_id.isChecked()) {
                store_id.setChecked(true);
            } else if (!item.getChecks() && store_id.isChecked()) {
                store_id.setChecked(false);
            }
        }
        //购物车配件点击事件回调
        adapter.setmItemOnCheckListener(new ItemOnCheckListener() {
            @Override
            public void noticeCheck(boolean isChecked) {
                int i = 0, j = 0;
                ids = "";
                //获取配件列表中选中/反选
                for (ShoppingCar sc : adapter.getDatas()) {
                    if (sc.getChecks().equals(true)) {
                        i++;
                    }
                    if (sc.getChecks().equals(false)) {
                        j++;
                    }
                }
                //如果全部选中
                if (adapter.getDatas().size() == i) {
                    isChecked = true;
                    ShoppingCarFragment.middleType = "middle";
                }
                //如果全部反选
                else if (adapter.getDatas().size() == j) {
                    isChecked = false;
                    ShoppingCarFragment.middleType = "middle";
                } else {
                    isChecked = false;
                    ShoppingCarFragment.belowType = "below";
                }
                //店铺checkbox根据配件全选和全部反选状态改变
                store_id.setChecked(isChecked);
                //获取选中配件累计金额
                for (int h = 0; h < adapter.getDatas().size(); h++) {
                    if (adapter.getItem(h).getChecks().equals(true)) {
                        sumprice += Double.valueOf(adapter.getItem(h).getGoodscartPrice()) * Long.valueOf(adapter.getItem(h).getGoodscartCount());
                        ids += adapter.getItem(h).getId() + ",";
                    }
                }
                item.setSumprice(sumprice);
                item.setChecks(isChecked);
                item.setIds(ids);
                ids = "";
                sumprice = 0.00;
                //回调给上级
                mItemOnCheckListener.noticeCheck(isChecked);
            }
        });


        store_id.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //在店铺对象中记录店铺是否选中状态
                if (isChecked) {
                    item.setChecks(true);
                    ShoppingCarFragment.middleType = "middle";
                } else {
                    item.setChecks(false);
                }
                //如果该店铺下配件是全选/反选状态，则更新店铺选择状态
                if (ShoppingCarFragment.middleType.equals("middle")) {
                    if (isChecked) {
                        for (int i = 0; i < adapter.getDatas().size(); i++) {
                            ShoppingCar car = adapter.getDatas().get(i);
                            car.setChecks(true);
                            //获取选中配件累计金额
                            sumprice += Double.valueOf(car.getGoodscartPrice()) * Long.valueOf(car.getGoodscartCount());
                            ids += car.getId() + ",";
                        }
                    } else {
                        for (int i = 0; i < adapter.getDatas().size(); i++) {
                            adapter.getDatas().get(i).setChecks(false);
                        }
                        sumprice = 0.0;
                        ids = "";
                        ShoppingCarFragment.middleType = "";
                        ShoppingCarFragment.topType = "";
                    }
                }
                item.setSumprice(sumprice);
                item.setChecks(isChecked);
                item.setIds(ids);
                adapter.notifyDataSetChanged();
                sumprice = 0.00;
                ids = "";
                mItemOnCheckListener.noticeCheck(isChecked);
            }
        });
        //监听结束前将checkbox状态检查一遍，防止滑动屏幕导致checkbox选中丢失
        if (item.getChecks() && !store_id.isChecked()) {
            store_id.setChecked(true);
        } else if (!item.getChecks() && store_id.isChecked()) {
            store_id.setChecked(false);
        }
        ViewUtils.setListViewHeightBasedOnChildren(lv);
    }

    @Override
    protected int getLayoutId(int position, ShoppingStore item) {
        return R.layout.fragment_item_shoppingcar_store;
    }
}
