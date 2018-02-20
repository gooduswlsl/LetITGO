package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemSellerBinding;
import com.sook.cs.letitgo.databinding.ItemSellerMapBinding;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_seller_map extends RecyclerView.Adapter<MyViewHolder> {
    ViewDataBinding binding;
    ArrayList<Seller> sellerArrayList;
    Context mContext;
    int REQUEST_SELLER = 0;

    public Adapter_seller_map(Context mContext, ArrayList<Seller> sellerArrayList) {
        this.sellerArrayList = sellerArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_seller_map, parent, false);
        return new MyViewHolder((ItemSellerMapBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {          // 항목을 뷰홀더에 바인딩
        final Seller seller = sellerArrayList.get(position);
        holder.sMapBinding.setSeller(seller);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, customer_dialog_store.class);
                it.putExtra("seller_seq", seller.seq);
                it.putExtra("position", position);
                ((Activity) (mContext)).startActivityForResult(it, REQUEST_SELLER);
            }
        });
    }

    public void addSellerList(ArrayList<Seller> sellerArrayList) {
        this.sellerArrayList.addAll(sellerArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sellerArrayList.size();
    }


    @BindingAdapter({"bind:sellerImg"})
    public static void loadImg(ImageView imageView, String fileName) {
        Picasso.with(imageView.getContext()).load(RemoteService.SELLER_IMG_URL + fileName).into(imageView);
    }

}