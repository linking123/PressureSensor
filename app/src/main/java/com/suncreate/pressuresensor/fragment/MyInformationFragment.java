package com.suncreate.pressuresensor.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.base.BaseFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.Notice;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.user.User;
import com.suncreate.pressuresensor.cache.CacheManager;
import com.suncreate.pressuresensor.ui.MainActivity;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.TDevice;
import com.suncreate.pressuresensor.util.UIHelper;
import com.suncreate.pressuresensor.widget.AvatarView;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 登录用户中心页面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @author kymjs (http://my.oschina.net/kymjs)
 * @version 创建时间：2014年10月30日 下午4:05:47
 */
public class MyInformationFragment extends BaseFragment {

    public static final String TAG = MyInformationFragment.class.getSimpleName();

    public static final int AVATAR_TYPE_SMALL = 0;//小头像
    public static final int AVATAR_TYPE_BIG = 1;//大头像

    @Bind(R.id.iv_avatar_login)
    AvatarView mIvAvatar_login;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.error_layout)
    EmptyLayout mErrorLayout;
    @Bind(R.id.ll_user_container)
    View mUserContainer;
    @Bind(R.id.rl_user_unlogin)
    View mUserUnLogin;
    @Bind(R.id.iv_avatar_unlogin)
    View mIvAvatar_unlogin;
    @Bind(R.id.iv_name_unlogin)
    TextView mIvName_unlogin;
    //设置
    @Bind(R.id.ll_setting)
    View mSetting;

    private boolean mIsWatingLogin;
    String mUserRole;
    private User mInfo;
    private AsyncTask<String, Void, User> mCacheTask;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_LOGOUT:
                    if (mErrorLayout != null) {
                        mIsWatingLogin = true;
                        steupUser();
                    }
                    break;
                case Constants.INTENT_ACTION_USER_CHANGE:
                    requestData(true);
                    break;
                case Constants.INTENT_ACTION_NOTICE:
                    setNotice();
                    break;
            }
        }
    };

    private void steupUser() {
        if (mIsWatingLogin) {
            mUserContainer.setVisibility(View.GONE);
            mUserUnLogin.setVisibility(View.VISIBLE);
        } else {
            mUserContainer.setVisibility(View.VISIBLE);
            mUserUnLogin.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_ACTION_USER_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        setNotice();
        requestData(false);
    }

    public void setNotice() {
        if (MainActivity.mNotice != null) {
            Notice notice = MainActivity.mNotice;
            int atmeCount = notice.getAtmeCount();// @我
            int msgCount = notice.getMsgCount();// 留言
            int reviewCount = notice.getReviewCount();// 评论
            int newFansCount = notice.getNewFansCount();// 新粉丝
            int newLikeCount = notice.getNewLikeCount();// 获得点赞
            int activeCount = atmeCount + reviewCount + msgCount + newFansCount + newLikeCount;//

        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_information,
                container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestData(true);
    }

    @Override
    public void initView(View view) {
        mUserRole = AppContext.getInstance().getUserRole();

        //未登录时，点头像，跳转到登录页
        mIvAvatar_unlogin.setOnClickListener(this);
        mIvName_unlogin.setOnClickListener(this);
        //点击头像进入个人信息
        mIvAvatar_login.setOnClickListener(this);
        mTvName.setOnClickListener(this);
        //设置
        mSetting.setOnClickListener(this);

        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().isLogin()) {
                    requestData(true);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
    }

    private void fillUI() {
        if (mInfo == null)
            return;
        mTvName.setText(StringUtils.isEmpty(mInfo.getUserRealname()) ? mInfo.getUserName() : mInfo.getUserRealname());
//        MonkeyApi.getMyInformationHead(AppContext.getInstance().getLoginUserExt().getId(), new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                if (responseBody != null) {
//                    try {
//                        Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//                        mIvAvatar_login.setImageBitmap(imap);
//                        String storageState = Environment.getExternalStorageState();
//                        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//                            File savedir = new File(AppConfig.SAVE_HEAD_PATH);
//                            File savefile = new File(AppConfig.SAVE_HEAD_PATH, mInfo.getId() + ".jpg");
//                            if (!savedir.exists()) {
//                                savedir.mkdirs();
//                            }
//                            if (!savefile.exists()) {
//                                savefile.createNewFile();
//                            }
//                            FileOutputStream out = new FileOutputStream(savefile);
//                            imap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                            out.flush();
//                            out.close();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//            }
//        });
    }

    private void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {
            mIsWatingLogin = false;
            String key = getCacheKey();
            if (refresh || TDevice.hasInternet()
                    && (!CacheManager.isExistDataCache(getActivity(), key))) {
                fillUI();
            } else {
                readCacheData(key);
            }
        } else {
            mIsWatingLogin = true;
        }
        steupUser();
    }

    private void readCacheData(String key) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(key);
    }

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }

    private String getCacheKey() {
        return "my_information" + AppContext.getInstance().getLoginUid();
    }

    private class CacheTask extends AsyncTask<String, Void, User> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<>(context);
        }

        @Override
        protected User doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return (User) seri;
            }
        }

        @Override
        protected void onPostExecute(User info) {
            super.onPostExecute(info);
            if (info != null) {
                mInfo = info;
                // mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                // } else {
                // mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
                fillUI();
            }
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.ll_setting) {
            UIHelper.showSetting(getActivity());
        } else {
            //屏蔽登录验证，直接跳转到下级页面；完成调试后需要恢复这段注释
            if (mIsWatingLogin) {
                UIHelper.showLoginActivity(getActivity());
                return;
            }
            switch (id) {
                //登录，头像跳转到个人信息页
                case R.id.iv_avatar_login:
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_INFORMATION_DETAIL);
                    break;
                //登录，文字跳转到个人信息页
                case R.id.tv_name:
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_INFORMATION_DETAIL);
                    break;
                //未登录，头像跳转到登录页
                case R.id.iv_avatar_unlogin:
                    UIHelper.showLoginActivity(getContext());
                    break;
                //未登录，文字跳转到登录页
                case R.id.iv_name_unlogin:
                    UIHelper.showLoginActivity(getContext());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ImageView iv_search = (ImageView) getActivity().findViewById(R.id.iv_search);
        iv_search.setVisibility(View.INVISIBLE);
    }
}
