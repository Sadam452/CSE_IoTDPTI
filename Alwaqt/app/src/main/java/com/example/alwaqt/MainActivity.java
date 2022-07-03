package com.example.alwaqt;
//import javax.swing.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText MosqueId;
    Button btnSubmit,btnSetLoc;
    TextView tvSearchId;
    TextView tvTime;
    ImageView ivHelp;
    final int GET_ID=1;
    String val;
    public static MainActivity fa;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_ID){
            if(resultCode == RESULT_OK){
                val = data.getStringExtra("edID_");
               MosqueId.setText(val);
            }
        }
    }

    private static int SPLASH_TIME_OUT=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fa = this;
        MosqueId = findViewById(R.id.MosqueId);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSetLoc = findViewById(R.id.btnSetLoc);
        tvSearchId = findViewById(R.id.tvSearchId);
        tvTime = findViewById(R.id.tvTime);
        ivHelp = findViewById(R.id.ivHelp);
        //direct user on home page if mosque is set already
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity5.PREFS_NAME,0);
                boolean hasSetMosque = sharedPreferences.getBoolean("hasSetMosque",false);
                if(hasSetMosque){
                    Intent intent = new Intent(MainActivity.this,MainActivity5.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
        //
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MosqueId.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please fill all the required fields to proceed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this,
                            MainActivity5.class);
                    intent.putExtra("MosqueID_",MosqueId.getText().toString());
                    startActivity(intent);
                }
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        MainActivity4.class);
                startActivityForResult(intent,GET_ID);
            }
        });

        tvSearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        MainActivity2.class);
                startActivityForResult(intent,GET_ID);
            }
        });

        btnSetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        MainActivity3.class);
                startActivity(intent);
            }
        });

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Context context = getApplicationContext();
                CharSequence text = "When any Mosque is registered with our database, a unique ID(Mosque ID) is generated for Mosque on successful registration." +
                        " Enter that ID in the input field given. If the mosque is registered with us, and you don't know its ID, then use the search ID feature, then select" +
                        " your Mosque from the list, & its ID will be automatically pasted in 'Enter Your Mosque Id' field. ";
               /* int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();*/
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Help")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .show();
            }
        });


    }
}