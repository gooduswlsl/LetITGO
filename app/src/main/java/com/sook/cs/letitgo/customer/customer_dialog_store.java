package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.databinding.DialogSellerBinding;

public class customer_dialog_store extends Activity {
    DialogSellerBinding binding;

    public customer_dialog_store() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_seller);

        Seller seller = (Seller) getIntent().getSerializableExtra("seller");
        binding.setSeller(seller);

    }

}