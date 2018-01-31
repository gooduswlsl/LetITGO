package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.databinding.ItemSellerBinding;


import java.util.ArrayList;

public class Adapter_seller_liked extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    ItemSellerBinding binding;
    ArrayList<Seller> sellerArrayList;
    Context mContext;

    public Adapter_seller_liked(Context mContext, ArrayList<Seller> sellerArrayList) {
        this.sellerArrayList = sellerArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_seller, parent, false);
        return new MyViewHolder((ItemSellerBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {          // 항목을 뷰홀더에 바인딩
        Seller seller = sellerArrayList.get(position);
        holder.sbinding.setSeller(seller);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return sellerArrayList.size();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @Override
    public void onClick(View v) {

        Log.d("Tag", String.valueOf(v.getTag()));


    }
}