package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.databinding.FragmentLikedMenuBinding;
import com.sook.cs.letitgo.databinding.FragmentMyOrderBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_my_order extends Fragment {
    private FragmentMyOrderBinding binding;
    private Adapter_order recyclerAdapter;
    private RecyclerView recyclerView;

    public customer_my_order() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_order(getContext(), new ArrayList<Order>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyOrderBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        recyclerView = binding.recyclerviewOrder;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);

        tabClick(binding.btn1);

        return binding.getRoot();
    }

    private void listOrder(String period) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Order>> call = remoteService.listMyOrder(((MyApp) getActivity().getApplicationContext()).getCustomer().getSeq(), period);
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                ArrayList<Order> list = response.body();
                if (response.isSuccessful() && list != null) {
                    recyclerAdapter = new Adapter_order(getActivity(), list);
                    recyclerView.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });
    }

    public void tabClick(View v) {
        binding.btn1.setSelected(false);
        binding.btn2.setSelected(false);
        binding.btn3.setSelected(false);

        v.setSelected(true);
        listOrder((v.getTag().toString()));
    }
}