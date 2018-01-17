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
import com.sook.cs.letitgo.Store;
import com.sook.cs.letitgo.databinding.ItemStoreBinding;


import java.util.ArrayList;

public class Adapter_store_liked extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    ItemStoreBinding binding;
    ArrayList<Store> storeArrayList;
    Context mContext;

    public Adapter_store_liked(Context mContext, ArrayList<Store> storeArrayList) {
        this.storeArrayList = storeArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_store, parent, false);
        return new MyViewHolder((ItemStoreBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {          // 항목을 뷰홀더에 바인딩
        Store store = storeArrayList.get(position);
        holder.sbinding.setStore(store);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return storeArrayList.size();
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