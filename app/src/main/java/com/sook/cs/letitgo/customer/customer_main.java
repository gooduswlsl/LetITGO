package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.ActivityCustomerBinding;
import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_main extends AppCompatActivity {
    ActivityCustomerBinding binding;
    android.support.v4.app.Fragment fragment;
    int REQUEST_SELLER = 0, REQUEST_MENU = 1, REQUEST_PROFILE = 100;
    private final String TAG = this.getClass().getSimpleName();
    Customer current_customer;
    String compare_regId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //regId가 동일한지 체크
        current_customer = ((MyApp) getApplicationContext()).getCustomer();
        compare_regId = FirebaseInstanceId.getInstance().getToken();
        if(!compare_regId.equals(current_customer.getRegId())){
            sendNewCustomerRegId(current_customer.getSeq(), compare_regId);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer);
        binding.setActivity(this);

        //푸시 메시지를 클릭한 경우
        String str = getIntent().getStringExtra("particularFragment");
        if(str !=null)
        {
            if(str.equals("goToCustomer_my"))
            {
                callFragment(5);
            }
        }
        else
            callFragment(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu);
        return true;
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

    private void sendNewCustomerRegId(int seq, String regId) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendNewCustomerRegId(seq, regId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG,"sending new regId successful");
                }
                else{
                    Log.d(TAG,"response not successful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });
    }
}