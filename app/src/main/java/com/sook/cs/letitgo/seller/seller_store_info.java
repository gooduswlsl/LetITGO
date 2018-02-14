package com.sook.cs.letitgo.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sook.cs.letitgo.MyApp;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.lib.StringLib;
import com.sook.cs.letitgo.remote.RemoteService;
import com.squareup.picasso.Picasso;

public class seller_store_info extends Fragment implements OnMapReadyCallback {

    private MapView mapView = null;
    private TextView name, tel, site, address, webpage;
    private ImageView img;


    Seller current_seller;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.seller_store_info, container, false);

        current_seller = ((MyApp) getActivity().getApplicationContext()).getSeller();  //현재 seller정보 가져오기

        mapView = layout.findViewById(R.id.map);
        mapView.getMapAsync(this);
        name = layout.findViewById(R.id.store_name);
        site = layout.findViewById(R.id.store_site);
        tel = layout.findViewById(R.id.store_tel);
        address = layout.findViewById(R.id.store_address);
        webpage =  layout.findViewById(R.id.store_webpage);
        img = layout.findViewById(R.id.store_img);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
        name.setText(current_seller.getName());
        site.setText(current_seller.getName()+" "+current_seller.getSite());
        tel.setText(current_seller.getTel());
        address.setText(current_seller.getAddress());
        webpage.setText(current_seller.getWebpage());

        if (StringLib.getInstance().isBlank(current_seller.getImg())) {
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.noimage).into(img);
        } else {
            Picasso.with(getActivity().getApplicationContext())
                    .load(RemoteService.SELLER_IMG_URL + current_seller.getImg())
                    .into(img);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        LatLng seoul = new LatLng(37.555744, 126.970431);

        LatLngBounds ZOOMIN = new LatLngBounds(
                new LatLng(current_seller.getLatitude(), current_seller.getLongitude()), new LatLng(current_seller.getLatitude(), current_seller.getLongitude()));
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
//        //줌 애니메이션
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
//        googleMap.animateCamera(zoom);
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(seoul));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZOOMIN.getCenter(), 15));

        //마커 표시하기
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng( current_seller.getLatitude(), current_seller.getLongitude()))
                .title(current_seller.getName());
        googleMap.addMarker(marker).showInfoWindow();
    }

}

