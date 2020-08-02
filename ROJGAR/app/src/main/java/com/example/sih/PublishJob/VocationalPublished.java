package com.example.sih.PublishJob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VocationalPublished extends AppCompatActivity {


    private TextView status;
    Button createJob;
    DatabaseReference reff;
    RecyclerView recyclerView;
    ArrayList<vocationalPost> list;
    vAdapter adapter;
    int i, j;
    String phone, S, M, check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocational_published);
        SharedPreferences preferences1 = getSharedPreferences(S,i);
        phone = preferences1.getString("Phone","");
        SharedPreferences preferences2 = getSharedPreferences(M,j);
        check = preferences2.getString("Lang","Eng");
        createJob = findViewById(R.id.createJob);
        status = findViewById(R.id.status);
        recyclerView = findViewById(R.id.jobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<vocationalPost>();

        if(!check.equals(getResources().getString(R.string.english))){
            createJob.setText("अपनी नोकरी बनाओ");
        }

        reff = FirebaseDatabase.getInstance().getReference().child("Atmanirbhar");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    dataSnapshot.child(phone).getValue().toString();
                    if(check.equals(getResources().getString(R.string.english))) {
                        status.setText("You have created the following jobs:");
                    } else {
                        status.setText("आपने निम्नलिखित नौकारिया बनाई है:");
                    }
                } catch (Exception e){
                    if(check.equals(getResources().getString(R.string.english))) {
                        status.setText("You have not created any job yet");
                    } else {
                        status.setText("आपने अभी तक कोई नौकरी नहीं बनाई है");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(VocationalPublished.this, "Something went wrong", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VocationalPublished.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Atmanirbhar").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list = new ArrayList<jobPost>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    vocationalPost v = dataSnapshot1.getValue(vocationalPost.class);
                    list.add(v);
                }
                adapter = new vAdapter(VocationalPublished.this, list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(VocationalPublished.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VocationalPublished.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                }
            }
        });

        createJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VocationalPublished.this, jobDetails.class);
                startActivity(intent);
            }
        });

    }
}
