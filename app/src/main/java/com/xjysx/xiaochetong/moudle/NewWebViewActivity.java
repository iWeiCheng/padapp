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
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.network.Network;
import com.xjysx.xiaochetong.util.FileUtils;
import com.xjysx.xiaochetong.util.ToastUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import okhttp3.Cookie;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;


/**
 * webView详情页
 * Created by danjj on 2016/12/8.
 */
@RuntimePermissions
public class NewWebViewActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.titleLeft_textView)
    TextView titleLeftTextView;

    @BindView(R.id.titleCeneter_textView)
    TextView titleCeneterTextView;
    @BindView(R.id.webView)
    com.tencent.smtt.sdk.WebView webView;
    @BindView(R.id.no_network_layout)
    RelativeLayout noNetWorkLayout;
    @BindView(R.id.reload)
    TextView reload;
    @BindView(R.id.more)
    ImageView more;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.tv_close)
    TextView tvClose;

    private String url1;
    private String backurl;
    private String phone;

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调</span>
    private Uri imageUri;
    private String imagePaths;
    private String compressPath;

    @Override
    protected int getTranslucentColor() {
        return R.color.webview_title_bg;
    }


    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_webview_new);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void getExtra(@NonNull Bundle bundle) {
        super.getExtra(bundle);
        url1 = bundle.getString("url");
    }

    @Override
    protected void setListener(Bundle savedInstanceState) {
        super.setListener(savedInstanceState);
        titleLeftTextView.setOnClickListener(this);
        reload.setOnClickListener(this);
        more.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
        tvClose.setOnClickListener(this);
        llMore.setOnClickListener(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        titleLeftTextView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        synCookies(this, url1);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //启用数据库
        webView.getSettings().setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webView.getSettings().setGeolocationEnabled(true);
        //设置定位的数据库路径
        webView.getSettings().setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(this, "app");
        webView.loadUrl(url1);
        webView.setWebViewClient(new WebViewClient() {


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (!error.getDescription().equals("net::ERR_FILE_NOT_FOUND")) {
                    noNetWorkLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (url.contains("platformapi/startapp")) {
                    startAlipayActivity(url);
                    // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        && (url.contains("platformapi") && url.contains("startapp"))) {
                    startAlipayActivity(url);
                } else if (url.startsWith("tel:")) {
                    phone = url;
                    NewWebViewActivityPermissionsDispatcher.callPhoneWithCheck(NewWebViewActivity.this, url);
                    return true;
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                dismissDialog();
                noNetWorkLayout.setVisibility(View.GONE);
                webView.loadUrl("javascript:window.java_obj.getSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                titleCeneterTextView.setText(webView.getTitle());
                super.onPageFinished(webView, s);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!title.startsWith("http")) {
                    titleCeneterTextView.setText(title);
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
                super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
            }


            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                NewWebViewActivityPermissionsDispatcher.openSelectWithCheck(NewWebViewActivity.this);
                return true;
            }


            @Override
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                NewWebViewActivityPermissionsDispatcher.openSelectWithCheck(NewWebViewActivity.this);
            }

        });


    }

    //=======================动态权限的申请===========================================================<
    @NeedsPermission({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void openSelect() {
        take();
    }

    /**
     * 为什么要获取这个权限给用户的说明
     *
     * @param request
     */
    @OnShowRationale({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
    @OnPermissionDenied({
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
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

                    }
                })
                .setPositiveButton("赋予权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewWebViewActivityPermissionsDispatcher.openSelectWithCheck(NewWebViewActivity.this);
                    }
                })
                .create().show();
    }

    /**
     * 如果用户选择了让设备“不再询问”，而调用的方法
     */
    @OnNeverAskAgain({
            Manifest.permission.CAMERA,
            /*Manifest.permission.WRITE_CONTACTS,*/
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
                        NewWebViewActivityPermissionsDispatcher.callPhoneWithCheck(NewWebViewActivity.this, phone);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        NewWebViewActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        Intent chooserIntent = Intent.createChooser(i, "选择图片");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }


    public void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        Iterator<Cookie> it = Network.cookieCache.iterator();
        while (it.hasNext()) {
            Cookie cookie1 = it.next();
            String cookieString = cookie1.name() + "=" + cookie1.value() + "; domain=" + cookie1.domain();
            cookieManager.setCookie(cookie1.domain(), cookieString);
        }
        CookieSyncManager.getInstance().sync();
    }


    public Bitmap getAppIcon() {
        getPackageManager().getApplicationLogo(getApplicationInfo());
        Drawable d = getPackageManager().getApplicationIcon(getApplicationInfo()); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        return drawableToBitmap(bd);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (backurl != null && !backurl.equals("") && backurl.length() == 1) {
                int steps = Integer.parseInt(backurl);
                for (int i = 0; i < steps; i++) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        clearCookie();
                    }
                }
                return false;
            } else {
                clearCookie();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titleLeft_textView:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    clearCookie();
                }
                break;
            case R.id.reload:
                showProgressDialog();
                webView.reload();
                break;
            case R.id.more:
                if (llMore.getVisibility() == View.VISIBLE) {
                    llMore.setVisibility(View.GONE);
                } else {
                    llMore.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_refresh:
                webView.reload();
                llMore.setVisibility(View.GONE);
                break;
            case R.id.tv_close:
                clearCookie();
                break;
            case R.id.ll_more:
                llMore.setVisibility(View.GONE);
                break;
        }
    }


    private void clearCookie() {
        //清除WebView中cookie
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();
        finish();
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
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void toFirstPage() {
        clearCookie();
    }
}



