package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemMenuImgBinding;
import com.sook.cs.letitgo.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_menu_img extends RecyclerView.Adapter<MyViewHolder> {
    private ViewDataBinding binding;
    private ArrayList<Menu> menuArrayList;
    private Context mContext;

    public Adapter_menu_img(Context mContext, ArrayList<Menu> menuArrayList) {
        this.menuArrayList = menuArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_menu_img, parent, false);
        return new MyViewHolder((ItemMenuImgBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {          // 항목을 뷰홀더에 바인딩
        final Menu menu = menuArrayList.get(position);
        holder.mimgBinding.setMenu(menu);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, customer_dialog_menu.class);
                it.putExtra("menu", menu);
                (mContext).startActivity(it);
            }
        });
    }

    public void addMenuList(ArrayList<Menu> menuArrayList) {
        this.menuArrayList.addAll(menuArrayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return menuArrayList.size();
    }


    @BindingAdapter({"bind:menuImg"})
    public static void loadImg(ImageView imageView, String fileName) {
        Picasso.with(imageView.getContext()).load(RemoteService.MENU_IMG_URL + fileName).into(imageView);
    }
}