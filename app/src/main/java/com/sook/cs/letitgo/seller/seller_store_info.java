package com.sook.cs.letitgo.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sook.cs.letitgo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class seller_store_info extends Fragment implements OnMapReadyCallback {

    private MapView mapView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.seller_store_info, container, false);
        mapView = (MapView) layout.findViewById(R.id.map);
        mapView.getMapAsync(this);
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        LatLng seoul = new LatLng(37.555744, 126.970431);

        LatLngBounds AUSTRALIA = new LatLngBounds(
                new LatLng(37.555744, 126.970431), new LatLng(37.555744, 126.970431));
//
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
//
//        //줌 애니메이션
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
//        googleMap.animateCamera(zoom);
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(seoul));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AUSTRALIA.getCenter(), 15));

        //마커 표시하기
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(37.555744, 126.970431))
                .title("코피티암")
                .snippet("간단매장주소");//부제
        googleMap.addMarker(marker).showInfoWindow();
    }

}

