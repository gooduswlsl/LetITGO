package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Menu;
import com.sook.cs.letitgo.item.Order;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class customer_payment extends AppCompatActivity {
    private int price;
    private ArrayList<Order> orderList;
    private DBHelperCart helper;
    RequestQueue queue;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.actionbar_center);
        ((TextView) ab.getCustomView().findViewById(R.id.ab_title)).setText("장바구니");

        orderList = (ArrayList<Order>) getIntent().getSerializableExtra("orderList");
        price = getIntent().getIntExtra("price", 0);
        ((TextView) findViewById(R.id.tv_price)).setText(price + "");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void backClick(View v) {
        finish();
    }

    public void payClick(View v) {
        Log.d("cart", "click");
        helper = new DBHelperCart(getApplicationContext(), "cart.db", null, 1);
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            upload(order);
            getRegId(order.getSeller_seq());
        }
        helper.flushDB();

        Intent it = new Intent(getApplicationContext(), customer_main.class);
        startActivity(it);
        finish();
    }

    public void upload(Order order) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.sendOrder(order);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("ok", "sendOrder successful");
                } else {
                    Log.d("ok", "sendOrder unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void getRegId(int seq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<String> call = remoteService.getRegId(seq);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String regId = response.body();
                    Log.d(TAG, "이번regId:" + regId);
                    queue = Volley.newRequestQueue(getApplicationContext());
                    send("주문이 도착하였습니다.", regId);
                } else {
                    Log.d(TAG, "getRegId unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    /*seller 핸드폰에 푸시메시지 보내기
  클라우드 서버에 메시지 전송하기 위해 Volley 라이브러리 이용
  메시지는 JSON객체로 묶음.*/
    public void send(String input, String regId) {

        //전송정보 담아둘 JSONObject 객체 생성
        JSONObject requestData = new JSONObject();

        try {
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();
            dataObj.put("contents", input);
            requestData.put("data", dataObj);

            JSONArray idArray = new JSONArray();
            idArray.put(0, regId);
            requestData.put("registration_ids", idArray);

        } catch (Exception e) {
            e.printStackTrace();
        }

        sendData(requestData, new SendResponseListener() {
            @Override
            public void onRequestCompleted() {
                Log.d(TAG, "onRequestCompleted() 호출됨.");
            }

            @Override
            public void onRequestStarted() {
                Log.d(TAG, "onRequestStarted() 호출됨.");
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                Log.d(TAG, "onRequestWithError() 호출됨.");
            }
        });

    }

    public interface SendResponseListener {
        public void onRequestStarted();

        public void onRequestCompleted();

        public void onRequestWithError(VolleyError error);
    }

    public void sendData(JSONObject requestData, final SendResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onRequestCompleted();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestWithError(error);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAANjIyu7A:APA91bGxMJrapRgMA0eeNq4PJkBdtlCe8mbdPnO14B-xoTmo-oG2Uzp6046qXT0-kFqxFdEqBhYjQz3yLZzy1mq2dT9psAsPMp_7KyRKRVqfXJvSTbcaguvyN3XKS9zUpwlFFUMXKyol");

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        queue.add(request);
    }

}
