package com.sook.cs.letitgo.seller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.customer.customer_main;
import com.sook.cs.letitgo.shared.Login1;

import retrofit2.Call;

public class Login extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Seller_main.class);
                startActivity(intent);
            }
        });

        Button facebook_login = (Button)findViewById(R.id.facebook_login);
        facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login1.class);
                startActivity(intent);
            }
        });

        Button btn_customer = (Button)findViewById(R.id.btn_customer);
        btn_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), customer_main.class);
                startActivity(intent);
            }
        });




        Button naver_login = (Button)findViewById(R.id.naver_login);
        naver_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login1.class);
                startActivity(intent);
            }
        });

        TextView signup=(TextView)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login1.class);
                startActivity(intent);
            }
        });

        TextView findid=(TextView)findViewById(R.id.findid);
        findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login1.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}