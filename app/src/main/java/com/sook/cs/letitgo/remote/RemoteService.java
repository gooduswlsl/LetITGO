package com.sook.cs.letitgo.remote;


import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Seller;

import java.util.ArrayList;

import okhttp3.MultipartBody;
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
    //String BASE_URL = "http://192.168.21.168:3000";  //학교
//    String BASE_URL = "http://192.168.30.77:3000";  //집
    String BASE_URL = "http://192.168.53.81:3000";  //예원학교

    String CUSTOMER_IMG_URL = BASE_URL + "/customer/";
    String SELLER_IMG_URL = BASE_URL + "/seller/";
    String MENU_IMG_URL = BASE_URL + "/menu/";

    //멤버
    @GET("/member/{phone}")
    Call<String> selectMemberInfo(@Path("phone")String phone);


    //소비자
    @POST("/member/customer")
    Call<String> insertCustomerInfo(@Body Customer customer);
    @GET("/member/customer/{customer_seq}")
    Call<Customer> selectCustomerInfo(@Path("customer_seq") int seq);

    @Multipart //소비자의 프로필사진 업로드
    @POST("/member/img_upload")
    Call<ResponseBody> uploadCustomerImg(@Part MultipartBody.Part file);

    //매장정보얻어오기
    @GET("/menu/sellerList")
    Call<ArrayList<Seller>> listSellerInfo();
    @GET("/menu/sellerList/{sSeq}")
    Call<Seller> selectSeller(@Path("sSeq") int sSeq);
    //메뉴정보얻어오기
    @GET("/menu/menuList")
    Call<ArrayList<Menu>> listMenuInfo();
    @GET("/menu/menuList/{mSeq}")
    Call<Menu> selectMenu(@Path("mSeq") int mSeq);
    //검색
    @GET("/menu/searchSeller")
    Call<ArrayList<Seller>> searchSeller(@Query("key") String key);
    @GET("/menu/searchMenu")
    Call<ArrayList<Menu>> searchMenu( @Query("key") String key);


    //판매자
    @POST("/member/seller")
    Call<String> insertSellerInfo(@Body Seller seller);
    @GET("/member/seller/{seller_seq}")
    Call<Seller> selectSellerInfo(@Path("seller_seq") int seq);

    @Multipart //판매자의 매장프로필 사진 업로드
    @POST("/member/img_upload2")
    Call<ResponseBody> uploadSellerImg(@Part MultipartBody.Part file);


    //매장 메뉴 등록, 추가, 수정
    @POST("/menu/update")
    Call<String> insertMenuInfo(@Body Menu menu);

    //매장 메뉴 리스트
    @GET("/menu/list")
    Call<ArrayList<Menu>> listMenu(@Query("seller_seq") int sellerSeq);

    //메뉴 사진 추가
    @Multipart
    @POST("/menu/icon_add")
    Call<ResponseBody> addMenuIcon(@Part MultipartBody.Part file);

}