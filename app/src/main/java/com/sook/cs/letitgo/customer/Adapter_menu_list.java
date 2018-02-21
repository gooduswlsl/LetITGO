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

import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ItemMenuBinding;
import com.sook.cs.letitgo.remote.RemoteService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_menu_list extends RecyclerView.Adapter<MyViewHolder> {
    ViewDataBinding binding;
    ArrayList<Menu> menuArrayList;
    Context mContext;
    int REQUEST_MENU = 1;

    public Adapter_menu_list(Context mContext, ArrayList<Menu> menuArrayList) {
        this.menuArrayList = menuArrayList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_menu, parent, false);
        return new MyViewHolder((ItemMenuBinding) binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {          // 항목을 뷰홀더에 바인딩
        final Menu menu = menuArrayList.get(position);
        holder.mbinding.setMenu(menu);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, customer_dialog_menu.class);
                it.putExtra("menu", menu);
                it.putExtra("position", position);
                ((Activity) (mContext)).startActivityForResult(it, REQUEST_MENU);
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