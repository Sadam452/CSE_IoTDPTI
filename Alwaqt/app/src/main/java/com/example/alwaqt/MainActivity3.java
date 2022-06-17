package com.example.alwaqt;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://alwaqt-413bd-default-rtdb.firebaseio.com/");
    EditText et1;
    EditText mosqueId1;
    EditText adminID;
    EditText password;
    Button btnSendCoor;
    ImageButton ibHelp1;
    int flag=0;
    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        et1 = findViewById(R.id.et1);
        mosqueId1 = findViewById(R.id.mosqueId1);
        adminID = findViewById(R.id.adminID);
        password = findViewById(R.id.password);
        btnSendCoor = findViewById(R.id.btnSendCoor);
        ibHelp1 = findViewById(R.id.ibHelp1);
        et1.setEnabled(false); //so that location can be entered automatically-- user can not change this field
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity3.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (!isGPSEnabled()) {
                    turnOnGPS();}
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            getLocation_();
        }
        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSendCoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mosque_id = mosqueId1.getText().toString();
                final String coordinates = et1.getText().toString();
                final String admin_id = adminID.getText().toString();
                final String pass = password.getText().toString();
                if (et1.getText().toString().isEmpty()) {
                    getLocation_();
                }
                if (et1.getText().toString().isEmpty() || mosqueId1.getText().toString().isEmpty() || adminID.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity3.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("mosque").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if mosque with given id exists
                            if (snapshot.hasChild(mosque_id)){
                                //get admin id and password
                                final String getPassword = snapshot.child(mosque_id).child("password").getValue(String.class);
                                final String getAdminId = snapshot.child(mosque_id).child("adminID").getValue(String.class);

                                if (getPassword.equals(pass) && getAdminId.equals(admin_id)){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("location", coordinates);
                                    databaseReference.child("mosque").child(mosque_id).updateChildren(map);
                                    Toast.makeText(MainActivity3.this, "Location has been successfully updated!", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MainActivity3.this, "Admin ID or Password is incorrect!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainActivity3.this, "Mosque ID doesn't exist!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
            }


            }
        });

        ibHelp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Please allow this application to access your location, so that we can get your accurate coordinates" +
                        ". Enter the Admin ID & password of Mosque Admin with the Mosque ID.";
                new AlertDialog.Builder(MainActivity3.this)
                        .setTitle("Help")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .show();
            }
        });
    }

    private void getLocation_() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity3.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity3.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int index = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(index).getLatitude();
                            double longitude = locationResult.getLocations().get(index).getLongitude();
                            et1.setText(latitude+","+longitude);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void turnOnGPS() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(MainActivity3.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity3.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }


    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;
    }

}