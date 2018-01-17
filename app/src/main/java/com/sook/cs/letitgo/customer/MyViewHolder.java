package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;

import com.sook.cs.letitgo.databinding.ItemStoreBinding;
import com.sook.cs.letitgo.databinding.ItemStoreImgBinding;

public class MyViewHolder extends RecyclerView.ViewHolder{
    ItemStoreBinding binding;
    ItemStoreImgBinding imgBinding;


    public MyViewHolder(ItemStoreBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        binding = DataBindingUtil.bind(itemView);
        binding.executePendingBindings();
    }

    public MyViewHolder(ItemStoreImgBinding imgBinding) {
        super(imgBinding.getRoot());
        this.imgBinding = imgBinding;
        imgBinding = DataBindingUtil.bind(itemView);
        imgBinding.executePendingBindings();
    }

}