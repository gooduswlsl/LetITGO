package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.databinding.FragmentLikedMenuBinding;
import com.sook.cs.letitgo.util.DataUtil;

public class customer_liked_menu extends Fragment {
    private FragmentLikedMenuBinding binding;
    private Adapter_menu_liked recyclerAdapter;
    private RecyclerView recyclerView;

    public customer_liked_menu() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_menu_liked(getActivity(), DataUtil.getMenuArrayList());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLikedMenuBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerviewMenu;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        return binding.getRoot();
    }
}