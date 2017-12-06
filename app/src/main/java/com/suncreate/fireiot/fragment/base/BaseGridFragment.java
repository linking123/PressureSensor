package com.suncreate.fireiot.fragment.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.adapter.base.BaseListAdapter;
import com.suncreate.fireiot.bean.base.PageBean;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.cache.CacheManager;
import com.suncreate.fireiot.ui.empty.EmptyLayout;
import com.suncreate.fireiot.widget.SuperRefreshLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.msebera.android.httpclient.Header;

/**
 * T as the base bean
 * Created by huanghaibin
 * on 16-5-23.
 */
public abstract class BaseGridFragment<T> extends BaseFragment implements
        SuperRefreshLayout.SuperRefreshLayoutListener,
        AdapterView.OnItemClickListener, BaseListAdapter.Callback,
        View.OnClickListener {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;
    public static final int TYPE_LOADING_SUCCESS = 5;
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    protected String CACHE_NAME = getClass().getName();
    protected GridView mGridView;
    protected SuperRefreshLayout mRefreshLayout;
    protected EmptyLayout mErrorLayout;
    protected BaseListAdapter<T> mAdapter;
    protected boolean mIsRefresh = true;
    protected TextHttpResponseHandler mHandler;
    protected PageBean<T> mBean;
    private String mTime;
    private View mFooterView;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;
    private boolean isLoadFinished;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_grid;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mGridView = (GridView) root.findViewById(R.id.gridView);
        mRefreshLayout = (SuperRefreshLayout) root.findViewById(R.id.superRefreshLayout);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mGridView.setOnItemClickListener(this);
        setFooterType(TYPE_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        //when open this fragment,read the obj

        mAdapter = getListAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == (view.getCount() - 1) && !isLoadFinished) {
                        onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onRequestError(statusCode);
                onRequestFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    ResultBean<PageBean<T>> resultBean = AppContext.createGson().fromJson(responseString, getType());
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                        onRequestSuccess(resultBean.getCode());
                        setListData(resultBean);
                    } else {
                        if (mIsRefresh) {
                            mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
                        } else {
                            setFooterType(TYPE_NO_MORE);
                            mRefreshLayout.setNoMoreData();
                        }
                    }
                    onRequestFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        };

        mExeService.execute(new Runnable() {
            @Override
            public void run() {
                mBean = (PageBean<T>) CacheManager.readObject(getActivity(), CACHE_NAME);
                //if is the first loading
                if (mBean != null && mBean.getItems().size() > 0) {
                    mRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();
                            mAdapter.addItem(mBean.getItems());
                            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                            mRefreshLayout.setVisibility(View.VISIBLE);
                            mRefreshLayout.setNoMoreData();
//                            onRefreshing();
                            setFooterType(TYPE_NO_MORE);
                        }
                    });
                } else {
                    mBean = new PageBean<>();
                    mBean.setItems(new ArrayList<T>());
                    onRefreshing();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        onRefreshing();
    }

    @Override
    public void onRefreshing() {
        mIsRefresh = true;
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    /**
     * request network data
     */
    protected void requestData() {
        if (mIsRefresh) {
            if (mBean != null) {
                mBean.setPageNum(0);
            }
        }
        onRequestStart();
        setFooterType(TYPE_LOADING);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    protected void onRequestStart() {

    }

    protected void onRequestSuccess(int code) {

    }

    /**
     * save readed list
     *
     * @param fileName fileName
     * @param key      key
     */
    protected void saveToReadedList(String fileName, String key) {

        // 放入已读列表
        AppContext.putReadedPostList(fileName, key, "true");
    }

    /**
     * update textColor
     *
     * @param title   title
     * @param content content
     */
    protected void updateTextColor(TextView title, TextView content) {
        if (title != null) {
            title.setTextColor(getResources().getColor(R.color.count_text_color_light));
        }
        if (content != null) {
            content.setTextColor(getResources().getColor(R.color.count_text_color_light));
        }
    }

    protected void onRequestError(int code) {
        setFooterType(TYPE_NET_ERROR);
        if (mAdapter.getDatas().size() == 0)
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    protected void onRequestFinish() {
        onComplete();
    }

    protected void onComplete() {
        mRefreshLayout.onLoadComplete();
        //setFooterType(TYPE_LOADING_SUCCESS);
        mIsRefresh = false;
    }

    protected void setListData(ResultBean<PageBean<T>> resultBean) {
        //is refresh
//        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());
        if (mIsRefresh) {
            //cache the time
            mTime = resultBean.getTime();
            mBean.setItems(resultBean.getResult().getItems());
            mAdapter.clear();
            mBean.setPageNum(1);//刷新
            mAdapter.addItem(mBean.getItems());
//            mBean.setPrevPageToken(resultBean.getResult().getPrevPageToken());
            mRefreshLayout.setCanLoadMore();
            mExeService.execute(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), mBean, CACHE_NAME);
                }
            });
        } else {
            mBean.setPageNum(resultBean.getResult().getPageNum());
            mAdapter.addItem(resultBean.getResult().getItems());
        }
        if (resultBean.getResult().getItems().size() < AppContext.PAGE_SIZE) {
            setFooterType(TYPE_NO_MORE);
            mRefreshLayout.setNoMoreData();
            isLoadFinished = true;
        }
        if (mAdapter.getDatas().size() > 0) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NODATA_ENABLE_CLICK);
        }
    }

    @Override
    public Date getSystemTime() {
        return new Date();
    }

    protected abstract BaseListAdapter<T> getListAdapter();

    protected abstract Type getType();

    protected boolean isNeedFooter() {
        return true;
    }

    protected void setFooterType(int type) {
        switch (type) {
            case TYPE_NORMAL:
            case TYPE_LOADING:
                mFooterText.setText("正在加载中…");
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText("网络错误");
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText("加载失败");
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText("没有更多数据");
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_LOADING_SUCCESS:
                mFooterText.setVisibility(View.GONE);
                mFooterProgressBar.setVisibility(View.GONE);
                break;
        }
    }
}
