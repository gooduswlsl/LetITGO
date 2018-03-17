package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentSellerTypeBinding;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_store_type extends Fragment {
    private FragmentSellerTypeBinding binding;
    private Adapter_seller_img adapterSellerImg;
    private Adapter_seller_list adapterSellerList;
    private RecyclerView recyclerView;
    private int type;

    public customer_store_type() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterSellerImg = new Adapter_seller_img(getActivity(), new ArrayList<Seller>());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = getArguments().getInt("type",0);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seller_type, container, false);
        binding.setFragment(this);
        binding.editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Log.d("search", "search click");
                        searchClick(v);
                        break;
                    default:
                        searchClick(v);
                        return false;
                }
                return true;
            }
        });

        recyclerView = binding.recyclerviewStore;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapterSellerImg);
        listInfo();

        return binding.getRoot();
    }

    private void listInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Seller>> call = remoteService.listSellerInfo(type);
        call.enqueue(new Callback<ArrayList<Seller>>() {
            @Override
            public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                ArrayList<Seller> list = response.body();
                if (response.isSuccessful() && list != null) {
                    adapterSellerImg.addSellerList(list);
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
        final String key = binding.editSearch.getText().toString();
        if (!key.equals("")) {
            recyclerView.removeAllViews();
            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
            Call<ArrayList<Seller>> call = remoteService.searchSeller(key, type);
            call.enqueue(new Callback<ArrayList<Seller>>() {
                @Override
                public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                    ArrayList<Seller> list = response.body();
                    if (response.isSuccessful() && list != null) {
                        adapterSellerList = new Adapter_seller_list(getActivity(), list, false);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(adapterSellerList);

                        binding.tvCount.setText(key + "에 대한 검색 결과가 " + list.size() + "건이 있습니다.");
                        binding.tvCount.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {

                }
            });
        }

    }

}