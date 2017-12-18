package com.suncreate.pressuresensor.util;

import android.util.Log;

import com.suncreate.pressuresensor.bean.City;
import com.suncreate.pressuresensor.bean.District;
import com.suncreate.pressuresensor.bean.user.AreaJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 357个中国城市
 *
 * @author ck
 * @since 2014年2月7日 16:20:32
 */
public class CityDataUtils {
    private static final String TAG = CityDataUtils.class.getSimpleName();

    public static final int CITY_AREA_CODE = 1;
    public static final int DISTRICT_AREA_CODE = 2;

    /**
     * 城市json文件-->城市对象list
     *
     * @param cityJson
     * @return
     */
    public static List<City> getSampleCityList(String cityJson) {
        List<City> list = new ArrayList<>();
        try {
            JSONObject jo1 = new JSONObject(cityJson);
            JSONArray ja1 = jo1.getJSONArray("cities");

            for (int i = 0; i < ja1.length(); i++) {
                JSONObject jo = ja1.getJSONObject(i);
                String cityName = jo.getString("n");
                String cityCode = jo.getString("c");
                list.add(new City(cityCode, cityName, PinYin.getPinYin(cityName)));
            }
        } catch (Exception e) {
            Log.e(TAG, "Get CityList Fail:" + e);
            return null;
        }
        return list;
    }

    /**
     * 根据编码获取对应城市或者区县名称
     *
     * @param areaList
     * @param areaCode
     * @return
     */
    public static String getNameByCode(List<AreaJson> areaList, String areaCode) {
        String cityName = "";
        try {
            for(AreaJson aj : areaList){
                if(aj.getC().equals(areaCode)){
                    cityName = aj.getN();
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Get AreaName Fail:" + e);
            cityName = "未知";
        }
        return cityName;
    }

    /**
     * 根据名称获取对应城市编码
     *
     * @param areaList
     * @param areaName
     * @return
     */
    public static String getCodeByName(List<AreaJson> areaList, String areaName) {
        String cityCode = "";
        try {
            for(AreaJson aj : areaList){
                if(aj.getN().equals(areaName)){
                    cityCode = aj.getC();
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Get AreaName Fail:" + e);
            cityCode = "341010";
        }
        return cityCode;
    }

    /**
     * 根据名称和城市编码获取对应区县编码
     *
     * @param areaList
     * @param areaName
     * @param cityCode
     * @return
     */
    public static String getCodeByName(List<AreaJson> areaList, String areaName, String cityCode) {
        String areaCode = "";
        String cityCodePre = cityCode.substring(0, 4);
        try {
            for(AreaJson aj : areaList){
                if (cityCodePre.equals(aj.getC().substring(0, 4)) && aj.getN().equals(areaName)) {
                    areaCode =aj.getC();
                    break;
                }
            }
        } catch (Exception e) {
            areaCode = "341010";
        }
        return areaCode;
    }

    /**
     * 根据城市编码获取区县集合
     *
     * @param jsonString
     * @param cityCode
     * @return
     */
    public static List<District> getDistrictListByCityCode(String jsonString, String cityCode) {
        List<District> list = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(cityCode) || 6 != cityCode.length()) {
                return list;
            } else {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("district");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    if (jo.getString("c").substring(0, 4).equals(cityCode.substring(0, 4))) {
                        String code = jo.getString("c");
                        String name = jo.getString("n");
                        list.add(new District(code, name));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Get DistrictList Fail:" + e);
        }
        return list;
    }
}
