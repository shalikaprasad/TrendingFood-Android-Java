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

public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    Button calluberrequestbtn;
    private Marker pickupMarker;
    private LatLng pickupLocation;

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

                        CustomerRef.child("Farmers").child(current_user_id).updateChildren(requestsMap)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            //Toast.makeText(CustomerMapActivity.this, "You can sell with this Retailer", Toast.LENGTH_SHORT).show();

                                        } else {
                                            //Toast.makeText(CustomerMapActivity.this, "You cannot sell with this Retailer", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        DatabaseReference reqlocation =CustomerRef.child("Farmers").child(current_user_id);
                        GeoFire geoReqlocation = new GeoFire(reqlocation);
                        geoReqlocation.setLocation("location", new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                        calluberrequestbtn.setText("Getting your Retailer....");

                    }else if(userCharacter.equals("Consumer")){

                        CustomerRef.child("Consumer").child(current_user_id).updateChildren(requestsMap)
                                .addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(CustomerMapActivity.this, "You can buy with this Retailer", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(CustomerMapActivity.this, "You cannot buy with this Retailer", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        DatabaseReference reqlocation =CustomerRef.child("Consumer").child(current_user_id);
                        GeoFire geoReqlocation = new GeoFire(reqlocation);
                        geoReqlocation.setLocation("location", new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));


                        calluberrequestbtn.setText("Getting your Retailer....");
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup)));

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
