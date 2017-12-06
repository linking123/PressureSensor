package top.baselitch.widget.bannerholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BannerHolderView extends FrameLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private HolderAttr.Builder holerAttBuilder;//参数构建器
    private HolderAttr holerAttr;//参数
    private HolderViewPager mViewPager;
    private RadioGroup radioGroup;//指示器
    private List<RadioButton> buttons;//指示器控件集合
    private Context context;
    private List<Bitmap> bitmaps;//图片数据集合
    private List<ImageView> imageViews;//图片展示控件集合
    private int bannerCount;
    private int currentItem;//当前位置
    private Handler handler = new Handler();
    public BannerHolderView(Context context) {
        super(context,null);
    }

    public BannerHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        bitmaps = new ArrayList<>();
        imageViews = new ArrayList<>();
        buttons = new ArrayList<>();
        holerAttBuilder = new HolderAttr.Builder();
        holerAttBuilder.setAttributeSet(context,attrs);
        holerAttr = holerAttBuilder.builder();
        initView();
    }

    public BannerHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(){
        FrameLayout layout = (FrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.widget_banner_holder,null);
        mViewPager = (HolderViewPager) layout.findViewById(R.id.banner_holder_vp);
        initViewPagerScroll(holerAttr.getSwitchDuration());
        radioGroup = (RadioGroup) layout.findViewById(R.id.banner_selects);
        addView(layout);
        initViewPager();
    }

    /**
     * 初始化Viewpager滑动切换持续时间
     * @param time 持续时间
     */
    private void initViewPagerScroll(int time){
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(mViewPager,bannerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initViewPager(){
        mViewPager.setFocusable(true);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }
    /**
     * 获取参数构建器
     * @return 参数构建器对象
     */
    public  HolderAttr.Builder getHolerAttr() {
        return holerAttBuilder;
    }

    /**
     * 设置参数
     * @param holerAttr 参数对象
     */
    public void setHolerAttr(HolderAttr.Builder holerAttr) {
        this.holerAttBuilder = holerAttr;
        this.holerAttr = holerAttr.builder();
    }

    /**
     * 设置图片对象集合
     * @param bitmaps Bitmap集合
     */
    public void setHolderBitmaps(List<Bitmap> bitmaps){
        this.bitmaps.clear();
        this.bitmaps.addAll(bitmaps);
        setImageViews();
    }

    /**
     * 设置ImageView集合
     */
    private void setImageViews(){
        if (bitmaps == null || bitmaps.size() <= 0) {
            return;
        }else if(bitmaps.size()==1){
            mViewPager.setScroller(false);
        }else{
            mViewPager.setScroller(true);
        }
        bannerCount = bitmaps.size();
        buttons.clear();
        for(int i=0;i<=bannerCount+1;i++){
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(params);
            Bitmap bitmap = null;
            if(i==0){
                bitmap = bitmaps.get(bannerCount-1);
            }else if(i==bannerCount+1){
                bitmap = bitmaps.get(0);
            }else{
                addIndex();
                bitmap = bitmaps.get(i-1);
            }
            if(holerAttr.isBackgroup()){
                Drawable drawable = new BitmapDrawable(bitmap);
                imageView.setBackgroundDrawable(drawable);
            }else{
                imageView.setImageBitmap(bitmap);
            }
            imageView.setTag(i);
            imageView.setOnClickListener(this);
            imageViews.add(imageView);
        }
        updateView();
    }

    /**
     * 添加指示器
     */
    private void addIndex(){
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,0,10,0);
        RadioButton radioButton = new RadioButton(context);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setButtonDrawable(holerAttr.getIndicatorResId());
        radioButton.setEnabled(false);
        buttons.add(radioButton);
    }
    /**
     * 更新View
     */
    private void updateView(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                mPagerAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(1,false);
                for(int i=0;i<buttons.size();i++){
                    RadioButton button = buttons.get(i);
                    button.setChecked(i==0?true:false);
                    radioGroup.addView(button);
                }
                if(bitmaps.size()>1&&holerAttr.isAutoLooper()){
                    handler.post(new Runnable() {
                        @Override

                        public void run() {
                            startAutoLooper();
                        }
                    });
                }
            }
        });
    }

    private void startAutoLooper(){
        handler.removeCallbacks(task);
        handler.postDelayed(task,holerAttr.getLooperTime());
    }
    private void stopAutoLooper(){
        handler.removeCallbacks(task);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if(holerAttr.isAutoLooper()){
                currentItem = currentItem%(bitmaps.size()+1)+1;
                if(currentItem == 1){
                    mViewPager.setCurrentItem(currentItem,false);
                    handler.postDelayed(task,holerAttr.getLooperTime());
                }else if(currentItem == bitmaps.size()+1){
                    mViewPager.setCurrentItem(currentItem,true);
                    handler.postDelayed(task,holerAttr.getLooperTime());
                }else{
                    mViewPager.setCurrentItem(currentItem,true);
                    handler.postDelayed(task,holerAttr.getLooperTime());
                }
            }
        }
    };

    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return imageViews.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }
    };

    private Scroller bannerScroller = new Scroller(getContext()){
        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, holerAttr.getSwitchDuration());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, holerAttr.getSwitchDuration());
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(positionOffset==0&&position<1){
            position = bitmaps.size();
            mViewPager.setCurrentItem(position,false);
        }else if(positionOffset==0&&position>bitmaps.size()){
            position = 1;
            mViewPager.setCurrentItem(position,false);
        }
        currentItem = position;
    }

    @Override
    public void onPageSelected(int position) {
        if(position==bitmaps.size()+1){//到最后一页
            checkOutButton(0);
        }else if(position==0){//到起始页
            checkOutButton(buttons.size()-1);
        }else{
            checkOutButton(position-1);
        }
    }
    private void checkOutButton(int p){
        for(int i=0;i<buttons.size();i++){
            buttons.get(i).setChecked(i==p);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        currentItem = mViewPager.getCurrentItem();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(holerAttr.isAutoLooper()){
            int action = ev.getAction();
            if(action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE){
                startAutoLooper();
            }else if(action == MotionEvent.ACTION_DOWN){
                stopAutoLooper();
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public void onClick(View v) {
        BannerClickListenenr clickListenenr = holerAttr.getBannerClickListenenr();
        if(clickListenenr != null){
            clickListenenr.onBannerClick((Integer) v.getTag());
        }
    }



}
