package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;

import com.sook.cs.letitgo.databinding.ItemMenuBinding;
import com.sook.cs.letitgo.databinding.ItemMenuImgBinding;
import com.sook.cs.letitgo.databinding.ItemSellerBinding;
import com.sook.cs.letitgo.databinding.ItemSellerImgBinding;
import com.sook.cs.letitgo.databinding.ItemSellerMapBinding;
import com.sook.cs.letitgo.databinding.ItemOrderBinding;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ItemSellerBinding sbinding;
    ItemSellerImgBinding simgBinding;
    ItemSellerMapBinding sMapBinding;
    ItemMenuBinding mbinding;
    ItemMenuImgBinding mimgBinding;
    ItemOrderBinding oBinding;

    public MyViewHolder(ItemSellerBinding sbinding) {
        super(sbinding.getRoot());
        this.sbinding = sbinding;
        sbinding = DataBindingUtil.bind(itemView);
        sbinding.executePendingBindings();
    }

    public MyViewHolder(ItemSellerImgBinding simgBinding) {
        super(simgBinding.getRoot());
        this.simgBinding = simgBinding;
        simgBinding = DataBindingUtil.bind(itemView);
        simgBinding.executePendingBindings();
    }

    public MyViewHolder(ItemSellerMapBinding sMapBinding) {
        super(sMapBinding.getRoot());
        this.sMapBinding = sMapBinding;
        sMapBinding = DataBindingUtil.bind(itemView);
        sMapBinding.executePendingBindings();
    }

    public MyViewHolder(ItemMenuBinding mbinding) {
        super(mbinding.getRoot());
        this.mbinding = mbinding;
        mbinding = DataBindingUtil.bind(itemView);
        mbinding.executePendingBindings();
    }

    public MyViewHolder(ItemMenuImgBinding mimgBinding) {
        super(mimgBinding.getRoot());
        this.mimgBinding = mimgBinding;
        mimgBinding = DataBindingUtil.bind(itemView);
        mimgBinding.executePendingBindings();
    }

    public MyViewHolder(ItemOrderBinding oBinding) {
        super(oBinding.getRoot());
        this.oBinding = oBinding;
        oBinding = DataBindingUtil.bind(itemView);
        oBinding.executePendingBindings();
    }
}