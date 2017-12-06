package top.baselitch.widget.bannerholder;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class HolderAttr {
    private int indicatorResId;//指示器资源ID
    private int switchDuration;//切换持续时间
    private long looperTime;//轮播时间
    private boolean isAutoLooper;//是否自动轮播
    private boolean isBackgroup;//是否设为背景
    private BannerClickListenenr bannerChangeListenenr;//回调监听
    private HolderAttr(Builder builder) {
        this.indicatorResId = builder.indicatorResId;
        this.switchDuration = builder.switchDuration;
        this.looperTime = builder.looperTime;
        this.isAutoLooper = builder.isAutoLooper;
        this.isBackgroup = builder.isBackgroup;
        this.bannerChangeListenenr = builder.bannerChangeListenenr;
    }
    public static class Builder{
        private int indicatorResId;//指示器资源ID
        private int switchDuration;//切换持续时间
        private long looperTime;//轮播时间
        private boolean isAutoLooper;//是否自动轮播
        private boolean isBackgroup;//是否设为背景
        private BannerClickListenenr bannerChangeListenenr;//回调监听
        public Builder setIndicatorResId(int indicatorResId) {
            this.indicatorResId = indicatorResId;
            return this;
        }
        public Builder setAttributeSet(Context context,AttributeSet attr){
            TypedArray typedArray = context.obtainStyledAttributes(attr,R.styleable.HolderAttr);
            this.indicatorResId = typedArray.getResourceId(R.styleable.HolderAttr_indicatorResId,R.drawable.banner_holder_selector);
            this.switchDuration = typedArray.getInteger(R.styleable.HolderAttr_switchDuration,500);
            this.looperTime = typedArray.getInteger(R.styleable.HolderAttr_looperTime,1000);
            this.isAutoLooper = typedArray.getBoolean(R.styleable.HolderAttr_isAutoLooper,false);
            this.isBackgroup = typedArray.getBoolean(R.styleable.HolderAttr_isBackgroup,true);
            return this;
        }

        public Builder setSwitchDuration(int switchDuration) {
            this.switchDuration = switchDuration;
            return this;
        }

        public Builder setLooperTime(long looperTime) {
            this.looperTime = looperTime;
            return this;
        }

        public Builder setAutoLooper(boolean autoLooper) {
            isAutoLooper = autoLooper;
            return this;
        }

        public Builder setBackgroup(boolean backgroup) {
            isBackgroup = backgroup;
            return this;
        }

        public Builder setBannerClickListenenr(BannerClickListenenr bannerChangeListenenr) {
            this.bannerChangeListenenr = bannerChangeListenenr;
            return this;
        }
        public HolderAttr builder(){
            return new HolderAttr(this);
        }
    }

    public int getIndicatorResId() {
        return indicatorResId;
    }

    public int getSwitchDuration() {
        return switchDuration;
    }

    public long getLooperTime() {
        return looperTime;
    }

    public boolean isAutoLooper() {
        return isAutoLooper;
    }

    public boolean isBackgroup() {
        return isBackgroup;
    }

    public BannerClickListenenr getBannerClickListenenr() {
        return bannerChangeListenenr;
    }
}
