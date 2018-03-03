package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemCartBinding;
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

public class Adapter_cart extends RecyclerView.Adapter<MyViewHolder> {
    private ViewDataBinding binding;
    private ArrayList<Order> cartArrayList;
    private Menu menu;
    private Seller seller;
    private Context mContext;
    private DBHelperCart cartHelper;

    public Adapter_cart(Context mContext, ArrayList<Order> cartArrayList) {
        this.cartArrayList = cartArrayList;
        this.mContext = mContext;
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
        Order order = cartArrayList.get(position);
        holder.cBinding.setOrder(order);
        int mSeq = order.getMenu_seq();
        setMenu(holder, mSeq);
        setSeller(holder, mSeq);
        setOnClick(holder, position);
    }

    private void setOnClick(final MyViewHolder holder, final int position) {

        View.OnClickListener numClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mSeq = holder.cBinding.getMenu().getmSeq();
                int num = Integer.parseInt(holder.cBinding.tvNum.getText().toString());
                if (v.getId() == R.id.btn_plus)
                    num++;
                else if (num > 1)
                    num--;
                holder.cBinding.tvNum.setText(String.valueOf(num));
                holder.cBinding.tvPrice.setText(String.valueOf(holder.cBinding.getMenu().getmPrice() * num));
                cartHelper.updateNum(mSeq, num);
            }
        };

        View.OnClickListener cancelClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartHelper.deleteCart(holder.cBinding.getMenu().getmSeq());
                cartArrayList.remove(position);
                notifyDataSetChanged();
                if(cartArrayList.size()==0){
                    ((customer_cart)mContext).empty();
                }
            }
        };

        View.OnKeyListener msgEdit = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int mSeq = holder.cBinding.getMenu().getmSeq();
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    cartHelper.updateMsg(mSeq, holder.cBinding.editMsg.getText().toString());
                    return true;
                }
                return false;
            }
        };

        View.OnClickListener timeClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mSeq = holder.cBinding.getMenu().getmSeq();
                customer_dialog_time timePicker = new customer_dialog_time();
                timePicker.setValues(holder, cartHelper, mSeq);
                timePicker.show(((Activity) mContext).getFragmentManager(), "time");
            }
        };

        holder.cBinding.editMsg.setOnKeyListener(msgEdit);
        holder.cBinding.btnPlus.setOnClickListener(numClick);
        holder.cBinding.btnMinus.setOnClickListener(numClick);
        holder.cBinding.btnCancel.setOnClickListener(cancelClick);
        holder.cBinding.btnTime.setOnClickListener(timeClick);
    }

    public void setMenu(final MyViewHolder holder, int mSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(mSeq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                menu = response.body();
                holder.cBinding.setMenu(menu);
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
                holder.cBinding.setSeller(seller);
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
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