package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.databinding.FragmentLikedMenuBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_liked_menu extends Fragment {
    private FragmentLikedMenuBinding binding;
    private Adapter_menu_list recyclerAdapter;
    private RecyclerView recyclerView;
    private DBHelperLiked helper;
    private int RESULT_OK = -1;

    public customer_liked_menu() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerAdapter = new Adapter_menu_list(getContext(), new ArrayList<Menu>(), true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLikedMenuBinding.inflate(inflater, container, false);
        liked();

        recyclerView = binding.recyclerviewMenu;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            recyclerAdapter.menuArrayList.remove(data.getIntExtra("position", 0));
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    private void liked() {
        helper = new DBHelperLiked(this.getContext(), "liked.db", null, 1);
        int[] mSeq = helper.getMenuList();
        String mSeqList = Arrays.toString(mSeq);
        mSeqList = mSeqList.replace("[", "(");
        mSeqList = mSeqList.replace("]", ")");
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Menu>> call = remoteService.listLikedMenu(mSeqList);
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                ArrayList<Menu> list = response.body();
                recyclerAdapter.addMenuList(list);
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {
            }
        });
    }
}