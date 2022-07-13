package com.example.alwaqt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity5 extends AppCompatActivity {

    public static String PREFS_NAME = "MyPrefsFile";
    public static String PREFS_ID = "";
    private Button btnRow;
    TextView tvName,tvDate,tvFajrAzaan,tvFajrNamaz,zuharAzaan,zuharNamaz,asrAzaan,asrNamaz,maghribAzaan,maghribNamaz,ishaAzaan,ishaNamaz,sunrise,jummah,sunset;
    ImageView iv5,iv6;
    ImageButton nearby2,search2,location2,help2,setting;
    //calculate hijri date
    UmmalquraCalendar cal = new UmmalquraCalendar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        tvFajrAzaan = findViewById(R.id.tvFajrAzaan);
        tvFajrNamaz = findViewById(R.id.tvFajrNamaz);
        zuharAzaan = findViewById(R.id.zuharAzaan);
        zuharNamaz = findViewById(R.id.zuharNamaz);
        asrAzaan = findViewById(R.id.asrAzaan);
        asrNamaz = findViewById(R.id.asrNamaz);
        maghribAzaan = findViewById(R.id.maghribAzaan);
        maghribNamaz = findViewById(R.id.maghribNamaz);
        ishaAzaan = findViewById(R.id.ishaAzaan);
        ishaNamaz = findViewById(R.id.ishaNamaz);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        jummah = findViewById(R.id.jummah);
        nearby2 = findViewById(R.id.nearby2);
        search2 = findViewById(R.id.search2);
        location2 = findViewById(R.id.location2);
        help2 = findViewById(R.id.help2);
        setting = findViewById(R.id.setting);
        iv5 = findViewById(R.id.iv5);
        iv6 = findViewById(R.id.iv6);
        //recieve MosqueID
        Intent intent = getIntent();
        String mosque_id = intent.getStringExtra("MosqueID_");



        SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity5.PREFS_NAME,0);
        boolean hasSetMosque1 = sharedPreferences1.getBoolean("hasSetMosque",false);
        if(!hasSetMosque1)
        {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("mosque");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (dataSnapshot.child(mosque_id).exists()) {
                        //String post = dataSnapshot.child(i_).child("location").getValue(String.class);
                        Toast.makeText(MainActivity5.this, "Mosque Set Successfully", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity5.PREFS_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean("hasSetMosque", true);
                        editor.putString("Mosque_ID",mosque_id);
                        editor.commit();
                        //MainActivity.fa.finish();
                        //
                        setMosque(mosque_id);
                        //
                        MainActivity.fa.finish();
                        //Intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    } else {
                        Toast.makeText(MainActivity5.this, "Please enter correct Mosque ID", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity5.this,MainActivity.class);
                        startActivity(intent);
                        MainActivity5.this.finish();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        //tvName = findViewById(R.id.tvName);
        SharedPreferences sharedPreferences2 = getSharedPreferences(MainActivity5.PREFS_NAME,0);
        String idFinal = sharedPreferences2.getString("Mosque_ID",mosque_id);
        if(hasSetMosque1){
           setMosque(idFinal);
        }

        //
        nearby2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity5.this,
                        MainActivity4.class);
                startActivity(intent2);
            }
        });
        search2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity5.this,
                        MainActivity2.class);
                startActivity(intent2);
            }
        });
        location2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity5.this,
                        MainActivity3.class);
                startActivity(intent2);
            }
        });
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Context context = getApplicationContext();
                CharSequence text = "Currently No announcement is made by your Mosque!!";
               /* int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();*/
                new AlertDialog.Builder(MainActivity5.this)
                        .setTitle("Announcements")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_info_details)
                        .show();
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Context context = getApplicationContext();
                CharSequence text = "Unable to fetch Mosque Details!!\n Please reach this page after some time";
               /* int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();*/
                new AlertDialog.Builder(MainActivity5.this)
                        .setTitle("Mosque Details")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_gallery)
                        .show();
            }
        });



    }
    private String getMonthName(int arg){
        switch (arg){
            case 1:
                //return "ٱلْمُحَرَّم";
                return "Muḥarram";
            case 2:
                //return "صَفَر";
                return "Ṣafar";
            case 3:
                //return "رَبِيع ٱلْأَوَّل";
                return "Rabī‘ al-awwal";
            case 4:
                //return "رَبِيع ٱلْآخِر";
                return "Rabī‘ ath-thānī";
            case 5:
                //return "جُمَادَىٰ ٱلْأُولَىٰ";
                return "Jumādá al-ūlá";
            case 6:
                //return "جُمَادَىٰ ٱلْآخِرَة";
                return "Jumādá al-ākhirah";
            case 7:
                //return "رَجَب";
                return "Rajab";
            case 8:
                //return "شَعْبَان";
                return "Sha‘bān";
            case 9:
                //return "رَمَضَان";
                return "Ramaḍān";
            case 10:
                //return "شَوَّال";
                return "Shawwāl";
            case 11:
                //return "ذُو ٱلْقَعْدَة";
                return "Dhū al-Qa‘dah";
            case 12:
                //return "ذُو ٱلْحِجَّة";
                return "Dhū al-Ḥijjah";
        }
        return "ذُو ٱلْحِجَّة";
    }
    private void setMosque(String id){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("mosque");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (dataSnapshot.child(id).exists()) {

                        String mosque_name = dataSnapshot.child(id).child("mosqueName").getValue(String.class);
                        tvName.setText(mosque_name);
                        //calculate date
                        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                        tvDate.setText(date.toString());
                        tvDate.append("\n");
                        tvDate.append(""+cal.get(Calendar.DAY_OF_MONTH));
                        String month = getMonthName(cal.get(Calendar.MONTH));
                        tvDate.append(" / "+month);
                        tvDate.append(" / "+cal.get(Calendar.YEAR));
                        //
                        String fajr_namaz = dataSnapshot.child(id).child("fajrNamaaz").getValue(String.class);
                        String fajr_azaan = dataSnapshot.child(id).child("fajrAzaan").getValue(String.class);
                        tvFajrAzaan.setText(fajr_azaan);
                        tvFajrNamaz.setText(fajr_namaz);

                        String zuhar_namaz = dataSnapshot.child(id).child("zuharNamaaz").getValue(String.class);
                        String zuhar_azaan = dataSnapshot.child(id).child("zuharAzaan").getValue(String.class);
                        zuharAzaan.setText(zuhar_azaan);
                        zuharNamaz.setText(zuhar_namaz);

                        String asr_namaz = dataSnapshot.child(id).child("asrNamaaz").getValue(String.class);
                        String asr_azaan = dataSnapshot.child(id).child("asrAzaan").getValue(String.class);
                        asrAzaan.setText(asr_azaan);
                        asrNamaz.setText(asr_namaz);

                        String maghrib_namaz = dataSnapshot.child(id).child("maghribNamaaz").getValue(String.class);
                        String maghrib_azaan = dataSnapshot.child(id).child("maghribAzaan").getValue(String.class);
                        maghribAzaan.setText(maghrib_azaan);
                        maghribNamaz.setText(maghrib_namaz);

                        String isha_namaz = dataSnapshot.child(id).child("ishaNamaaz").getValue(String.class);
                        String isha_azaan = dataSnapshot.child(id).child("ishaAzaan").getValue(String.class);
                        ishaAzaan.setText(isha_azaan);
                        ishaNamaz.setText(isha_namaz);

                        String sunrise_ = dataSnapshot.child(id).child("sunrise").getValue(String.class);
                        String sunset_ = dataSnapshot.child(id).child("sunset").getValue(String.class);
                        String jummah_ = dataSnapshot.child(id).child("jummah").getValue(String.class);
                        sunrise.setText("Sunrise\n"+sunrise_);
                        sunset.setText("Sunset\n"+sunset_);
                        jummah.setText("Jummah\n"+jummah_);
                    } else {
                        Toast.makeText(MainActivity5.this, "We tried far and away but sorry we are Unable to fetch Mosque details currently!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}