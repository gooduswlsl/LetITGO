package com.sook.cs.letitgo.seller;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Sales;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class seller_sales extends Fragment {

    private WebView webview = null;
    private TextView jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec, title;
    Seller current_seller;
    private Button sales_list;
    ArrayList<Sales> month_sales = new ArrayList<>();

    private final String TAG = this.getClass().getSimpleName();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.seller_sales, container, false);
        current_seller = ((MyApp) getActivity().getApplicationContext()).getSeller();

        initView(view);

        title.setText(current_seller.getName()+" "+current_seller.getSite()+" 매출 그래프"+"\n(단위: 천)");
        getMonthSales(current_seller.getSeq());

        WebSettings websetting = webview.getSettings();

        websetting.setJavaScriptEnabled(true); // 자바 스크립트 실행 여부
        websetting.setBuiltInZoomControls(false); // 확대 축소 기능
        websetting.setLoadsImagesAutomatically(true);
        websetting.setLoadWithOverviewMode(false);
        websetting.setUseWideViewPort(false);

        webview.setWebViewClient(new MyWebViewClient());
        webview.addJavascriptInterface(new WebAppInterface(), "android");
        webview.loadUrl("file:///android_asset/chart.html");

        sales_list.setOnClickListener(new View.OnClickListener() {  //매장매출확인

            @Override
            public void onClick(final View view) {
                Intent intent = new Intent( getContext(), SalesList.class );
                startActivity(intent);
            }
        });

        return view;
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
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

    // 월별 매장 매출 가져오기
    private void getMonthSales(int memberSeq) {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Sales>> call
                = remoteService.getMonthSales(memberSeq);
        call.enqueue(new Callback<ArrayList<Sales>>() {

            @Override
            public void onResponse(Call<ArrayList<Sales>> call,
                                   Response<ArrayList<Sales>> response) {
                    month_sales = response.body();

                    if (response.isSuccessful()) {
                        Log.d(TAG,month_sales.toString());
                        showMonthSales(month_sales);
                } else {
                    Log.d(TAG, "not success");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Sales>> call, Throwable t) {
                Log.d(TAG, "no internet connectivity");
                Log.d(TAG, t.toString());
            }
        });

    }

    private void initView(View view){
        webview = (WebView) view.findViewById(R.id.web);
        jan=(TextView) view. findViewById(R.id.jan);
        feb=(TextView) view. findViewById(R.id.feb);
        mar=(TextView) view. findViewById(R.id.mar);
        apr=(TextView) view. findViewById(R.id.apr);
        may=(TextView) view. findViewById(R.id.may);
        jun=(TextView) view. findViewById(R.id.jun);
        jul=(TextView) view. findViewById(R.id.jul);
        aug=(TextView) view. findViewById(R.id.aug);
        sep=(TextView) view. findViewById(R.id.sep);
        oct=(TextView) view. findViewById(R.id.oct);
        nov=(TextView) view. findViewById(R.id.nov);
        dec=(TextView) view. findViewById(R.id.dec);
        title=(TextView) view.findViewById(R.id.title);
        sales_list=(Button) view.findViewById(R.id.sales_list);

    }

    private void showMonthSales(ArrayList<Sales> month_sales){
        int i;
        for(i=0; i<month_sales.size(); i++){
            switch(month_sales.get(i).getMonth()){
                case 1:
                    jan.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 2:
                    feb.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 3:
                    mar.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 4:
                    apr.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 5:
                    may.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 6:
                    jun.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 7:
                    jul.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 8:
                    aug.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 9:
                    sep.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 10:
                    oct.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 11:
                    nov.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
                case 12:
                    dec.setText("\uFFE6"+String.format("%,d",month_sales.get(i).getSales()));
                    break;
            }
        }
    }
}