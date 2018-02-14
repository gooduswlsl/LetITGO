package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.DialogMenuBinding;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_dialog_menu extends Activity {
    DialogMenuBinding binding;
    int menu_seq, position;
    int num;
    MyDBHelpers helper;

    public customer_dialog_menu() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_menu);
        menu_seq = getIntent().getIntExtra("menu_seq", 0);
        position = getIntent().getIntExtra("position", 0);
        if (menu_seq != 0)
            selectMenuList(menu_seq);

        setStar();
    }


    private void setStar() {
        helper = new MyDBHelpers(this, "liked.db", null, 1);

        if (helper.isLikedMenu(menu_seq))
            binding.imgStar.setImageResource(R.drawable.star);
        else
            binding.imgStar.setImageResource(R.drawable.star_empty);
    }

    private void selectMenuList(int menu_seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenu(menu_seq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                Menu menu = response.body();
                binding.setMenu(menu);
                setSeller(menu.seller_seq);
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });


    }

    private void setSeller(int seller_seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Seller> call = remoteService.selectSeller(seller_seq);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                Seller seller = response.body();
                binding.setSeller(seller);
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
            }
        });
    }

    public void clickStar(View v) {
        Intent it = new Intent();
        it.putExtra("position", position);
        it.putExtra("menu_seq", menu_seq);
        if (helper.isLikedMenu(menu_seq)) {
            binding.imgStar.setImageResource(R.drawable.star_empty);
            helper.deleteMenu(menu_seq);
            setResult(RESULT_OK, it);
        } else {
            binding.imgStar.setImageResource(R.drawable.star);
            helper.insertMenu(menu_seq);
            setResult(RESULT_CANCELED, it);
        }
    }

    public void clickMinus(View v) {
        num = Integer.parseInt(binding.tvNum.getText().toString());
        if (num != 0) {
            binding.tvNum.setText(String.valueOf(--num));
        }
    }

    public void clickPlus(View v) {
        num = Integer.parseInt(binding.tvNum.getText().toString());
        binding.tvNum.setText(String.valueOf(++num));
    }

    public void clickX(View v) {
        finish();
    }

}