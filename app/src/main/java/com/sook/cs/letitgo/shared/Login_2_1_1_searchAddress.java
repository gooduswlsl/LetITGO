package com.sook.cs.letitgo.shared;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.sook.cs.letitgo.R;

public class Login_2_1_1_searchAddress extends AppCompatActivity {

    private WebView webView;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        init_webView();
        handler = new Handler(); // 핸들러를 통한 JavaScript 이벤트 반응
    }

    public void init_webView() {

        webView = (WebView) findViewById(R.id.webView);  // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true); // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
// 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp"); // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("http://codeman77.ivyro.net/getAddress.php"); // webview url load
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override

                public void run() {
                    Intent intent = new Intent();
                    intent.putExtra("address","("+arg1+") "+arg2+" "+arg3);
                    setResult(RESULT_OK,intent);
                    finish();

                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();
                }
            });
        }
    }


}

