package com.sook.cs.letitgo.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.databinding.FragmentLikedStoreBinding;
import com.sook.cs.letitgo.util.DataUtil;

public class customer_liked_store extends Fragment {
    private FragmentLikedStoreBinding binding;
    private Adapter_seller_liked recyclerAdapter;
    private RecyclerView recyclerView;

    public customer_liked_store() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_seller_liked(getActivity(), DataUtil.getStoreArrayList());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLikedStoreBinding.inflate(inflater, container, false);

        recyclerView = binding.recyclerviewStore;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        return binding.getRoot();
    }
}