package com.sook.cs.letitgo.remote;


import com.sook.cs.letitgo.Store;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    String BASE_URL = "http://192.168.21.168:3000";
    String MEMBER_ICON_URL = BASE_URL + "/store/";
    String IMAGE_URL = BASE_URL + "/img/";

    //사용자 정보
    @GET("/store/{phone}")
    Call<Store> selectStoreInfo(@Path("phone") String phone);

    @POST("/store/info")
    Call<String> insertStoreInfo(@Body Store store);

    @FormUrlEncoded
    @POST("/store/phone")
    Call<String> insertStorePhone(@Field("phone") String phone);

  }