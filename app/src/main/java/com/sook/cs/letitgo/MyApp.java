package com.sook.cs.letitgo;

import android.app.Application;
import android.os.StrictMode;

import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Member;
import com.sook.cs.letitgo.item.Store;


/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {
    private Member member;
    private Store store;
    private Customer customer;

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

    public Store getStore() {
        if (store == null) store = new Store();

        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Customer getCustomer() {
        if (customer == null) customer = new Customer();
        return customer;
    }


    public int getStoreSeq() {
        return store.seq;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
