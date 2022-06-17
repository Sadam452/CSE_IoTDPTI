package com.example.alwaqt;

import static android.os.Build.VERSION_CODES.M;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Path;

public class MainActivity4 extends AppCompatActivity {

    EditText getID;
    Button search;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getID = findViewById(R.id.getID);
        search = findViewById(R.id.search);


//        mosqueName = findViewById(R.id.mosqueName);
//        namaaz1 = findViewById(R.id.namaaz1);
//        namaaz2 = findViewById(R.id.namaaz2);
//        namaaz3 = findViewById(R.id.namaaz3);
//        namaaz4 = findViewById(R.id.namaaz4);
//        namaaz5 = findViewById(R.id.namaaz5);
//        mosqueName.setVisibility(View.GONE);
//        namaaz1.setVisibility(View.GONE);
//        namaaz2.setVisibility(View.GONE);
//        namaaz3.setVisibility(View.GONE);
//        namaaz4.setVisibility(View.GONE);
//        namaaz5.setVisibility(View.GONE);
//        mosqueName.setEnabled(false);
//        namaaz1.setEnabled(false);namaaz2.setEnabled(false);namaaz3.setEnabled(false);namaaz4.setEnabled(false);namaaz5.setEnabled(false);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getID.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity4.this, "Please fill the Mosque ID field", Toast.LENGTH_SHORT).show();
                }
                else{
//                    mosqueName.setVisibility(View.VISIBLE);
//                    namaaz1.setVisibility(View.VISIBLE);
//                    namaaz2.setVisibility(View.VISIBLE);
//                    namaaz3.setVisibility(View.VISIBLE);
//                    namaaz4.setVisibility(View.VISIBLE);
//                    namaaz5.setVisibility(View.VISIBLE);
                    //@Override
                    //protected void onStart() {
                     //   super.onStart();
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
    }



    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}