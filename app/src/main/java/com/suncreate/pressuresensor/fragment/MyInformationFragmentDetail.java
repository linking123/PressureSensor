package com.suncreate.pressuresensor.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.suncreate.pressuresensor.AppContext;
import com.suncreate.pressuresensor.R;
import com.suncreate.pressuresensor.api.remote.MonkeyApi;
import com.suncreate.pressuresensor.base.BaseDetailFragment;
import com.suncreate.pressuresensor.bean.Constants;
import com.suncreate.pressuresensor.bean.SimpleBackPage;
import com.suncreate.pressuresensor.bean.base.ResultBean;
import com.suncreate.pressuresensor.bean.user.User;
import com.suncreate.pressuresensor.fragment.LoginRegister.RegisterRetrieveFragment;
import com.suncreate.pressuresensor.ui.OSCPhotosActivity;
import com.suncreate.pressuresensor.ui.empty.EmptyLayout;
import com.suncreate.pressuresensor.util.AreaHelper;
import com.suncreate.pressuresensor.util.DialogHelp;
import com.suncreate.pressuresensor.util.FileUtil;
import com.suncreate.pressuresensor.util.ImageUtils;
import com.suncreate.pressuresensor.util.StringUtils;
import com.suncreate.pressuresensor.util.UIHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 登录用户信息详情
 */

public class MyInformationFragmentDetail extends BaseDetailFragment<User> implements EasyPermissions.PermissionCallbacks, View.OnClickListener {

    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;

    @Bind(R.id.iv_avatar)
    ImageView mUserFace;
    @Bind(R.id.tv_my_name_who)
    TextView tv_my_name_who;
    //电话区域
    @Bind(R.id.tv_my_phone_num)
    TextView myPhoneNum;
    @Bind(R.id.tv_my_QQ_num)
    TextView myQQnum;
    @Bind(R.id.tv_name)
    TextView mName;
    //姓名区域
    @Bind(R.id.rl_my_name_box)
    RelativeLayout mMyName;
    //地址区域box
    @Bind(R.id.rl_my_area_box)
    RelativeLayout mAreaBox;
    //地址显示
    @Bind(R.id.tv_my_address_where)
    TextView userArea;
    //qq区域
    @Bind(R.id.rl_qq_box)
    RelativeLayout mQQ;
    @Bind(R.id.error_layout)
    EmptyLayout mErrorLayout;
    //注销
    @Bind(R.id.btn_logout)
    Button mlogout;

    private User mUser;
    private boolean isChangeFace = false;
    private final static int CROP = 200;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/monkey/Portrait/";
    private Uri origUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private String mAreaCode;

    //地址选择器
    OptionsPickerView addrOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_USER_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.restpassword_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销广播接收
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(
                R.layout.fragment_my_information_detail, container, false);
        initView(view);
        sendRequestDataForNet();
        return view;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case Constants.INTENT_ACTION_USER_CHANGE:
                    sendRequestDataForNet();
                    break;
            }
        }
    };

    @Override
    protected String getCacheKey() {
        return "MyInfo_" + mId;
    }

    @Override
    protected void sendRequestDataForNet() {
//        mUser = AppContext.getInstance().getLoginUserExt();
        if (mUser == null) {
            //没有登录
            return;
        }
        sendRequiredData();
//        MonkeyApi.getMyInformationHead(AppContext.getInstance().getLoginUserExt().getId(), new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                if (responseBody != null) {
//                    try {
//                        Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//                        mUserFace.setImageBitmap(imap);
//                        String storageState = Environment.getExternalStorageState();
//                        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
//                            File savedir = new File(AppConfig.SAVE_HEAD_PATH);
//                            File savefile = new File(AppConfig.SAVE_HEAD_PATH, mUser.getId() + ".jpg");
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
//
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

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  sendRequiredData();
            }
        });
        mUserFace.setOnClickListener(this);
        mMyName.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mlogout.setOnClickListener(this);
        //区域选择
        mAreaBox.setOnClickListener(this);
        //选择地址
        addrOptions = AreaHelper.chooseAddress(addrOptions, this.getContext());
        addrOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                AreaHelper.onOptionsSelect(options1, option2, options3, userArea);
                String mUserArea = userArea.getText().toString();
                if (!TextUtils.isEmpty(mUserArea)) {
                    String[] s = mUserArea.split(" ");
                    mAreaCode = AppContext.getInstance().getAreaCodeByName(s[s.length - 1], s[s.length - 2]);
                    MonkeyApi.updateUser(null, null, null, mUserArea, mAreaCode, null, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            AppContext.showToastShort("信息更新失败！");
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            try {
                                ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean>() {
                                }.getType());
                                if (resultBean != null && resultBean.getCode() == 1) {
                                    getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                                    AppContext.getInstance().setProperty("user.region", mAreaCode);
                                } else {
                                    AppContext.showToast(resultBean.getMessage());
                                }
                            } catch (Exception e) {
                                onFailure(statusCode, headers, responseString, e);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                showClickAvatar();
                break;
            case R.id.rl_my_name_box:
                if (AppContext.getInstance().getUserRole().equals("2")) {
                    DialogHelp.getTitleMessageDialog(this.getContext(), getString(R.string.str_title_tips), getString(R.string.str_tech_name), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                } else if (AppContext.getInstance().getUserRole().equals("3")) {
                    DialogHelp.getTitleMessageDialog(this.getContext(), getString(R.string.str_title_tips), getString(R.string.str_gar_name), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                } else {
                    Bundle b = new Bundle();
                    b.putString("name", tv_my_name_who.getText().toString());
                    UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_PERSON_INFO_EDIT_NAME, b);
                }
                break;
            case R.id.rl_my_area_box:
                addrOptions.show();
                break;
            case R.id.rl_qq_box:
                Bundle qq = new Bundle();
                qq.putString("myQQnum", myQQnum.getText().toString());
                UIHelper.showSimpleBack(getContext(), SimpleBackPage.MY_PERSON_INFO_EDIT_EMAIL, qq);
                break;
            case R.id.btn_logout:
                AppContext.getInstance().Logout();
                getActivity().finish();
                break;
            default:
                break;
        }
    }

    public void sendRequiredData() {
        MonkeyApi.getMyInformation(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ResultBean<User> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<User>>() {
                }.getType());
                if (resultBean != null && resultBean.isSuccess()) {
                    User u = resultBean.getResult();
                    String userRealname = u.getUserRealname();
                    if (!StringUtils.isEmpty(userRealname)) {
                        tv_my_name_who.setText(userRealname);
                    }
                    if (null != u.getUserAreaId()) {
                        userArea.setText(AppContext.getInstance().getAreaFullNameByCode(u.getUserAreaId().toString()));
                    }

                    String userRealPhone = u.getUserMobile();
                    if (!StringUtils.isEmpty(userRealPhone)) {
                        myPhoneNum.setText(userRealPhone.trim());
                    }

                    String userRealQQ = u.getUserQq();
                    if (!StringUtils.isEmpty(userRealQQ)) {
                        myQQnum.setText(userRealQQ.trim());
                    }
                }
            }
        });
    }

    @Override
    protected java.lang.reflect.Type getType() {
        return new TypeToken<com.suncreate.pressuresensor.bean.base.ResultBean<User>>() {
        }.getType();
    }

    public void showClickAvatar() {
        //没有头像，为了开发方便，暂时注释，后期需放开
        if (mUser == null) {
            AppContext.showToast("请登录");
            AppContext.getInstance().Logout();
            return;
        }
        DialogHelp.getSelectDialog(getActivity(), "选择操作", getResources().getStringArray(R.array.avatar_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    handleSelectPicture();
                } else {
                    //显示大图
                    //Bitmap bitmap=BitmapFactory.decodeFile(AppConfig.SAVE_HEAD_PATH+mUser.getId()+".jpg");
//                    UIHelper.showUserAvatar(getActivity(), AppConfig.SAVE_HEAD_PATH+mUser.getId()+".jpg");
                    OSCPhotosActivity.showImagePrivew(getContext(), MonkeyApi.getUserPortraitImgUrl(mUser.getId(), true));
                }
            }
        }).show();
    }

    private void handleSelectPicture() {
        DialogHelp.getSelectDialog(getActivity(), "选择图片", getResources().getStringArray(R.array.choose_picture), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToSelectPicture(i);
            }
        }).show();
    }

    private void goToSelectPicture(int position) {
        switch (position) {
            case ACTION_TYPE_ALBUM:
                startImagePick();
                break;
            case ACTION_TYPE_PHOTO:
                startTakePhotoByPermissions();
                break;
            default:
                break;
        }
    }

    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        showWaitDialog("正在上传头像...");

        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 100, 100);
        } else {
            AppContext.showToast("图像不存在，上传失败");
        }
        if (protraitBitmap != null) {
            try {
                MonkeyApi.updatePortrait(AppContext.getInstance()
                                .getLoginUid(), protraitFile,
                        new TextHttpResponseHandler() {

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                AppContext.showToast("更换头像失败");
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                try {
                                    com.suncreate.pressuresensor.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.pressuresensor.bean.base.ResultBean>() {
                                    }.getType());
                                    if (resultBean != null && resultBean.getCode() == 1) {
                                        AppContext.showToast("更换成功");
                                        // 显示新头像
                                        mUserFace.setImageBitmap(protraitBitmap);
                                        isChangeFace = true;
                                        getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
                                    } else {
                                        AppContext.showToast(resultBean.getMessage());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    onFailure(statusCode, headers, responseString, e);
                                }
                            }

                            @Override
                            public void onFinish() {
                                hideWaitDialog();
                            }
                        });
            } catch (FileNotFoundException e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        }
    }

    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }

    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/monkey/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "osc_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        String theLarge = savePath + fileName;

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            AppContext.showToast("无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(getActivity(), uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "osc_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        return Uri.fromFile(protraitFile);
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output", this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
        }
    }

    private static final int CAMERA_PERM = 1;

    @AfterPermissionGranted(CAMERA_PERM)
    private void startTakePhotoByPermissions() {
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this.getContext(), perms)) {
            try {
                startTakePhoto();
            } catch (Exception e) {
                Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
            }
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this,
                    getResources().getString(R.string.str_request_camera_message),
                    CAMERA_PERM, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            startTakePhoto();
        } catch (Exception e) {
            Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.public_menu_password:
                UIHelper.showRegisterRetrievePage(getActivity(), null, null, RegisterRetrieveFragment.REPAIR_PWD);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
