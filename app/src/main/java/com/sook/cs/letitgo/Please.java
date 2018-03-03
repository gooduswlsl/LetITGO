package com.sook.cs.letitgo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Please extends AppCompatActivity {


        String regId;

        RequestQueue queue;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.please);

            Button sendButton = (Button) findViewById(R.id.sendButton);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    send("여기에 메세지 넣어라");
                }
            });

//            데이터 전송에 사용하는 volly의 큐 객체 생성
           queue = Volley.newRequestQueue(getApplicationContext());

            regId = FirebaseInstanceId.getInstance().getToken();
            println("비교1:"+regId);
//            regId="c_xhIzjD0HM:APA91bFiIpCITsbxPJWR1N9GUPZTy0SwLliheS-4Mov_QT2WFSoYVmpl-Pb6_AXNpVEihma8EhlCQ9O801LhgYL3xiwCga0NHTIR4K7slmg0ZU6ONSmRuQJGrQ4sUzx-KvRqjnBtgX1H";
//            println("비교2:"+regId);
        }


        public void send(String input) {

            //전송정보 담아둘 JSONObject객체 생성
            JSONObject requestData = new JSONObject();

            try {
                requestData.put("priority", "high");

                JSONObject dataObj = new JSONObject(); //jsonobject또 만들어서 저장하게 된다.
                dataObj.put("contents", input); //쓴 내용 여기다 저장
                requestData.put("data", dataObj);

                JSONArray idArray = new JSONArray();  //단말의 등록id를 데이터에 추가
                idArray.put(0, regId);
                requestData.put("registration_ids", idArray);

            } catch(Exception e) {
                e.printStackTrace();
            }

            sendData(requestData, new SendResponseListener() {
                @Override
                public void onRequestCompleted() {
                    println("onRequestCompleted() 호출됨.");
                }

                @Override
                public void onRequestStarted() {
                    println("onRequestStarted() 호출됨.");
                }

                @Override
                public void onRequestWithError(VolleyError error) {
                    println("onRequestWithError() 호출됨.");
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
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            listener.onRequestCompleted();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onRequestWithError(error);
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<String,String>();
                    headers.put("Authorization","key=AAAANjIyu7A:APA91bGxMJrapRgMA0eeNq4PJkBdtlCe8mbdPnO14B-xoTmo-oG2Uzp6046qXT0-kFqxFdEqBhYjQz3yLZzy1mq2dT9psAsPMp_7KyRKRVqfXJvSTbcaguvyN3XKS9zUpwlFFUMXKyol");

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

        public void println(String data) {
            Log.d("please",data);
        }
    }
