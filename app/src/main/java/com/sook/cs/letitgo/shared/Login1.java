package com.sook.cs.letitgo.shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sook.cs.letitgo.R;


public class Login1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void onButton1Clicked(View v) {
        Intent intent = new Intent(getApplicationContext(), Login2.class);
        intent.putExtra("kind","seller");
        startActivity(intent);
    }
    public void onButton2Clicked(View v){
        Intent intent = new Intent(getApplicationContext(),Login2.class);
        intent.putExtra("kind","customer");
        startActivity(intent);
    }



}
