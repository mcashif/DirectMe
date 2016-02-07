package com.mks.apple.directme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.Location;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.InputStream;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;


import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NodeList;

import android.graphics.Color;

import android.content.Intent;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

public class MapsActivity extends FragmentActivity implements LocationListener,
        OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;



    TextView tv;

    String ClientAdd;
    String Clientinfo;

    float initialDistance;
    float distanceCovered;

    MarkerOptions c_mo,d_mo;

    int mapLineColor=Color.RED;

    Marker driver_marker, client_marker;

    private GoogleMap mMap;

    boolean first_time_flag=false;

    LatLng driver_latlong;
    LatLng clint_latlong;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {


        if(first_time_flag==false) {

            first_time_flag = true;
            distanceCovered=0;

            // Creating a LatLng object for the current location
            driver_latlong = new LatLng(location.getLatitude(), location.getLongitude());

            d_mo = new MarkerOptions().position(driver_latlong).title("Hi" + ", you are here!").snippet("Call:" + ClientAdd)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).flat(true).rotation(245);
            c_mo = new MarkerOptions().position(clint_latlong).title(Clientinfo).snippet(ClientAdd)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN));

           driver_marker= mMap.addMarker(d_mo);
           client_marker= mMap.addMarker(c_mo);

            DrawDirection();

            initialDistance= MapUtils.getDistancef(driver_latlong, clint_latlong);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(driver_latlong)      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            tv.setText("Yor are " + MapUtils.getDistance(driver_latlong, clint_latlong) + " far...");


        } else {

            if (MapUtils.getDistancef(driver_latlong, clint_latlong) < 500.0f) {

                mMap.clear();

                mapLineColor=Color.GREEN;

                driver_latlong = new LatLng(location.getLatitude(), location.getLongitude());
                float bearing = MapUtils.getBearing(driver_latlong, clint_latlong);

                Toast.makeText(this, "Almost there!!!!!!!",
                        Toast.LENGTH_LONG).show();

                float currentDistance=MapUtils.getDistancef(driver_latlong, clint_latlong);
                float totalDifference = initialDistance-currentDistance;
                distanceCovered=distanceCovered+totalDifference;
                String diff=distanceCovered+"M";

                tv.setText("Total " +diff+ " Meter Covered...");

                d_mo = new MarkerOptions().position(driver_latlong).title("Hi" + ", you are here!").snippet("Call:" + ClientAdd)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).flat(true).rotation(bearing+180);

                driver_marker= mMap.addMarker(d_mo);
                client_marker= mMap.addMarker(c_mo);

                DrawDirection();

            } else {

                mMap.clear();

                if(mapLineColor==Color.RED)
                    mapLineColor=Color.BLUE;
                else
                    mapLineColor=Color.RED;



                driver_latlong = new LatLng(location.getLatitude(), location.getLongitude());
                float bearing = MapUtils.getBearing(driver_latlong, clint_latlong);

                float currentDistance=MapUtils.getDistancef(driver_latlong, clint_latlong);
                float totalDifference = initialDistance-currentDistance;
                distanceCovered=distanceCovered+totalDifference;
                String diff=distanceCovered+"M";

                tv.setText("Total " +diff+ " Meter Covered...");

                MarkerOptions d_mo = new MarkerOptions().position(driver_latlong).title("Hi" + ", you are here!").snippet("Call:" + ClientAdd)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).flat(true).rotation(bearing+180);

                driver_marker= mMap.addMarker(d_mo);
                client_marker= mMap.addMarker(c_mo);

                DrawDirection();

            }
        }

    }



    public void DrawDirection(){

    String url = getMapsApiDirectionsUrl(driver_latlong, clint_latlong);
    ReadTask downloadTask = new ReadTask();
    downloadTask.execute(url);


    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        ClientAdd = getIntent().getStringExtra("add");
        Clientinfo = getIntent().getStringExtra("inf");


        mLocationRequest = LocationRequest.create();
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        clint_latlong = new LatLng(Double.parseDouble(getIntent().getStringExtra("lat")), Double.parseDouble(getIntent().getStringExtra("lon")));

        tv=(TextView)findViewById(R.id.textViewMap);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Wait...");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {



        mMap = googleMap;

        enableMyLocation();

    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
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

    private String getMapsApiDirectionsUrl(LatLng src, LatLng dest) {

        // Origin of route
        String str_origin = "origin="+src.latitude+","+src.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;

    }

    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            if (routes != null) {
                // traversing through routes
                for (int i = 0; i < routes.size(); i++) {
                    points = new ArrayList<LatLng>();
                    polyLineOptions = new PolylineOptions();
                    List<HashMap<String, String>> path = routes.get(i);

                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    polyLineOptions.addAll(points);
                    polyLineOptions.width(4);
                    polyLineOptions.color(mapLineColor);
                }

                mMap.addPolyline(polyLineOptions);
            }
        }
    }
}