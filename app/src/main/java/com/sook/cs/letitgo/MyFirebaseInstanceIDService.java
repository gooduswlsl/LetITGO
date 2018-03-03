package com.sook.cs.letitgo;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

//클라우드 서버에 단말을 등록하는 역할
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private final String TAG = this.getClass().getSimpleName();

    //단말의 등록ID 성공시 호출
    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh() 호출됨.");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token : " + refreshedToken);
    }

}
