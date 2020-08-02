package com.example.sih.PublishJob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVocational extends AppCompatActivity {
    EditText JobName, Description, NumberOfDays;
    Button Post;
    DatabaseReference reff;
    String phone, S;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "");
        setContentView(R.layout.activity_add_vocational);
        JobName = findViewById(R.id.job_name);
        Description = findViewById(R.id.description);
        NumberOfDays = findViewById(R.id.number_of_days);
        Post = findViewById(R.id.post);
        reff = FirebaseDatabase.getInstance().getReference();

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JobName.getText().toString().equals("") || Description.getText().toString().equals("") || NumberOfDays.getText().toString().equals("")){
                    Toast.makeText(AddVocational.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    reff.child("Atmanirbhar").child(phone).child("JobName").setValue(JobName.getText().toString().trim());
                    reff.child("Atmanirbhar").child(phone).child("Description").setValue(Description.getText().toString().trim());
                    reff.child("Atmanirbhar").child(phone).child("Days").setValue(NumberOfDays.getText().toString().trim());
                    reff.child("Atmanirbhar").child(phone).child("Phone").setValue(phone);
                    Toast.makeText(AddVocational.this, "Job added successully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddVocational.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}