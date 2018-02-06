package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
    int menu_seq;

    public customer_dialog_menu() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_menu);
        menu_seq = getIntent().getIntExtra("menu_seq", 0);
        if (menu_seq != 0)
            selectMenuList(menu_seq);

    }

    private void selectMenuList(int menu_seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Menu> call = remoteService.selectMenuList(menu_seq);
        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                Menu menu = response.body();
                binding.setMenu(menu);
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
            }
        });
    }

}