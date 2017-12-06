package com.suncreate.fireiot.fragment.master;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.suncreate.fireiot.AppContext;
import com.suncreate.fireiot.R;
import com.suncreate.fireiot.api.remote.MonkeyApi;
import com.suncreate.fireiot.base.BaseDetailFragment;
import com.suncreate.fireiot.bean.Constants;
import com.suncreate.fireiot.bean.base.ResultBean;
import com.suncreate.fireiot.bean.user.Store;
import com.suncreate.fireiot.util.DialogHelp;
import com.suncreate.fireiot.util.FileUtil;
import com.suncreate.fireiot.util.ImageUtils;
import com.suncreate.fireiot.util.StringUtils;
import com.suncreate.fireiot.util.UIHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 技师详情
 * <p>
 * desc
 */
public class StoreCertificationFragment extends BaseDetailFragment<Store> implements View.OnClickListener {
    public static final int ACTION_TYPE_ALBUM = 0;
    public static final int ACTION_TYPE_PHOTO = 1;
    private Dialog dialog;
    private View inflate;
    private int num;
    private Uri imageUri;
    //    @Bind(R.id.ID)
//    LinearLayout ID;
//
    @Bind(R.id.certificianAlready)
    TextView certificianAlready;
//    @Bind(R.id.ID1)
//    LinearLayout ID1;
//
//    @Bind(R.id.ID3)
//    LinearLayout ID3;

    @Bind(R.id.upload_img1)
    ImageButton upload_img1;


    @Bind(R.id.upload_img2)
    ImageButton upload_img2;


    @Bind(R.id.upload_img3)
    ImageButton upload_img3;


    @Bind(R.id.ID_First)
    ImageView ID_First;


    @Bind(R.id.ID_Second)
    ImageView ID_Second;

    @Bind(R.id.ID_Third)
    ImageView ID_Third;

    private boolean isChangeFace = false;

    private final static int CROP = 200;

    private int mum = 0;
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/monkey/Portrait/";
    private Uri origUri;
    private File protraitFile;
    private File protraitFile1;
    private File protraitFile2;
    private Bitmap protraitBitmap;
    private Bitmap protraitBitmap1;
    private Bitmap protraitBitmap2;
    private String protraitPath;
    private String protraitPath1;
    private String protraitPath2;

    @Override
    public void initView(View root) {
        //   icon_master_technician.setOnClickListener(this);
        upload_img1.setOnClickListener(this);
        upload_img2.setOnClickListener(this);
        upload_img3.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        // return R.layout.fragment_garage_detail;
        return R.layout.fragment_store_certification;
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<Store>>() {
        }.getType();
    }

    @Override
    protected void sendRequestDataForNet() {

        MonkeyApi.getStoreInfo(mCertificationHandler);

    }


   TextHttpResponseHandler mCertificationHandler = new TextHttpResponseHandler() {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            AppContext.showToast("网络异常，加载失败");
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                ResultBean<Store> resultBean = AppContext.createGson().fromJson(responseString, getType());
//                    if (resultBean != null && resultBean.getSuccess() && resultBean.getAttributes().getItems() != null) {
                if (resultBean != null && resultBean.isSuccess()) {
                    executeOnLoadDataSuccess(resultBean.getResult());
                    saveCache(resultBean.getResult());
                } else if (resultBean != null && resultBean.getCode() == 3) {
                    AppContext.getInstance().logoutOperation();
                    UIHelper.showLoginActivity(getActivity());
                } else {

                }
            } catch (Exception e) {

            }
        }
    };
    @Override
    protected void executeOnLoadDataSuccess(Store detail) {
        super.executeOnLoadDataSuccess(detail);
        String status = detail.getStoreStatus();
        if (!StringUtils.isEmpty(status)) {
            if (status.equals("0")) {
                certificianAlready.setText("审核状态：未审核");
            } else if (status.equals("1")) {
                certificianAlready.setText("审核状态：审核中");
            } else if (status.equals("2")) {
                certificianAlready.setText("审核状态：已认证");
            }
        }
        String handImage1 = detail.getStoreCard();
//        if (!StringUtils.isEmpty(handImage)) {
//            getImgLoader().load(handImage).error(R.drawable.widget_dface).into(icon_master_technician);
//        }
        //   getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getStoreLogo())).error(R.drawable.widget_dface).into(icon_master_technician);
        if (!StringUtils.isEmpty(handImage1)) {
            try {
                MonkeyApi.getPartImg(handImage1, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            ID_First.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        ID_First.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ID_First.setVisibility(View.GONE);
        }

        String handImage2 = detail.getStoreCardOpposite();
//        if (!StringUtils.isEmpty(handImage)) {
//            getImgLoader().load(handImage).error(R.drawable.widget_dface).into(icon_master_technician);
//        }
        //   getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getStoreLogo())).error(R.drawable.widget_dface).into(icon_master_technician);
        if (!StringUtils.isEmpty(handImage2)) {
            try {
                MonkeyApi.getPartImg(handImage2, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            ID_Second.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        ID_Second.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ID_Second.setVisibility(View.GONE);
        }

        String handImage3 = detail.getStoreLicense();
//        if (!StringUtils.isEmpty(handImage)) {
//            getImgLoader().load(handImage).error(R.drawable.widget_dface).into(icon_master_technician);
//        }
        //   getImgLoader().load(MonkeyApi.getPartImgUrl(detail.getStoreLogo())).error(R.drawable.widget_dface).into(icon_master_technician);
        if (!StringUtils.isEmpty(handImage3)) {
            try {
                MonkeyApi.getPartImg(handImage3, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if (responseBody != null) {
                            Bitmap imap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            ID_Third.setImageBitmap(imap);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        ID_Third.setVisibility(View.GONE);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ID_Third.setVisibility(View.GONE);
        }

    }

    @Override
    protected String getCacheKey() {
        return "store_" + mId;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //上传身份证正面
            case R.id.upload_img1:
                num = 1;
                handleSelectPicture();


                break;
            //上传身份证反面
            case R.id.upload_img2:
                num = 2;
                handleSelectPicture();

                break;
            //上传职业执照
            case R.id.upload_img3:
                num = 3;
                handleSelectPicture();

                break;

            default:
                break;
        }
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
        //  showWaitDialog("正在上传头像...");
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 1200, 800);
        } else {
            //  AppContext.showToast("图像不存在，上传失败");
        }

        if (!StringUtils.isEmpty(protraitPath1) && protraitFile1.exists()) {
            protraitBitmap1 = ImageUtils
                    .loadImgThumbnail(protraitPath1, 1200, 800);
        } else {
            //  AppContext.showToast("图像不存在，上传失败");
        }
        if (!StringUtils.isEmpty(protraitPath2) && protraitFile2.exists()) {
            protraitBitmap2 = ImageUtils
                    .loadImgThumbnail(protraitPath2, 1200, 800);
        } else {
            //  AppContext.showToast("图像不存在，上传失败");
        }

        if (num == 3) {
            try {
                //上传职业执照

                MonkeyApi.getUploadStoreImg(Constants.STORE_IMG_TYPE.LICENCE,protraitFile2, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("更换头像失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            com.suncreate.fireiot.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.fireiot.bean.base.ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.showToast("上传营业执照成功");
                                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));


                                try {
                                    MonkeyApi.getStoreInfo(
                                            new TextHttpResponseHandler() {

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    AppContext.showToast("更换头像失败");
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                    try {
                                                        com.suncreate.fireiot.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.fireiot.bean.base.ResultBean>() {
                                                        }.getType());
                                                        if (resultBean != null && resultBean.getCode() == 1) {
                                                            AppContext.showToast("更换成功");
                                                            // 显示新头像
                                                            ID_Third.setImageBitmap(protraitBitmap2);
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
                                } catch (Exception e) {
                                    AppContext.showToast("图像不存在，上传失败");
                                }


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
            } catch (Exception e) {
                AppContext.showToast("图像不存在，上传失败");
            }

        } else if (num == 2) {
            try {

                //上传身份证反面
                MonkeyApi.getUploadStoreImg(Constants.STORE_IMG_TYPE.CARD_REVERSE, protraitFile1, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("更换头像失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            com.suncreate.fireiot.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.fireiot.bean.base.ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.showToast("上传身份证反面成功");
                                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));

                                try {
                                    MonkeyApi.getStoreInfo(
                                            new TextHttpResponseHandler() {

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    AppContext.showToast("更换头像失败");
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                    try {
                                                        com.suncreate.fireiot.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.fireiot.bean.base.ResultBean>() {
                                                        }.getType());
                                                        if (resultBean != null && resultBean.getCode() == 1) {
                                                            AppContext.showToast("更换成功");
                                                            // 显示新头像
                                                            ID_Second.setImageBitmap(protraitBitmap1);
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
                                } catch (Exception e) {
                                    AppContext.showToast("图像不存在，上传失败");
                                }


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
            } catch (Exception e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        } else if (num == 1) {
            try {
                //上传身份证正面
                MonkeyApi.getUploadStoreImg(Constants.STORE_IMG_TYPE.CARD_POSITIVE, protraitFile, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        AppContext.showToast("更换头像失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        try {
                            com.suncreate.fireiot.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.fireiot.bean.base.ResultBean>() {
                            }.getType());
                            if (resultBean != null && resultBean.getCode() == 1) {
                                AppContext.showToast("上传身份证正面成功");
                                getActivity().sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));

                                try {
                                    MonkeyApi.getStoreInfo(
                                            new TextHttpResponseHandler() {

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                    AppContext.showToast("更换头像失败");
                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                    try {
                                                        com.suncreate.fireiot.bean.base.ResultBean resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<com.suncreate.fireiot.bean.base.ResultBean>() {
                                                        }.getType());
                                                        if (resultBean != null && resultBean.getCode() == 1) {
                                                            AppContext.showToast("更换成功");
                                                            // 显示新头像
                                                            ID_First.setImageBitmap(protraitBitmap);
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
                                } catch (Exception e) {
                                    AppContext.showToast("图像不存在，上传失败");
                                }


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
            } catch (Exception e) {
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
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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

    private Uri getUploadTempFile1(Uri uri) {
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
        String cropFileName = "osc_crop_one_" + timeStamp + "." + ext;


        // 裁剪头像的绝对路径
        protraitPath1 = FILE_SAVEPATH + cropFileName;

        protraitFile1 = new File(protraitPath1);

        return Uri.fromFile(protraitFile1);
    }

    // 裁剪头像的绝对路径
    private Uri getUploadTempFile2(Uri uri) {
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
        String cropFileName = "osc_crop_two_" + timeStamp + "." + ext;


        // 裁剪头像的绝对路径
        protraitPath2 = FILE_SAVEPATH + cropFileName;

        protraitFile2 = new File(protraitPath2);

        return Uri.fromFile(protraitFile2);
    }

    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        if (num == 1) {
            intent.putExtra("output", this.getUploadTempFile(data));
            intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 4000);// 输出图片大小
//        intent.putExtra("outputY", 2000);
            intent.putExtra("scale", true);// 去黑边
            intent.putExtra("scaleUpIfNeeded", true);// 去黑边
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else if (num == 2) {
            intent.putExtra("output", this.getUploadTempFile1(data));
            intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 4000);// 输出图片大小
//        intent.putExtra("outputY", 2000);
            intent.putExtra("scale", true);// 去黑边
            intent.putExtra("scaleUpIfNeeded", true);// 去黑边
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else if (num == 3) {
            intent.putExtra("output", this.getUploadTempFile2(data));
            intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);// 裁剪框比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 4000);// 输出图片大小
//        intent.putExtra("outputY", 2000);
            intent.putExtra("scale", true);// 去黑边
            intent.putExtra("scaleUpIfNeeded", true);// 去黑边
            startActivityForResult(intent,
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
        }
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


    public void onPermissionsGranted(int requestCode, List<String> perms) {
        try {
            startTakePhoto();
        } catch (Exception e) {
            Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
        }
    }


    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this.getContext(), R.string.permissions_camera_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}