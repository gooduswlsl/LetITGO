package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.sook.cs.letitgo.Menu;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.Store;
import com.sook.cs.letitgo.databinding.DialogMenuBinding;
import com.sook.cs.letitgo.databinding.DialogStoreBinding;

public class customer_dialog_menu extends Activity {
    DialogMenuBinding binding;

    public customer_dialog_menu() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_menu);

        Menu menu = (Menu)getIntent().getSerializableExtra("menu");
        binding.setMenu(menu);

    }

}