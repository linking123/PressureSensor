package com.suncreate.fireiot.bean.carBrand;

import java.util.Comparator;

/**
 * Created by Administrator on 2016/11/16.
 */

public class PinyinComparatorForCarBrand  implements Comparator<CarBrand> {

    @Override
    public int compare(CarBrand o1, CarBrand o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o1.getBrandLetter().equals("@")
                || o2.getBrandLetter().equals("#")) {
            return -1;
        } else if (o1.getBrandLetter().equals("#")
                || o2.getBrandLetter().equals("@")) {
            return 1;
        } else {
            return o1.getBrandLetter().compareTo(o2.getBrandLetter());
        }
    }
}
