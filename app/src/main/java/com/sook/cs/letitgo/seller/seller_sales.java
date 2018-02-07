package com.sook.cs.letitgo.seller;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sook.cs.letitgo.R;

public class seller_sales extends Fragment {

    private WebView webview = null;
    private ProgressDialog pDialog = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.seller_sales, container, false);

        webview = (WebView) view.findViewById(R.id.web);
        WebSettings websetting = webview.getSettings();

        websetting.setJavaScriptEnabled(true); // 자바 스크립트 실행 여부
        websetting.setBuiltInZoomControls(false); // 확대 축소 기능
        websetting.setLoadsImagesAutomatically(true);
        websetting.setLoadWithOverviewMode(false);
        websetting.setUseWideViewPort(true);

        webview.setWebViewClient(new MyWebViewClient());
        webview.addJavascriptInterface(new WebAppInterface(), "android");
        webview.loadUrl("file:///android_asset/chart.html");

        return view;

    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            if (pDialog == null) {
//                pDialog = new ProgressDialog(getActivity());
//                pDialog.setMessage("차트 생성중입니다..");
//                pDialog.show();
//            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if (pDialog != null) {
//                pDialog.dismiss();
//                pDialog = null;
//            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class WebAppInterface {
        @JavascriptInterface
        public void funct(final String tmp) {

        }
    }
}
