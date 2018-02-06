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
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.databinding.ItemSellerImgBinding;

import java.util.ArrayList;

public class Adapter_seller_list extends RecyclerView.Adapter<MyViewHolder> {
    private ItemSellerImgBinding binding;
    private ArrayList<Seller> sellerArrayList;
    private Context mContext;

    public Adapter_seller_list(Context mContext, ArrayList<Seller> sellerArrayList) {
        this.sellerArrayList = sellerArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_seller_img, parent, false);
        return new MyViewHolder((ItemSellerImgBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {          // 항목을 뷰홀더에 바인딩
        final Seller seller = sellerArrayList.get(position);
        holder.simgBinding.setSeller(seller);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tag", String.valueOf(v.getTag()));

                Intent it = new Intent(mContext, customer_dialog_store.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("store", sellerArrayList.get((Integer) v.getTag()));
                it.putExtras(bundle);
                (mContext).startActivity(it);


            }
        });
    }

    public void addSellerList(ArrayList<Seller> sellerArrayList){
        this.sellerArrayList.addAll(sellerArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sellerArrayList.size();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }


}