package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentMyOrderBinding;
import com.sook.cs.letitgo.databinding.FragmentOrderlistBinding;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_orderlist extends Fragment {
    FragmentOrderlistBinding binding;
    private Adapter_orderList adapter;
    private RecyclerView recyclerView;

    //  permit값 보내기 (기본값 0, 거절 -1, 수락1, 준비시작 2, 준비완료 3, 수령완료 4)

    public customer_orderlist() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new Adapter_orderList(getContext(), new ArrayList<Order>());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.orderArrayList.remove(data.getIntExtra("position", 0));
        adapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderlistBinding.inflate(inflater, container, false);
        binding.setFragment(this);

        recyclerView = binding.recyclerviewOrder;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        listOrder();
        return binding.getRoot();
    }

    private void listOrder() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Order>> call = remoteService.showOrder(((MyApp) getActivity().getApplicationContext()).getCustomer().getSeq());
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                ArrayList<Order> list = response.body();
                if (response.isSuccessful() && list != null) {
                    if (list.size() == 0)
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                    else
                        binding.tvEmpty.setVisibility(View.GONE);

                    adapter = new Adapter_orderList(getActivity(), list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });
    }
}