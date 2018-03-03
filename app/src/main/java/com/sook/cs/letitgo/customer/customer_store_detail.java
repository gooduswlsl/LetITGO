package com.sook.cs.letitgo.customer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentMenuBinding;
import com.sook.cs.letitgo.databinding.FragmentMenuTypeBinding;
import com.sook.cs.letitgo.item.Menu;
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
    private FragmentMenuTypeBinding binding;
    private Adapter_menu_img adapterMenuImg;
    private Adapter_menu_list adapterMenuList;
    private RecyclerView recyclerView;
    int seller_seq;

    public customer_store_detail() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterMenuImg = new Adapter_menu_img(this, new ArrayList<Menu>());

        seller_seq = getIntent().getIntExtra("seller_seq", 0);
        if (seller_seq != 0)
            setTitle();
        setView();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Toast.makeText(getApplicationContext(), "CART", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void setView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_menu_type);
        binding.setActivity(this);
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

        recyclerView = binding.recyclerviewMenu;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterMenuImg);
        listInfo();
    }

    private void setTitle() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<Seller> call = remoteService.selectSeller(seller_seq);
        call.enqueue(new Callback<Seller>() {
            @Override
            public void onResponse(Call<Seller> call, Response<Seller> response) {
                Seller seller = response.body();
                setTitle(seller.getName());
            }

            @Override
            public void onFailure(Call<Seller> call, Throwable t) {
                Log.d("sellerdialog", t.toString());
            }
        });


    }

    private void listInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Menu>> call = remoteService.listMenu(seller_seq);
        call.enqueue(new Callback<ArrayList<Menu>>() {
            @Override
            public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                ArrayList<Menu> list = response.body();
                if (response.isSuccessful() && list != null) {
                    adapterMenuImg.addMenuList(list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {

            }
        });
    }

    public void searchClick(View v) {
        final String key = binding.editSearch.getText().toString();
        if (!key.equals("")) {
            recyclerView.removeAllViews();
            RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
            Call<ArrayList<Menu>> call = remoteService.searchMenu(key, 0);
            call.enqueue(new Callback<ArrayList<Menu>>() {
                @Override
                public void onResponse(Call<ArrayList<Menu>> call, Response<ArrayList<Menu>> response) {
                    ArrayList<Menu> list = response.body();
                    if (response.isSuccessful() && list != null) {
                        adapterMenuList = new Adapter_menu_list(customer_store_detail.this, list);
                        recyclerView.setLayoutManager(new LinearLayoutManager(customer_store_detail.this));
                        recyclerView.setAdapter(adapterMenuList);

                        binding.tvCount.setText(key + "에 대한 검색 결과가 " + list.size() + "건이 있습니다.");
                        binding.tvCount.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Menu>> call, Throwable t) {

                }
            });
        }

    }

}