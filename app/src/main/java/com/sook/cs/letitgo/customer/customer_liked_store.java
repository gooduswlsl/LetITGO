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

import com.sook.cs.letitgo.databinding.FragmentLikedStoreBinding;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.sook.cs.letitgo.util.DataUtil;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_liked_store extends Fragment {
    private FragmentLikedStoreBinding binding;
    private Adapter_seller_list recyclerAdapter;
    private RecyclerView recyclerView;
    private MyDBHelpers helper;
    private int RESULT_OK = -1, RESULT_CANCEL = 0;

    public customer_liked_store() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_seller_list(getActivity(), new ArrayList<Seller>());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLikedStoreBinding.inflate(inflater, container, false);
        liked();

        recyclerView = binding.recyclerviewStore;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            recyclerAdapter.sellerArrayList.remove(data.getIntExtra("position", 0));
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private void liked() {
        helper = new MyDBHelpers(this.getContext(), "liked.db", null, 1);
        int[] sSeq = helper.getStoreList();
        if (sSeq.length == 0)
            return;
        String sSeqList = Arrays.toString(sSeq);
        sSeqList = sSeqList.replace("[", "(");
        sSeqList = sSeqList.replace("]", ")");
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Seller>> call = remoteService.listLikedSeller(sSeqList);
        call.enqueue(new Callback<ArrayList<Seller>>() {
            @Override
            public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                ArrayList<Seller> list = response.body();
                recyclerAdapter.addSellerList(list);
            }

            @Override
            public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {
            }
        });
    }


}