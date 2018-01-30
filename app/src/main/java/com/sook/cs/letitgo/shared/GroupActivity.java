package com.sook.cs.letitgo.shared;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sook.cs.letitgo.R;


public class GroupActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    public void buttonClick(View v){
       Intent it = new Intent();
       setResult(Integer.parseInt(v.getTag().toString()), it);
       finish();
    }


}
