package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentLikedBinding;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_liked extends Fragment {
    FragmentLikedBinding binding;

    public customer_liked() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_liked, container, false);
        binding.setFragment(this);
        callFragment(1);
        return binding.getRoot();
    }


    public void likedClick(View v) {
        int num = Integer.parseInt(v.getTag().toString());
        callFragment(num);
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (fragment_no) {
            case 1:
                customer_liked_store fragment1 = new customer_liked_store();
                transaction.replace(R.id.fragment_container2, fragment1);
                transaction.commit();
                break;
            case 2:
                customer_liked_menu fragment2 = new customer_liked_menu();
                transaction.replace(R.id.fragment_container2, fragment2);
                transaction.commit();
                break;

        }
    }
}