package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentOrderlistBinding;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_orderlist extends Fragment {
    FragmentOrderlistBinding binding;

  //  permit값 보내기 (기본값 0, 거절 -1, 수락1, 준비시작 2, 준비완료 3, 수령완료 4)

    public customer_orderlist() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orderlist, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }
}