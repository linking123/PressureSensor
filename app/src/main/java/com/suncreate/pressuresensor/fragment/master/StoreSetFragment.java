package com.suncreate.pressuresensor.fragment.master;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.carBrand.CarBrand;
import com.suncreate.pressuresensor.bean.carBrand.SortAdapterForCarBrand;
import com.suncreate.pressuresensor.bean.user.Store;
import com.suncreate.pressuresensor.util.AreaHelper;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.FileUtil;
import com.suncreate.pressuresensor.util.ImageUtils;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.widget.SuperRefreshLayout;
import com.suncreate.pressuresensor.widget.togglebutton.ToggleButton;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 店铺设置
 * <p>
 * desc
 */
public class StoreSetFragment extends BaseDetailFragment<Store> implements View.OnClickListener {
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    private String brandId = null;
    private String brandName = null;
    private Dialog dialog;
    private SimpleAdapter myAdapter1;
    private List<Map<String, Object>> listitem1;
    private View inflate;
    private Uri imageUri;
    private final int MUTI_CHOICE_DIALOG = 1;
    //    boolean[] selected = new boolean[]{false, false, false, false, false, false, false, false, false, false};
    private boolean isChangeFace = false;
    private List<CarBrand> carsBrandsitem;
    private final static int CROP = 200;
    protected SuperRefreshLayout mRefreshLayout;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/monkey/Portrait/";
    private Uri origUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private GridView mgridView1;
    private List<CarBrand> sourceDateList;
    private SortAdapterForCarBrand adapter;
    private PopupWindow popupwindow2;
    private Context mContext;
    ArrayList<HashMap<String, Object>> storeList = new ArrayList<HashMap<String, Object>>();
    private int[] sNum = new int[300];
    private int[] selectNum = new int[163];
    private int sum = 0;
    private String isWork = ";周末不营业;";
    private boolean[] selected = new boolean[163];
    private   String selectedAreaCode;
    private String selectedAddress;
    private String isOpenspace;
    private String storeLat;
    private String storeLng;
    private int a;
    private String protraitPath1;
    private File protraitFile1;
    private Bitmap protraitBitmap1;
    //汽车维修
    @Bind(R.id.checkBox1)
    CheckBox mcheckBox1;
    //保养维修
    @Bind(R.id.checkBox2)
    CheckBox mcheckBox2;
    //美容装饰
    @Bind(R.id.checkBox3)
    CheckBox mcheckBox3;
    //安装改装
    @Bind(R.id.checkBox4)
    CheckBox mcheckBox4;

    //机修
    @Bind(R.id.checkBox14)
    CheckBox mcheckBox14;
    //钣金喷漆
    @Bind(R.id.checkBox24)
    CheckBox mcheckBox24;
    //电器
    @Bind(R.id.checkBox34)
    CheckBox mcheckBox34;
    //美容改装
    @Bind(R.id.checkBox44)
    CheckBox mcheckBox44;

    @Bind(R.id.layout_store)
    LinearLayout layout_store;

    @Bind(R.id.tv_master_technician_brand)
    TextView tv_master_technician_brand;

    @Bind(R.id.tv_master_technician_time_select_first)
    TextView tv_master_technician_time_select_first;

    @Bind(R.id.store_gridView)
    GridView store_gridView;

    //店铺修理厂端布局
    @Bind(R.id.field_layout)
    RelativeLayout field_layout;

    //技师布局
    @Bind(R.id.tech_layout)
    RelativeLayout tech_layout;

    //开启布局
    @Bind(R.id.open_layout)
    RelativeLayout open_layout;

    //店铺Logo
    @Bind(R.id.icon_master_technician)
    ImageView icon_master_technician;

    //店铺名称
    @Bind(R.id.tv_master_technician_name)
    EditText tv_master_technician_name;

    //店铺地址
    @Bind(R.id.tv_master_technician_location)
    EditText tv_master_technician_location;

    //保存设置
    @Bind(R.id.btn__store_save)
    Button btn__store_save;

    //联系方式
    @Bind(R.id.tv_phone_edit)
    EditText storeTelephone;


    //银行账户
    @Bind(R.id.tv_bank_store_name)
    EditText storeBankaccount;

    //账户名称
    @Bind(R.id.tv_bank_my_name_edit)
    EditText storeAccountname;

    //开户银行
    @Bind(R.id.tv_bank_edit)
    EditText storeBankname;


    //品牌名称
    @Bind(R.id.car_brand_name)
    TextView car_brand_name;

    //特长服务项目
    @Bind(R.id.tv_master_technician_service1)
    EditText tv_master_technician_service1;

    @Bind(R.id.car_select_brand)
    Button car_select_brand;

    @Bind(R.id.tv_master_technician_time_select_final)
    TextView tv_master_technician_time_select_final;

    @Bind(R.id.car_brand_id)
    TextView car_brand_id;
    //营业时间说明
    @Bind(R.id.tv_time)
    TextView tv_time;

    @Bind(R.id.default_workTime_set)
    ToggleButton default_workTime_set;


    //店内技师
    @Bind(R.id.tv_master_technician_service2)
    TextView tv_master_technician_service2;


    //店内技师说明
    @Bind(R.id.tv_master_technician_service3)
    EditText tv_master_technician_service3;

    //请选择工位数
    @Bind(R.id.tv_master_technician_service58)
    TextView tv_master_technician_service58;
    //工位数
    @Bind(R.id.radioButton1)
    RadioButton radioButton1;

    //技师类别
    @Bind(R.id.btn_tech_level)
    TextView btn_tech_level;

    private String mAreaCode;
    private String mSelectedAreaCode;

    //地址选择器
    OptionsPickerView addrOptions;
    //工位数是否开启
    @Bind(R.id.tv_master_technician_service57)
    TextView tv_master_technician_service57;

    //地址显示
    @Bind(R.id.tv_my_address_where)
    TextView userArea;

    //工位开启按钮
    @Bind(R.id.default_address_set_store)
    ToggleButton default_address_set_store;

    @Bind(R.id.radioButton2)
    RadioButton radioButton2;

    @Bind(R.id.tv_master_technician_gps)
    TextView tv_master_technician_gps;

    @Bind(R.id.radioButton3)
    RadioButton radioButton3;

    //请选择工位数
    @Bind(R.id.tv_master_technician_service4)
    TextView tv_master_technician_service4;

    @Bind(R.id.tv_master_technician_line12)
    View tv_master_technician_line12;

    @Bind(R.id.icon_master_technician_line11)
    View icon_master_technician_line11;


    @Bind(R.id.icon_master_technician_line5)
    View icon_master_technician_line5;

    @Bind(R.id.tv_master_technician_line5)
    View tv_master_technician_line5;

    @Bind(R.id.icon_master_technician_line55)
    View icon_master_technician_line55;

    @Bind(R.id.tv_master_technician_line56)
    View tv_master_technician_line56;

    @Bind(R.id.upload_img1)
    ImageButton upload_img1;

    @Bind(R.id.ID_first11)
    TextView ID_first11;

    @Bind(R.id.ID_First)
    ImageView ID_First;

    //地址区域box
    @Bind(R.id.rl_my_area_box)
    RelativeLayout mAreaBox;

    @Override
    public void initView(View view) {
        if(AppContext.getInstance().getUserRole().equals(2)){
            icon_master_technician.setOnClickListener(this);
            tv_master_technician_brand.setOnClickListener(this);
            tv_master_technician_time_select_first.setOnClickListener(this);
            tv_master_technician_time_select_final.setOnClickListener(this);
            btn__store_save.setOnClickListener(this);
            car_select_brand.setOnClickListener(this);
            field_layout.setVisibility(View.INVISIBLE);
            field_layout.getLayoutParams().height = 0;


        }
        else if(AppContext.getInstance().getUserRole().equals(3))
        {
            icon_master_technician.setOnClickListener(this);
            tv_master_technician_brand.setOnClickListener(this);
            tv_master_technician_time_select_first.setOnClickListener(this);
            tv_master_technician_time_select_final.setOnClickListener(this);
            upload_img1.setOnClickListener(this);
            btn__store_save.setOnClickListener(this);
            tv_master_technician_service3.setOnClickListener(this);
            car_select_brand.setOnClickListener(this);
            //区域选择
            mAreaBox.setOnClickListener(this);
            tech_layout.setVisibility(View.INVISIBLE);
            tech_layout.getLayoutParams().height = 0;
            if (isOpenspace == "0") {
                default_address_set_store.setToggleOff();
            } else {
                default_address_set_store.setToggleOn();
            }
            default_address_set_store.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    if (on) {
                        isOpenspace = "1";
                        open_layout.setVisibility(View.VISIBLE);

                    } else {
                        isOpenspace = "0";
                        open_layout.setVisibility(View.INVISIBLE);
                        open_layout.getLayoutParams().height = 0;
                    }
                }
            });
        }
        default_workTime_set.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    isWork = "，周末正常营业";
                    tv_time.setText(tv_master_technician_time_select_first.getText() +
                            " - " + tv_master_technician_time_select_final.getText() + isWork);
                } else {
                    isWork = "，周末不营业";
                    tv_time.setText(tv_master_technician_time_select_first.getText() +
                            " - " + tv_master_technician_time_select_final.getText() + isWork);
                }
            }
        });


        //选择地址
        addrOptions = AreaHelper.chooseAddress(addrOptions, this.getContext());
        addrOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                AreaHelper.onOptionsSelect(options1, option2, options3, userArea);
                String mUserArea = userArea.getText().toString();
                if (!TextUtils.isEmpty(mUserArea)) {
                    String[] s = mUserArea.split(" ");
                    mAreaCode = AppContext.getInstance().getAreaCodeByName(s[s.length - 1], s[s.length - 2]);
                    if (!StringUtils.isEmpty(mAreaCode)) {
                        mSelectedAreaCode = mAreaCode;
                    }
                }
            }
        });
    }

    TextHttpResponseHandler brandHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String str = responseString;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<CarBrand>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<CarBrand>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                    carsBrandsitem = resultBean.getResult().getItems();
                    String[] iconUrl = new String[carsBrandsitem.size()];
                    for (int m = 0; m < carsBrandsitem.size(); m++) {
                        CarBrand cb = carsBrandsitem.get(m);
                        iconUrl[m] = "carbrand/" + carsBrandsitem.get(m).getBrandIcon() + ".png";
                        AssetManager assetManager = getContext().getAssets();
                        InputStream inputStream = null;
                        try {
                            inputStream = assetManager.open(iconUrl[m]);
                            Bitmap imap = BitmapFactory.decodeStream(inputStream);
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("brand_img", imap);
                            map.put("brand_name", cb.getBrandName());
                            storeList.add(map);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SimpleAdapter adapter = new SimpleAdapter(getContext(), storeList,
                                R.layout.fragment_store_setting_item, new String[]
                                {"brand_img"}, new int[]{R.id.brand_img});
                        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Object data, String textRepresentation) {
                                if (view instanceof ImageView && data instanceof Bitmap) {
                                    ImageView iv = (ImageView) view;
                                    iv.setImageBitmap((Bitmap) data);
                                    return true;
                                } else

                                    return false;
                            }
                        });


                        store_gridView.setAdapter(adapter);

                    }

                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };


    TextHttpResponseHandler bHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            String str = responseString;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<PageBean<CarBrand>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<CarBrand>>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                    carsBrandsitem = resultBean.getResult().getItems();
                    storeList.clear();
                    car_brand_id.setText("");
                    showDialog(MUTI_CHOICE_DIALOG);
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    @Override
    protected void sendRequestDataForNet() {
        MonkeyApi.getStoreInfo(mDetailHandler);
    }

    @Override
    protected void executeOnLoadDataSuccess(Store detail) {
        super.executeOnLoadDataSuccess(detail);

        String status = detail.getStoreStatus();
        if (status.equals("2")) {
            tv_master_technician_name.setEnabled(false);
            tv_master_technician_location.setEnabled(false);
            storeBankaccount.setEnabled(false);
            storeAccountname.setEnabled(false);
            storeBankname.setEnabled(false);
            mAreaBox.setEnabled(false);

        }

        //店铺logo
        String handImage = detail.getStoreLogo();
        if (!StringUtils.isEmpty(handImage)) {
            try {
                MonkeyApi.getPartImg(handImage, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            icon_master_technician.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        icon_master_technician.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            icon_master_technician.setVisibility(View.GONE);
        }

        //修理厂级别
        String level = detail.getStoreTechnicianLevel();
            if ("0".equals(level)) {
                btn_tech_level.setText(R.string.str_community);
                btn_tech_level.setBackgroundResource(R.drawable.btn_bg_primary);
            } else if ("1".equals(level)) {
                btn_tech_level.setText(R.string.str_region);
                btn_tech_level.setBackgroundResource(R.drawable.btn_bg_region);
            } else {
                btn_tech_level.setVisibility(View.INVISIBLE);
        }

        //区域
        String areaCode = detail.getStoreAreaCode();
        mSelectedAreaCode = detail.getStoreAreaCode();
        String area = AppContext.getInstance().getAreaFullNameByCode(areaCode);
        userArea.setText(area);


        //店铺名称
        String storeName = detail.getStoreName();
        if (!StringUtils.isEmpty(storeName)) {
            tv_master_technician_name.setText(storeName);
        } else {
            tv_master_technician_name.setText("");
        }

        //店铺地址
        String storeLocation = detail.getStoreAddress();
        if (!StringUtils.isEmpty(storeLocation)) {
            tv_master_technician_location.setText(storeLocation);
        } else {
            tv_master_technician_location.setText("");
        }
        //专修品牌
        String carBrandIdd = detail.getSpecialBrand();
        if (!StringUtils.isEmpty(carBrandIdd)) {
            car_brand_id.setText(carBrandIdd);
        } else {
            car_brand_id.setText(car_brand_id.getText());
            carBrandIdd = "1,";
        }
        car_brand_id.setText(carBrandIdd);

        if (sum == 0) {
            String carbrandName = "";
            String carbrandLetter = "";
            String carbrandType = "1";
            //  String brand = carBrandIdd.substring(1);
            String[] brandId = carBrandIdd.split(",");
            for (int i = 0; i < brandId.length; i++) {
                MonkeyApi.getCarBrandNameAndIcon(brandId[i], carbrandName, carbrandLetter, carbrandType, brandHandler);
                sum++;
                selectNum[i] = Integer.parseInt(brandId[i]);
            }
        }
        //营业时间说明
        String storeFirstTimeDesc = detail.getStoreTimeDesc();
        if (!StringUtils.isEmpty(storeFirstTimeDesc)) {
            tv_time.setText(storeFirstTimeDesc);
        } else {
            tv_time.setText("");
        }


        //联系方式
        String Telephone = detail.getStoreTelephone();
        if (!StringUtils.isEmpty(Telephone)) {
            storeTelephone.setText(Telephone);
        } else {
            storeTelephone.setText("");
        }
        //银行账户
        String Bankaccount = detail.getStoreBankaccount();
        if (!StringUtils.isEmpty(Bankaccount)) {
            storeBankaccount.setText(Bankaccount);
        } else {
            storeBankaccount.setText("");
        }

        //账户名称
        String Accountname = detail.getStoreAccountname();
        if (!StringUtils.isEmpty(Accountname)) {
            storeAccountname.setText(Accountname);
        } else {
            storeAccountname.setText("");
        }

        //开户银行
        String storeFirstBankname = detail.getStoreBankname();
        if (!StringUtils.isEmpty(storeFirstBankname)) {
            storeBankname.setText(storeFirstBankname);
        } else {
            storeBankname.setText("");
        }

        //服务分类
        String storeSerivceType = detail.getStoreSerivceType();
        if (!StringUtils.isEmpty(storeSerivceType)) {
            String[] serviceType = storeSerivceType.split(",");
            for (int i = 0; i < serviceType.length; i++) {
                if (serviceType[i].indexOf("CREPAIRS") != -1) {
                    mcheckBox1.setChecked(true);
                }
                if (serviceType[i].indexOf("MAINTENANCE") != -1) {
                    mcheckBox2.setChecked(true);
                }
                if (serviceType[i].indexOf("BDECORATION") != -1) {
                    mcheckBox3.setChecked(true);
                }
                if (serviceType[i].indexOf("IALTERATION") != -1) {
                    mcheckBox4.setChecked(true);
                }
            }
        }
        //技师类别
        String techLevel = detail.getStoreTechnicianLevel();
        if ("1".equals(techLevel)) {
            btn_tech_level.setText("初级");
            btn_tech_level.setBackgroundResource(R.drawable.btn_bg_primary);
        } else if ("2".equals(techLevel)) {
            btn_tech_level.setText("中级");
            btn_tech_level.setBackgroundResource(R.drawable.btn_bg_middle);
        } else if ("3".equals(techLevel)) {
            btn_tech_level.setText("高级");
            btn_tech_level.setBackgroundResource(R.drawable.btn_bg_high);
        } else {
            btn_tech_level.setVisibility(View.INVISIBLE);
        }




        //技师类别 MAINTENANCE,PAINTING,CAPACITANCE,BMODIFIED
        String storeTecType = detail.getStoreTecType();
        if (!StringUtils.isEmpty(storeTecType)) {
            String[] type = storeTecType.split(",");
            for (int i = 0; i < type.length; i++) {
                if (type[i].indexOf("MAINTENANCE") != -1) {
                    mcheckBox14.setChecked(true);
                }
                if (type[i].indexOf("PAINTING") != -1) {
                    mcheckBox24.setChecked(true);
                }
                if (type[i].indexOf("CAPACITANCE") != -1) {
                    mcheckBox34.setChecked(true);
                }
                if (type[i].indexOf("BMODIFIED") != -1) {
                    mcheckBox44.setChecked(true);
                }
            }
        }

        String de = detail.getOpenPlace();
        isOpenspace = detail.getOpenPlace();
        //工位描述
        String siteDesc = detail.getSiteDesc();
        tv_master_technician_service4.setText(siteDesc);

        if(!StringUtils.isEmpty(de)) {
            if (de.equals("1")) {
                //工位数
                String stationNum = detail.getStationNum();
                if (!StringUtils.isEmpty(stationNum)) {
                    if (stationNum.indexOf("1") != -1) {
                        radioButton1.setChecked(true);
                    }
                    if (stationNum.indexOf("2") != -1) {
                        radioButton2.setChecked(true);
                    }
                    if (stationNum.indexOf("3") != -1) {
                        radioButton3.setChecked(true);
                    }
                }
            } else if (de.equals("0")) {
                default_address_set_store.setToggleOff();
                open_layout.setVisibility(View.INVISIBLE);
            }
        }

        //工位图片
        String fieldImage = detail.getJsRecommondId();
        if (!StringUtils.isEmpty(fieldImage)) {
            try {
                MonkeyApi.getPartImg(fieldImage, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            ID_First.setImageBitmap(imap);
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        ID_First.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ID_First.setVisibility(View.GONE);
        }

        storeLat = detail.getStoreLat().toString();
        if (StringUtils.isEmpty(storeLat))
        {
            storeLat = AppContext.lat.toString();
        }


        storeLng = detail.getStoreLng().toString();
        if (StringUtils.isEmpty(storeLng))
        {
            storeLng = AppContext.lon.toString();
        }


        //特长服务项目
        String specialService = detail.getStoreInfo();
        tv_master_technician_service1.setText(specialService);

        //店内技师
        String technician = detail.getStoreTec();
        tv_master_technician_service3.setText(technician);

        selectedAreaCode = detail.getStoreAreaCode();
        selectedAddress = detail.getStoreAddress();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_set;
    }


    @Override
    protected String getCacheKey() {
        return "";
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rl_my_area_box:
                addrOptions.show();
                break;
            case R.id.icon_master_technician:
                a = 1;
                handleSelectPicture();
                break;
            case R.id.car_select_brand:
                String carBrandId = "";
                String carbrandName = "";
                String carbrandLetter = "";
                String carbrandType = "1";
                MonkeyApi.getCarBrandNameAndIcon(carBrandId, carbrandName, carbrandLetter, carbrandType, bHandler);
                break;
            case R.id.tv_master_technician_time_select_first:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_master_technician_time_select_first.setText(String.format("%d:%02d", hourOfDay, minute));
                        tv_time.setText(tv_master_technician_time_select_first.getText() + "至" + tv_master_technician_time_select_final.getText() + isWork);
                    }
                }, 0, 0, true).show();
                break;
            case R.id.upload_img1:
                handleSelectPicture();
                a = 2;
                break;

            //CREPAIRS-汽车维修 MAINTENANCE-保养修护 BDECORATION-美容装饰 IALTERATION-安装改装
            case R.id.btn__store_save:
                if(AppContext.getInstance().getUserRole().equals(2)) {
                    String storeSerivceType = "";
                    if (mcheckBox1.isChecked()) {
                        storeSerivceType += "CREPAIRS,";
                    }
                    if (mcheckBox2.isChecked()) {
                        storeSerivceType += "MAINTENANCE,";
                    }
                    if (mcheckBox3.isChecked()) {
                        storeSerivceType += "BDECORATION,";
                    }
                    if (mcheckBox4.isChecked()) {
                        storeSerivceType += "IALTERATION";
                    }

                    //技师类别
                    String storeTecType = "";
                    if (mcheckBox14.isChecked()) {
                        storeTecType += "MAINTENANCE,";
                    }
                    if (mcheckBox24.isChecked()) {
                        storeTecType += "PAINTING,";
                    }
                    if (mcheckBox34.isChecked()) {
                        storeTecType += "CAPACITANCE,";
                    }
                    if (mcheckBox44.isChecked()) {
                        storeTecType += "BMODIFIED";
                    }

                    if (StringUtils.isEmpty(tv_master_technician_name.getText().toString())) {
                        Toast.makeText(getContext(), "请输入店铺名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeTelephone.getText().toString())) {
                        Toast.makeText(getContext(), "请输入联系方式", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeBankaccount.getText().toString())) {
                        Toast.makeText(getContext(), "请输入银行账户", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeAccountname.getText().toString())) {
                        Toast.makeText(getContext(), "请输入账户名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeBankname.getText().toString())) {
                        Toast.makeText(getContext(), "请输入开户银行", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeSerivceType)) {
                        Toast.makeText(getContext(), "请选择预约服务类型", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isEmpty(storeTecType)) {
                        Toast.makeText(getContext(), "请选择技师类别", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isEmpty(tv_master_technician_service1.getText().toString())) {
                        Toast.makeText(getContext(), "请填写特长服务项目", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    MonkeyApi.getStore(tv_master_technician_name.getText().toString(), selectedAreaCode, selectedAddress, null, null, null,
                            null, null, car_brand_id.getText().toString(), "1", storeSerivceType,
                            tv_master_technician_service1.getText().toString(),
                            storeTelephone.getText().toString(), storeBankaccount.getText().toString(),
                            storeAccountname.getText().toString(), storeTecType, storeBankname.getText().toString(),
                            tv_time.getText().toString(), storeHandler);
                }
                else if(AppContext.getInstance().getUserRole().equals(3))
                {
                    String storeSerivceType = "";
                    if (mcheckBox1.isChecked()) {
                        storeSerivceType += "CREPAIRS,";
                    }
                    if (mcheckBox2.isChecked()) {
                        storeSerivceType += "MAINTENANCE,";
                    }
                    if (mcheckBox3.isChecked()) {
                        storeSerivceType += "BDECORATION,";
                    }
                    if (mcheckBox4.isChecked()) {
                        storeSerivceType += "IALTERATION";
                    }


                    String stationNum = "";
                    if (radioButton1.isChecked()) {
                        stationNum = "1";
                    }
                    if (radioButton2.isChecked()) {
                        stationNum = "2";
                    }
                    if (radioButton3.isChecked()) {
                        stationNum = "3";
                    }

                    if (StringUtils.isEmpty(tv_master_technician_name.getText().toString())) {
                        Toast.makeText(getContext(), "请输入店铺名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(tv_master_technician_location.getText().toString())) {
                        Toast.makeText(getContext(), "请输入店铺地址", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeTelephone.getText().toString())) {
                        Toast.makeText(getContext(), "请输入联系方式", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(mSelectedAreaCode)) {
                        Toast.makeText(getContext(), "请选择店铺区域", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (StringUtils.isEmpty(storeSerivceType)) {
                        Toast.makeText(getContext(), "请选择预约服务类型", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isEmpty(tv_master_technician_service1.getText().toString())) {
                        Toast.makeText(getContext(), "请填写特长服务项目", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isEmpty(tv_master_technician_service3.getText().toString())) {
                        Toast.makeText(getContext(), "请填写店内技师", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (StringUtils.isEmpty(storeBankaccount.getText().toString())) {
                        Toast.makeText(getContext(), "请输入银行账户", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeAccountname.getText().toString())) {
                        Toast.makeText(getContext(), "请输入账户名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (StringUtils.isEmpty(storeBankname.getText().toString())) {
                        Toast.makeText(getContext(), "请输入开户银行", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isOpenspace == "0") {

                        MonkeyApi.getStore(tv_master_technician_name.getText().toString(), mSelectedAreaCode, tv_master_technician_location.getText().toString(), null,
                                "0",
                                null,
                                storeLng, storeLat, car_brand_id.getText().toString(), tv_master_technician_service3.getText().toString(), storeSerivceType,
                                tv_master_technician_service1.getText().toString(),
                                storeTelephone.getText().toString(), storeBankaccount.getText().toString(),
                                storeAccountname.getText().toString(), "1", storeBankname.getText().toString(),
                                tv_time.getText().toString(), storeHandler);
                    } else {
                        MonkeyApi.getStore(tv_master_technician_name.getText().toString(), mSelectedAreaCode, tv_master_technician_location.getText().toString(), tv_master_technician_service4.getText().toString(),
                                "1",
                                stationNum,
                                storeLng, storeLat, car_brand_id.getText().toString(), tv_master_technician_service3.getText().toString(), storeSerivceType,
                                tv_master_technician_service1.getText().toString(),
                                storeTelephone.getText().toString(), storeBankaccount.getText().toString(),
                                storeAccountname.getText().toString(), "1", storeBankname.getText().toString(),
                                tv_time.getText().toString(), storeHandler);
                    }
                    break;
                }
                break;
            case R.id.tv_master_technician_time_select_final:
                new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv_master_technician_time_select_final.setText(String.format("%d:%02d", hourOfDay, minute));
                        tv_time.setText(tv_master_technician_time_select_first.getText() + "至" + tv_master_technician_time_select_final.getText() + isWork);
                    }
                }, 0, 0, true).show();
                break;
            default:
                break;
        }
    }

    TextHttpResponseHandler storeHandler = new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("保存失败!");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                }.getType());
                if (resultBean != null && resultBean.getCode() == 1) {
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    onFailure(statusCode, headers, responseString, null);
                }
            } catch (Exception e) {
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };



    protected void executeOnLoadDataError() {
        AppContext.showToast("");
    }

    public Dialog showDialog(final int id) {
        Dialog dialog = null;
        switch (id) {
            case (MUTI_CHOICE_DIALOG):

                final String[] choiceItems = new String[carsBrandsitem.size()];

                for (int i = 0; i < carsBrandsitem.size(); i++) {
                    CarBrand cb = carsBrandsitem.get(i);
                    if (!TextUtils.isEmpty(cb.getBrandName())) {
                        choiceItems[i] = cb.getBrandName();

                    }
                    selected[i] = false;

                }

                for (int j = 0; j < carsBrandsitem.size(); j++) {
                    for (int k = 0; k < selectNum.length; k++) {
                        int m = selectNum[k];
                        if (m != 0) {
                            selected[m - 1] = true;
                        }
                    }
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请选择车牌：");
                builder.setIcon(R.drawable.arrow);

                DialogInterface.OnMultiChoiceClickListener mutiListener
                        = new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,
                                        int which, boolean isChecked) {
                        selected[which] = isChecked;
                    }
                };
                builder.setMultiChoiceItems(choiceItems, selected,
                        mutiListener);

                DialogInterface.OnClickListener btnListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface,
                                                int which) {
                                sum++;
                                int j = 0;
                                int k = 0;
                                int a =0;
                                for (int i = 0; i < selected.length; i++) {
                                    if (selected[i] == true) {
                                        sum++;
                                        j++;
                                        sNum[k++] = i;
                                        car_brand_name.setText(car_brand_name.getText() + choiceItems[i]);
                                        car_brand_id.setText(car_brand_id.getText() + "," + carsBrandsitem.get(i).getId());
                                        selectNum[a++] = i+1;
                                    }
                                    else{
                                        selectNum[i] = 0;
                                    }
                                }
                                if (j != 0) {
                                    car_brand_id.setText(car_brand_id.getText().toString().substring(1));
                                }
                                String[] iconUrl = new String[j];
                                int n =0;
                                for (int m = 0; m < j; m++) {
                                    CarBrand cb = carsBrandsitem.get(sNum[m]);
                                    iconUrl[m] = "carbrand/" + carsBrandsitem.get(sNum[m]).getBrandIcon() + ".png";
                                    AssetManager assetManager = getContext().getAssets();
                                    InputStream inputStream = null;
                                    try {
                                        inputStream = assetManager.open(iconUrl[m]);
                                        Bitmap imap = BitmapFactory.decodeStream(inputStream);
                                        HashMap<String, Object> map = new HashMap<String, Object>();
                                        map.put("brand_img", imap);
                                        storeList.add(map);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    SimpleAdapter adapter = new SimpleAdapter(getContext(), storeList,
                                            R.layout.fragment_store_setting_item, new String[]
                                            {"brand_img"}, new int[]{R.id.brand_img});
                                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                        @Override
                                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                                            if (view instanceof ImageView && data instanceof Bitmap) {
                                                ImageView iv = (ImageView) view;
                                                iv.setImageBitmap((Bitmap) data);
                                                return true;
                                            } else
                                                return false;
                                        }
                                    });
                                    store_gridView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        };
                if (sum != 0) {
                    builder.setPositiveButton("确定", btnListener);
                } else if (sum == 0) {
                    builder.setPositiveButton("确定", null);
                }
                builder.setNegativeButton("取消", null);
                builder.show();
                dialog = builder.create();
                break;

        }
        return dialog;
    }


    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Store>>() {
        }.getType();
    }


    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(getActivity(), "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectPicture(i);
            }
        }).show();
    }

    private void goToSelectPicture(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                startImagePick();
                break;
            case ACTION_TYPE_PHOTO:
                startTakePhotoByPermissions();
                break;
            default:
                break;
        }
    }

    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        showWaitDialog("正在上传头像...");
        if (a == 1) {
            // 获取头像缩略图
            if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
                protraitBitmap = ImageUtils
                        .loadImgThumbnail(protraitPath, 900, 600);
            } else {
                AppContext.showToast("图像不存在，上传失败");
            }
        } else {
            if (!StringUtils.isEmpty(protraitPath1) && protraitFile1.exists()) {
                protraitBitmap1 = ImageUtils
                        .loadImgThumbnail(protraitPath1, 1200, 800);
            } else {
                AppContext.showToast("图像不存在，上传失败");
            }
        }
        if (a == 1) {
            try {
                MonkeyApi.updatePortrait(AppContext.getInstance()
                                .getLoginUid(), protraitFile,
                        new TextHttpResponseHandler() {

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                AppContext.showToast("更换头像失败");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                try {
                                    ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                                    }.getType());
                                    if (resultBean != null && resultBean.getCode() == 1) {
                                        AppContext.showToast("更换成功");
                                        // 显示新头像
                                        icon_master_technician.setImageBitmap(protraitBitmap);
                                        isChangeFace = true;
                                        getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                                    } else {
                                        AppContext.showToast(resultBean.getMessage());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    onFailure(statusCode, headers, responseString, e);
                                }
                            }

                            @Override
                            public void onFinish() {
                                hideWaitDialog();
                            }
                        });
            } catch (FileNotFoundException e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        }
        if (a == 1) {
            try {
                //上传店铺logo
                MonkeyApi.getUploadStoreImg("logo", protraitFile, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("头像上传失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                            }.getType());
                            if (resultBean != null) {
                                AppContext.showToast("上传店铺Logo成功!");
                                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                            } else {
                                AppContext.showToast(resultBean.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            onFailure(statusCode, headers, responseString, e);
                        }
                    }

                    @Override
                    public void onFinish() {
                        hideWaitDialog();
                    }
                });
            } catch (Exception e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        }

        if (a == 2) {
            try {
                MonkeyApi.getUploadStoreImg("recommond", protraitFile1, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("更换头像失败");


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            com.suncreate.pressuresensor.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.pressuresensor.bean.base.ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.showToast("上传成功");
                                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));

                                try {
                                    MonkeyApi.getStoreInfo(
                                            new TextHttpResponseHandler() {

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    AppContext.showToast("更换头像失败");
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                    try {
                                                        com.suncreate.pressuresensor.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.pressuresensor.bean.base.ResultBean>() {
                                                        }.getType());
                                                        if (resultBean != null && resultBean.getCode() == 1) {
                                                            AppContext.showToast("更换成功");
                                                            // 显示新头像
                                                            ID_First.setImageBitmap(protraitBitmap1);
                                                            isChangeFace = true;
                                                            getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                                                        } else {
                                                            AppContext.showToast(resultBean.getMessage());
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        onFailure(statusCode, headers, responseString, e);
                                                    }
                                                }

                                                @Override
                                                public void onFinish() {
                                                    hideWaitDialog();
                                                }
                                            });
                                } catch (Exception e) {
                                    AppContext.showToast("图像不存在，上传失败");
                                }


                            } else {
                                AppContext.showToast(resultBean.getMessage());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            onFailure(statusCode, headers, responseString, e);
                        }
                    }

                    @Override
                    public void onFinish() {
                        hideWaitDialog();
                    }
                });
            } catch (Exception e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        }
    }


    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/monkey/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        String theLarge = savePath + fileName;

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            AppContext.showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(getActivity(), uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        return Uri.fromFile(protraitFile);
    }
    private Uri getUploadTempFile1(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            AppContext.showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(getActivity(), uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_one_" + timeStamp + "." + ext;


        // 裁剪头像的绝对路径
        protraitPath1 = FILE_SAVEPATH + cropFileName;

        protraitFile1 = new File(protraitPath1);

        return Uri.fromFile(protraitFile1);
    }
    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        if (a == 1) {
            intent.putExtra("output", this.getUploadTempFile(data));
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);// 裁剪框比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);// 去黑边
            intent.putExtra("scaleUpIfNeeded", true);// 去黑边
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else if (a == 2) {
            intent.putExtra("output", this.getUploadTempFile1(data));
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);// 裁剪框比例
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);// 去黑边
            intent.putExtra("scaleUpIfNeeded", true);// 去黑边
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
        }
    }

    private static final int CAMERA_PERM = 1;

    @AfterPermissionGranted(CAMERA_PERM)
    private void startTakePhotoByPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this.getContext(), perms)) {
            try {
                startTakePhoto();
            } catch (Exception e) {
                Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
            }
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.str_request_camera_message),
                    CAMERA_PERM, perms);
        }
    }


    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            startTakePhoto();
        } catch (Exception e) {
            Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
        }
    }


    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}



