package com.xjysx.xiaochetong.network;




import com.xjysx.xiaochetong.entities.MainBean;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface GetMainDataApi {
    @POST()
    Observable<BaseBean<MainBean>> getMainData(@Url String url, @QueryMap Map<String, String> map);
}
