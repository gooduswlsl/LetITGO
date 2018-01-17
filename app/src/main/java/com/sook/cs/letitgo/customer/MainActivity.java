package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sook.cs.letitgo.databinding.ActivityMainCustomerBinding;


public class MainActivity extends AppCompatActivity{
    ActivityMainCustomerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_customer);
//        binding.setActivity(this);
    }

    public void customerClick(View v){
        Intent it = new Intent(this, customer_main.class);
        startActivity(it);
       // Log.d("dd","dd");
    }

}