package com.sook.cs.letitgo.shared;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.customer.customer_main;
import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.lib.EtcLib;
import com.sook.cs.letitgo.lib.GeoLib;
import com.sook.cs.letitgo.lib.RemoteLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.sook.cs.letitgo.seller.Seller_main;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 시작 액티비티이며 이 액티비티에서 사용자 정보를 조회해서
 * 메인 액티비티를 실행할 지, 프로필 액티비티를 실행할 지를 결정한다.
 */
public class IndexActivity extends AppCompatActivity {
    int count = 0;
    Context context;
    AnimationDrawable frameAnimation;


    /**
     * 레이아웃을 설정하고 인터넷에 연결되어 있는지를 확인한다.
     * 만약 인터넷에 연결되어 있지 않다면 showNoService() 메소드를 호출한다.
     *
     * @param savedInstanceState 액티비티가 새로 생성되었을 경우, 이전 상태 값을 가지는 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        context = this;

        ImageView view = (ImageView) findViewById(R.id.imageAnimation);
        view.setBackgroundResource(R.drawable.splash_main);
        frameAnimation = (AnimationDrawable) view.getDrawable();

        if (!RemoteLib.getInstance().isConnected(context)) {
            showNoService();
            return;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            frameAnimation.start();
        } else {
            frameAnimation.stop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        count = 1;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
            insertSeller();
        else if (resultCode == 1)
            insertCustomer();
    }

    /**
     * 일정 시간(1.2초) 이후에 startTask() 메소드를 호출해서
     * 서버에서 사용자 정보를 조회한다.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count != 1)
                    startTask();
            }
        }, 2000);
    }

    /**
     * 현재 인터넷에 접속할 수 없기 때문에 서비스를 사용할 수 없다는 메시지와
     * 화면 종료 버튼을 보여준다.
     */
    private void showNoService() {
        TextView messageText = (TextView) findViewById(R.id.message);
        messageText.setVisibility(View.VISIBLE);

        Button closeButton = (Button) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        closeButton.setVisibility(View.VISIBLE);
    }

    /**
     * 현재 폰의 전화번호와 동일한 사용자 정보를 조회할 수 있도록
     * selectMemberInfo() 메소드를 호출한다.
     */
    public void startTask() {
        String phone = EtcLib.getInstance().getPhoneNumber(this);
        selectMemberInfo(phone);

        GeoLib.getInstance().setLastKnownLocation(this);
    }

    /**
     * 리트로핏을 활용해서 서버로부터 사용자 정보를 조회한다.
     * 사용자 정보를 조회했다면 setMemberInfoItem() 메소드를 호출하고
     * 그렇지 않다면 goProfileActivity() 메소드를 호출한다.
     *
     * @param phone 전화번호
     */

    public void selectMemberInfo(String phone) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Log.d("ok", "selectmemberinfo");

        Call<String> call = remoteService.selectMemberInfo(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) { //번호 있을 때
                    int seq = Integer.parseInt(response.body());

                    if (response.code() == 201) // 소비자
                        setCustomer(seq);
                    else {
                        //Log.d("ok", "store");
                        setSeller(seq);
                    }
                } else {//번호없음
                    Log.d("ok", "select unsuccessful");
                    goProfileActivity();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("ok", "failure");
            }
        });
    }

    private void setCustomer(int seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Customer> call = remoteService.selectCustomerInfo(seq);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                if (response.isSuccessful()) {
                    ((MyApp) getApplicationContext()).setCustomer(response.body());
                    Intent it = new Intent(IndexActivity.this, customer_main.class);

                    String str = getIntent().getStringExtra("particularFragment");
                    if (str != null) {
                        if (str.equals("goToCustomer_my")) {
                            it.putExtra("particularFragment", "goToCustomer_my");
                        }
                    }

                    startActivity(it);
                } else {
                    Log.d("ok", "set customer unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.d("ok", "failure");
            }
        });
    }

    private void setSeller(int seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Seller> call = remoteService.selectSellerInfo(seq);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                if (response.isSuccessful()) {
                    ((MyApp) getApplicationContext()).setSeller(response.body());
                    Log.d("ok", "seller success");

                    Intent it = new Intent(IndexActivity.this, Seller_main.class);
                    String str = getIntent().getStringExtra("particularFragment");
                    if (str != null) {
                        if (str.equals("goToSeller_order")) {
                            it.putExtra("particularFragment", "goToSeller_order");
                        }
                    }

                    startActivity(it);
                } else {
                    Log.d("ok", "set seller unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
                Log.d("ok", "failure");
            }
        });


    }


    /**
     * 번호가 없을 때!!
     * 사용자 정보를 조회하지 못했다면 insertMemberPhone() 메소드를 통해
     * 전화번호를 서버에 저장하고 MainActivity를 실행한 후 ProfileActivity를 실행한다.
     * 그리고 현재 액티비티를 종료한다.
     *
     * @param
     */
    private void goProfileActivity() {
        Intent intent = new Intent(IndexActivity.this, GroupActivity.class);
        startActivityForResult(intent, 0);
    }

    private void insertCustomer() {
        Intent it = new Intent(IndexActivity.this, ProfileActivity.class);
        startActivity(it);
        finish();
    }

    private void insertSeller() {
        Log.d("ok", "seller!!!!!!!");

        Intent it = new Intent(IndexActivity.this, Login2_1_store_info.class);
        startActivity(it);
        finish();

    }
}