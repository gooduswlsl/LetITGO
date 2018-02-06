package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentSellerBinding;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.sook.cs.letitgo.util.DataUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_store extends Fragment {
    FragmentSellerBinding binding;
     Adapter_seller_list recyclerAdapter;
     RecyclerView recyclerView;

    public customer_store() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_seller_list(getActivity(), DataUtil.getStoreArrayList());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller, container, false);
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

        recyclerView = binding.recyclerviewStore;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recyclerAdapter);
        listInfo();



        return binding.getRoot();
    }

    private void listInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Seller>> call = remoteService.listSellerInfo();

                call.enqueue(new Callback<ArrayList<Seller>>(){


                    @Override
                    public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                        ArrayList<Seller> list = response.body();
                        if(response.isSuccessful() && list!= null){
                            recyclerAdapter.addSellerList(list);
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {
                        Log.d("storelist", t.toString());
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public void searchClick(View v) {
        Toast.makeText(getActivity(), binding.editSearch.getText().toString(), Toast.LENGTH_SHORT).show();
    }

}