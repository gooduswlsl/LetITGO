package com.sook.cs.letitgo.customer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapterSeller extends FragmentStatePagerAdapter {

    public FragmentAdapterSeller(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        customer_store_type fragment = new customer_store_type();
        Bundle bundle = new Bundle(1);
        bundle.putInt("type", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override

    public int getCount() {
        return 8; // 원하는 페이지 수
    }

}