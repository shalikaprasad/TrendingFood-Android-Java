package com.site11.funwithultimate.trendingfood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.site11.funwithultimate.trendingfood.Home.Farmer.Farmers_Home;

import java.util.HashMap;
import java.util.List;

public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    Button calluberrequestbtn;
    private Marker pickupMarker;
    private LatLng pickupLocation;
    private Boolean requestBol = false;

    private DatabaseReference UsersRef, CustomerRef;
    String current_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.cusmap);
        mapFragment.getMapAsync(this);

        calluberrequestbtn = findViewById(R.id.calluberrequestbtn);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        CustomerRef = FirebaseDatabase.getInstance().getReference().child("CustomerRequest");
        current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    public void calluberbtn(View view)
    {
        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userCharacter = dataSnapshot.child("character").getValue().toString();
                    String userFullName = dataSnapshot.child("fullname").getValue().toString();

                    HashMap requestsMap = new HashMap();
                    requestsMap.put("fullname", userFullName);
                    requestsMap.put("character", userCharacter);

                    if(userCharacter.equals("Farmer")){
                        sharedetails();
                    }else if(userCharacter.equals("Consumer")){
                        sharedetails();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("මේ ඔයා").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup)));

    }

    public void sharedetails(){

        if (requestBol){
            requestBol = false;
            geoQuery.removeAllListeners();
            retailerLocationRef.removeEventListener(retailerLocationRefListener);

            if(retailerFoundID != null){
                DatabaseReference retailerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(retailerFoundID).child("customerRideId");
                retailerRef.removeValue();
                retailerFoundID = null;
            }
            retailerFound = false;
            radius = 1;
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Maps").child("CustomerRequest");
            GeoFire geoFire = new GeoFire(ref);
            geoFire.removeLocation(userId);

            if(pickupMarker != null){
                pickupMarker.remove();
            }
            calluberrequestbtn.setText("වෙන මුදලාලි කෙනෙක් හොයන්න");

        }else {
            requestBol = true;
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Maps").child("CustomerRequest");
            GeoFire geoFire = new GeoFire(ref);

            geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
            pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            //pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("මේ ඔයා").icon(BitmapDescriptorFactory.fromResource(R.mipmap.consumer)));

            calluberrequestbtn.setText("මුදලාලිව හොයමින් ඉන්නේ....");
            getClosestRetailer();
        }

    }

    private int radius = 1;
    private Boolean retailerFound = false;
    private String retailerFoundID;

    GeoQuery geoQuery;
    private void getClosestRetailer(){
        DatabaseReference retailerLocation = FirebaseDatabase.getInstance().getReference().child("Maps").child("RetailerAvailable");

        GeoFire geoFire = new GeoFire(retailerLocation);
        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                if (!retailerFound && requestBol) {
                    retailerFound = true;
                    retailerFoundID = key;

                    DatabaseReference retailerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(retailerFoundID);
                    String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    HashMap map = new HashMap();
                    map.put("customerRideId", customerId);
                    retailerRef.updateChildren(map);

                    getRetailerLocation();
                    calluberrequestbtn.setText("මුදලාලිව හොයමින් ඉන්නේ....");
                }


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!retailerFound) {
                    radius++;
                    getClosestRetailer();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    DatabaseReference retailerLocationRef;
    private Marker mRetailerMarker;
    private ValueEventListener retailerLocationRefListener;
    private void getRetailerLocation(){

        retailerLocationRef = FirebaseDatabase.getInstance().getReference().child("Maps").child("RetailerWorking").child(retailerFoundID).child("l");
        retailerLocationRefListener = retailerLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && requestBol){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;

                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng retailerLatLng = new LatLng(locationLat,locationLng);
                    if(mRetailerMarker != null){
                        mRetailerMarker.remove();
                    }
                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(retailerLatLng.latitude);
                    loc2.setLongitude(retailerLatLng.longitude);

                    float distance = loc1.distanceTo(loc2);


                    if (distance<100){
                        calluberrequestbtn.setText("මුදලාලි ලග ඉන්නේ....");
                    }else{
                        calluberrequestbtn.setText("මුදලාලිව හොයා ගත්තා..." + String.valueOf(distance) + "m");
                    }

                    mRetailerMarker = mMap.addMarker(new MarkerOptions().position(retailerLatLng).title("ඔයාගේ මුදලාලි").icon(BitmapDescriptorFactory.fromResource(R.mipmap.store)));


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void customerlogoutbtn(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent logout = new Intent(CustomerMapActivity.this, Farmers_Home.class);
        startActivity(logout);
        finish();
        return;
    }

    public void customersettingbtn(View view) {
    }
}
