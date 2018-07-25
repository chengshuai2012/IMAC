package com.link.cloud.base;
import com.google.gson.JsonObject;
import com.link.cloud.bean.BindUserInfo;
import com.link.cloud.bean.DownLoadDataBean;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface BaseService {
    /**
     * 同步接口
     * @param params
     * @return
     */
    @POST("syncUserFeature")
    Observable<BaseEntity<ArrayList<DownLoadDataBean>>> syncUserFeature(@Body JsonObject params);

    /**
     * 根据设备ID和手机号查询会员信息
     *
     * @param params REQUEST BODY请求体
     * @return Observable<Member>
     */
//    @POST("getMemInfoByVeinDeviceIDPhone")
//    Observable<Member> getMemInfo( @Body JsonObject params);
    @POST("validationUser")
    Observable<BaseEntity<BindUserInfo>> getMemInfo(@Body JsonObject params);

}
