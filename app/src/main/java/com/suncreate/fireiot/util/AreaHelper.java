package com.suncreate.fireiot.util;

import android.content.Context;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import com.suncreate.fireiot.bean.citypicker.JsonFileReader;
import com.suncreate.fireiot.bean.citypicker.ProvinceBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2016/10/27.
 */

public class AreaHelper {
    //  省份
    public  static ArrayList<ProvinceBean> provinceBeanList = new ArrayList<>();
    //  城市
    public static ArrayList<String> cities;
    public static ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    public static ArrayList<String> district;
    public static ArrayList<List<String>> districts;
    public static ArrayList<List<List<String>>> districtList = new ArrayList<>();

    public static String address;
    //选择地址
    public static OptionsPickerView chooseAddress(OptionsPickerView addrOptions,Context context){
        //  创建地址选项选择器
        addrOptions = new OptionsPickerView(context);
        // 获取json数据
        String province_data_json = JsonFileReader.getJson(context, "json/province_data.json");
        // 解析json数据
        parseJson(province_data_json);

        // 设置三级联动效果
        addrOptions.setPicker(provinceBeanList, cityList, districtList, true);

        // 设置选择的三级单位
        //addrOptions.setLabels("省", "市", "区");
        // 设置选择器标题
        addrOptions.setTitle("选择区域");

        // 设置是否循环滚动
        addrOptions.setCyclic(false, false, false);

        // 设置默认选中的三级项目
        addrOptions.setSelectOptions(0, 0, 0);
        // 监听确定选择按钮

        return addrOptions;
    }

    //  解析json填充集合
    public static void parseJson(String str) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(str);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                org.json.JSONObject provinceObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String provinceName = provinceObject.getString("name");
                provinceBeanList.add(new ProvinceBean(provinceName));
                //  获取城市数组
                JSONArray cityArray = provinceObject.optJSONArray("city");
                cities = new ArrayList<>();//   声明存放城市的集合
                districts = new ArrayList<>();//声明存放区县集合的集合
                //  遍历城市数组
                for (int j = 0; j < cityArray.length(); j++) {
                    //  获取城市对象
                    org.json.JSONObject cityObject = cityArray.optJSONObject(j);
                    //  将城市放入集合
                    String cityName = cityObject.optString("name");
                    cities.add(cityName);
                    district = new ArrayList<>();// 声明存放区县的集合
                    //  获取区县的数组
                    JSONArray areaArray = cityObject.optJSONArray("area");
                    //  遍历区县数组，获取到区县名称并放入集合
                    for (int k = 0; k < areaArray.length(); k++) {
                        String areaName = areaArray.getString(k);
                        district.add(areaName);
                    }
                    //  将区县的集合放入集合
                    districts.add(district);
                }
                //  将存放区县集合的集合放入集合
                districtList.add(districts);
                //  将存放城市的集合放入集合
                cityList.add(cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void onOptionsSelect(int options1, int option2, int options3, TextView view){
        // 返回的分别是三个级别的选中位置
        String city = AreaHelper.provinceBeanList.get(options1).getPickerViewText();

        // 如果是直辖市或者特别行政区只设置市和区/县
        if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
            AreaHelper.address = AreaHelper.provinceBeanList.get(options1).getPickerViewText()
                    + " " + AreaHelper.districtList.get(options1).get(option2).get(options3);
        } else {
            AreaHelper.address = AreaHelper.provinceBeanList.get(options1).getPickerViewText()
                    + " " + AreaHelper.cityList.get(options1).get(option2)
                    + " " + AreaHelper.districtList.get(options1).get(option2).get(options3);
        }
        view.setText(AreaHelper.address);
    }
}
