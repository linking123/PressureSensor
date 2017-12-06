package com.suncreate.fireiot.bean;

import com.suncreate.fireiot.R;
import com.suncreate.fireiot.fragment.AboutFireIotFragment;
import com.suncreate.fireiot.fragment.BrowserFragment;
import com.suncreate.fireiot.fragment.CityPickerLocationFragment;
import com.suncreate.fireiot.fragment.Field.FieldAppointmentFragment;
import com.suncreate.fireiot.fragment.Field.FieldDetailFragment;
import com.suncreate.fireiot.fragment.Field.FieldListFragment;
import com.suncreate.fireiot.fragment.LoginRegister.CarOwnerProtocalTextFragment;
import com.suncreate.fireiot.fragment.LoginRegister.GarageProtocalTextFragment;
import com.suncreate.fireiot.fragment.LoginRegister.RegisterFragment;
import com.suncreate.fireiot.fragment.LoginRegister.RegisterRetrieveFragment;
import com.suncreate.fireiot.fragment.LoginRegister.TechProtocalTextFragment;
import com.suncreate.fireiot.fragment.LoginRegister.VerifyFragment;
import com.suncreate.fireiot.fragment.MyInformationFragment;
import com.suncreate.fireiot.fragment.MyInformationFragmentDetail;
import com.suncreate.fireiot.fragment.SettingsFragment;
import com.suncreate.fireiot.fragment.SettingsNotificationFragment;
import com.suncreate.fireiot.fragment.UserCenterFragment;
import com.suncreate.fireiot.fragment.carBrand.CarBrandFragment;
import com.suncreate.fireiot.fragment.carBrand.CarDisplacementFragment;
import com.suncreate.fireiot.fragment.carBrand.CarModelFragment;
import com.suncreate.fireiot.fragment.carBrand.CarSeriesFragment;
import com.suncreate.fireiot.fragment.carBrand.CarYearModelFragment;
import com.suncreate.fireiot.fragment.master.AddressAddUseFragment;
import com.suncreate.fireiot.fragment.master.AddressNotingFragment;
import com.suncreate.fireiot.fragment.master.AddressSelectFragment;
import com.suncreate.fireiot.fragment.master.BankCardManagerFragment;
import com.suncreate.fireiot.fragment.master.GarageAppointmentFragment;
import com.suncreate.fireiot.fragment.master.GarageDetailFragment;
import com.suncreate.fireiot.fragment.master.GarageListFragment;
import com.suncreate.fireiot.fragment.master.GoodsDetailFragment;
import com.suncreate.fireiot.fragment.master.GoodsListFragment;
import com.suncreate.fireiot.fragment.master.GoodsSearchListFragment;
import com.suncreate.fireiot.fragment.master.GoodsSupplierShopDetailFragment;
import com.suncreate.fireiot.fragment.master.GoodsSupplierShopFragment;
import com.suncreate.fireiot.fragment.master.OrderFillDetailFragment;
import com.suncreate.fireiot.fragment.master.RoleChangeFragment;
import com.suncreate.fireiot.fragment.master.ShoppingCarFragment;
import com.suncreate.fireiot.fragment.master.StoreCertificationFragment;
import com.suncreate.fireiot.fragment.master.StoreSetsFragment;
import com.suncreate.fireiot.fragment.master.TechServersListFragment;
import com.suncreate.fireiot.fragment.master.TechSettingsFragment;
import com.suncreate.fireiot.fragment.master.TechnicianAppointmentFragment;
import com.suncreate.fireiot.fragment.master.TechnicianDetailFragment;
import com.suncreate.fireiot.fragment.master.TechnicianListFragment;
import com.suncreate.fireiot.fragment.me.MyCarManagerAddFragment;
import com.suncreate.fireiot.fragment.me.MyCarManagerEditFragment;
import com.suncreate.fireiot.fragment.me.MyCarManagerFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialBankCardAddFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterFreezeBalanceListFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterPaymentDetailDetailFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterPaymentDetailListFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterRechargeFinishFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterRechargeFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterWithDrawFinishFragment;
import com.suncreate.fireiot.fragment.me.MyFinancialCenterWithDrawFragment;
import com.suncreate.fireiot.fragment.me.MyPersonInfoEditEmailFragment;
import com.suncreate.fireiot.fragment.me.MyPersonInfoFragment;
import com.suncreate.fireiot.fragment.order.FieldOrderDetailFragment;
import com.suncreate.fireiot.fragment.order.FieldOrderFragment;
import com.suncreate.fireiot.fragment.order.FieldOrderSettleFragment;
import com.suncreate.fireiot.fragment.order.GoodsOrderDetailFragment;
import com.suncreate.fireiot.fragment.order.GoodsOrderFragment;
import com.suncreate.fireiot.fragment.order.ServiceOrderDetailFragment;
import com.suncreate.fireiot.fragment.order.ServiceOrderPhotoAfterFragment;
import com.suncreate.fireiot.fragment.order.ServiceOrderPhotoBeforeFragment;
import com.suncreate.fireiot.fragment.order.ServiceOrderQuotationFragment;
import com.suncreate.fireiot.fragment.order.ServiceOrderSettleFragment;
import com.suncreate.fireiot.fragment.order.ServicesOrderFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementDetailFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementPublishChooseAccessoryFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementPublishChooseSupplierFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementPublishFragment;
import com.suncreate.fireiot.fragment.requirement.AccessoryRequirementSupplierGoodsListFragment;
import com.suncreate.fireiot.fragment.requirement.GoodsTypeListFragment;
import com.suncreate.fireiot.fragment.requirement.ServiceRequirementDetailFragment;
import com.suncreate.fireiot.fragment.requirement.ServiceRequirementPublishFragment;
import com.suncreate.fireiot.ui.LoginActivity;
import com.suncreate.fireiot.viewpagerfragment.FinancialInOutDetailViewPagerFragment;
import com.suncreate.fireiot.viewpagerfragment.RequirementViewPagerFragment;
import com.suncreate.fireiot.viewpagerfragment.SearchViewPageFragment;

public enum SimpleBackPage {

//    TWEET_PUB(3, R.string.actionbar_title_tweetpub, TweetPubFragment.class),

    USER_CENTER(5, R.string.actionbar_title_user_center,
            UserCenterFragment.class),

    MY_INFORMATION(7, R.string.actionbar_title_my_information,
            MyInformationFragment.class),

    SETTING(15, R.string.actionbar_title_setting, SettingsFragment.class),

    SETTING_NOTIFICATION(16, R.string.actionbar_title_setting_notification,
            SettingsNotificationFragment.class),

    SEARCH(20, R.string.actionbar_title_search, SearchViewPageFragment.class),

    BROWSER(26, R.string.app_name, BrowserFragment.class),

    MY_INFORMATION_DETAIL(28, R.string.actionbar_title_my_information,
            MyInformationFragmentDetail.class),

    TECHNICIAN_LIST(44, R.string.actionbar_title_technician, TechnicianListFragment.class),

    TECHNICIAN_DETAIL(45, R.string.actionbar_title_technician_home, TechnicianDetailFragment.class),

    TECHNICIAN_APPOINTMENT(46, R.string.actionbar_appointment_confirm, TechnicianAppointmentFragment.class),

    GARAGE_LIST(47, R.string.actionbar_title_garage, GarageListFragment.class),

    GARAGE_DETAIL(48, R.string.actionbar_title_garage_home, GarageDetailFragment.class),

    GARAGE_APPOINTMENT(49, R.string.actionbar_appointment_confirm, GarageAppointmentFragment.class),

    VERIFY(50, R.string.verify_reget_psw, VerifyFragment.class),

    REGISTER_RETRIEVE(51, R.string.verify_register, RegisterRetrieveFragment.class),

    REGISTER(52, R.string.verify_register, RegisterFragment.class),

    FIELD(55, R.string.actionbar_title_repair, FieldListFragment.class),

    FIELD_APPOINTMENT(56, R.string.actionbar_appointment, FieldAppointmentFragment.class),

    ORDER_DETAIL(58, R.string.order_detail, GoodsOrderDetailFragment.class),

    LOGIN(59, R.string.btn_login, LoginActivity.class),

    GOODS_LIST(60, R.string.actionbar_title_accessories, GoodsListFragment.class),

    GOODS_DETAIL(61, R.string.actionbar_title_goods_details, GoodsDetailFragment.class),

    FIELD_DETAIL(62, R.string.actionbar_title_repair_home, FieldDetailFragment.class),

    SERVICE_REQUIREMENT_DETAIL(63, R.string.service_requirement_detail, ServiceRequirementDetailFragment.class),

    ACCESSORY_REQUIREMENT_DETAIL(65, R.string.accessory_requirement_detail, AccessoryRequirementDetailFragment.class),

    SHOPPINGCAR_LIST(67, R.string.txt_shopping_cart, ShoppingCarFragment.class),

    SERVICE_REQUIREMENT_PUBLISH(69, R.string.requirement_publish, ServiceRequirementPublishFragment.class),

    ACCESSORY_REQUIREMENT_PUBLISH(70, R.string.requirement_accessory_info, AccessoryRequirementPublishFragment.class),

    ORDER_FILL_DETAIL(71, R.string.actionbar_fill, OrderFillDetailFragment.class),

    MY_FINANCIAL_CENTER(73, R.string.txt_financial, MyFinancialCenterFragment.class),

    MY_FINANCIAL_CENTER_RECHARGE(74, R.string.txt_financial_recharge, MyFinancialCenterRechargeFragment.class),

    MY_FINANCIAL_CENTER_RECHARGE_FINISH(75, R.string.txt_financial_recharge_detail, MyFinancialCenterRechargeFinishFragment.class),

    MY_FINANCIAL_CENTER_WITHDRAW(76, R.string.txt_financial_withdraw, MyFinancialCenterWithDrawFragment.class),

    MY_FINANCIAL_CENTER_WITHDRAW_FINISH(77, R.string.txt_financial_withdraw, MyFinancialCenterWithDrawFinishFragment.class),

    MY_FINANCIAL_CENTER_PAYMENT_DETAIL(78, R.string.financial_center_payment_details, MyFinancialCenterPaymentDetailListFragment.class),

    MY_FINANCIAL_CENTER_PAYMENT_DETAIL_DETAIL(79, R.string.financial_center_payment_details, MyFinancialCenterPaymentDetailDetailFragment.class),

    CAROWNER_PROTOCAL_TEXT(80, R.string.car_owner_protocol_title, CarOwnerProtocalTextFragment.class),

    TECH_PROTOCAL_TEXT(81, R.string.tech_protocol_title, TechProtocalTextFragment.class),

    GARAGE_PROTOCAL_TEXT(82, R.string.garage_protocol_title, GarageProtocalTextFragment.class),


    MY_CAR_MANAGER(86, R.string.txt_vehicle_manage, MyCarManagerFragment.class),

    MY_CAR_MANAGER_ADD(87, R.string.txt_vehicle_manage_add, MyCarManagerAddFragment.class),

    MY_CAR_MANAGER_EDIT(88, R.string.txt_vehicle_manage_edit, MyCarManagerEditFragment.class),

    ACCESSORY_REQUIREMENT(89, R.string.main_tab_name_tweet, AccessoryRequirementFragment.class),

    ACCESSORY_REQUIREMENT_PUBLISH_CHOOSE_ACCESSORY(90, R.string.requirement_publish_choose_business, AccessoryRequirementPublishChooseAccessoryFragment.class),

    GOODS_ORDER(91, R.string.goods_orders, GoodsOrderFragment.class),

    SERVICE_ORDER(92, R.string.service_orders, ServicesOrderFragment.class),

    FIELD_ORDER(93, R.string.place_orders, FieldOrderFragment.class),

    FIELD_ORDER_DETAIL(94, R.string.place_orders, FieldOrderDetailFragment.class),

    SERVICE_ORDER_DETAIL(95, R.string.service_orders, ServiceOrderDetailFragment.class),

    FINANCIAL_IN_OUT_DETAIL(96, R.string.financial_center_payment_details, FinancialInOutDetailViewPagerFragment.class),

    ACCESSORY_REQUIREMENT_PUBLISH_CHOOSE_SUPPLIER(97, R.string.requirement_publish_choose_supplier, AccessoryRequirementPublishChooseSupplierFragment.class),

    CITY_PICKER_LOCATION(100, R.string.select_city, CityPickerLocationFragment.class),

    CAR_BRAND(101, R.string.car_brand_choose, CarBrandFragment.class),

    CAR_MODEL(102, R.string.car_model_choose, CarModelFragment.class),

    CAR_SERIES(103, R.string.car_series_choose, CarSeriesFragment.class),

    CAR_DISPLACEMENT(104, R.string.car_displacement_choose, CarDisplacementFragment.class),

    CAR_YEAR_MODEL(105, R.string.car_year_choose, CarYearModelFragment.class),

    GOODS_SUPPLIER_SHOP(106, R.string.goods_supplier_shop, GoodsSupplierShopFragment.class),

    GOODS_SUPPLIER_SHOP_DETAIL(107, R.string.goods_supplier_shop_detail, GoodsSupplierShopDetailFragment.class),

    ROLE_CHANGE(108, R.string.change_role, RoleChangeFragment.class),

    SERVERS_DETAIL(109, R.string.actionbar_title_servers, TechServersListFragment.class),

    ADDRESS_NOTING(110, R.string.requirement_detail, AddressNotingFragment.class),

    // STORE_SETTINGS(111, R.string.actionbar_title_store_settings, StoreSettingsFragment.class),

    STORE_CERTIFICATION(112, R.string.actionbar_title_store_certification, StoreCertificationFragment.class),

//    CAR_BRAND_MANY_FRAGMENT(113,R.string.car_brand_many,CarBrandManyFragment.class),

//    SERVICE_TAKE_PHOTO_PRE(114, R.string.take_photo_before_service, ServiceOrderPhotoBeforeFragment.class),
//
//    SERVICE_TAKE_PHOTO_POST(115, R.string.take_photo_after_service, ServiceOrderPhotoAfterFragment.class),
//
//    ORDER_OFFER_TAKE_PHOTO(116, R.string.take_photo_quotation, ServiceOrderQuotationFragment.class),

    REQUIREMNET_VIEW_PAGE(117, R.string.main_tab_name_tweet, RequirementViewPagerFragment.class),

    TECH_SETTINGS(118, R.string.actionbar_title_tell_settings, TechSettingsFragment.class),

    ADDRESS_SELECT(121, R.string.txt_shipping_address, AddressSelectFragment.class),

    ADDRESS_ADD_USE(122, R.string.txt_shipping_address_add, AddressAddUseFragment.class),

    ADDRESS_GOODS_SEARCH(126, R.string.goods_search, GoodsSearchListFragment.class),

    SERVICE_ORDER_PHOTO_BEFORE_ACTION(131, R.string.service_order_photo_before_action, ServiceOrderPhotoBeforeFragment.class),

    SERVICE_ORDER_PHOTO_AFTER_ACTION(132, R.string.service_order_photo_after_action, ServiceOrderPhotoAfterFragment.class),

    SERVICE_ORDER_QUOTATION_ACTION(133, R.string.service_order_quotation_action, ServiceOrderQuotationFragment.class),

    SERVICE_ORDER_SETTLE_ACTION(134, R.string.service_order_settle_action, ServiceOrderSettleFragment.class),

    BANK_CARD_MANAGER(139, R.string.bank_card_manager, BankCardManagerFragment.class),

    BANK_CARD_MANAGER_ADD(140, R.string.bank_card_manager_add, MyFinancialBankCardAddFragment.class),

    FINANCIAL_CENTER_FREEZE_BALANCE_ORDER(141, R.string.freezeBalanceOrderList, MyFinancialCenterFreezeBalanceListFragment.class),

    FIELD_ORDER_SETTLE_ACTION(142, R.string.service_order_settle_action, FieldOrderSettleFragment.class),

    GOODS_TYPE_LIST(143, R.string.str_goods_type, GoodsTypeListFragment.class),

    ACCESSORY_REQUIRE_SUPPLIER_GOODS_LIST(144, R.string.str_supplier_goods_list, AccessoryRequirementSupplierGoodsListFragment.class),

    STORE_SET(145, R.string.actionbar_title_store_settings, StoreSetsFragment.class),


    /* fire start */
//    SCANNING(301, R.string.index_scan_ing, ScanningFragment.class),
//    TYPE_IN_INFO(302, R.string.type_in_info, ScanInfoSettingFragment.class),
//    SCAN_RECORD(303, R.string.scan_record, ScanRecordListFragment.class),
//    SCAN_RECORD_DETAIL(304, R.string.scan_record_detail, ScanRecordDetailFragment.class),
    ABOUT_FIRE_IOT(305, R.string.actionbar_title_about_fireiot, AboutFireIotFragment.class),
    MY_PERSON_INFO(306, R.string.txt_user, MyPersonInfoFragment.class),
    MY_PERSON_INFO_EDIT_NAME(307, R.string.txt_user_name_edit, MyPersonInfoEditEmailFragment.class),
    MY_PERSON_INFO_EDIT_PHONE(308, R.string.txt_user_phone_edit, MyPersonInfoEditEmailFragment.class),
    MY_PERSON_INFO_EDIT_EMAIL(309, R.string.txt_user_email_edit, MyPersonInfoEditEmailFragment.class),

    ;

    /* fire end */
    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }


}
