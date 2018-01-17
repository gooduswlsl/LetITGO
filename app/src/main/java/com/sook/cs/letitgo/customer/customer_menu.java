package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentMenuBinding;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_menu extends Fragment {
    FragmentMenuBinding binding;

    public customer_menu() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false);
        binding.setFragment(this);
        binding.editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        searchClick(v);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
        return binding.getRoot();
    }

    public void searchClick(View v) {
        Toast.makeText(getActivity(), binding.editSearch.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
