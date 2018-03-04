package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.item.Seller;
import com.sook.cs.letitgo.databinding.DialogSellerBinding;

public class customer_dialog_store extends Activity implements OnMapReadyCallback {
    DialogSellerBinding binding;
    int seller_seq, position;
    MapView map;
    Seller seller;
    DBHelperLiked helper;

    public customer_dialog_store() {
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_seller);
        seller = (Seller) getIntent().getSerializableExtra("seller");
        seller_seq = seller.getSeq();
        binding.setSeller(seller);

        map = binding.map;
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        Log.d("dialog", "onCreate");
        setStar();
    }

    private void setStar() {
        helper = new DBHelperLiked(this, "liked.db", null, 1);

        if (helper.isLikedStore(seller_seq))
            binding.imgStar.setImageResource(R.drawable.star);
        else
            binding.imgStar.setImageResource(R.drawable.star_empty);
    }

    public void clickStar(View v) {
        Intent it = new Intent();
        it.putExtra("position", position);
        it.putExtra("seller_seq", seller_seq);
        if (helper.isLikedStore(seller_seq)) {
            binding.imgStar.setImageResource(R.drawable.star_empty);
            helper.deleteStore(seller_seq);
        } else {
            binding.imgStar.setImageResource(R.drawable.star);
            helper.insertStore(seller_seq);
        }
    }

    public void clickMore(View view) {
        Intent it = new Intent(getApplicationContext(), customer_store_detail.class);
        it.putExtra("seller_seq", seller_seq);
        startActivity(it);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(seller.getLatitude(), seller.getLongitude());
        LatLngBounds ZOOMIN = new LatLngBounds(latLng, latLng);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZOOMIN.getCenter(), 16));
        googleMap.addMarker(markerOptions).showInfoWindow();
    }
    @Override public void onBackPressed() { finish();}
}