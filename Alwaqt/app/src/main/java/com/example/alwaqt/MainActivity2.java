package com.example.alwaqt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    EditText edID;
    Button btnFind;
    ImageButton ibHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        edID = findViewById(R.id.edID);
        btnFind = findViewById(R.id.btnFind);
        ibHelp = findViewById(R.id.ibHelp);
        edID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edID.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity2.this,"Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("edID_",edID.getText().toString().trim());

                    setResult(RESULT_OK,intent);
                    MainActivity2.this.finish();
                }

            }
        });

        ibHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence text = "Enter the name of mosque, or name of place in given input field, and many mosques will appear in drop down menu. Please select the" +
                        " appropriate Mosque based on your location. You just need to select the Mosque and you will be taken back to previous page.";
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Help")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .show();
            }
        });

    }
}