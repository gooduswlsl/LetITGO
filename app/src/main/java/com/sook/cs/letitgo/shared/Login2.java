package com.sook.cs.letitgo.shared;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.sook.cs.letitgo.R;


public class Login2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final EditText editText = (EditText) findViewById(R.id.editText);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final EditText editText3 = (EditText) findViewById(R.id.editText3);
        final String kind;

        Intent intent = getIntent();
        kind = intent.getStringExtra("kind");

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = editText2.getText().toString();
                String phonenum = editText.getText().toString();
                String passwd = editText3.getText().toString();

                if (id.equals("")||passwd.equals("")||phonenum.equals("")) {
                    if(id.equals("")){
                        Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    }

                    if (passwd.equals("")) {
                        Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    }

                    if(phonenum.equals("")){
                        Toast.makeText(getApplicationContext(),"전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if(kind.equals("seller")){
                        Intent intent = new Intent(getApplicationContext(),Login2_1_store_info.class);
                        startActivity(intent);
                    }else if (kind.equals("customer")){
                        Intent intent = new Intent(getApplicationContext(),Login_finish.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }


}
