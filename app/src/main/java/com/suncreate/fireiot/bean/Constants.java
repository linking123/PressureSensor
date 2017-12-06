package com.suncreate.fireiot.bean;

import static com.suncreate.fireiot.bean.Constants.SERVICE_TYPE.BDECORATION;
import static com.suncreate.fireiot.bean.Constants.SERVICE_TYPE.CREPAIRS;
import static com.suncreate.fireiot.bean.Constants.SERVICE_TYPE.IALTERATION;
import static com.suncreate.fireiot.bean.Constants.SERVICE_TYPE.MAINTENANCE;

/**
 * 常量类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月27日 下午12:14:42
 */

public class Constants {

    public static final String INTENT_ACTION_USER_CHANGE = "net.oschina.action.USER_CHANGE";

    public static final String INTENT_ACTION_COMMENT_CHANGED = "net.oschina.action.COMMENT_CHANGED";

    public static final String INTENT_ACTION_NOTICE = "net.oschina.action.APPWIDGET_UPDATE";

    public static final String INTENT_ACTION_LOGOUT = "net.oschina.action.LOGOUT";

    public static final String WEICHAT_APPID = "wxa8213dc827399101";
    public static final String WEICHAT_SECRET = "5c716417ce72ff69d8cf0c43572c9284";

    public static final String QQ_APPID = "100942993";
    public static final String QQ_APPKEY = "8edd3cc7ca8dcc15082d6fe75969601b";

    public static final String INTENT_ACTION_IMAGE_PICKED = "INTENT_ACTION_IMAGE_PICKED";
    public static final String INTENT_ACTION_REQUIREMENT_NEW = "INTENT_ACTION_REQUIREMENT_NEW";
    public static final String INTENT_ACTION_CAR_NEW = "INTENT_ACTION_CAR_NEW";

    public static final String INTENT_ACTION_DEFAULT_CAR = "INTENT_ACTION_DEFAULT_CAR";
    public static final String AREA_JSON_NAME = "json/areacode.json";


    /**
     * 验证码类型
     */
    public static final class VERIFY_TYPE {
        /**
         * 注册
         **/
        public static final int REGISTER = 0;
        /**
         * 找回密码
         **/
        public static final int GET_BACK = 1;
    }

    /**
     * 技师服务类别
     */
    public static final class SERVICE_TYPE {
        /**
         * 汽车维修
         **/
        public static final String CREPAIRS = "CREPAIRS";
        /**
         * 保养修护
         **/
        public static final String MAINTENANCE = "MAINTENANCE";
        /**
         * 美容装饰
         **/
        public static final String BDECORATION = "BDECORATION";
        /**
         * 安装改装
         **/
        public static final String IALTERATION = "IALTERATION";
    }


    public static final String getServiceTypeByPosition(int position) {
        String serviceType;
        switch (position) {
            //分类 服务分类 1CREPAIRS-汽车维修 2MAINTENANCE-保养修护 BDECORATION-美容装饰 IALTERATION-安装改装
            case 0:
                serviceType = SERVICE_TYPE.CREPAIRS;
                break;
            case 1:
                serviceType = SERVICE_TYPE.MAINTENANCE;
                break;
            case 2:
                serviceType = SERVICE_TYPE.BDECORATION;
                break;
            case 3:
                serviceType = SERVICE_TYPE.IALTERATION;
                break;
            default:
                serviceType = "";
                break;
        }
        return serviceType;
    }

    /**
     * 服务类别值
     *
     * @param serviceType
     * @return
     */
    public static final String converServiceType(String serviceType) {
        String strServiceType = null;
        switch (serviceType) {
            case CREPAIRS:
                strServiceType = "汽车维修";
                break;
            case MAINTENANCE:
                strServiceType = "保养修护";
                break;
            case BDECORATION:
                strServiceType = "美容装饰";
                break;
            case IALTERATION:
                strServiceType = "安装改装";
                break;
        }
        return strServiceType;
    }

    /**
     * 技师店铺状态
     */
    public static final class STORE_STATUS {
        /**
         * 未开通
         **/
        public static final int NON_ACTIVATED = 0;
        /**
         * 审核中
         **/
        public static final int UNDER_REVIEW = 1;
        /**
         * 正常
         **/
        public static final int NORMAL = 2;
        /**
         * 已关闭
         **/
        public static final int CLOSED = 3;
    }

    /**
     * 服务订单状态
     */
    public static final class SERVICE_ORDER_TYPE {
        /**
         * 待报价
         **/
        public static final int QUOTE = 10;
        /**
         * 待确认
         **/
        public static final int CONFIRM = 20;
        /**
         * 待结算
         **/
        public static final int ACCOUNT = 30;//
        /**
         * 待评价
         **/
        public static final int EVALUATE = 40;//
        /**
         * 已完成
         **/
        public static final int COMPLETED = 50;
        /**
         * 已取消
         **/
        public static final int CANCELD = 0;
    }

    /**
     * 报价单类型
     */
    public static final class QUOTE_TYPE {
        /**
         * 报价单
         **/
        public static final String QUOTE = "1";
        /**
         * 结算单
         **/
        public static final String SETTLE = "2";
    }

    /**
     * 服务订单状态值
     *
     * @param orderStatus
     * @return
     */
    public static final String convertServiceOrderStatus(Integer orderStatus) {
        if (null == orderStatus) {
            return "未知";
        }
        String strOrderStatus;
        switch (orderStatus) {
            case 10:
                strOrderStatus = "待报价";
                break;
            case 20:
                strOrderStatus = "待确认";
                break;
            case 30:
                strOrderStatus = "待结算";//服务中
                break;
            case 40:
                strOrderStatus = "待评价";
                break;
            case 50:
                strOrderStatus = "已完成";
                break;
            case 0:
                strOrderStatus = "已取消";
                break;
            default:
                strOrderStatus = "未知";
                break;
        }
        return strOrderStatus;
    }

    /**
     * 配件订单状态值
     *
     * @param orderStatus
     * @return
     */
    public static final String convertGoodsOrderStatus(Integer orderStatus) {
        if (null == orderStatus) {
            return "未知";
        }
        String strOrderStatus;
        switch (orderStatus) {
            case 10:
                strOrderStatus = "待付款";
                break;
            case 20:
                strOrderStatus = "待发货";
                break;
            case 30:
                strOrderStatus = "待收货";
                break;
            case 40:
                strOrderStatus = "待评价";
                break;
            case 50:
                strOrderStatus = "已完成";
                break;
            case 0:
                strOrderStatus = "已取消";
                break;
            default:
                strOrderStatus = "未知";
                break;
        }
        return strOrderStatus;
    }

    /**
     * 店铺照片
     */
    public static final class STORE_IMG_TYPE {
        public static final String LOGO = "logo";//店铺LOGO
        public static final String CARD_POSITIVE = "cardPositive";//身份证正面
        public static final String CARD_REVERSE = "cardReverse";//身份证反面
        public static final String LICENCE = "licence";//营业执照
        public static final String RECOMMOND = "recommond";//技师推荐等
    }

    public static final class PUSH_MSG_TYPE{
        public static final int STATION_MSG = 0;//站内消息
        public static final int SERVICE_ORDER_DETAIL = 1;//服务订单详情
        public static final int ACCESSORY_ORDER_DETAIL = 2;//配件订单详情
    }
}
