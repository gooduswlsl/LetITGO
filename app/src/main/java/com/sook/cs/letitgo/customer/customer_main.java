package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ActivityCustomerBinding;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_main extends AppCompatActivity {
    ActivityCustomerBinding binding;
    android.support.v4.app.Fragment fragment;
    TextView title;
    int REQUEST_SELLER = 0, REQUEST_MENU = 1, REQUEST_PROFILE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer);
        binding.setActivity(this);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.actionbar_cart);
        title = ab.getCustomView().findViewById(R.id.ab_title);

        callFragment(1);
        setImages(1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELLER || requestCode == REQUEST_MENU) {
            fragment = getSupportFragmentManager().findFragmentByTag("fragment_liked");
            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == REQUEST_PROFILE) {
            fragment = getSupportFragmentManager().findFragmentByTag("fragment_my");
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void cartClick(View v) {
        Intent intent = new Intent(this, customer_cart.class);
        startActivity(intent);
    }

    public void menuClick(View v) {
        int num = Integer.parseInt(v.getTag().toString());
        callFragment(num);
        setImages(num);
    }

    private void callFragment(int fragment_no) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (fragment_no) {
            case 1:
                customer_store fragment1 = new customer_store();
                transaction.replace(R.id.fragment_container, fragment1);
                transaction.commit();
                break;

            case 2:
                customer_menu fragment2 = new customer_menu();
                transaction.replace(R.id.fragment_container, fragment2);
                transaction.commit();
                break;

            case 3:
                customer_maps fragment3 = new customer_maps();
                transaction.replace(R.id.fragment_container, fragment3);
                transaction.commit();
                break;

            case 4:
                customer_liked fragment4 = new customer_liked();
                transaction.replace(R.id.fragment_container, fragment4, "fragment_liked");
                transaction.commit();
                break;

            case 5:
                customer_my fragment5 = new customer_my();
                transaction.replace(R.id.fragment_container, fragment5, "fragment_my");
                transaction.commit();
                break;

        }
    }

    private void setImages(int fragment_no) {
        binding.btnStore.setImageResource(R.drawable.ic_seller);
        binding.btnMenu.setImageResource(R.drawable.ic_menu);
        binding.btnMap.setImageResource(R.drawable.ic_map);
        binding.btnLiked.setImageResource(R.drawable.ic_liked);
        binding.btnMy.setImageResource(R.drawable.ic_my);
        switch (fragment_no) {
            case 1:
                binding.btnStore.setImageResource(R.drawable.ic_seller2);
                title.setText("매장 검색");
                break;
            case 2:
                binding.btnMenu.setImageResource(R.drawable.ic_menu2);
                title.setText("메뉴 검색");
                break;
            case 3:
                binding.btnMap.setImageResource(R.drawable.ic_map2);
                title.setText("지도");
                break;
            case 4:
                binding.btnLiked.setImageResource(R.drawable.ic_liked2);
                title.setText("즐겨찾기");
                break;
            case 5:
                binding.btnMy.setImageResource(R.drawable.ic_my2);
                title.setText("마이페이지");
                break;
        }
    }
}