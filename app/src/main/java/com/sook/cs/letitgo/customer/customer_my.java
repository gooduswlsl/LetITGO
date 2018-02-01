package com.sook.cs.letitgo.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_my extends Fragment {



    public customer_my(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

}
