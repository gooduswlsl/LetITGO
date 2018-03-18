package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemCartMenuBinding;
import com.sook.cs.letitgo.databinding.ItemOrderlistDetailBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_orderList_detail extends RecyclerView.Adapter<MyViewHolder> {
    public ViewDataBinding binding;
    private ArrayList<Order> orderArrayList;
    private Menu menu;
    private Context mContext;

    public Adapter_orderList_detail(Context mContext, ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_orderlist_detail, parent, false);
        return new MyViewHolder((ItemOrderlistDetailBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = orderArrayList.get(position);
        holder.oldBinding.setOrder(order);
        setMenu(holder, order.getMenu_seq());
    }

    public void setMenu(final MyViewHolder holder, int mSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(mSeq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                menu = response.body();
                holder.oldBinding.setMenu(menu);
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }

}