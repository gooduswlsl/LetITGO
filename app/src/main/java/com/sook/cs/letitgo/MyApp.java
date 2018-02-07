package com.sook.cs.letitgo;

import android.app.Application;
import android.os.StrictMode;

import com.sook.cs.letitgo.item.Customer;
//import com.sook.cs.letitgo.item.Member;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Seller;


/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {
   // private Member member;
    private Seller seller;
    private Customer customer;
    private Menu menu;

    @Override
    public void onCreate() {
        super.onCreate();

        // FileUriExposedException 문제를 해결하기 위한 코드
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

//    public Member getMember() {
//        if (member == null) member = new Member();
//        return member;
//    }

    //public void setMember(Member member) {
     //  this.member = member;
   //}

    public Seller getSeller() {
        if (seller == null) seller = new Seller();

        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Customer getCustomer() {
        if (customer == null) customer = new Customer();
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Menu getMenu(){
        if(menu==null) menu = new Menu();
        return menu;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }

    public int getStoreSeq() {
        return seller.seq;
    }

}
