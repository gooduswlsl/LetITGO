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

import static com.sook.cs.letitgo.R.id.btn1;
import static com.sook.cs.letitgo.R.id.btn2;

public class seller_order extends Fragment {

    Seller current_seller;
    private final String TAG = this.getClass().getSimpleName();
    Order_ListViewAdapter adapter;
    private TextView no_order, loading_bg;
    private Button today, week, month;
    private ImageView imgAndroid;
    private Animation anim;
    private String clicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clicked="-1 day";
        return inflater.inflate(R.layout.seller_order, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        current_seller = ((MyApp) getActivity().getApplicationContext()).getSeller();

        adapter = new Order_ListViewAdapter();
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

        Log.d(TAG,"oncreate");
        showOrderList(clicked);

        no_order = getView().findViewById(R.id.no_order);
        today = getView().findViewById(btn1);
        week = getView().findViewById(btn2);
        month = getView().findViewById(R.id.btn3);

        //더러워서 나중에 코드 수정 예정
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG,"today");
                today.setTextColor(Color.parseColor("#8C8C8C"));
                week.setTextColor(Color.parseColor("#95c85b"));
                month.setTextColor(Color.parseColor("#95c85b"));
                adapter = new Order_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                clicked=today.getTag().toString();
                showOrderList(clicked);
            }
        });
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG,"week");
                week.setTextColor(Color.parseColor("#8C8C8C"));
                today.setTextColor(Color.parseColor("#95c85b"));
                month.setTextColor(Color.parseColor("#95c85b"));
                adapter = new Order_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                clicked=week.getTag().toString();
                showOrderList(clicked);
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.d(TAG,"month");
                month.setTextColor(Color.parseColor("#8C8C8C"));
                week.setTextColor(Color.parseColor("#95c85b"));
                today.setTextColor(Color.parseColor("#95c85b"));
                adapter = new Order_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                clicked=month.getTag().toString();
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
                adapter = new Order_ListViewAdapter();
                adapter.queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                listview.setAdapter(adapter);
                showOrderList(clicked);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    // DB에서 memberSeq에 해당하는 메뉴내용 가져오기
    private void showOrderList(String period) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Order>> call
                = remoteService.listOrder(((MyApp) getActivity().getApplicationContext()).getSeller().getSeq(), period);
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
                    Log.d(TAG, "not success");
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
}