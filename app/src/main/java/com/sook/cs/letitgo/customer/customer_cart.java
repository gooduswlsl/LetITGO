package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ActivityCartBinding;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_cart extends AppCompatActivity {
    private ActivityCartBinding binding;
    private Adapter_cart recyclerAdapter;
    private RecyclerView recyclerView;
    private DBHelperCart helper;
    private ArrayList<Order> orderArrayList;

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
        helper = new DBHelperCart(this, "cart.db", null, 1);
        orderArrayList = helper.getCartList();

        if(orderArrayList.size()==0) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.btnOrder.setVisibility(View.GONE);
        }
        else {
            binding.tvEmpty.setVisibility(View.INVISIBLE);
            recyclerAdapter = new Adapter_cart(this, orderArrayList);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    public void orderClick(View v) {

    }

    public void empty(){
        binding.tvEmpty.setVisibility(View.VISIBLE);
        binding.btnOrder.setVisibility(View.GONE);
    }

}