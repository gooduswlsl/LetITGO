package com.sook.cs.letitgo.seller;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Customer;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class seller_recipt extends Fragment {

    Seller current_seller;
    private final String TAG = this.getClass().getSimpleName();
    Recipt_ListViewAdapter adapter;
    private TextView no_order, loading_bg, tv_waiting;
    private Button new_order, preparing, today;
    private ImageView imgAndroid;
    private Animation anim;
    private String clicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clicked="new_order";
        return inflater.inflate(R.layout.seller_recipt, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        current_seller = ((MyApp) getActivity().getApplicationContext()).getSeller();

        adapter = new Recipt_ListViewAdapter();
        adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // 리스트뷰 참조 및 Adapter달기
        final ListView listview;
        final SwipeRefreshLayout swipeRefreshLayout;

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLo);
        listview = (ListView) getView().findViewById(R.id.listview);
        listview.setAdapter(adapter);
        imgAndroid = getView().findViewById(R.id.img_android);
        loading_bg = getView().findViewById(R.id.loading_bg);
        progressDialog();

        showOrderList(clicked);

        no_order = getView().findViewById(R.id.no_order);
        new_order = getView().findViewById(R.id.btn1);
        tv_waiting = getView().findViewById(R.id.waiting);
        preparing = getView().findViewById(R.id.btn2);
        today = getView().findViewById(R.id.btn3);

        //더러워서 나중에 코드 수정 예정
        new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG,"new_order");
                new_order.setTextColor(Color.parseColor("#8C8C8C"));
                preparing.setTextColor(Color.parseColor("#95c85b"));
                today.setTextColor(Color.parseColor("#95c85b"));
                adapter = new Recipt_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                clicked=new_order.getTag().toString();
                showOrderList(clicked);
            }
        });
        preparing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG,"preparing");
                preparing.setTextColor(Color.parseColor("#8C8C8C"));
                new_order.setTextColor(Color.parseColor("#95c85b"));
                today.setTextColor(Color.parseColor("#95c85b"));
                adapter = new Recipt_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                clicked=preparing.getTag().toString();
                showOrderList(clicked);
            }
        });
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG,"today");
                today.setTextColor(Color.parseColor("#8C8C8C"));
                preparing.setTextColor(Color.parseColor("#95c85b"));
                new_order.setTextColor(Color.parseColor("#95c85b"));
                adapter = new Recipt_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                clicked=today.getTag().toString();
                showOrderList(clicked);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
                // 해당 position의 item
                final Order item = (Order) parent.getItemAtPosition(position);

                // TODO : use item data.
                LayoutInflater inflater = getLayoutInflater(null);
                View alertLayout = inflater.inflate(R.layout.order_layout, null);

                final TextView menu_name = alertLayout.findViewById(R.id.order_menu_name);
                final TextView time_take = alertLayout.findViewById(R.id.time_take);
                final TextView order_price = alertLayout.findViewById(R.id.order_price);
                final TextView message = alertLayout.findViewById(R.id.message);
                final TextView customer_name = alertLayout.findViewById(R.id.order_customer_name);
                final TextView customer_phone = alertLayout.findViewById(R.id.order_customer_phone);
                final ImageView profile = alertLayout.findViewById(R.id.order_customer_profile);

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setView(alertLayout);
                alert.setCancelable(false);

                //position의 order에 해당하는 관련 Customer, Menu객체 가져오기
                final Customer order_customer = adapter.getPositionCustomer(position);
                final Menu order_menu = adapter.getPositionMenu(position);

                //예약시간String 깔끔하게
                String all = item.getTime_take();
                int here = all.indexOf("T");
                String date = all.substring(0, here);
                String time = all.substring(here + 1, here + 6);

                menu_name.setText(order_menu.getmName() + " / " + item.getNum() + "개");
                time_take.setText(date + " " + time);
                order_price.setText((order_menu.getmPrice() * item.getNum()) + "원");
                message.setText(item.getMessage());
                customer_name.setText(order_customer.getName());
                customer_phone.setText(order_customer.getPhone());


                if (StringLib.getInstance().isBlank(order_customer.getImg())) {
                    Picasso.with(getActivity().getApplicationContext()).load(R.drawable.ic_person).into(profile);
                } else {
                    Picasso.with(getActivity().getApplicationContext())
                            .load(RemoteService.CUSTOMER_IMG_URL + order_customer.getImg())
                            .into(profile);
                }

                alert.setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                final AlertDialog dialog = alert.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                adapter = new Recipt_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                showOrderList(clicked);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    // DB에서 memberSeq에 해당하는 메뉴내용 가져오기
    private void showOrderList(String tag) {
        getCustomerRegId();
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Order>> call
                = remoteService.getTodayOrder(((MyApp) getActivity().getApplicationContext()).getSeller().getSeq(), tag);
        call.enqueue(new Callback<ArrayList<Order>>() {

            @Override
            public void onResponse(Call<ArrayList<Order>> call,
                                   Response<ArrayList<Order>> response) {
                ArrayList<Order> list = response.body();

                if (list == null) {
                    Log.d(TAG,"list가 null");
                    loading_bg.setVisibility(View.GONE);
                    imgAndroid.clearAnimation();
                    imgAndroid.setVisibility(View.GONE);
                    list = new ArrayList<>();
                }

                if (response.isSuccessful()) {
                    Log.d(TAG, "list size " + list.size());
                    no_order.setVisibility(View.GONE);
                    adapter.imgAndroid=imgAndroid;
                    adapter. loading_bg= loading_bg;
                    adapter.setItemList(list);
                    adapter.setClicked(clicked);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "showOrderList unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });

    }

    public void progressDialog(){
        anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.loading);
        imgAndroid.setAnimation(anim);
    }

    private void getCustomerRegId() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.getWaitingNumber(((MyApp) getActivity().getApplicationContext()).getSeller().getSeq());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String waiting=response.body();
                    tv_waiting.setText("현재 대기중인 메뉴 : "+waiting+"개");

                } else {
                    Log.d(TAG, "getCustomerRegId unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG,t.toString());
            }
        });
    }

}