package com.mks.apple.directme;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapAllActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    ArrayList<ClientDetail> listOfClients;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    List<String> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_all);



        Bundle bundle = getIntent().getExtras();
        listOfClients = bundle.getParcelableArrayList("KEYCLIENT");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng clint_latlong=new LatLng(0,0);

        markers = new ArrayList<String>();

        enableMyLocation();
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setOnMarkerClickListener(this);

        for (int a = 0; a < listOfClients.size(); a++) {

            clint_latlong = new LatLng(Double.parseDouble(listOfClients.get(a).getLatitude()), Double.parseDouble(listOfClients.get(a).getLongitude()));

            MarkerOptions c_mo = new MarkerOptions()
                    .position(clint_latlong)
                    .title(listOfClients.get(a).getAddress())
                    .snippet(listOfClients.get(a).getInstructions())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.deliver));

            Marker mrk= mMap.addMarker(c_mo);
            markers.add(mrk.getId());

        }


        CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(clint_latlong)      // Sets the center of the map to location user
                    .zoom(10)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        int index=markers.indexOf(marker.getId());

        Intent mapIntent = new Intent(MapAllActivity.this, MapsActivity.class);
            mapIntent.putExtra("lat", listOfClients.get(index).getLatitude()); //Optional parameters
            mapIntent.putExtra("lon", listOfClients.get(index).getLongitude()); //Optional parameters
            mapIntent.putExtra("add", listOfClients.get(index).getAddress()); //Optional parameters
            mapIntent.putExtra("inf", listOfClients.get(index).getInstructions()); //Optional parameters
        MapAllActivity.this.startActivity(mapIntent);

        return true;
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


}