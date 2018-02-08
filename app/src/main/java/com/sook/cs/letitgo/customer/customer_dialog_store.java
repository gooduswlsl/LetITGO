package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    int seller_seq;

    MyDBHelpers helper;
    SQLiteDatabase db;

    public customer_dialog_store() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_seller);
        seller_seq = getIntent().getIntExtra("seller_seq", 0);
        if (seller_seq != 0)
            selectSellerList(seller_seq);

        helper = new MyDBHelpers(this, "liked.db", null, 1);
        db = helper.getWritableDatabase();

        if (helper.isLiked(seller_seq))
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

    public void starClick(View v) {
        if (helper.isLiked(seller_seq)) {
            binding.imgStar.setImageResource(R.drawable.star_empty);
            //helper.deleteStore(db, seller_seq);
            helper.deleteStore(seller_seq);
            Log.d("star", String.valueOf(seller_seq));
            Log.d("star", "isliked");
        } else {
            binding.imgStar.setImageResource(R.drawable.star);
            helper.insertStore(seller_seq);
            Log.d("star", "islikedfail");
        }
    }
//
//    private boolean isLiked(int sSeq) {
//        String sql = "select * from likedSeller where sSeq = "+sSeq;
//        Cursor cursor = db.rawQuery(sql, null);
//        cursor.close();
//        if (cursor != null)
//            return true;
//        else
//            return false;
//    }

}