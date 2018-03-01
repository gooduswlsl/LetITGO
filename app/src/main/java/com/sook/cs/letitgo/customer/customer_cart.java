package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ActivityCartBinding;
import com.sook.cs.letitgo.databinding.FragmentLikedMenuBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_cart extends AppCompatActivity {
    ActivityCartBinding binding;
    //private FragmentLikedMenuBinding binding;
    private Adapter_cart recyclerAdapter;
    private RecyclerView recyclerView;
    private MyDBHelpers_cart helper;
    private int RESULT_OK = -1;

    public customer_cart() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        recyclerAdapter = new Adapter_cart(this, new ArrayList<Order>());

        recyclerView = binding.recyclerviewCart;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        listCart();
    }


    private void listCart() {
        helper = new MyDBHelpers_cart(this, "cart.db", null, 1);
        ArrayList<Order> orderArrayList = helper.getCartList();
        Log.d("cart!", String.valueOf(orderArrayList.get(0).getMenu_seq()));

        Log.d("cart!", String.valueOf(orderArrayList.get(1).getMenu_seq()));

        Log.d("cart!", String.valueOf(orderArrayList.get(2).getMenu_seq()));

       // recyclerAdapter.addCartList(helper.getCartList());
        recyclerAdapter = new Adapter_cart(this, helper.getCartList());
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void orderClick(View v) {

    }
}