package com.suncreate.pressuresensor.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import com.baidu.mapapi.utils.OpenClientUtil;
import com.suncreate.pressuresensor.AppConfig;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.bean.Banner;
import com.suncreate.pressuresensor.bean.Comment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.Notice;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.fragment.BrowserFragment;
import com.suncreate.pressuresensor.fragment.LoginRegister.RegisterRetrieveFragment;
import com.suncreate.pressuresensor.fragment.LoginRegister.VerifyFragment;
import com.suncreate.pressuresensor.fragment.master.GoodsDetailFragment;
import com.suncreate.pressuresensor.interf.ICallbackResult;
import com.suncreate.pressuresensor.interf.OnWebViewImageListener;
import com.suncreate.pressuresensor.service.DownloadService;
import com.suncreate.pressuresensor.service.DownloadService.DownloadBinder;
import com.suncreate.pressuresensor.service.NoticeService;
import com.suncreate.pressuresensor.ui.LoginActivity;
import com.suncreate.pressuresensor.ui.OSCPhotosActivity;
import com.suncreate.pressuresensor.ui.SimpleBackActivity;
import com.suncreate.pressuresensor.widget.AvatarView;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.utils.DensityUtils;

/**
 * 界面帮助类
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年10月10日 下午3:33:36
 */
public class UIHelper {

    /**
     * 全局web样式
     */
    // 链接样式文件，代码块高亮的处理
    public final static String linkCss = "<script type=\"text/javascript\" " +
            "src=\"file:///android_asset/shCore.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/brush.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/client.js\"></script>"
            + "<script type=\"text/javascript\" src=\"file:///android_asset/detail_page" +
            ".js\"></script>"
            + "<script type=\"text/javascript\">SyntaxHighlighter.all();</script>"
            + "<script type=\"text/javascript\">function showImagePreview(var url){window" +
            ".location.url= url;}</script>"
            + "<link rel=\"stylesheet\" type=\"text/css\" " +
            "href=\"file:///android_asset/shThemeDefault.css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/shCore" +
            ".css\">"
            + "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/common" +
            ".css\">";
    public final static String WEB_STYLE = linkCss;

    public static final String WEB_LOAD_IMAGES = "<script type=\"text/javascript\"> var " +
            "allImgUrls = getAllImgSrc(document.body.innerHTML);</script>";

    private static final String SHOWIMAGE = "ima-api:action=showImage&data=";

    /**
     * 显示登录界面
     *
     * @param context
     */
    public static void showLoginActivity(Context context) {
        Intent intent;
        intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 显示注册界面,三合一
     *
     * @param context
     */
    public static void showRegisterPage(Context context) {
        Bundle args = new Bundle();
        showSimpleBack(context, SimpleBackPage.REGISTER, args);
    }

    /**
     * 显示找回密码 发送验证码页面
     *
     * @param context
     */
    public static void showSendVerifyPage(Context context, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        showSimpleBack(context, SimpleBackPage.VERIFY, args);
    }

    /**
     * 输入
     *
     * @param context
     * @param verifyKey
     */
    public static void showRegisterRetrievePage(Context context, String userName, String verifyKey, int pwdType) {
        Bundle args = new Bundle();
        args.putString(RegisterRetrieveFragment.VERIFY_USER_NAME, userName);
        args.putString(VerifyFragment.VERIFY_KEY, verifyKey);
        args.putInt(RegisterRetrieveFragment.PWD_TYPE, pwdType);
        showSimpleBack(context, SimpleBackPage.REGISTER_RETRIEVE, args);
    }

    /**
     * 跳转导航(地址导航)
     *
     * @param context
     */
    public static void showNavigateActivity(Context context, String address) {
        if (TextUtils.isEmpty(address)) {
            AppContext.showToast("无法获取对方位置信息");
        } else {
            try {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/navi?query=" + address));
                context.startActivity(i1);
            } catch (Exception e) {
                showDialog(context);
            }
        }
    }

    /**
     * 跳转导航(经纬度导航)
     *
     * @param context
     */
    public static void showLonLatNavigateActivity(Context context, String lat, String lon) {
        if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
            AppContext.showToast("无法获取对方位置信息");
        } else {
            try {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/navi?location=" + lat + "," + lon));
                context.startActivity(i1);
            } catch (Exception e) {
                showDialog(context);
            }
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public static void showDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(context);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 获取一个环形进度条等待窗口
     */
    public static ProgressDialog getprogress(Activity aty, String msg) {
        // 实例化一个ProgressBarDialog
        ProgressDialog progressDialog = new ProgressDialog(aty);
        progressDialog.setMessage(msg);
        progressDialog.getWindow().setLayout(
                DensityUtils.getScreenW(aty),
                DensityUtils.getScreenH(aty));
        progressDialog.setCancelable(true);
        // 设置ProgressBarDialog的显示样式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        return progressDialog;
    }

    public static void showBannerDetail(Context context, Banner banner) {
        String newsId = banner.getId();
        switch (newsId) {
//            case Banner.BANNER_TYPE_URL:
//                showNewsDetail(context, Integer.parseInt(String.valueOf(newsId)), 0);
//                break;
////            case Banner.BANNER_TYPE_SOFTWARE:
//                showSoftwareDetailById(context, Integer.parseInt(String.valueOf(newsId)));
//                break;
//            case Banner.BANNER_TYPE_POST:
//                showPostDetail(context, StringUtils.toInt(String.valueOf(newsId)),
//                        0);
//                break;
//            case Banner.BANNER_TYPE_BLOG:
//                showBlogDetail(context, StringUtils.toLong(String.valueOf(newsId)));
//                break;
//            case Banner.BANNER_TYPE_EVENT:
//                showEventDetail(context,
//                        StringUtils.toInt(newsId));
//                break;
//            case Banner.BANNER_TYPE_NEWS:
            default:
                showUrlRedirect(context, banner.getAdUrl());
                break;
        }
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public static void initWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setDefaultFontSize(15);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        int sysVersion = Build.VERSION.SDK_INT;
        if (sysVersion >= 11) {
            settings.setDisplayZoomControls(false);
        } else {
            ZoomButtonsController zbc = new ZoomButtonsController(webView);
            zbc.getZoomControls().setVisibility(View.GONE);
        }
        webView.setWebViewClient(UIHelper.getWebViewClient());
    }

    /**
     * 添加网页的点击图片展示支持
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @JavascriptInterface
    public static void addWebImageShow(final Context cxt, WebView wv) {
        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new OnWebViewImageListener() {
            @Override
            @JavascriptInterface
            public void showImagePreview(String bigImageUrl) {
                if (bigImageUrl != null && !StringUtils.isEmpty(bigImageUrl)) {
                    OSCPhotosActivity.showImagePrivew(cxt, bigImageUrl);
                }
            }
        }, "mWebViewImageListener");
    }

    /**
     * 获取webviewClient对象
     *
     * @return
     */
    public static WebViewClient getWebViewClient() {

        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showUrlRedirect(view.getContext(), url);
                return true;
            }
        };
    }

    public static String setHtmlCotentSupportImagePreview(String body) {
        // 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
        if (AppContext.get(AppConfig.KEY_LOAD_IMAGE, true)
                || TDevice.isWifiOpen()) {
            // 过滤掉 img标签的width,height属性
            body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
            body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
            // 添加点击图片放大支持
            // 添加点击图片放大支持
            body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
                    "$1$2\" onClick=\"showImagePreview('$2')\"");
        } else {
            // 过滤掉 img标签
            body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
        }

        // 过滤table的内部属性
        body = body.replaceAll("(<table[^>]*?)\\s+border\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<table[^>]*?)\\s+cellspacing\\s*=\\s*\\S+", "$1");
        body = body.replaceAll("(<table[^>]*?)\\s+cellpadding\\s*=\\s*\\S+", "$1");

        return body;
    }

    /**
     * url跳转
     *
     * @param context
     * @param url
     */
    public static void showUrlRedirect(Context context, String url) {
        if (url == null)
            return;

        if (url.startsWith(SHOWIMAGE)) {
            String realUrl = url.substring(SHOWIMAGE.length());
            try {
                JSONObject json = new JSONObject(realUrl);
                int idx = json.optInt("index");
                String[] urls = json.getString("urls").split(",");
                OSCPhotosActivity.showImagePrivew(context, urls[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        URLsUtils urls = URLsUtils.parseURL(url);
        if (urls != null) {
            showLinkRedirect(context, urls.getObjType(), urls.getObjId(),
                    urls.getObjKey());
        } else {
            openBrowser(context, url);
        }
    }

    public static void showLinkRedirect(Context context, int objType,
                                        int objId, String objKey) {
        switch (objType) {
            case URLsUtils.URL_OBJ_TYPE_ZONE:
                showUserCenter(context, objId, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_OTHER:
                openBrowser(context, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_TEAM:
                openSysBrowser(context, objKey);
                break;
            case URLsUtils.URL_OBJ_TYPE_GIT:
                openSysBrowser(context, objKey);
                break;
        }
    }

    /**
     * 打开内置浏览器
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {

        if (StringUtils.isImgUrl(url)) {
            OSCPhotosActivity.showImagePrivew(context, url);
            return;
        }

//        if (url.startsWith("http://www.fireiot.com/tweet-topic/")) {
//            Bundle bundle = new Bundle();
//            int i = url.lastIndexOf("/");
//            if (i != -1) {
//                bundle.putString("topic",
//                        URLDecoder.decode(url.substring(i + 1)));
//            }
//            UIHelper.showSimpleBack(context, SimpleBackPage.TWEET_TOPIC_LIST,
//                    bundle);
//            return;
//        }
        try {
            // 启用外部浏览器
            // Uri uri = Uri.parse(url);
            // Intent it = new Intent(Intent.ACTION_VIEW, uri);
            // context.startActivity(it);
            Bundle bundle = new Bundle();
            bundle.putString(BrowserFragment.BROWSER_KEY, url);
            showSimpleBack(context, SimpleBackPage.BROWSER, bundle);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    /**
     * 打开系统中的浏览器
     *
     * @param context
     * @param url
     */
    public static void openSysBrowser(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.showToastShort("无法浏览此网页");
        }
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Fragment fragment,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(fragment.getActivity(),
                SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        context.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Context context,
                                               int requestCode, SimpleBackPage page, Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBackForResult(Activity context,
                                               int requestCode, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivityForResult(intent, requestCode);
    }

    public static void showSimpleBack(Fragment fragment, SimpleBackPage page) {
        Intent intent = new Intent(fragment.getActivity(), SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        fragment.startActivity(intent);
    }

    public static void showSimpleBack(Activity activity, SimpleBackPage page) {
        Intent intent = new Intent(activity, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        activity.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showSimpleBack(Context context, SimpleBackPage page,
                                      Bundle args) {
        Intent intent = new Intent(context, SimpleBackActivity.class);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_ARGS, args);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SimpleBackActivity.BUNDLE_KEY_PAGE, page.getValue());
        context.startActivity(intent);
    }

    /**
     * 组合动态的回复文本
     *
     * @param name
     * @param body
     * @return
     */
    public static SpannableStringBuilder parseActiveReply(String name,
                                                          String body) {
        Spanned span = Html.fromHtml(body.trim());
        SpannableStringBuilder sp = new SpannableStringBuilder(name + "：");
        sp.append(span);
        // 设置用户名字体加粗、高亮
        // sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
        // name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(Color.parseColor("#008000")), 0,
                name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sp;
    }

    /**
     * 发送App异常崩溃报告
     *
     * @param context
     */
    public static void sendAppCrashReport(final Context context) {

        DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 退出
                //   System.exit(-1);
            }
        }).show();
    }

    /**
     * 发送通知广播
     *
     * @param context
     * @param notice
     */
    public static void sendBroadCast(Context context, Notice notice) {
        if (!((AppContext) context.getApplicationContext()).isLogin()
                || notice == null)
            return;
        Intent intent = new Intent(Constants.INTENT_ACTION_NOTICE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("notice_bean", notice);
        intent.putExtras(bundle);
        context.sendBroadcast(intent);
    }

    /**
     * 发送通知广播
     *
     * @param context
     */
    public static void sendBroadcastForNotice(Context context) {
        Intent intent = new Intent(NoticeService.INTENT_ACTION_BROADCAST);
        context.sendBroadcast(intent);
    }

    /**
     * 显示用户中心页面
     *
     * @param context
     * @param hisuid
     * @param hisuid
     * @param hisname
     */
    public static void showUserCenter(Context context, int hisuid,
                                      String hisname) {
        if (hisuid == 0 && hisname.equalsIgnoreCase("匿名")) {
            AppContext.showToast("提醒你，该用户为非会员");
            return;
        }
        Bundle args = new Bundle();
        args.putInt("his_id", hisuid);
        args.putString("his_name", hisname);
        showSimpleBack(context, SimpleBackPage.USER_CENTER, args);
    }

    /**
     * 显示用户头像大图
     *
     * @param context
     * @param avatarUrl
     */
    public static void showUserAvatar(Context context, String avatarUrl) {
        if (StringUtils.isEmpty(avatarUrl)) {
            return;
        }
        String url = AvatarView.getLargeAvatar(avatarUrl);
        OSCPhotosActivity.showImagePrivew(context, url);
    }

    /**
     * 显示登陆用户的个人中心页面
     *
     * @param context
     */
    public static void showMyInformation(Context context) {
        showSimpleBack(context, SimpleBackPage.MY_INFORMATION);
    }

    /**
     * 显示用户的消息中心
     *
     * @param context
     */
    public static void showMyMes(Context context) {
//        showSimpleBack(context, SimpleBackPage.MY_MES);
    }

    /**
     * 显示设置界面
     *
     * @param context
     */
    public static void showSetting(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING);
    }

    /**
     * 显示通知设置界面
     *
     * @param context
     */
    public static void showSettingNotification(Context context) {
        showSimpleBack(context, SimpleBackPage.SETTING_NOTIFICATION);
    }

    /**
     * 显示关于界面
     *
     * @param context
     */
    public static void showAboutFireIot(Context context) {
        showSimpleBack(context, SimpleBackPage.ABOUT_FIRE_IOT);
    }

    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    AppContext.showToastShort("缓存清除成功");
                } else {
                    AppContext.showToastShort("缓存清除失败");
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static void openDownLoadService(Context context, String downurl,
                                           String tilte) {
        final ICallbackResult callback = new ICallbackResult() {

            @Override
            public void OnBackResult(Object s) {
            }
        };
        ServiceConnection conn = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadBinder binder = (DownloadBinder) service;
                binder.addCallback(callback);
                binder.start();

            }
        };
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
        intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
        context.startService(intent);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * 发送广播告知评论发生变化
     *
     * @param context
     * @param isBlog
     * @param id
     * @param catalog
     * @param operation
     * @param replyComment
     */
    public static void sendBroadCastCommentChanged(Context context,
                                                   boolean isBlog, int id, int catalog, int
                                                           operation,
                                                   Comment replyComment) {
        Intent intent = new Intent(Constants.INTENT_ACTION_COMMENT_CHANGED);
        Bundle args = new Bundle();
        args.putInt(Comment.BUNDLE_KEY_ID, id);
        args.putInt(Comment.BUNDLE_KEY_CATALOG, catalog);
        args.putBoolean(Comment.BUNDLE_KEY_BLOG, isBlog);
        args.putInt(Comment.BUNDLE_KEY_OPERATION, operation);
        args.putParcelable(Comment.BUNDLE_KEY_COMMENT, replyComment);
        intent.putExtras(args);
        context.sendBroadcast(intent);
    }

    /**
     * 显示单篇详情
     *
     * @param context
     * @param id
     */
    public static void showSingleDetail(Context context, Long id) {
        Bundle bundle = new Bundle();
        bundle.putString(GoodsDetailFragment.NAME, "呵呵");
        bundle.putLong(GoodsDetailFragment.ID, id);
        UIHelper.showSimpleBack(context, SimpleBackPage.GOODS_DETAIL, bundle);
    }
}
