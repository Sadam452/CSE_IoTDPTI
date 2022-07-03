package com.example.alwaqt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
// To keep the specific class and all its properties and members.

public class MainActivity4 extends AppCompatActivity {
    LinearLayout layout;
    EditText getID;TextView nm;
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
        nm = findViewById(R.id.nearby);
        nm.setMovementMethod(new ScrollingMovementMethod());
        nm.setMovementMethod(LinkMovementMethod.getInstance());
        layout = findViewById(R.id.layout1);
        nm.setVisibility(View.GONE);
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
                    nm.setVisibility(View.GONE);
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
                                if (tmp2 > tmp - 0.1 && tmp2 < tmp + 0.1 && tmp3 > tmp1 - 0.1 && tmp3 < tmp1 + 0.1) {
                                    myList.add(i_);
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
                /*System.out.println(myList.size()+" ok");
                String a= myList.get(0);
                FirebaseRecyclerOptions<MainModel> options =
                        new FirebaseRecyclerOptions.Builder<MainModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("mosque").orderByChild("location")
                                        .startAt(myList.get(0)).endAt(myList.get(myList.size()-1)),MainModel.class).build();
                mainAdapter = new MainAdapter(options);
                mainAdapter.startListening();
                recyclerView.setAdapter(mainAdapter);*/
                    //new query
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference usersRef = db.child("mosque");
                    Query query = usersRef.orderByChild("location");

                    List<Task<DataSnapshot>> tasks = new ArrayList<>();
                    for (String locat : myList) {
                        tasks.add(query.equalTo(locat).get());
                    }
                    //new

                   // while(i<myList.size()) {
                    nm.setVisibility(View.VISIBLE);
                    nm.setText(myList.size()+" nearby Mosques found based on your current location!");
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (i=0;i<myList.size();i++) {
                                    String name_ = snapshot.child(myList.get(i)).child("mosqueName").getValue(String.class);
                                    String fajr = snapshot.child(myList.get(i)).child("fajrNamaaz").getValue(String.class);
                                    String zuhar = snapshot.child(myList.get(i)).child("zuharNamaaz").getValue(String.class);
                                    String asr = snapshot.child(myList.get(i)).child("asrNamaaz").getValue(String.class);
                                    String maghrib = snapshot.child(myList.get(i)).child("maghribNamaaz").getValue(String.class);
                                    String isha = snapshot.child(myList.get(i)).child("ishaNamaaz").getValue(String.class);
                                    String jummah = snapshot.child(myList.get(i)).child("jummah").getValue(String.class);
                                    String l = snapshot.child(myList.get(i)).child("location").getValue(String.class);

                                    nm.append("\n\n\n");
                                    nm.append(Html.fromHtml("Mosque Name :    <span style=\"color:#47b895;\">"+name_+"</span>"));
                                    nm.append("\nFajr Namaaz :       "+fajr);
                                    nm.append("\nZuhar Namaaz :    "+zuhar);
                                    nm.append("\nAsr Namaaz :        "+asr);
                                    nm.append("\nMaghrib Namaaz :"+maghrib);
                                    nm.append("\nIsha Namaaz :       "+isha);
                                    String href = "maps.google.com/?q="+l;
                                    //nm.append("\nLocation      :     <a href=\""+href+"\">Locate</a>");
                                    nm.append("\n");
                                    nm.append(Html.fromHtml("<a style=\"display:inline;\"href=\"http://maps.google.com/maps?q="+l+"\"> Locate Mosque on Map </a>"));
                                    nm.setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    //}
                    /*Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            //Do what you need to do with your list.
                            for (Object object : list) {
                                MainModel mm = ((DataSnapshot) object).getValue(MainModel.class);
                                if(mm != null) {
                                   // Log.d("TAG", mm.getLocation());
                                    System.out.println("location ="+mm.getLocation());
                                }
                               /* ValueEventListener eventListener = new ValueEventListener() {


                                        for(DataSnapshot ds : dataSnapshot.getChildren())
                                            for(DataSnapshot dSnapshot : ds.getChildren()) {
                                                MainModel streetClass = dSnapshot.getValue(MainModel.class);
                                                System.out.println("location="+streetClass.getLocation());
                                            }



                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                };
                            }
                        }
                    }); */
                }
                else {
                    nm.setText(myList.size()+"nearby Mosques found based on your current location!\n Please click on 'Locate Nearby Mosques' button again!");
                    //Toast.makeText(MainActivity4.this, "Please try finding the nearby mosques again, if you still got the same message then no nearby mosque uses our services. Thank you :)", Toast.LENGTH_SHORT).show();
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