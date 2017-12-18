package com.suncreate.pressuresensor.bean.carBrand;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SortManyAdapterForCarBrand extends BaseAdapter implements SectionIndexer {
    private List<CarBrand> list = null;
    private Context mContext;


    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected    =  new HashMap<Integer, Boolean>();;
    public SortManyAdapterForCarBrand(Context mContext, List<CarBrand> list) {
        this.mContext = mContext;
        this.list = list;
        initDate();
    }

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }



//    public SortManyAdapterForCarBrand(Context mContext,String[] beans)
//    {
//        this.mContext = mContext;
//        this.beans= beans;
//        isSelected = new HashMap<Integer, Boolean>();
//    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<CarBrand> list) {
        this.list = list;
        notifyDataSetChanged();




    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final CarBrand mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_many_select_car, null);
            viewHolder.ivIcon = (ImageView) view.findViewById(R.id.iv_car_icon);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_car_name);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.tv_catagory);
            viewHolder.car_brand_check =(CheckBox) view.findViewById(R.id.car_brand_check);

          view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.car_brand_check.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }

            }
        });

        // 根据isSelected来设置checkbox的选中状况
       viewHolder.car_brand_check.setChecked(getIsSelected().get(position));


        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getBrandLetter());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }

        String carbrandUrl = this.list.get(position).getBrandIcon();

        if (!StringUtils.isEmpty(carbrandUrl)) {

            String carbrandName = this.list.get(position).getBrandName();
            if (!StringUtils.isEmpty(carbrandName)) {
                viewHolder.tvTitle.setText(carbrandName);
            }

            String iconUrl = "carbrand/" + carbrandUrl + ".png";

            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = null;
            try {
                inputStream = assetManager.open(iconUrl);
                Bitmap imap = BitmapFactory.decodeStream(inputStream);
                viewHolder.ivIcon.setImageBitmap(imap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return view;
    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        ImageView ivIcon;
        CheckBox car_brand_check;
    }

        public static HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            SortManyAdapterForCarBrand.isSelected = isSelected;
        }
    public void addItem(List<CarBrand> items) {
        checkListNull();
        list.addAll(items);
        notifyDataSetChanged();
    }

    public void checkListNull() {
        if (list == null) {
            list = new ArrayList<CarBrand>();
        }
    }

    //
    public void clear() {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.clear();
        notifyDataSetChanged();
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getBrandLetter().charAt(0);
    }

    /**
     * 获取section首次出现位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getBrandLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}