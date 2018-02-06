package com.sook.cs.letitgo.remote;


import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Seller;

import java.util.ArrayList;

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
import retrofit2.http.Query;

/**
 * 서버에 호출할 메소드를 선언하는 인터페이스
 */
public interface RemoteService {
    //String BASE_URL = "http://192.168.10.130:3000";  //마이크임팩트
    String BASE_URL = "http://192.168.21.168:3000";  //학교
    //String BASE_URL = "http://175.193.168.234:3000";  //301호

    String CUSTOMER_IMG_URL = BASE_URL + "/customer/";
    String SELLER_IMAGE_URL = BASE_URL + "/seller/";

    //멤버
    @GET("/member/{phone}")
    Call<String> selectMemberInfo(@Path("phone")String phone);


    //소비자
    @POST("member/customer")
    Call<String> insertCustomerInfo(@Body Customer customer);
    @GET("member/customer/{customer_seq}")
    Call<Customer> selectCustomerInfo(@Path("customer_seq") int seq);

    @Multipart
    @POST("/member/img_upload")
    Call<ResponseBody> uploadCustomerImg(@Part MultipartBody.Part file);

    //매장정보얻어오기
    @GET("menu/sellerList")
    Call<ArrayList<Seller>> listSellerInfo();
    @GET("menu/sellerList/{seller_seq}")
    Call<Seller> selectSellerList(@Path("seller_seq") int seller_seq);


    //판매자
    @POST("/member/seller")
    Call<String> insertSellerInfo(@Body Seller seller);
    @GET("member/seller/{seller_seq}")
    Call<Seller> selectSellerInfo(@Path("seller_seq") int seq);

    @Multipart
    @POST("/member/img_upload2")
    Call<ResponseBody> uploadSellerImg(@Part MultipartBody.Part file);

    //매장 메뉴 등록, 추가, 수정
    @POST("/menu/update")
    Call<String> insertMenuInfo(@Body Menu menu);

    //매장 메뉴 리스트
    @GET("/menu/list")
    Call<ArrayList<Menu>> listMenu(@Query("seller_seq") int sellerSeq);

}