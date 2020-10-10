package com.xjysx.xiaochetong.moudle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xjysx.xiaochetong.R;
import com.xjysx.xiaochetong.base.BaseActivity;
import com.xjysx.xiaochetong.network.BaseBean;
import com.xjysx.xiaochetong.network.BaseObserver;
import com.xjysx.xiaochetong.network.Network;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by danjj on 2020/10/10 0010.
 */
public class DemoActivity extends BaseActivity {

    @BindView(R.id.serialId)
    TextView serialIdTv;
    @BindView(R.id.imei)
    TextView imeiTv;
    @BindView(R.id.androidId)
    TextView androidIdTv;
    @BindView(R.id.submit)
    TextView submit;
    private String serialNum;
    private String serialNum2;
    private String imei1;
    private String imei2;
    private String ANDROID_ID;
    private BaseObserver observer = new BaseObserver() {
        @Override
        public void onSuccess(Object o) {

        }

        @Override
        public void onError(int code, String message, BaseBean baseBean) {

        }

        @Override
        public void networkError(Throwable e) {

        }

        @Override
        public void onNext(Object o) {

        }
    };


    @Override
    protected void loadLayout() {
        setContentView(R.layout.activity_demo);
    }


    @Override
    public void initPresenter() {
        //实例化TelephonyManager对象
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Method method = null;
        try {
            method = telephonyManager.getClass().getMethod("getDeviceId", int.class);
            //获取IMEI号
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            serialNum = getSerialNumber();
            try {
                imei1 = telephonyManager.getDeviceId();
            } catch (Exception e) {
                imei1 = "";
            }
            try {
                imei2 = (String) method.invoke(telephonyManager, 1);
                //获取MEID号
                String meid = (String) method.invoke(telephonyManager, 2);
            } catch (Exception e) {
                imei2 = "";
            }
            ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
            serialIdTv.setText(String.format("serialId1:%s", serialNum));
            imeiTv.setText(String.format("imei1:%s  imei2:%s", imei1, imei2));
            androidIdTv.setText(String.format("androidId:%s", ANDROID_ID));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @OnClick(R.id.submit)
    public void onViewClicked() {
        IdBean idBean = new IdBean(serialNum, serialNum2, imei1, imei2, ANDROID_ID);
        String json = new Gson().toJson(idBean);

        try {
            Network.getVersionApi().getVersion("https://www.baidu.com", getObjectToMap(idBean))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class IdBean {
        private String serialNum;
        private String serialNum2;
        private String imei1;
        private String imei2;
        private String ANDROID_ID;

        public IdBean(String serialNum, String serialNum2, String imei1, String imei2, String ANDROID_ID) {
            this.serialNum = serialNum;
            this.serialNum2 = serialNum2;
            this.imei1 = imei1;
            this.imei2 = imei2;
            this.ANDROID_ID = ANDROID_ID;
        }
    }

    //Object转Map
    public static Map<String, String> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<String, String> map = new LinkedHashMap<String, String>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String value = field.get(obj) == null ? "" : field.get(obj).toString();
            if (value == null) {
                value = "";
            }
            map.put(fieldName, value);
        }
        return map;
    }

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0+
                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("e", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }
}
