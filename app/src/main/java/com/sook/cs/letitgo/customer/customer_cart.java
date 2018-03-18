package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ActivityCartBinding;
import com.sook.cs.letitgo.item.Menu;
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
    private ArrayList<Order> orderList;
    private Animation anim;
    RequestQueue queue;
    private int pp;
    private int cSeq, mPrice, price, i;
    private final String TAG = this.getClass().getSimpleName();

    public customer_cart() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.actionbar_back);
        ((TextView) ab.getCustomView().findViewById(R.id.ab_title)).setText("장바구니");

        cSeq = ((MyApp) getApplicationContext()).getCustomer().getSeq();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        recyclerAdapter = new Adapter_cart(this, new ArrayList<Order>(), cSeq);

        recyclerView = binding.recyclerviewCart;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        listCart();
    }


    private void listCart() {
        helper = new DBHelperCart(this, "cart.db", null, 1);
        orderList = helper.getCartgList();

        if (orderList.size() == 0) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.btnOrder.setVisibility(View.GONE);
        } else {
            binding.tvEmpty.setVisibility(View.INVISIBLE);
            recyclerAdapter = new Adapter_cart(this, orderList, cSeq);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    public void orderClick(View v) {
        orderList = helper.getCartList(-1, cSeq);
        price = 0;

        for (int i = 0; i < orderList.size(); i++) {
            final Order order = orderList.get(i);
            final int num = order.getNum();
            final int mPrice = order.getPrice();
            price += num * mPrice;

        }
        Intent it = new Intent(getApplicationContext(), customer_payment.class);
        it.putExtra("orderList", orderList);
        it.putExtra("price", price);
        startActivity(it);
    }

    public int getmPrice(int mSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(mSeq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                mPrice = response.body().getmPrice();
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
        return mPrice;
    }

    public void empty() {
        binding.tvEmpty.setVisibility(View.VISIBLE);
        binding.btnOrder.setVisibility(View.GONE);
    }

    public void delete(int cPosition) {
        recyclerAdapter.cartList.remove(cPosition);
        recyclerAdapter.notifyDataSetChanged();
    }

    public void backClick(View v) {
        finish();
    }

}