package com.xjysx.xiaochetong.moudle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xjysx.xiaochetong.FileUtils;
import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.app.BaseApplication;
import com.xjysx.xiaochetong.app.DialogUtils;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.datebase.UserDao;
import com.xjysx.xiaochetong.entities.UserBean;
import com.xjysx.xiaochetong.entities.VersionBean;
import com.xjysx.xiaochetong.network.BaseBean;
import com.xjysx.xiaochetong.network.BaseObserver;
import com.xjysx.xiaochetong.network.Network;
import com.xjysx.xiaochetong.util.DeviceUtil;
import com.xjysx.xiaochetong.util.SignatureUtil;
import com.xjysx.xiaochetong.util.ToastUtil;
import com.xjysx.xiaochetong.widget.custom.CustomAlertDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Cookie;
import okhttp3.ResponseBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 网页
 * Created by dan on 2017/10/12/012.
 */
@RuntimePermissions
public class WebViewActivity extends BaseActivity {

    private static final int REQUEST_CODE_SCAN = 1001;
    @BindView(R.id.titleRight)
    TextView titleRight;
    @BindView(R.id.titleCeneter_textView)
    TextView titleCeneterTextView;
    @BindView(R.id.webView)
    WebView webview;
    @BindView(R.id.btn_go)
    TextView btn;
    @BindView(R.id.edt)
    EditText edt;
    @BindView(R.id.view_empty)
    View mErrorView;


    public static final String MESSAGE_RECEIVED_ACTION = "com.sjdcar.xct.MESSAGE_RECEIVED_ACTION";


    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;
    private String imagePaths;
    private String compressPath;
    private int fingerCode = -1;
    private String user_id = "";


    private int forced = 0;
    private String url;
    private String phone;
    private int height;

    private static final String TAG = "WebViewActivity";
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    private boolean isDelay = false;

    private WebViewActivity webViewActivity;

    private Subscription mSubscription;

    private BaseObserver<VersionBean> observer_version = new BaseObserver<VersionBean>() {

        @Override
        public void onSuccess(VersionBean versionBean) {
            Log.d("ss", versionBean.toString());
            //验证签名
            String sign = versionBean.getSign();
            Map<String, String> map = new HashMap<>();
            map.put("versionNumber", versionBean.getVersionNumber());
            map.put("versionDesc", versionBean.getVersionDesc());
            map.put("versionUrl", versionBean.getVersionUrl());
            map.put("appType", versionBean.getAppType());
            map.put("updateFlag", versionBean.getUpdateFlag());
            map.put("message", versionBean.getMessage());
            map.put("random", versionBean.getRandom());
            map.put("createTime", versionBean.getCreateTime());
            map.put("type", versionBean.getType());
            String signature = SignatureUtil.getSignature(map);
            if (!sign.equals(signature)) {
                return;
            }
            Log.d("ss", "验证成功");
            forced = Integer.parseInt(versionBean.getUpdateFlag());
            //防止getVersionNumber获取到的是错误的值
            try {
                int versionCode = versionBean.getVersionNumber().length() > 0 ? Integer.parseInt(versionBean.getVersionNumber()) : 0;
                if (versionCode > BaseApplication.getVersionCode()) {//需要更新
                    showNewVersionAlert(versionBean.getVersionUrl(), versionBean.getVersionDesc());
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {
            Log.d("error", message);

        }

        @Override
        public void networkError(Throwable e) {

        }

    };


    private boolean mIsLoadSuccess = true;


    private void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected int getTranslucentColor() {
        return R.color.transparent;
    }

    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_webview);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使得布局延伸到状态栏和导航栏区域
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

//            //透明状态栏/导航栏
//            window.setStatusBarColor(Color.TRANSPARENT);
//            //这样的效果跟上述的主题设置效果类似
        }
    }

    @Override
    public void initPresenter() {
        webViewActivity = this;
        unsubscribe();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initView();
    }

    public void initView() {

        height = DeviceUtil.getStatusBarHeight(this);
        url = getIntent().getStringExtra("url");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setUserAgent(webview.getSettings().getUserAgentString() + " APP/android");
        webview.loadUrl(url);
        synCookies(this, url);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        //启用数据库
        webview.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webview.getSettings().setGeolocationEnabled(true);
        //设置定位的数据库路径
        webview.getSettings().setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webview.getSettings().setDomStorageEnabled(true);
        webview.addJavascriptInterface(this, "app");
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        //开启 database storage API 功能
        webview.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        Log.i(TAG, "cacheDirPath=" + cacheDirPath);
        //设置数据库缓存路径
        webview.getSettings().setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        webview.getSettings().setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        webview.getSettings().setAppCacheEnabled(true);

        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
                super.onReceivedError(webView, webResourceRequest, webResourceError);
                Logger.e("出错");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    return;
//                }
                // 在这里显示自定义错误页
                mErrorView.setVisibility(View.VISIBLE);
                mIsLoadSuccess = false;
                hideProgress();
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setComponent(null);
//                        intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                if (url.contains("platformapi/startapp") || url.contains("platformapi/startApp")) {
                    startAlipayActivity(url);
                    // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        && (url.contains("platformapi") && (url.contains("startapp")) || url.contains("startApp"))) {
                    startAlipayActivity(url);
                } else if (url.startsWith("tel:")) {
                    phone = url;
                    WebViewActivityPermissionsDispatcher.callPhoneWithCheck(WebViewActivity.this, url);
                    return true;
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                hideProgress();
                if (mIsLoadSuccess) {
                    mErrorView.setVisibility(View.GONE);
                } else {
                    mErrorView.setVisibility(View.VISIBLE);
                }
            }


        });

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                if (!s.contains("请稍候"))
                    titleCeneterTextView.setText(s);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
                super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = valueCallback;
                WebViewActivityPermissionsDispatcher.openSelectWithCheck(WebViewActivity.this);
                return true;
            }


            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                WebViewActivityPermissionsDispatcher.openSelectWithCheck(WebViewActivity.this);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                return super.onConsoleMessage(consoleMessage);
            }


        });
    }


    public void synCookies(Context context, String url) {
        CookieJarImpl cj = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        List<Cookie> cookies = cj.getCookieStore().getCookies();
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        StringBuffer sb = new StringBuffer();

        if (cookies.size() > 0) {
            String cookieString = cookies.get(0).name() + "=" + cookies.get(0).value() + "; domain=" + cookies.get(0).domain();
            cookieManager.setCookie(cookies.get(0).domain(), cookieString);
        }
        CookieSyncManager.getInstance().sync();
    }

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_aaa111222333bbb" + ".jpg");
        File temp = new File(imageStorageDir + File.separator + "temp" + ".jpg");
        imageUri = Uri.fromFile(file);
        imagePaths = file.getAbsolutePath();
        compressPath = temp.getAbsolutePath();
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            cameraIntents.add(i);
        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }


    private boolean mIsExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (webview.canGoBack()) {
                webview.goBack();
                return false;
            } else {
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                Log.e("result", result + "");
                if (result == null) {
                    afterOpenCamera();
                    imageUri = Uri.fromFile(new File(compressPath));
                    mUploadMessage.onReceiveValue(imageUri);
                    mUploadMessage = null;
                    Log.e("imageUri", imageUri + "");
                } else {
                    mUploadMessage.onReceiveValue(result);
                    mUploadMessage = null;
                }

            }

        }
    }

    /**
     * 解决拍照后在相册中找不到的问题
     */
    private void addImageGallery(File file) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * 拍照结束后
     */
    private void afterOpenCamera() {
        File f = new File(imagePaths);
        addImageGallery(f);
        FileUtils.compressFile(f.getPath(), compressPath);
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE
                || mUploadCallbackAboveL == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                afterOpenCamera();
                imageUri = Uri.fromFile(new File(compressPath));
                results = new Uri[]{imageUri};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
        return;
    }


    private void showNewVersionAlert(final String url, String desc) {
        DialogUtils.showAlert(this, desc, R.string.title_update_version
                , R.string.update_version, R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            try {
                                webViewActivity.startActivity(intent);
                            } catch (Exception e) {
                                Logger.e(e.getMessage());
                            }
                            if (forced == 1) {
                                finish();
                            }
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            if (forced == 1) {
                                finish();
                            }
                        } else if (which == DialogInterface.BUTTON_NEUTRAL) {
                            if (forced == 1) {
                                finish();
                            }
                        }
                    }
                },
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (forced == 1) {
                            finish();
                        }
                    }
                }
        );
    }


    /**
     * js调用清除WebView缓存
     */
    @JavascriptInterface
    public void clearAllUIWebViewData() {
        //清理Webview缓存数据库
        try {
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //WebView 缓存文件
        File appCacheDir = new File(getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME);
        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());

        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());

        //删除webview 缓存目录
        if (webviewCacheDir.exists()) {
            deleteFile(webviewCacheDir.getAbsolutePath());
        }
        //删除webview 缓存 缓存目录
        if (appCacheDir.exists()) {
            deleteFile(appCacheDir.getAbsolutePath());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:clearAllUIWebViewDataDone()");
            }
        });
    }


    //=======================动态权限的申请===========================================================<
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openSelect() {
        take();
    }

    /**
     * 为什么要获取这个权限给用户的说明
     *
     * @param request
     */
    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("有部分权限需要你的授权")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show()
        ;
    }

    /**
     * 如果用户不授予权限调用的方法
     */
    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        showPermissionDenied();
    }

    public void showPermissionDenied() {
        new AlertDialog.Builder(this)
                .setTitle("权限说明")
                .setCancelable(false)
                .setMessage("需要部分必要的权限，如果不授予可能会影响正常使用！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("赋予权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebViewActivityPermissionsDispatcher.openSelectWithCheck(WebViewActivity.this);
                    }
                })
                .create().show();
    }

    /**
     * 如果用户选择了让设备“不再询问”，而调用的方法
     */
    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        Toast.makeText(this, "不再询问授权权限！", Toast.LENGTH_SHORT).show();
    }


    //拨打电话权限
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void callPhone(String phone) {
        Intent call_intent = new Intent(Intent.ACTION_CALL, Uri.parse(phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.showToast(this, "您禁止了拨打电话权限，请在设置-权限管理中允许应用拨打电话");
            return;
        }
        startActivity(call_intent);
    }

    @OnShowRationale(Manifest.permission.CALL_PHONE)
    void showRationaleForCall(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("有部分权限需要你的授权")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    /**
     * 如果用户不授予权限调用的方法
     */
    @OnPermissionDenied({Manifest.permission.CALL_PHONE})
    void showDeniedForCall() {
        showCallPermissionDenied();
    }

    public void showCallPermissionDenied() {
        new AlertDialog.Builder(this)
                .setTitle("权限说明")
                .setCancelable(false)
                .setMessage("需要拨号权限，如果不授予可能会影响正常使用！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("赋予权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WebViewActivityPermissionsDispatcher.callPhoneWithCheck(WebViewActivity.this, phone);
                    }
                })
                .create().show();
    }

    /**
     * 如果用户选择了让设备“不再询问”，而调用的方法
     */
    @OnNeverAskAgain({Manifest.permission.CALL_PHONE})
    void showNeverAskForCall() {
        Toast.makeText(this, "不再询问授权权限！", Toast.LENGTH_SHORT).show();
    }


    private List<Address> getAddress(Location location) {
        List<Address> result = null;
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(getContext(), Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OnClick({R.id.btn_go, R.id.view_empty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_go:
                webview.loadUrl(edt.getText().toString());
                hideIM(edt, this);
                break;
            case R.id.view_empty:
                showProgress();
                webview.reload();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WebViewActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    // 调起支付宝并跳转到指定页面
    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * js调用app方法
     */
    @JavascriptInterface
    public void getAddress() {
        String address = preferences.getString("address", "");
        String longitude = preferences.getString("longitude", "");
        String latitude = preferences.getString("latitude", "");
        final String content = address + "," + longitude + "," + latitude;
        //app调用js setAddress(String address)方法
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webview.loadUrl("javascript:setAddress('" + content + "')");
            }
        });
    }


    /**
     * js调用app方法
     *
     * @param url #45b5fd
     */
    @JavascriptInterface
    public void openWebview(String url) {
        Intent intent = new Intent(this, NewWebViewActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }


    /**
     * 微信是否安装
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 支付宝是否安装
     *
     * @param context
     * @return
     */
    public static boolean isAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }


    private void updateImage(String filePath) {
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    filePath, filePath.substring(filePath.lastIndexOf("/"), filePath.length()), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
    }

    private void downloadBitmap(String path) {
        mSubscription = Network.downloadBitmap().download(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        byte[] bys = new byte[0];
                        try {
                            bys = responseBody.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                        String picName = System.currentTimeMillis() + "";
                        String filePath = FileUtils.saveBitmap(bitmap, picName);
                        updateImage(filePath);
                        Map<String, Object> map = new HashMap<>();
                        if (filePath.length() > 0) {
                            map.put("code", 1);
                            map.put("data", filePath);
                        } else {
                            map.put("code", 0);
                        }
                        final String result_scan = new Gson().toJson(map);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webview.loadUrl("javascript:downloadDone('" + result_scan + "')");
                            }
                        });

                    }
                });
    }
}
