package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemCartBinding;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_cart extends RecyclerView.Adapter<MyViewHolder> {
    private ViewDataBinding binding;
    public ArrayList<Order> cartList;
    private Seller seller;
    private int cSeq;
    private Context mContext;
    private DBHelperCart cartHelper;
    public Adapter_cart_menu adapterCartMenu;

    public Adapter_cart(Context mContext, ArrayList<Order> cartList, int cSeq) {
        this.cartList = cartList;
        this.mContext = mContext;
        this.cSeq = cSeq;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cart, parent, false);
        cartHelper = new DBHelperCart(mContext, "cart.db", null, 1);
        return new MyViewHolder((ItemCartBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = cartList.get(position);
        int sSeq = order.getSeller_seq();
        RecyclerView recyclerView = holder.cBinding.recyclerview;
        adapterCartMenu = new Adapter_cart_menu(mContext, cartHelper.getCartList(sSeq, cSeq), position);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapterCartMenu);

        holder.cBinding.setOrder(order);
        setSeller(holder, sSeq);
        setOnClick(holder);
    }

    private void setOnClick(final MyViewHolder holder) {

        View.OnClickListener showView = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cBinding.layout.setVisibility(View.VISIBLE);
                holder.cBinding.tvDown.setVisibility(View.GONE);
            }
        };

        View.OnClickListener hideView = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.cBinding.layout.setVisibility(View.GONE);
                holder.cBinding.tvDown.setVisibility(View.VISIBLE);
            }
        };

        View.OnKeyListener msgEdit = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int sSeq = holder.cBinding.getSeller().getSeq();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    cartHelper.updateMsg(sSeq, holder.cBinding.editMsg.getText().toString());
                    return true;
                }
                return false;
            }
        };

        View.OnClickListener timeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sSeq = holder.cBinding.getSeller().getSeq();
                customer_dialog_time timePicker = new customer_dialog_time();
                timePicker.setValues(holder, cartHelper, sSeq);
                timePicker.show(((Activity) mContext).getFragmentManager(), "time");
            }
        };

        holder.cBinding.tvDown.setOnClickListener(showView);
        holder.cBinding.tvUp.setOnClickListener(hideView);
        holder.cBinding.editMsg.setOnKeyListener(msgEdit);
        holder.cBinding.btnTime.setOnClickListener(timeClick);
    }

    public void setSeller(final MyViewHolder holder, int sSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Seller> call = remoteService.selectSeller(sSeq);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                seller = response.body();
                holder.cBinding.setSeller(seller);
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

}