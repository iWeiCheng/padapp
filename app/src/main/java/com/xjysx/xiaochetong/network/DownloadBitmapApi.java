package com.xjysx.xiaochetong.network;




import com.xjysx.xiaochetong.entities.VersionBean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface DownloadBitmapApi {
    @POST()
    Observable<ResponseBody> download(@Url String url);
}
