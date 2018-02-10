package com.sook.cs.letitgo.customer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentSellerBinding;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_store_detail extends AppCompatActivity {
    private FragmentSellerBinding binding;
    private Adapter_seller_img adapterSellerImg;
    private Adapter_seller_list adapterSellerList;
    private RecyclerView recyclerView;
    Context context;
    int seller_seq;

    public customer_store_detail() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_store_detail);
        adapterSellerImg = new Adapter_seller_img(context, new ArrayList<Seller>());

        seller_seq = getIntent().getIntExtra("seller_seq", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);  // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        //actionBar.setHomeAsUpIndicator(R.drawable.button_back); //뒤로가기 버튼을 본인이 만든 아이콘으로 하기 위해 필요

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:{
//                finish();
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    //추가된 소스, ToolBar에 추가된 항목의 select 이벤트를 처리하는 함수
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_cart:
                // User chose the "Cart" item, show the app settings UI...
                return true;

            case android.R.id.home: {
                finish();
                return true;
            }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                // Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_customer_store_detail, container, false);
       // binding.setFragment(this);
        binding.editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Log.d("search", "search click");
                        searchClick(v);
                        break;
                    default:
                        searchClick(v);
                        return false;
                }
                return true;
            }
        });

        recyclerView = binding.recyclerviewStore;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 5);
        recyclerView.setLayoutManager(layoutManager);

recyclerView.setAdapter(adapterSellerImg);
        listInfo();


        return binding.getRoot();
    }

    private void listInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Seller>> call = remoteService.listSellerInfo();
        call.enqueue(new Callback<ArrayList<Seller>>() {
            @Override
            public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                ArrayList<Seller> list = response.body();
                if (response.isSuccessful() && list != null) {
                    adapterSellerImg.addSellerList(list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {
                Log.d("storelist", t.toString());
            }
        });
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }


    public void searchClick(View v) {
        final String key = binding.editSearch.getText().toString();
        Log.d("search", "search click method");
        if (!key.equals("")) {
            recyclerView.removeAllViews();
            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
            Call<ArrayList<Seller>> call = remoteService.searchSeller(key);
            call.enqueue(new Callback<ArrayList<Seller>>() {
                @Override
                public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                    ArrayList<Seller> list = response.body();
                    if (response.isSuccessful() && list != null) {
                        adapterSellerList = new Adapter_seller_list(context, list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapterSellerList);

                        binding.tvCount.setText(key + "에 대한 검색 결과가 " + list.size() + "건이 있습니다.");
                        binding.tvCount.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {

                }
            });
        }

    }

}