package com.example.alwaqt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity {
    EditText et1;
    Button btnSendCoor;
    ImageButton ibHelp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        et1 = findViewById(R.id.et1);
        btnSendCoor = findViewById(R.id.btnSendCoor);
        ibHelp1 = findViewById(R.id.ibHelp1);

        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSendCoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et1.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity3.this,"Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibHelp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Please allow this application to access your location, so that we can get your accurate coordinates" +
                        ". You can try entering the location manually also[format: <lat>,<lon>].";
                new AlertDialog.Builder(MainActivity3.this)
                        .setTitle("Help")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .show();
            }
        });
    }
}