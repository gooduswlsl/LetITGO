package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentSellerBinding;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_store extends Fragment {
    private FragmentSellerBinding binding;
    private TabLayout tab;
    private ViewPager pager;

    public customer_store() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller, container, false);
        binding.setFragment(this);

        tab = binding.tab;
        pager = binding.viewPager;

        pager.setAdapter(new FragmentAdapterSeller(getFragmentManager()));
        tab.addTab(tab.newTab().setText("ALL"),0,true);
        tab.addTab(tab.newTab().setText("KOREAN"),1);
        tab.addTab(tab.newTab().setText("CHINESE"),2);
        tab.addTab(tab.newTab().setText("JAPANESE"),3);
        tab.addTab(tab.newTab().setText("AMERICAN"),4);
        tab.addTab(tab.newTab().setText("SNACK"),5);
        tab.addTab(tab.newTab().setText("CAFE"),6);
        tab.addTab(tab.newTab().setText("ETC"),7);
        tab.addOnTabSelectedListener(listener);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        return binding.getRoot();
    }

    TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener(){

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            pager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

}