package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemOrderBinding;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Adapter_order extends RecyclerView.Adapter<MyViewHolder> {
    private ViewDataBinding binding;
    private ArrayList<Order> orderArrayList;
    private Menu menu;
    private Seller seller;
    private Context mContext;

    public Adapter_order(Context mContext, ArrayList<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_order, parent, false);
        return new MyViewHolder((ItemOrderBinding) binding);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {          // 항목을 뷰홀더에 바인딩
        final Order order = orderArrayList.get(position);
        holder.oBinding.setOrder(order);
        setMenu(holder, order.getMenu_seq());
        setSeller(holder, order.getSeller_seq());

        holder.oBinding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = holder.oBinding.layoutMore;
                ImageButton btnMore = holder.oBinding.btnMore;
                if (btnMore.getTag().toString().equals("0")) {
                    layout.setVisibility(View.VISIBLE);
                    btnMore.setImageResource(R.drawable.btn_up);
                    btnMore.setTag("1");
                } else {
                    layout.setVisibility(View.GONE);
                    btnMore.setImageResource(R.drawable.btn_down);
                    btnMore.setTag("0");
                }
            }
        });
    }

    public void addOrderList(ArrayList<Order> orderArrayList) {
        this.orderArrayList.addAll(orderArrayList);
        notifyDataSetChanged();
    }

    public void setMenu(final MyViewHolder holder, int mSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(mSeq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                menu = response.body();
                holder.oBinding.setMenu(menu);
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
                holder.oBinding.setSeller(seller);
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


    @BindingAdapter({"bind:menuImg"})
    public static void loadImg(ImageView imageView, String fileName) {
        Picasso.with(imageView.getContext()).load(RemoteService.MENU_IMG_URL + fileName).into(imageView);
    }

}