package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentMenuBinding;
import com.sook.cs.letitgo.util.DataUtil;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_menu extends Fragment {
    FragmentMenuBinding binding;
    private Adapter_menu_list recyclerAdapter;
    private RecyclerView recyclerView;

    public customer_menu() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_menu_list(getActivity(), DataUtil.getMenuArrayList());
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
        recyclerView = binding.recyclerviewMenu;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        return binding.getRoot();
    }

    public void searchClick(View v) {
        Toast.makeText(getActivity(), binding.editSearch.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}
