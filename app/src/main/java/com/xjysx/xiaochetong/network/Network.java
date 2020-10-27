package com.xjysx.xiaochetong.network;

import android.content.Context;
import android.util.Log;


import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * api
 */


public class Network {



    public static final String KEY = "20190329#$@$";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static String BASE_URL = "https://v2-api.jsdama.com/";
    public static String SERVICE = "https://v2-api.jsdama.com";
    public static String WORK_URL = SERVICE + "/agent/interface/pad_interface.jsp";//正式地址

    private static GetVersionApi getVersionApi;
    private static DownloadBitmapApi downloadBitmapApi;
    private static GetMainDataApi getMainDataApi;
    private static IdentifyApi identifyApi;


    public static SetCookieCache cookieCache = new SetCookieCache();


    public Network() {

    }

    public static void init(Context context) {
        Map<String, Object> map = new HashMap<>();
        NetworkInterceptor interceptor = new NetworkInterceptor(map);
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(context));
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
        OkHttpUtils.initClient(okHttpClient);
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static GetVersionApi getVersionApi() {
        if (getVersionApi == null) {
            getVersionApi = retrofit.create(GetVersionApi.class);
        }
        Log.e("method", "getVersion");
        return getVersionApi;
    }

    /**
     * 获取主页信息
     *
     * @return
     */
    public static GetMainDataApi getMainData() {
        if (getMainDataApi == null) {
            getMainDataApi = retrofit.create(GetMainDataApi.class);
        }
        Log.e("method", "getMainData");
        return getMainDataApi;
    }


    /**
     * 下载图片
     *
     * @return
     */
    public static DownloadBitmapApi downloadBitmap() {
        if (downloadBitmapApi == null) {
            downloadBitmapApi = retrofit.create(DownloadBitmapApi.class);
        }
        Log.e("method", "downloadBitmapApi");
        return downloadBitmapApi;
    }

    /**
     * 下载图片
     *
     * @return
     */
    public static IdentifyApi identify() {
        if (identifyApi == null) {
            identifyApi = retrofit.create(IdentifyApi.class);
        }
        Log.e("method", "IdentifyApi");
        return identifyApi;
    }

}
