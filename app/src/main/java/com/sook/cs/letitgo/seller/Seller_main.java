package com.sook.cs.letitgo.seller;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Seller_main extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    Seller current_seller;
    String compare_regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //toolbar 새로만들기
        setSupportActionBar(toolbar);

        //regId가 동일한지 체크
        current_seller = ((MyApp) getApplicationContext()).getSeller();
        compare_regId = FirebaseInstanceId.getInstance().getToken();
        if(!compare_regId.equals(current_seller.getRegId())){
            sendNewSellerRegId(current_seller.getSeq(), compare_regId);
        }

        ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.seller_actionbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("매장정보"));
        tabLayout.addTab(tabLayout.newTab().setText("주문내역"));
        tabLayout.addTab(tabLayout.newTab().setText("메뉴정보"));
        tabLayout.addTab(tabLayout.newTab().setText("매장매출"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //푸시 메시지를 클릭한 경우
        String str = getIntent().getStringExtra("particularFragment");
        if(str !=null)
        {
            if(str.equals("goToSeller_order"))
            {
                viewPager.setCurrentItem(1);
            }
        }

    }

    private void sendNewSellerRegId(int seq, String regId) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendNewSellerRegId(seq, regId);
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

