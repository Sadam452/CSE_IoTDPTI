package com.example.alwaqt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity {
    Button btnFind;
    ImageButton ibHelp;
   // Spinner edID = (Spinner) findViewById(R.id.spinner2);
  // @Override
   /*public boolean onCreateOptionsMenu(Menu menu) {
      /* Spinner edID = (Spinner) findViewById(R.id.spinner2);
       ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.Mosque_Names, android.R.layout.simple_spinner_item);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       edID.setAdapter(adapter);
       return super.onCreateOptionsMenu(menu);
   }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnFind = findViewById(R.id.btnFind);
        ibHelp = findViewById(R.id.ibHelp);
        String[] Mosque_Names = {"--SELECT YOUR MOSQUE#-", "Jamia Masjid Srinagar #6000", "Jamia Masjid Awantipora #6001","Hanafi Masjid Srinagar #6002"};
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, Mosque_Names);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        // add adapter to spinner
        spinner.setAdapter(stringArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // put code which recognize a selected element
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String arr[];
                arr = new String[2];
                arr = spinner.getSelectedItem().toString().split("#");
                if(spinner.getSelectedItem().toString().isEmpty() || arr[1].trim().length()<=2){
                    Toast.makeText(MainActivity2.this,"Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent();
                    //if(arr[1].trim() !=" "){
                    intent.putExtra("edID_",arr[1].trim());

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