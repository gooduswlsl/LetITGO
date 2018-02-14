package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.databinding.DialogSellerBinding;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class customer_dialog_store extends Activity {
    DialogSellerBinding binding;
    int seller_seq, position;

    MyDBHelpers helper;

    public customer_dialog_store() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_seller);
        seller_seq = getIntent().getIntExtra("seller_seq", 0);
        position = getIntent().getIntExtra("position", 0);
        if (seller_seq != 0)
            selectSellerList(seller_seq);

        setStar();
    }

    private void setStar() {
        helper = new MyDBHelpers(this, "liked.db", null, 1);

        if (helper.isLikedStore(seller_seq))
            binding.imgStar.setImageResource(R.drawable.star);
        else
            binding.imgStar.setImageResource(R.drawable.star_empty);
    }

    private void selectSellerList(int seller_seq) {
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
                Log.d("sellerdialog", t.toString());
            }
        });
    }

    public void clickStar(View v) {
        Intent it = new Intent();
        it.putExtra("position", position);
        it.putExtra("seller_seq", seller_seq);
        if (helper.isLikedStore(seller_seq)) {
            binding.imgStar.setImageResource(R.drawable.star_empty);
            helper.deleteStore(seller_seq);
            setResult(RESULT_OK, it);
        } else {
            binding.imgStar.setImageResource(R.drawable.star);
            helper.insertStore(seller_seq);
            setResult(RESULT_CANCELED, it);
        }
    }

    public void clickMore(View view) {
        Intent it = new Intent(customer_dialog_store.this, customer_store_detail.class);
        it.putExtra("seller_seq", seller_seq);
        startActivity(it);
    }

}