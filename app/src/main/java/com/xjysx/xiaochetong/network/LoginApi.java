package com.xjysx.xiaochetong.network;




import com.xjysx.xiaochetong.entities.LoginBean;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface LoginApi {
    @POST()
    Observable<BaseBean<LoginBean>> login(@Url String url, @QueryMap Map<String, String> map);
}
