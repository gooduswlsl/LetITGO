package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentMyBinding;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_my extends Fragment {
    FragmentMyBinding binding;

    public customer_my() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false);
        binding.setFragment(this);
        callFragment(1);
        setButton(1);
        return binding.getRoot();
    }

    public void myClick(View v) {
        int num = Integer.parseInt(v.getTag().toString());
        callFragment(num);
        setButton(num);
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        switch (fragment_no) {
            case 1:
                customer_my_order fragment1 = new customer_my_order();
                transaction.replace(R.id.fragment_container2, fragment1, "fragment_order");
                transaction.commit();
                break;
            case 2:
                customer_my_info fragment2 = new customer_my_info();
                transaction.replace(R.id.fragment_container2, fragment2, "fragment_info");
                transaction.commit();
                break;
        }
    }

    private void setButton(int fragment_no) {
        binding.btnMyOrder.setImageResource(R.drawable.tab_orderx);
        binding.btnMyInfo.setImageResource(R.drawable.tab_infox);
        switch (fragment_no) {
            case 1:
                binding.btnMyOrder.setImageResource(R.drawable.tab_order);
                break;
            case 2:
                binding.btnMyInfo.setImageResource(R.drawable.tab_info);
                break;
        }
    }


}