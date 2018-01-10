package com.suncreate.pressuresensor.fragment.master;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Banner;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.Goods;
import com.suncreate.pressuresensor.bean.user.ShoppingCar;
import com.suncreate.pressuresensor.util.ShowContactTell;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.widget.ViewGoodsHeader;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * p配件详情
 * <p>
 * desc
 */
public class GoodsDetailFragment extends BaseDetailFragment<Goods> implements View.OnClickListener {
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    private static final String NEWS_BANNER = "news_banner";
    @Bind(R.id.tv_googs_supplier_name)
    RelativeLayout mGoodsSupplier;
    @Bind(R.id.btn_shoppingcar)
    Button bShoopingcar;
    @Bind(R.id.ll_contact_phone)
    LinearLayout mLlContactPhone;
    @Bind(R.id.tv_master_technician_year)
    TextView tv_master_technician_year;
    @Bind(R.id.tv_master_good_inventory)
    TextView tv_master_good_inventory;
    @Bind(R.id.tv_master_technician_time)
    TextView tv_master_technician_time;
    @Bind(R.id.tv_master_good_count)
    TextView tv_master_good_count;
    @Bind(R.id.tv_master_technician_location)
    TextView tv_master_technician_location;
    @Bind(R.id.goods_class)
    TextView goods_class;
    @Bind(R.id.goods_brand)
    TextView goods_brand;
    @Bind(R.id.goods_prodmode)
    TextView goods_prodmode;
    @Bind(R.id.goods_spec)
    TextView goods_spec;
    @Bind(R.id.goods_oe)
    TextView goods_oe;
    @Bind(R.id.goods_carmodel)
    TextView goods_carmodel;
    @Bind(R.id.goods_unit)
    TextView goods_unit;
    @Bind(R.id.tvstore)
    TextView tvstore;
    @Bind(R.id.btn_buy)
    Button bBuy;
    @Bind(R.id.imgerror)
    ImageView imgerror;

    public static final String ID = "id";
    public static final String NAME = "name";
    private View inflate;
    private Dialog dialog;
    private EditText normal;
    private Button selector_del;
    private Button selector_add;
    private Button btn_goodscar_sure;
    private Button btn_buy_sure;
    private int amount = 1; //购买数量
    private int goods_storage = 0; //库存
    private Long goods_id; //配件ID
    private String goods_price; //配件价格
    private String goods_name; //配件名
    private List<Bitmap> mpas;
    private String storeid;
    private List<Banner> bannerList;
    String storeTelephone;
    @Bind(R.id.layout_loop)
    LinearLayout mLoopView;
    private ViewGoodsHeader mHeaderView;
    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_detail;
    }

    @Override
    protected String getCacheKey() {
        return "";
    }

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getGoodsDetail(mDetailId, mDetailHandler);
    }

    @Override
    public void initView(View view) {
        mLlContactPhone.setOnClickListener(this);
        mGoodsSupplier.setOnClickListener(this);
        bShoopingcar.setOnClickListener(this);
        bBuy.setOnClickListener(this);
        mHeaderView = new ViewGoodsHeader(getActivity());
        mLoopView.addView(mHeaderView);
    }

    @Override
    protected void executeOnLoadDataSuccess(Goods detail) {
        super.executeOnLoadDataSuccess(detail);
        //商品ID
        goods_id = Long.valueOf(detail.getId());
        //商品名称
        goods_name = detail.getGoodsName();
        String goodsName = detail.getGoodsName();
        if (!StringUtils.isEmpty(goodsName)) {
            tv_master_technician_year.setText(goodsName.trim());
        }
        //库存
        if (!StringUtils.isEmpty(detail.getGoodsInventory())) {
            goods_storage = Integer.valueOf(detail.getGoodsInventory());
            tv_master_good_inventory.setText("库存" + detail.getGoodsInventory());
        }
        //价格
        goods_price = detail.getGoodsCurrentPrice();
        String goodsPrice = "￥" + goods_price + "元";
        if (!StringUtils.isEmpty(goodsPrice)) {
            tv_master_technician_time.setText(goodsPrice.trim());
        }
        //已售数量
        String saleNum = "已售" + detail.getGoodsSalenum();
        if (!StringUtils.isEmpty(saleNum)) {
            tv_master_good_count.setText(saleNum.trim());
        }
        //地址
        String address = StringUtils.getSubString(0, 20, detail.getStoreAddress());
        if (!StringUtils.isEmpty(address)) {
            tv_master_technician_location.setText(address.trim());
        }
        //类别
        String className = detail.getTotalName();
        if (!StringUtils.isEmpty(className)) {
            goods_class.setText(className.trim());
        }
        //品牌
        String brandName = detail.getGoodsbrandName();
        if (!StringUtils.isEmpty(brandName)) {
            goods_brand.setText(brandName.trim());
        }
        //型号
        String goods_mode = detail.getGoodsMode();
        if (!StringUtils.isEmpty(goods_mode)) {
            goods_prodmode.setText(goods_mode.trim());
        }
        //规格
        String goodspec = detail.getGoodsSpec();

        if (!StringUtils.isEmpty(goodspec)) {
            goods_spec.setText(goodspec.trim());
        }
        //oe号
        String oe = detail.getGoodsOe();
        if (!StringUtils.isEmpty(oe)) {
            goods_oe.setText(oe.trim());
        }
        //单位
        String unit = detail.getGoodsUnit();
        if (!StringUtils.isEmpty(unit)) {
            goods_unit.setText(unit.trim());
        }
        //适用车型
        String carmode = detail.getGoodscarmodelName();
        if (!StringUtils.isEmpty(carmode)) {
            carmode = carmode.replace(",", "\n");
            goods_carmodel.setText(carmode.trim());
        } else {
            goods_carmodel.setText("通用车型");
        }
        //型号goodsMode
        String mode = detail.getGoodsMode();
        if (!StringUtils.isEmpty(mode)) {
            goods_prodmode.setText(mode.trim());
        }
        //店铺名称
        String store_name = detail.getStoreName();
        if (!StringUtils.isEmpty(store_name)) {
            tvstore.setText(store_name.trim());
        }
        //店铺电话
        storeTelephone = detail.getStoreTelephone();
        //店铺ID
        storeid = detail.getStoreId();
//        //获取商品图片
        String imgid = detail.getGoodsPhoto();
        bannerList = new ArrayList<Banner>();
        if (!StringUtils.isEmpty(imgid)) {
            for (int i = 0; i < imgid.split(",").length; i++) {
                Banner banner = new Banner();
                banner.setAdAccId(imgid.split(",")[i]);
                banner.setAdUrl("#");
                bannerList.add(banner);
            }
            mHeaderView.initData(getImgLoader(), bannerList);
        } else {
            mLoopView.setVisibility(View.GONE);
            imgerror.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Goods>>() {
        }.getType();
    }

    protected Type getShoppingCarType() {
        return new TypeToken<ResultBean<ShoppingCar>>() {
        }.getType();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_contact_phone:
                if (null != storeTelephone) {
                    ShowContactTell.showContact(getContext(), storeTelephone);
                } else {
                    AppContext.showToast("店主有点懒，没有留下联系方式！");
                }
                break;
            case R.id.tv_googs_supplier_name:
                Bundle b = new Bundle();
                b.putString("storeId", storeid);
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.GOODS_SUPPLIER_SHOP, b);
                break;
            case R.id.btn_shoppingcar:
                if (AppContext.getInstance().isLogin()) {
                    show(1);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
                break;
            case R.id.btn_buy:
                if (AppContext.getInstance().isLogin()) {
                    show(2);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
                break;
            case R.id.selector_del:
                if (amount > 1) {
                    amount--;
                    normal.setText(amount + "");
                }
                normal.clearFocus();
                break;
            case R.id.selector_add:
                if (amount < goods_storage) {
                    amount++;
                    normal.setText(amount + "");
                } else {
                    AppContext.showToast("超出库存数!");
                }
                normal.clearFocus();
                break;
            case R.id.btn_goodscar_sure:
                showWaitDialog();
                MonkeyApi.addGoodsCar(storeid, String.valueOf(goods_id), String.valueOf(amount), "0", carHandler);
                break;
            case R.id.btn_buy_sure:
                dialog.dismiss();
                Bundle c = new Bundle();
                c.putString("id", goods_id.toString());
                c.putString("storeId", storeid);
                c.putString("count", String.valueOf(amount));
                c.putString("buyType", "buy");
                UIHelper.showSimpleBack(getActivity(), SimpleBackPage.ORDER_FILL_DETAIL, c);
                break;
            default:
                break;
        }
    }

    protected TextHttpResponseHandler carHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("购物车添加失败~");
            dialog.dismiss();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<ShoppingCar> resultBean = AppContext.createGson().fromJson(responseString, getShoppingCarType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    AppContext.showToast("购物车添加成功~");
                    dialog.dismiss();
                } else {
                    executeOnLoadDataError();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

    public void show(int i) {
        dialog = new Dialog(getActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout, null);
        //初始化控件
        normal = (EditText) inflate.findViewById(R.id.normal);
        selector_del = (Button) inflate.findViewById(R.id.selector_del);
        selector_add = (Button) inflate.findViewById(R.id.selector_add);
        btn_goodscar_sure = (Button) inflate.findViewById(R.id.btn_goodscar_sure);
        btn_buy_sure = (Button) inflate.findViewById(R.id.btn_buy_sure);
        selector_del.setOnClickListener(this);
        selector_add.setOnClickListener(this);
        btn_goodscar_sure.setOnClickListener(this);
        btn_buy_sure.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        //设置edittext初始值
        normal.setText(amount + "");
        if (i == 1) {
            btn_buy_sure.setVisibility(View.GONE);
        }
        if (i == 2) {
            btn_goodscar_sure.setVisibility(View.GONE);
        }
        normal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtils.isEmpty(normal.getText().toString())) {
                    amount = Integer.valueOf(normal.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        dialog.show();//显示对话框
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.shoppingcar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_shoppingcar:
                if (AppContext.getInstance().isLogin()) {
                    UIHelper.showSimpleBack(getActivity(), SimpleBackPage.SHOPPINGCAR_LIST);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
                return false;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
