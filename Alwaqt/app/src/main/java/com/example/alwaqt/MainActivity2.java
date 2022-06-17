package com.example.alwaqt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class MainActivity2 extends AppCompatActivity {
    Button btnFind;
    ImageButton ibHelp;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    //String[] Mosque_Names = {"--SELECT YOUR MOSQUE#-", "Jamia Masjid Srinagar #6000", "Jamia Masjid Awantipora #6001","Hanafi Masjid Srinagar #6002"};
    ArrayList<String> Mosque_Names = new ArrayList<String>();
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

        recyclerView = (RecyclerView)findViewById(R.id.rv1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Mosque_Names.add("--SELECT YOUR MOSQUE#-"); //to add first entry which says select your mosque
        btnFind = findViewById(R.id.btnFind);
        ibHelp = findViewById(R.id.ibHelp);
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("mosque"),MainModel.class)
                        .build();
        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        FirebaseDatabase.getInstance().getReference().child("mosque").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // int i=1;
                for (DataSnapshot snapshot_:snapshot.getChildren()){
                    String tmp = snapshot_.child("mosqueName").getValue().toString();
                    String tmp_ = snapshot_.child("mosqueID").getValue().toString();
                    Mosque_Names.add(tmp+" #"+tmp_);
                    //i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                        " appropriate Mosque based on your location. You just need to select the Mosque and you will be taken back to previous page." ;
                new AlertDialog.Builder(MainActivity2.this)
                        .setTitle("Help")
                        .setMessage(text)
                        .setIcon(android.R.drawable.ic_menu_help)
                        .show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private void textSearch(String str)
    {
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("mosque").orderByChild("mosqueName").startAt(str).endAt(str+"~"),MainModel.class)
                        .build();
        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);

    }
}