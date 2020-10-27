package com.xjysx.xiaochetong.network;




import com.xjysx.xiaochetong.entities.MainBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface IdentifyApi {
    @POST("/upload")
    Observable<String> identify(@Body RequestBody body);
}
