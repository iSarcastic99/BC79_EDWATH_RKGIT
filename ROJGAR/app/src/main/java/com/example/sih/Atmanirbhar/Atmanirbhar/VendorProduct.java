package com.example.sih.Atmanirbhar.Atmanirbhar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VendorProduct extends AppCompatActivity {
    EditText ProductName, Description, Price;
    Button Post;
    DatabaseReference reff, reff1, reff2;
    String phone, S;
    Boolean isUploaded = false;
    long count1;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "");
        setContentView(R.layout.activity_vendor_product);
        ProductName = findViewById(R.id.product_name);
        Description = findViewById(R.id.description);
        Price = findViewById(R.id.price);
        Post = findViewById(R.id.post);
        reff = FirebaseDatabase.getInstance().getReference();
        reff1 = FirebaseDatabase.getInstance().getReference().child("Atmanirbhar");
        reff2 = FirebaseDatabase.getInstance().getReference().child("Products");
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count1 = snapshot.child("Atmanirbhar").getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ProductName.getText().toString().equals("") || Description.getText().toString().equals("") || Price.getText().toString().equals("" )){
                    Toast.makeText(VendorProduct.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    reff1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long count = snapshot.child(phone).getChildrenCount();
                            if(!isUploaded) {
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("ProductName").setValue(ProductName.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Description").setValue(Description.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Price").setValue(Price.getText().toString());
                                reff.child("Atmanirbhar").child(phone).child(Long.toString(count + 1)).child("Phone").setValue(phone);
                                reff.child("Products").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("ProductName").setValue(ProductName.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("Description").setValue(Description.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("Price").setValue(Price.getText().toString());
                                reff.child("Products").child("Atmanirbhar").child(Long.toString(count1 + 1)).child("Phone").setValue(phone);
                                Toast.makeText(VendorProduct.this, "Product added successully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VendorProduct.this, MainActivity.class);
                                startActivity(intent);
                                isUploaded = true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }
}