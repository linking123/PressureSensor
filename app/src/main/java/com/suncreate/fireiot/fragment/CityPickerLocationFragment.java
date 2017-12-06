package com.suncreate.fireiot.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.AppManager;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.citypicker.CityWebSites;
import com.suncreate.fireiot.bean.citypicker.PinyinComparator;
import com.suncreate.fireiot.bean.citypicker.SideBarForCityPicker;
import com.suncreate.fireiot.bean.citypicker.SortAdapterForCityPicker;
import com.suncreate.fireiot.fragment.base.BaseFragment;
import com.suncreate.fireiot.ui.MainActivity;
import com.suncreate.fireiot.util.EditTextWithDel;
import com.suncreate.fireiot.util.PinyinUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * 城市分站列表 页面
 *
 * @author
 */

public class CityPickerLocationFragment extends BaseFragment {

    @Bind(R.id.et_search)
    EditTextWithDel mCitySeachName;
    @Bind(R.id.sidebar)
    SideBarForCityPicker mSideBar;
    @Bind(R.id.dialog)
    TextView mDialog;
    @Bind(R.id.country_lvcountry)
    ListView mSortListView;

    private List<CityWebSites> sourceDateList;
    private SortAdapterForCityPicker adapter;
    protected String CACHE_NAME = getClass().getName();
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    protected PageBean<CityWebSites> mBean;
    private String cityAreaId;  //城市区域ID,带出去

    //回调
    CallBackValue callBackValue;

    /**
     * fragment与activity产生关联是  回调这个方法
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //当前fragment从activity重写了回调接口  得到接口的实例化对象
        callBackValue = (CallBackValue) AppManager.getActivity(MainActivity.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initEvents();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.city_picker;
    }

    @Override
    protected void initData() {
        super.initData();

        mSideBar.setTextView(mDialog);

        onRequestStart();
    }

    protected void onRequestStart() {
        MonkeyApi.getSubstation(StringUtils.isEmpty(cityAreaId) ? "" : cityAreaId, mHandlerIn);
    }

    AsyncHttpResponseHandler mHandlerIn = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                throwable) {
            AppContext.showToastShort("网络错误，获取分站失败！");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            ResultBean<PageBean<CityWebSites>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<CityWebSites>>>() {
            }.getType());

            final PageBean<CityWebSites> result = resultBean.getResult();
            bindDataToListView(result);
        }
    };

    //共用的向listview填充数据方法
    private void bindDataToListView(PageBean<CityWebSites> resultBean) {
        List<CityWebSites> datas = resultBean.getItems();
        sourceDateList = new ArrayList<CityWebSites>();
        for (CityWebSites data : datas) {
            sourceDateList.add(data);
        }
        Collections.sort(sourceDateList, new PinyinComparator());
        adapter = new SortAdapterForCityPicker(getContext(), sourceDateList);
        mSortListView.addHeaderView(initHeadView());
        mSortListView.setAdapter(adapter);
    }

    private View initHeadView() {
        Bundle newBundle = new Bundle();
        View headView = getLayoutInflater(newBundle).inflate(R.layout.city_picker_headview, null);
        Button mBtnCityName = (Button) headView.findViewById(R.id.btn_city_name);
        if (!TextUtils.isEmpty(AppContext.city)) {
            mBtnCityName.setText(AppContext.city);
        } else {
            mBtnCityName.setText("未知");
        }
        return headView;
    }

    private void initEvents() {
        //设置右侧触摸监听
        mSideBar.setOnTouchingLetterChangedListener(new SideBarForCityPicker.OnTouchingLetterChangedListener() {
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
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                mTitle.setText(((CitySortModel) adapter.getItem(position - 1)).getName());
                //传送选择的城市名,并返回首页
                String citySelected = "";
                if (position > 0) {
                    citySelected = ((CityWebSites) adapter.getItem(position - 1)).getWebsiteName();
                } else {
                    citySelected = AppContext.city;
                }
                callBackValue.sendMessageValue(citySelected);
                String citySelectedStr = citySelected.replace("站", "市");
                AppContext.citycode = AppContext.getInstance().getCityCodeByName(citySelectedStr);
                getActivity().finish();
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mCitySeachName.addTextChangedListener(new TextWatcher() {
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

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CityWebSites> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = sourceDateList;
        } else {
            mSortList.clear();
            for (CityWebSites sortModel : sourceDateList) {
                String name = sortModel.getWebsiteName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    public List<CityWebSites> filledData(String[] date) {
        List<CityWebSites> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            CityWebSites sortModel = new CityWebSites();
            sortModel.setWebsiteName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setWebsiteLetter(sortString.toUpperCase());
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

    //定义一个回调接口，传值给activity
    public interface CallBackValue {
        public void sendMessageValue(String strValue);
    }
}
