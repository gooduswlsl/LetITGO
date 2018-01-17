package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ActivityCustomerBinding;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_main extends AppCompatActivity {
    ActivityCustomerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer);
        binding.setActivity(this);
        callFragment(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "CART", Toast.LENGTH_SHORT).show();
        return true;
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
                transaction.replace(R.id.fragment_container, fragment4);
                transaction.commit();
                break;

            case 5:
                customer_my fragment5 = new customer_my();
                transaction.replace(R.id.fragment_container, fragment5);
                transaction.commit();
                break;

        }
    }

    private void setImages(int fragment_no) {
        ActionBar ab = getSupportActionBar();
        binding.btnStore.setText("매장검색");
        binding.btnMenu.setText("메뉴검색");
        binding.btnMap.setText("지도");
        binding.btnLiked.setText("즐겨찾기");
        binding.btnMy.setText("마이");
        switch (fragment_no) {
            case 1:
                binding.btnStore.setText("이것");
                ab.setTitle("매장 검색");
                break;
            case 2:
                binding.btnMenu.setText("이것");
                ab.setTitle("메뉴 검색");
                break;
            case 3:
                binding.btnMap.setText("이것");
                ab.setTitle("지도");
                break;
            case 4:
                binding.btnLiked.setText("이것");
                ab.setTitle("즐겨찾기");
                break;
            case 5:
                binding.btnMy.setText("이것");
                ab.setTitle("마이페이지");
                break;
        }
    }
}