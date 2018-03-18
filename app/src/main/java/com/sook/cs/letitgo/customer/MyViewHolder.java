package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;

import com.sook.cs.letitgo.databinding.ItemCartBinding;
import com.sook.cs.letitgo.databinding.ItemCartMenuBinding;
import com.sook.cs.letitgo.databinding.ItemMenuBinding;
import com.sook.cs.letitgo.databinding.ItemMenuImgBinding;
import com.sook.cs.letitgo.databinding.ItemOrderlistBinding;
import com.sook.cs.letitgo.databinding.ItemOrderlistDetailBinding;
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
    ItemOrderlistBinding olBinding;
    ItemOrderlistDetailBinding oldBinding;
    ItemCartBinding cBinding;
    ItemCartMenuBinding cmBinding;

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

    public MyViewHolder(ItemOrderlistBinding olBinding) {
        super(olBinding.getRoot());
        this.olBinding = olBinding;
        olBinding = DataBindingUtil.bind(itemView);
        olBinding.executePendingBindings();
    }

    public MyViewHolder(ItemOrderlistDetailBinding oldBinding) {
        super(oldBinding.getRoot());
        this.oldBinding = oldBinding;
        oldBinding = DataBindingUtil.bind(itemView);
        oldBinding.executePendingBindings();
    }

    public MyViewHolder(ItemCartBinding cBinding) {
        super(cBinding.getRoot());
        this.cBinding = cBinding;
        cBinding = DataBindingUtil.bind(itemView);
        cBinding.executePendingBindings();
    }

    public MyViewHolder(ItemCartMenuBinding cmBinding) {
        super(cmBinding.getRoot());
        this.cmBinding = cmBinding;
        cmBinding = DataBindingUtil.bind(itemView);
        cmBinding.executePendingBindings();
    }


}