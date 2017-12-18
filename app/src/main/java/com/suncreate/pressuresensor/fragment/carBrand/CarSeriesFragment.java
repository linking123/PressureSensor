package com.suncreate.pressuresensor.fragment.carBrand;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.carBrand.CarBrand;
import com.suncreate.pressuresensor.bean.carBrand.PinyinComparatorForCarBrand;
import com.suncreate.pressuresensor.bean.carBrand.SortAdapterForCarBrand;
import com.suncreate.pressuresensor.fragment.base.BaseFragment;
import com.suncreate.pressuresensor.util.PinyinUtils;
import com.suncreate.pressuresensor.util.UIHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * 选择车系
 */
public class CarSeriesFragment extends BaseFragment {

    @Bind(R.id.car_list_view)
    ListView mSortListView;

    private List<CarBrand> SourceDateList;
    private SortAdapterForCarBrand adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        initEvents();
        setAdapter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.car_series_choose_box;
    }

    private void setAdapter() {
        SourceDateList = filledData(getResources().getStringArray(R.array.car_brands));
        Collections.sort(SourceDateList, new PinyinComparatorForCarBrand());
        adapter = new SortAdapterForCarBrand(getContext(), SourceDateList);
        mSortListView.setAdapter(adapter);
    }

    private void initEvents() {

        //ListView的点击事件
        mSortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                mTitle.setText(((CarBrandSortModel) adapter.getItem(position - 1)).getName());
//                Log.e(TAG, "点击了： position: " + mTitle.getText().toString().trim());
                //Toast.makeText(getContext(), ((CarBrandSortModel) adapter.getItem(position - 1)).getName(), Toast.LENGTH_SHORT).show();

                UIHelper.showSimpleBack(getContext(), SimpleBackPage.CAR_DISPLACEMENT);
            }
        });

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
        return mSortList;
    }

}
