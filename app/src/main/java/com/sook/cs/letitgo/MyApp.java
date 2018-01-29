package com.sook.cs.letitgo;

import android.app.Application;
import android.os.StrictMode;


/**
 * 앱 전역에서 사용할 수 있는 클래스
 */
public class MyApp extends Application {
    private Store store;

    @Override
    public void onCreate() {
        super.onCreate();

        // FileUriExposedException 문제를 해결하기 위한 코드
        // 관련 설명은 책의 [참고] 페이지 참고
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public Store getStore() {
        if (store == null) store = new Store();

        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getStoreSeq() {
        return store.seq;
    }

}
