package com.suncreate.pressuresensor.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.interf.Permission;
import com.suncreate.pressuresensor.ui.ShareDialog;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.FontSizeUtils;
import com.suncreate.pressuresensor.util.HTMLUtil;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 通用的详情fragment
 * Created by 火蚁 on 15/5/25.
 */
public abstract class BaseDetailFragment<T extends Serializable> extends BaseFragment {

    public static final String TAG = "BaseDetailFragment";

    protected int mId;

    //protected WebView mWebView;
    protected long mDetailId;//通用详情ID

    protected T mDetail;

    private AsyncTask<String, Void, T> mCacheTask;

    private ShareDialog mDialog;

    String[] locations = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected boolean checkLocationPermission() {
        return EasyPermissions.hasPermissions(getActivity(), locations);
    }

    protected void requestLocationPermission() {
        EasyPermissions.requestPermissions(this, "Android 6.0以上扫描蓝牙需要该权限", Permission.LOCATION, locations);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container,
                false);
        mId = getActivity().getIntent().getIntExtra("id", 0);
        ButterKnife.bind(this, view);
        initView(view);
        initData();
        requestData(true);
        return view;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
//        mWebView = (WebView) view.findViewById(R.id.webview);
//        UIHelper.initWebView(mWebView);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle b = getArguments();
        if (null != b) {
            mDetailId = b.getLong("id", 0);
        }
    }

    private void requestData(boolean refresh) {
        String key = getCacheKey();
        if (TDevice.hasInternet()
                && (!CacheManager.isExistDataCache(getActivity(), key) || refresh)) {
            sendRequestDataForNet();
        } else {
            readCacheData(key);
        }
    }

    @Override
    public void onDestroyView() {
        recycleWebView();
        mDialog = null;
        super.onDestroyView();
    }

    private void recycleWebView() {
//        if (mWebView != null) {
//            mWebView.setVisibility(View.GONE);
//            mWebView.removeAllViews();
//            mWebView.destroy();
//            mWebView = null;
//        }
    }

    private void readCacheData(String cacheKey) {
        cancelReadCache();
        mCacheTask = new CacheTask(getActivity()).execute(cacheKey);
    }

    private void cancelReadCache() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    protected TextHttpResponseHandler mDetailHandler = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("网络异常，加载失败");
            readCacheData(getCacheKey());
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<T> resultBean = AppContext.createGson().fromJson(responseString, getType());
//                    if (resultBean != null && resultBean.getSuccess() && resultBean.getAttributes().getItems() != null) {
                if (resultBean != null && resultBean.isSuccess()) {
                    executeOnLoadDataSuccess(resultBean.getResult());
                    saveCache(resultBean.getResult());
                } else if (resultBean != null && resultBean.getCode() == 3) {
                    AppContext.getInstance().logoutOperation();
                    UIHelper.showLoginActivity(getActivity());
                } else if (resultBean != null && resultBean.getMessage() != null) {
                    AppContext.showToastShort(resultBean.getMessage());
                } else {
                    executeOnLoadDataError();
                }
            } catch (Exception e) {
//                executeOnLoadDataError();
                onFailure(statusCode, headers, responseString, e);
            }
        }

        @Override
        public void onFinish() {
            super.onFinish();
            hideWaitDialog();
        }
    };

    private class CacheTask extends AsyncTask<String, Void, T> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected T doInBackground(String... params) {
            if (mContext.get() != null) {
                Serializable seri = CacheManager.readObject(mContext.get(),
                        params[0]);
                if (seri == null) {
                    return null;
                } else {
                    return (T) seri;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(T detail) {
            super.onPostExecute(detail);
            if (detail != null) {
                executeOnLoadDataSuccess(detail);
            } else {
                executeOnLoadDataError();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    protected void executeOnLoadDataSuccess(T detail) {
        this.mDetail = detail;
        if (this.mDetail == null) {
            executeOnLoadDataError();
            return;
        }

//        if (mWebView != null) {
//            mWebView.loadDataWithBaseURL("", this.getWebViewBody(detail), "text/html", "UTF-8", "");
//            // 显示存储的字体大小
//            mWebView.loadUrl(FontSizeUtils.getSaveFontSize());
//        }
    }

    protected void executeOnLoadDataError() {
        AppContext.showToast("数据加载失败");
    }

    protected void saveCache(T detail) {
        new SaveCacheTask(getActivity(), detail, getCacheKey()).execute();
    }

    private class SaveCacheTask extends AsyncTask<Void, Void, Void> {
        private final WeakReference<Context> mContext;
        private final Serializable seri;
        private final String key;

        private SaveCacheTask(Context context, Serializable seri, String key) {
            mContext = new WeakReference<Context>(context);
            this.seri = seri;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... params) {
            CacheManager.saveObject(mContext.get(), seri, key);
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.common_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    int i = 0;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                sendRequestDataForNet();
                return false;
            case R.id.font_size:
                showChangeFontSize();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog fontSizeChange;

    private void showChangeFontSize() {

        final String[] items = getResources().getStringArray(
                R.array.font_size);
        fontSizeChange = DialogHelp.getSingleChoiceDialog(getActivity(), items, FontSizeUtils.getSaveFontSizeIndex(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 更改字体大小
                FontSizeUtils.saveFontSize(i);
                // mWebView.loadUrl(FontSizeUtils.getFontSize(i));
                fontSizeChange.dismiss();
            }
        }).show();
    }

    // 刷新数据
    protected void refresh() {
        sendRequestDataForNet();
    }

    /***
     * 获取去除html标签的body
     *
     * @param body
     * @return
     */
    protected String getFilterHtmlBody(String body) {
        if (body == null)
            return "";
        return HTMLUtil.delHTMLTag(body.trim());
    }

    // 获取缓存的key
    protected abstract String getCacheKey();

    // 从网络中读取数据
    protected abstract void sendRequestDataForNet();

    // 解析数据
//    protected abstract T parseData(String responseString);

    // 返回填充到webview中的内容
    // protected abstract String getWebViewBody(T detail);

    public ShareDialog getDialog() {
        return mDialog;
    }

    protected abstract Type getType();
}
