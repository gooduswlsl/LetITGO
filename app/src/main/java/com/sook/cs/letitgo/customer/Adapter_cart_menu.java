package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemCartBinding;
import com.sook.cs.letitgo.databinding.ItemCartMenuBinding;
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

public class Adapter_cart_menu extends RecyclerView.Adapter<MyViewHolder> {
    public ViewDataBinding binding;
    private ArrayList<Order> cartArrayList;
    private Menu menu;
    private Seller seller;
    private int cPosition;
    private Context mContext;
    private DBHelperCart cartHelper;

    public Adapter_cart_menu(Context mContext, ArrayList<Order> cartArrayList, int cPosition) {
        this.cartArrayList = cartArrayList;
        this.mContext = mContext;
        this.cPosition = cPosition;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cart_menu, parent, false);
        cartHelper = new DBHelperCart(mContext, "cart.db", null, 1);
        return new MyViewHolder((ItemCartMenuBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = cartArrayList.get(position);
        holder.cmBinding.setOrder(order);
        setMenu(holder, order.getMenu_seq());
        setOnClick(holder, position);
    }

    private void setOnClick(final MyViewHolder holder, final int position) {

        View.OnClickListener numClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mSeq = holder.cmBinding.getMenu().getmSeq();
                int num = Integer.parseInt(holder.cmBinding.tvNum.getText().toString());
                if (v.getId() == R.id.btn_plus)
                    num++;
                else if (num > 1)
                    num--;
                holder.cmBinding.tvNum.setText(String.valueOf(num));
                holder.cmBinding.tvPrice.setText(String.valueOf(holder.cmBinding.getMenu().getmPrice() * num));
                cartHelper.updateNum(mSeq, num);
            }
        };

        View.OnClickListener cancelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartHelper.deleteCart(holder.cmBinding.getMenu().getmSeq());
                cartArrayList.remove(position);
                notifyDataSetChanged();
                if (cartArrayList.size() == 0) {
                    ((customer_cart)mContext).delete(cPosition);
                    if(cartHelper.getCartgList().size()==0)
                        ((customer_cart)mContext).empty();
                }
            }
        };

        holder.cmBinding.btnPlus.setOnClickListener(numClick);
        holder.cmBinding.btnMinus.setOnClickListener(numClick);
        holder.cmBinding.btnCancel.setOnClickListener(cancelClick);
    }

    public void setMenu(final MyViewHolder holder, int mSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(mSeq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                menu = response.body();
                holder.cmBinding.setMenu(menu);
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    @BindingAdapter({"bind:menuImg"})
    public static void loadImg(ImageView imageView, String fileName) {
        Picasso.with(imageView.getContext()).load(RemoteService.MENU_IMG_URL + fileName).into(imageView);
    }
}