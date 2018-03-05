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
    int menu_seq, position, num;
    Menu menu;
    DBHelperLiked helper;
    DBHelperCart helperCart;

    public customer_dialog_menu() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_menu);
        menu = (Menu) getIntent().getSerializableExtra("menu");
        binding.setMenu(menu);

        position = getIntent().getIntExtra("position", 0);
        menu_seq = menu.getmSeq();

        setSeller(menu.getSeller_seq());
        setStar();
    }


    private void setStar() {
        helper = new DBHelperLiked(this, "liked.db", null, 1);

        if (helper.isLikedMenu(menu_seq))
            binding.imgStar.setImageResource(R.drawable.star);
        else
            binding.imgStar.setImageResource(R.drawable.star_empty);
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
        //  it.putExtra("menu_seq", menu_seq);
        if (helper.isLikedMenu(menu_seq)) {
            Log.d("likeddialog", "click");
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

    public void clickOK(View v) {
        num = Integer.parseInt(binding.tvNum.getText().toString());
        helperCart = new DBHelperCart(this, "cart.db", null, 1);
        if (helperCart.isInCart(menu_seq)) {
            helperCart.updateCart(menu_seq, num);
        } else {
            helperCart.insertCart(menu_seq, menu.getSeller_seq(), num, menu.getmPrice());
            Log.d("cart", String.valueOf(menu.getSeller_seq()));
        }

        Intent intent = new Intent(getApplicationContext(), customer_dialog_cart.class);
        intent.putExtra("num", num);
        intent.putExtra("menu", menu);
        startActivity(intent);
        finish();
    }
}