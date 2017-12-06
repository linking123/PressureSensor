package com.suncreate.fireiot.meidia;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.suncreate.fireiot.R;

/**
 * Created by JuQiu
 * on 16/7/18.
 * <p>
 * 动弹发布界面, 图片预览器
 * <p>
 * 提供图片预览/图片操作 返回选中图片等功能
 */

public class TweetPicturesPreviewer extends RecyclerView implements TweetSelectImageAdapter.Callback, SelectImageActivity.Callback {
    private static final int DEFAULT_IMAGE_COUNT = 3;

    private TweetSelectImageAdapter mImageAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private RequestManager mCurImageLoader;

    private int mImageCount;

    public TweetPicturesPreviewer(Context context) {
        super(context);
    }

    public TweetPicturesPreviewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TweetPicturesPreviewer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TweetPicturesPreviewer);
        mImageCount = ta.getInteger(R.styleable.TweetPicturesPreviewer_imageCount, DEFAULT_IMAGE_COUNT);
        ta.recycle();

        mImageAdapter = new TweetSelectImageAdapter(this, mImageCount == 1);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mImageCount);
        this.setLayoutManager(layoutManager);
        this.setAdapter(mImageAdapter);
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);

        ItemTouchHelper.Callback callback = new TweetPicturesPreviewerItemTouchCallback(mImageAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(this);
    }

    public void set(String[] paths) {
        mImageAdapter.clear();
        for (String path : paths) {
            mImageAdapter.add(path);
        }
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreClick() {
        SelectImageActivity.showImage(getContext(), mImageCount, true, mImageAdapter.getPaths(), this);
    }

    //传一张图片
    public void onLoadMoreClick1Img() {
        SelectImageActivity.showImage(getContext(), 1, true, mImageAdapter.getPaths(), this);
    }

    @Override
    public RequestManager getImgLoader() {
        if (mCurImageLoader == null) {
            mCurImageLoader = Glide.with(getContext());
        }
        return mCurImageLoader;
    }

    @Override
    public void onStartDrag(ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public String[] getPaths() {
        return mImageAdapter.getPaths();
    }

    @Override
    public void doSelectDone(String[] images) {
        set(images);
    }
}
