package com.sook.cs.letitgo.remote;


import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Member;
import com.sook.cs.letitgo.item.Seller;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    String BASE_URL = "http://192.168.10.130:3000";
    String MEMBER_ICON_URL = BASE_URL + "/member/";
    String IMAGE_URL = BASE_URL + "/img/";

    //멤버
    @GET("/member/{phone}")
    Call<String> selectMemberInfo(@Path("phone")String phone);

    @POST("/member/info")
    Call<String> insertMemberInfo(@Body Member member);

    @Multipart
    @POST("/member/img_upload")
    Call<ResponseBody> uploadMemberIcon(@Part("member_seq") RequestBody memberSeq,
                                        @Part MultipartBody.Part file);

    //소비자
    @POST("member/customer")
    Call<String> insertCustomerInfo(@Body Customer customer);

    @GET("member/customer/{customer_seq}")
    Call<Customer> selectCustomerInfo(@Path("customer_seq") int seq);

    //판매자
    @GET("member/store/{store_seq}")
    Call<Seller> selecStoreInfo(@Path("store_seq") int seq);




}