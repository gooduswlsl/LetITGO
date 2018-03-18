package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemOrderlistBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_orderList extends RecyclerView.Adapter<MyViewHolder> {
    private ViewDataBinding binding;
    public ArrayList<Order> orderArrayList;
    private Menu menu;
    private Seller seller;
    private Context mContext;
    private int REQUEST_ORDER = 9;

    public Adapter_orderList(Context mContext, ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_orderlist, parent, false);
        return new MyViewHolder((ItemOrderlistBinding) binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {          // 항목을 뷰홀더에 바인딩
        final Order order = orderArrayList.get(position);
        holder.olBinding.setOrder(order);
        if (order.getCount() == 1) {
            holder.olBinding.text.setVisibility(View.INVISIBLE);
            holder.olBinding.tvNums.setVisibility(View.INVISIBLE);
        } else {
            holder.olBinding.tvNums.setText(String.valueOf(order.getCount() - 1));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, customer_dialog_order.class);
                it.putExtra("order", order);
                it.putExtra("position", position);
                ((Activity) (mContext)).startActivityForResult(it, 9);
                //mContext.startActivity(it);
            }
        });

        setMenu(holder, order.getMenu_seq());
        setSeller(holder, order.getSeller_seq());
        switch (order.getPermit()) {
            case -1:
                holder.olBinding.tvStatus.setText("주문거절");
                holder.olBinding.tvStatus.setTextColor(Color.parseColor("#FFA7A7"));
                break;
            case 0:
                holder.olBinding.tvStatus.setText("수락대기");
                holder.olBinding.tvStatus.setTextColor(Color.parseColor("#BDBDBD"));
                break;
            case 1:
                holder.olBinding.tvStatus.setText("주문수락");
                holder.olBinding.tvStatus.setTextColor(Color.parseColor("#89cb8c"));
                break;
            case 2:
                holder.olBinding.tvStatus.setText("메뉴준비중");
                holder.olBinding.tvStatus.setTextColor(Color.parseColor("#89cb8c"));
                break;
            case 3:
                holder.olBinding.tvStatus.setText("메뉴준비완료");
                holder.olBinding.tvStatus.setTextColor(Color.parseColor("#49a54e"));
                break;
        }
    }

    public void setMenu(final MyViewHolder holder, int mSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(mSeq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                menu = response.body();
                holder.olBinding.setMenu(menu);
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }

    public void setSeller(final MyViewHolder holder, int sSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Seller> call = remoteService.selectSeller(sSeq);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                seller = response.body();
                holder.olBinding.setSeller(seller);
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }


}