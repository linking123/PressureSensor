package com.suncreate.fireiot.fragment.carBrand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.SimpleBackPage;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.carBrand.CarBrand;
import com.suncreate.fireiot.bean.carBrand.PinyinComparatorForCarBrand;
import com.suncreate.fireiot.bean.carBrand.SideBarForCarBrand;
import com.suncreate.fireiot.bean.carBrand.SortAdapterForCarBrand;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.fragment.HomeFragment;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.util.Car.CarUtils;
import com.suncreate.fireiot.util.EditTextWithDel;
import com.suncreate.fireiot.util.PinyinUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 选择车品牌
 */
public class CarBrandFragment extends BaseFragment implements Serializable {

    public static final int REQUEST_CODE_FOR_MODEL = 1;

    protected String CACHE_NAME = getClass().getName();

    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    protected PageBean<CarBrand> mBean;

    @Bind(R.id.et_search)
    EditTextWithDel mCarSeachName;
    @Bind(R.id.sidebar)
    SideBarForCarBrand mSideBar;
    @Bind(R.id.dialog)
    TextView mDialog;
    @Bind(R.id.car_list_view)
    ListView mSortListView;

    private List<CarBrand> sourceDateList;
    private SortAdapterForCarBrand adapter;
    protected int mCurrentPage = 0;
    protected TextHttpResponseHandler mHandlerIn;
    protected boolean mIsRefresh = true;
    String carbrandIcon;
    String flag;

    String carBrandId = "";
    String carbrandName = "";
    String carbrandLetter = "";
    String carbrandType = "1";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        Bundle b = getArguments();
        flag = b.getString("flag");

        mSideBar.setTextView(mDialog);
        initEvents();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.car_brand_choose_box;
    }

    @Override
    protected void initData() {
        super.initData();

        mSideBar.setTextView(mDialog);

        mHandlerIn = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AppContext.showToastShort("网络错误，获取车品牌失败！");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                final ResultBean<PageBean<CarBrand>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<CarBrand>>>() {
                }.getType());

                final PageBean<CarBrand> result = resultBean.getResult();
                mExeService.execute(new Runnable() {
                    @Override
                    public void run() {
                        CacheManager.saveObject(getContext(), result, CACHE_NAME);
                    }
                });
                //绑定数据
                bindDataToListView(result);
            }
        };

        mExeService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isExistDataCache = CacheManager.isExistDataCache(getActivity(), CACHE_NAME);
                if (!isExistDataCache) {
                    onRequestStart();
                } else {
                    mBean = (PageBean<CarBrand>) CacheManager.readObject(getActivity(), CACHE_NAME);
                    mRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mBean != null) {
                                //绑定数据
                                bindDataToListView(mBean);
                            } else {
                                onRequestStart();
                            }
                        }
                    });
                }
            }
        });
    }

    //共用的向listview填充数据方法
    private void bindDataToListView(PageBean<CarBrand> resultBean) {
        List<CarBrand> datas = resultBean.getItems();
        sourceDateList = new ArrayList<CarBrand>();
        for (CarBrand data : datas) {
            sourceDateList.add(data);
        }
        Collections.sort(sourceDateList, new PinyinComparatorForCarBrand());
        adapter = new SortAdapterForCarBrand(getContext(), sourceDateList);
        mSortListView.setAdapter(adapter);
    }

    protected void onRequestStart() {
        MonkeyApi.getCarBrandNameAndIcon(carBrandId, carbrandName, carbrandLetter, carbrandType, mHandlerIn);
    }

    private void initEvents() {
        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBarForCarBrand.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mSortListView.setSelection(position + 1);
                }
            }
        });

        //ListView的点击事件
        mSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CarBrand carBrand = ((CarBrand) adapter.getItem(position));
                if (carBrand != null) {
                    Bundle b = new Bundle();

                    String carBrandId = Integer.toString(carBrand.getId());
                    carbrandIcon = carBrand.getBrandIcon();
                    String carBrandName = carBrand.getBrandName();
                    b.putString("carBrandId", carBrandId);
                    b.putString("carbrnadIcon", carbrandIcon);
                    b.putString("carBrandName", carBrandName);
                    UIHelper.showSimpleBackForResult(CarBrandFragment.this, REQUEST_CODE_FOR_MODEL, SimpleBackPage.CAR_MODEL, b);
                }
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mCarSeachName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_FOR_MODEL) {
            String carId = data.getStringExtra("carId");
            String carBrandAndModelAndDisplacementAndYear = data.getStringExtra("carBrandAndModelAndDisplacementAndYear");

            Intent intent = new Intent();
            intent.putExtra("carId", carId);
            String carBrandId = CarUtils.getNumbers(carbrandIcon);
            intent.putExtra("carBrandId", carBrandId);
            intent.putExtra("carbrandIcon", carbrandIcon);
            intent.putExtra("carBrandAndModelAndDisplacementAndYear", carBrandAndModelAndDisplacementAndYear);

            //因为多个地方用到车型选择，所以这里需要一个标识，返回哪里？通过选择的时候传过来的flag标识
            int a = 0;
            switch (flag) {
                case "from_MyCarManageAddFragment":
                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_CARMANAGERADDFRAGMENT;
                    break;
                case "from_MyCarManagerEditFragment":
                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_CARMANAGEREDITFRAGMENT;
                    break;
//                case "from_HomeFragment":
//                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_HOMEFRAGMENT;
//                    break;
//                case "from_TechnicianListFragment":
//                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_TECHNICIANLISTFRAGMENT;
//                    break;
//                case "from_GoodsListFragment":
//                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_GOODSLISTFRAGMENT;
//                    break;
//                case "from_FieldListFragment":
//                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_FIELDLISTFRAGMENT;
//                    break;
//                case "from_GarageListFragment":
//                    a = HomeFragment.REQUEST_CODE_FOR_BRAND_GARAGELISTFRAGMENT;
//                    break;
                default:
                    break;
            }
            getActivity().setResult(a, intent);
            getActivity().finish();
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     */
    private void filterData(String filterStr) {
        List<CarBrand> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = sourceDateList;
        } else {
            mSortList.clear();
            for (CarBrand sortModel : sourceDateList) {
                String name = sortModel.getBrandName();
                if (name.toUpperCase().contains(filterStr.toUpperCase()) || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparatorForCarBrand());
        adapter.updateListView(mSortList);
    }

    public List<CarBrand> filledData(String[] date) {
        List<CarBrand> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CarBrand sortModel = new CarBrand();
            sortModel.setBrandName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setBrandLetter(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        mSideBar.setIndexText(indexString);
        return mSortList;
    }

}
