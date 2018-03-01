package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.DialogMenuBinding;

public class customer_dialog_cart extends Activity {
    DialogMenuBinding binding;

    public customer_dialog_cart() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_cart);
    }

    public void clickCart(View v){
        Intent intent = new Intent(getApplicationContext(), customer_cart.class);
        startActivity(intent);
        finish();
    }

    public void clickShop(View v){
        finish();
    }

}