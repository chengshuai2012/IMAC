package com.link.cloud.base;
import com.google.gson.JsonObject;
import com.link.cloud.bean.DownLoadData;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;


/**
 * Created by Shaozy on 2016/8/10.
 */
public interface BaseService {
    /**
     * 同步接口
     * @param params
     * @return
     */
    @POST("syncUserFeature")
    Observable<BaseEntity<DownLoadData>> syncUserFeature(@Body JsonObject params);

}
