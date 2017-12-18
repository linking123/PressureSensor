package com.suncreate.pressuresensor.fragment.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.adapter.base.BaseListAdapter;
import com.suncreate.pressuresensor.bean.base.PageBean;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;

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
public abstract class BaseNoFreshListFragment<T> extends BaseFragment implements
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
    protected ListView mListView;
    protected EmptyLayout mErrorLayout;
    protected BaseListAdapter<T> mAdapter;
    protected TextHttpResponseHandler mHandler;
    protected PageBean<T> mBean;
    private String mTime;
    private View mFooterView;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mListView = (ListView) root.findViewById(R.id.listView);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mListView.setOnItemClickListener(this);
        setFooterType(TYPE_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);
        if (isNeedFooter())
            mListView.addFooterView(mFooterView);
    }

    @Override
    protected void initData() {
        super.initData();
        //when open this fragment,read the obj

        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);

        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onRequestError(statusCode);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    ResultBean<PageBean<T>> resultBean = AppContext.createGson().fromJson(responseString, getType());
//                    if (resultBean != null && resultBean.getSuccess() && resultBean.getAttributes().getItems() != null) {
                   // if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                    if(resultBean!=null){
                        onRequestSuccess(resultBean.getCode());
                        setListData(resultBean);
                    }
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
                if (mBean == null) {
                    mBean = new PageBean<>();
                    mBean.setItems(new ArrayList<T>());
                } else {
                    mRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.clear();
                            mAdapter.addItem(mBean.getItems());
                            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                            setFooterType(TYPE_NO_MORE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    }

    /**
     * request network data
     */
    protected void requestData() {
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


    protected void setListData(ResultBean<PageBean<T>> resultBean) {
        //is refresh
//        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());
            //cache the time
            mTime = resultBean.getTime();
            mBean.setItems(resultBean.getResult().getItems());
            mAdapter.clear();
            mBean.setPageNum(1);//刷新
            mAdapter.addItem(mBean.getItems());
//            mBean.setPrevPageToken(resultBean.getResult().getPrevPageToken());
            mExeService.execute(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), mBean, CACHE_NAME);
                }
            });
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
                mFooterText.setText(getResources().getString(R.string.footer_type_loading));
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_LOADING_SUCCESS:
                mFooterText.setVisibility(View.GONE);
                mFooterProgressBar.setVisibility(View.GONE);
                break;
        }
    }
}
