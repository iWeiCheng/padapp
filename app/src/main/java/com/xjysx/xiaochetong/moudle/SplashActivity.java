package com.xjysx.xiaochetong.moudle;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.SPUtils;
import com.xjysx.xiaochetong.app.Const;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.util.RandomNum;
import com.xjysx.xiaochetong.util.SignatureUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv_splash)
    ImageView imageView;

    private boolean isFirstOpen = true;

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {

        WeakReference<SplashActivity> weakReference;

        public MyHandler(SplashActivity splashActivity) {
            weakReference = new WeakReference<>(splashActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                    Intent intent = new Intent(weakReference.get(), SplashWebViewActivity.class);
                    weakReference.get().startActivity(intent);
                }
//            }
            weakReference.get().finish();
        }
    }


    @Override
    protected void loadLayout() {
//        Window window = getWindow();
//        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // 加上这句设置为全屏 不加则只隐藏title
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initPresenter() {
//        imageView.setImageResource(R.mipmap.bb1);
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
//        versionCode = AppUtils.getVersionCode(this);
//        versionName = AppUtils.getVersionName(this);

        // 判断是否是第一次开启应用
        isFirstOpen = (boolean) SPUtils.get(this, Const.FIRST_OPEN, true);
        Log.d("tag", "isFirstOpen:" + isFirstOpen);
//        if (isFirstOpen) {
//            SplashActivityPermissionsDispatcher.startWelcomeGuideActivityWithCheck(this);
//        } else {
            handler.sendEmptyMessageDelayed(1, 1500);
//        }
    }

    @Override
    protected int getTranslucentColor() {
        return R.color.transparent;
    }


    private void getVersion() {
        showProgressDialog();
        String str_random = RandomNum.getrandom();
        Map<String, String> map = new HashMap<>();
        map.put("method", "getVersion");
        map.put("type", "1");
//        map.put("versionName", getResources().getString(R.string.versionName));
        map.put("random", str_random);
        String str_signature = SignatureUtil.getSignature(map);
        map.put("signature", str_signature);

//        mSubscription = Network.getVersionApi().getVersion(Network.WORK_URL, map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer_version);

    }

    //=======================动态权限的申请===========================================================<
    @NeedsPermission({
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void startWelcomeGuideActivity() {
        handler.sendEmptyMessageDelayed(2, 1000);
    }

    /**
     * 为什么要获取这个权限给用户的说明
     *
     * @param request
     */
    @OnShowRationale({
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage("有部分权限需要你的授权")
//                .setPositiveButton(R.string.imtrue, (dialog, button) -> request.proceed())
//                .setNegativeButton(R.string.cancel, (dialog, button) -> request.cancel())
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
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        showPermissionDenied();
    }

    public void showPermissionDenied() {
        new AlertDialog.Builder(this)
                .setTitle("权限说明")
                .setCancelable(false)
                .setMessage("本应用需要部分必要的权限，如果不授予可能会影响正常使用！")
                .setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("赋予权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SplashActivityPermissionsDispatcher.startWelcomeGuideActivityWithCheck(SplashActivity.this);
                    }
                })
                .create().show();
    }

    /**
     * 如果用户选择了让设备“不再询问”，而调用的方法
     */
    @OnNeverAskAgain({
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        Toast.makeText(this, "不再询问授权权限！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

//=======================动态权限的申请===========================================================>

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private boolean isExites() {
        if (!isTaskRoot()) {
            finish();
            return true;
        }
        return false;
    }
}
