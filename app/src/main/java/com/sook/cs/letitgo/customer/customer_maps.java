package com.sook.cs.letitgo.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.sook.cs.letitgo.R;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by YEONJIN on 2018-01-08.
 */

public class customer_maps extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private MapView mapView;
    private LatLng latLng;
    private GoogleMap googleMap;
    private Adapter_seller_map adapterSellerMap;
    private RecyclerView recyclerView;
    private PicassoMarker target;
    private HashMap<Marker, Seller> markerMap = new HashMap<>();

    public customer_maps() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterSellerMap = new Adapter_seller_map(this.getContext(), new ArrayList<Seller>());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {  //액티비티가 처음 생성될 때
        super.onActivityCreated(savedInstanceState);
        mapView = getView().findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }

        recyclerView = getView().findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterSellerMap);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        listInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        latLng = GeoItem.getKnownLocation();
        if (latLng == null) {
            Log.d("map", "onmapready latlng null");
            latLng = new LatLng(37.56, 126.97); //null이면 서울
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("내위치")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .draggable(true);

        googleMap.addMarker(markerOptions).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setOnInfoWindowClickListener(this);
        listInfo();
    }


    private void listInfo() {
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
        Call<ArrayList<Seller>> call = remoteService.listSellerMap(GeoItem.getKnownLocation().latitude, GeoItem.getKnownLocation().longitude);
        call.enqueue(new Callback<ArrayList<Seller>>() {
            @Override
            public void onResponse(Call<ArrayList<Seller>> call, Response<ArrayList<Seller>> response) {
                ArrayList<Seller> list = response.body();
                if (response.isSuccessful() && list != null) {
                    adapterSellerMap.addSellerList(list);
                    setMap(list);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Seller>> call, Throwable t) {
                Log.d("map", "onfailure");
            }
        });
    }

    private void setMap(ArrayList<Seller> list) {
        Log.d("map", "setMap");
        for (Seller item : list) {
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(item.latitude, item.longitude))
                    .title(item.getName())
                    .snippet(item.getSite())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.empty));

            Marker marker = googleMap.addMarker(markerOption);
            target = new PicassoMarker(marker);
            Picasso.with(getContext()).load((RemoteService.SELLER_IMG_URL + item.getImg())).resize(120, 120).into(target);
            markerMap.put(marker, item);
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent it = new Intent(getContext(), customer_dialog_store.class);
        it.putExtra("seller", markerMap.get(marker));
        (getContext()).startActivity(it);
    }
}