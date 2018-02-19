package com.sook.cs.letitgo.customer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.FragmentMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sook.cs.letitgo.item.GeoItem;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.remote.RemoteService;
import com.sook.cs.letitgo.remote.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_maps extends Fragment implements OnMapReadyCallback {
    FragmentMapsBinding binding;
    private MapView mapView;
    private Adapter_seller_list adapterSellerList;
    private RecyclerView recyclerView;

    public customer_maps() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterSellerList = new Adapter_seller_list(getActivity(), new ArrayList<Seller>());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {  //액티비티가 처음 생성될 때
        super.onActivityCreated(savedInstanceState);
        mapView = getView().findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }

        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterSellerList);
        listInfo();
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
    public void onMapReady(GoogleMap googleMap) {
        LatLng SEOUL = new LatLng(37.56, 126.97);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("수도");
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    private void listInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Seller>> call = remoteService.listSellerMap(GeoItem.getKnownLocation().latitude, GeoItem.getKnownLocation().longitude);
        call.enqueue(new Callback<ArrayList<Seller>>() {
            @Override
            public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                ArrayList<Seller> list = response.body();
                if (response.isSuccessful() && list != null) {
                    adapterSellerList.addSellerList(list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {
                Log.d("map", "onfailure");
            }
        });
    }

}