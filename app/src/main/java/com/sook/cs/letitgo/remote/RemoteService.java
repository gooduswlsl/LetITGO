package com.sook.cs.letitgo.remote;

import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Sales;
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
    String BASE_URL = "http://192.168.0.248:3000";  //집

    String CUSTOMER_IMG_URL = BASE_URL + "/customer/";
    String SELLER_IMG_URL = BASE_URL + "/seller/";
    String MENU_IMG_URL = BASE_URL + "/menu/";

    //멤버
    @GET("/member/{phone}")
    Call<String> selectMemberInfo(@Path("phone") String phone);


    //소비자
    @POST("/member/customer")
    Call<String> insertCustomerInfo(@Body Customer customer);

    @GET("/member/customer/{customer_seq}")
    Call<Customer> selectCustomerInfo(@Path("customer_seq") int seq);

    @POST("/member/sendNewCustomerRegId")
    Call<String> sendNewCustomerRegId(@Query("seq") int seq, @Query("regId") String regId);

    @Multipart
    @POST("/member/img_upload")
    Call<ResponseBody> uploadCustomerImg(@Part MultipartBody.Part file);

    //소비자 회원정보 수정, 탈퇴
    @POST("/member/changeProfile")
    Call<String> changeCustomerInfo(@Body Customer customer);

    @POST("/member/leaveMember")
    Call<String> leaveMember(@Query("seq") int seq);

    //매장정보얻어오기
    @GET("/menu/sellerList")
    Call<ArrayList<Seller>> listSellerInfo(@Query("type") int type);

    @GET("/menu/sellerList/{sSeq}")
    Call<Seller> selectSeller(@Path("sSeq") int sSeq);

    @GET("/menu/sellerMap")
    Call<ArrayList<Seller>> listSellerMap(@Query("lat") double lat, @Query("lng") double lng);

    //메뉴정보얻어오기
    @GET("/menu/menuList")
    Call<ArrayList<Menu>> listMenuInfo(@Query("type") int type);

    @GET("/menu/menuList/{mSeq}")
    Call<Menu> selectMenu(@Path("mSeq") int mSeq);

    //검색
    @GET("/menu/searchSeller")
    Call<ArrayList<Seller>> searchSeller(@Query("key") String key, @Query("type") int type);
    @GET("/menu/searchMenu")
    Call<ArrayList<Menu>> searchMenu(@Query("key") String key, @Query("type") int type);

    //즐겨찾기
    @GET("/menu/likedSeller")
    Call<ArrayList<Seller>> listLikedSeller(@Query("sSeqList") String sSeqList);

    @GET("/menu/likedMenu")
    Call<ArrayList<Menu>> listLikedMenu(@Query("mSeqList") String mSeqList);


    //판매자
    @POST("/member/seller")
    Call<String> insertSellerInfo(@Body Seller seller);

    @GET("/member/seller/{seller_seq}")
    Call<Seller> selectSellerInfo(@Path("seller_seq") int seq);

    @POST("/member/sendNewSellerRegId")
    Call<String> sendNewSellerRegId(@Query("seq") int seq, @Query("regId") String regId);

    @Multipart
    @POST("/member/img_upload2")
    Call<ResponseBody> uploadSellerImg(@Part MultipartBody.Part file);

    @GET("/member/getSellerRegId/{seller_seq}")
    Call<String> getRegId(@Path("seller_seq") int seq);

    @GET("/member/getCustomerRegId/{customer_seq}")
    Call<String> getCustomerRegId(@Path("customer_seq") int customer_seq);

    //판매자 회원정보 수정, 탈퇴
    @POST("/member/changeSellerInfo")
    Call<String> changeSellerInfo(@Body Seller seller);
    @POST("/member/leaveSeller")
    Call<String> leaveSeller(@Query("seq") int seq);

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

    //소비자가 주문하기 눌렀을 경우
    @POST("/order/sendOrder")
    Call<String> sendOrder(@Body Order order, @Query("cSeq") int cSeq);

    //매장 주문 리스트
    @GET("/order/list")
    Call<ArrayList<Order>> listOrder(@Query("seller_seq") int sellerSeq);

    @POST("/order/sendPermit")
    Call<String> sendPermit(@Query("permit") int permit,
                            @Query("seq") int seq);

    @POST("/order/sendTotal_price")
    Call<String> sendTotal_price(@Query("seq") int seq, @Query("total_price") int total_price);

    //매장 매출
    @GET("/order/getMonthSales")
    Call<ArrayList<Sales>> getMonthSales(@Query("seller_seq") int sellerSeq);

    @GET("/order/myList")
    Call<ArrayList<Order>> listMyOrder(@Query("cust_seq") int cust_seq, @Query("period") String period);


}