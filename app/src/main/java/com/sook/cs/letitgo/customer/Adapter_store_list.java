package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.Store;
import com.sook.cs.letitgo.databinding.ItemStoreImgBinding;

import java.util.ArrayList;

public class Adapter_store_list extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {
    ItemStoreImgBinding binding;
    ArrayList<Store> storeArrayList;
    Context mContext;

    public Adapter_store_list(Context mContext, ArrayList<Store> storeArrayList) {
        this.storeArrayList = storeArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_store_img, parent, false);
        return new MyViewHolder((ItemStoreImgBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {          // 항목을 뷰홀더에 바인딩
        Store store = storeArrayList.get(position);
        holder.simgBinding.setStore(store);
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

        Intent it = new Intent(mContext, customer_dialog_store.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("store", storeArrayList.get((Integer) v.getTag()));
        it.putExtras(bundle);
        (mContext).startActivity(it);


    }
}