package com.example.alwaqt;

import static android.os.Build.VERSION_CODES.M;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity4 extends AppCompatActivity {

    EditText getID;
    Button search,btnLocate;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    private LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    String loc="";
    int i,flag_=0;
    ArrayList<String> myList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getID = findViewById(R.id.getID);
        search = findViewById(R.id.search);
        btnLocate = findViewById(R.id.btnLocate);
        btnLocate.setEnabled(false);
        //for location
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(MainActivity4.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (!isGPSEnabled()) {
                    turnOnGPS();}
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            getLocation_();
        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getID.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity4.this, "Please fill the Mosque ID field", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseRecyclerOptions<MainModel> options =
                            new FirebaseRecyclerOptions.Builder<MainModel>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("mosque").orderByChild("mosqueID").equalTo(getID.getText().toString()),MainModel.class)
                                    .build();
                    mainAdapter = new MainAdapter(options);
                    recyclerView.setAdapter(mainAdapter);
                    mainAdapter.startListening();
                    FirebaseDatabase.getInstance().getReference().child("mosque").orderByChild("mosqueID").equalTo(getID.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue() == null){
                                Toast.makeText(MainActivity4.this, "Oops! it looks that the Mosque ID doesn't exist! Please enter correct ID", Toast.LENGTH_SHORT).show();
                            }

                           // Toast.makeText(MainActivity4.this, "O"+snapshot.getValue(String.class), Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity4.this, "Some error occured! Try again after few moments", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //code for detecting nearby mosques
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code to show nearby mosques according to current location contained in "loc"
               // Toast.makeText(MainActivity4.this, "Location = "+loc, Toast.LENGTH_SHORT).show();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("mosque");
                i=1000;
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String i_ = "" + i;
                            if (dataSnapshot.child(i_).exists()) {
                                String post = dataSnapshot.child(i_).child("location").getValue(String.class);
                                String[] latLon = post.split(",");
                                String[] latLon_ = loc.split(",");
                                double tmp = Double.parseDouble(latLon[0]);
                                double tmp1 = Double.parseDouble(latLon[1]);
                                double tmp2 = Double.parseDouble(latLon_[0]);
                                double tmp3 = Double.parseDouble(latLon_[1]);
                                if (tmp2 > tmp - 0.01 && tmp2 < tmp + 0.01 && tmp3 > tmp1 - 0.01 && tmp3 < tmp1 + 0.01) {
                                    myList.add(post);
                                }
//                                System.out.println(myList.size()+" 1 ="+myList.get(0));

                                //System.out.println(myList_.size());
                                i=i+1;
                            } else {
                                break;
                            }
                    }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //show nearby mosques here
                if(myList.size()>0){
                System.out.println(myList.size()+" ok");
                String a= myList.get(0);
                FirebaseRecyclerOptions<MainModel> options =
                        new FirebaseRecyclerOptions.Builder<MainModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("mosque").orderByChild("location")
                                        .startAt(myList.get(0)).endAt(myList.get(myList.size()-1)),MainModel.class).build();
                mainAdapter = new MainAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);
                }
                else {
                    Toast.makeText(MainActivity4.this, "Please try finding the nearby mosques again, if you still got the same message then no nearby mosque uses our services. Thank you :)", Toast.LENGTH_SHORT).show();
                }
                myList.clear();

            }

        });
    }

//////gps
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
    LocationServices.getFusedLocationProviderClient(MainActivity4.this)
            .requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(MainActivity4.this)
                            .removeLocationUpdates(this);
                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                        int index = locationResult.getLocations().size() - 1;
                        double latitude = locationResult.getLocations().get(index).getLatitude();
                        double longitude = locationResult.getLocations().get(index).getLongitude();
                        loc = latitude+","+longitude;
                        btnLocate.setEnabled(true);
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
                    Toast.makeText(MainActivity4.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(MainActivity4.this,
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


    @Override
    protected void onStop() {
        super.onStop();
//        if ()
//        mainAdapter.stopListening();
    }
}