package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.DialogMenuBinding;
import com.sook.cs.letitgo.databinding.DialogOrderBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_dialog_order extends Activity {
    DialogOrderBinding binding;
    private Adapter_orderList_detail adapter;
    private RecyclerView recyclerView;
    Order order;
    int cSeq, position;
    Menu menu;

    public customer_dialog_order() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        cSeq = ((MyApp) getApplicationContext()).getCustomer().getSeq();

        binding = DataBindingUtil.setContentView(this, R.layout.dialog_order);
        order = (Order) getIntent().getSerializableExtra("order");
        position = getIntent().getIntExtra("position", -1);

        binding.setOrder(order);
        recyclerView = binding.recyclerview;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        setSeller(order.getSeller_seq());
        showButton();
        listOrder();
    }

    private void showButton() {
        if (order.getPermit() == 3)
            binding.btnTake.setVisibility(View.VISIBLE);
        else
            binding.btnTake.setVisibility(View.GONE);
    }

    private void setSeller(int seller_seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Seller> call = remoteService.selectSeller(seller_seq);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                Seller seller = response.body();
                binding.setSeller(seller);
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
            }
        });
    }

    private void listOrder() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Order>> call = remoteService.dialogOrder(cSeq, order.getSeller_seq(), order.getTime_order());
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                ArrayList<Order> list = response.body();
                if (list.size() == 0)
                    Log.d("err", "0");
                adapter = new Adapter_orderList_detail(getApplicationContext(), list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Log.d("err", t.toString());

            }
        });
    }

    public void takeClick(View v) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendCustPermit(cSeq, order.getSeller_seq(), order.getTime_order());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent it = new Intent();
                    it.putExtra("position", position);
                    setResult(RESULT_OK, it);
                    finish();
                } else {
                    Log.d("order", "response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("order", t.toString());
            }
        });
    }

    public void clickX(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

}