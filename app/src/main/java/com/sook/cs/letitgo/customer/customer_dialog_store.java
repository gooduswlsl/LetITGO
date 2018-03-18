package com.sook.cs.letitgo.customer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sook.cs.letitgo.R;
import com.sook.cs.letitgo.databinding.DialogSellerBinding;
import com.sook.cs.letitgo.item.Seller;

public class customer_dialog_store extends AppCompatActivity implements OnMapReadyCallback {
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
        ActionBar ab = getSupportActionBar();
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(R.layout.actionbar_back);
        ((TextView) ab.getCustomView().findViewById(R.id.ab_title)).setText("매장정보");

        binding = DataBindingUtil.setContentView(this, R.layout.dialog_seller);
        seller = (Seller) getIntent().getSerializableExtra("seller");
        seller_seq = seller.getSeq();
        binding.setSeller(seller);

        map = binding.map;
        map.onCreate(savedInstanceState);
        map.getMapAsync(this);

        Log.d("dialog", "onCreate");
        setStar();
        setImg(seller.getType());
    }

    private void setImg(int type) {
        ImageView img = binding.imageView;
        switch (type) {
            case 1:
                img.setImageResource(R.drawable.pic1);
                break;
            case 2:
                img.setImageResource(R.drawable.pic2);
                break;
            case 3:
                img.setImageResource(R.drawable.pic3);
                break;
            case 4:
                img.setImageResource(R.drawable.pic4);
                break;
            default:
                img.setImageResource(R.drawable.pic);
                break;
        }
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

    public void backClick(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}