package com.example.sih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Freelancing extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView uphone, uname, jobType;
    Boolean English = true;
    String lang, M, J, check, S, phone, u_name, path, activity, domain;
    int j, i, x;
    DrawerLayout drawer;
    ImageView profile;
    NavigationView navigationView;
    StorageReference mStorageReference;
    ActionBarDrawerToggle t;
    Menu menu1, menu2;
    MenuItem Gov, Non_Gov, Tender, Free_Lancing;
    DatabaseReference reff, reff3, reff4;
    RecyclerView freelance;
    ArrayList<job_data> details;
    jobs_adapter govAdapter;
    ProgressDialog pd;
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Freelancing");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone= preferences.getString("Phone","");
        path = preferences.getString("path", "");
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences2 = getSharedPreferences(J,x);
        activity = preferences2.getString("Activity","");
        domain = preferences.getString("Domain", "");
        setContentView(R.layout.activity_free__lancing);

        freelance = findViewById(R.id.freelance);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_2);
        View view =getSupportActionBar().getCustomView();

        freelance.setLayoutManager(new LinearLayoutManager(this));
        details = new ArrayList<>();

        jobType = view.findViewById(R.id.jobsType);
        if (activity.equals("Government")){
            if(check.equals(getResources().getString(R.string.english))) {
                jobType.setText("Government Jobs");
            } else{
                jobType.setText(R.string.government_jobs1);
            }
        }

        else if (activity.equals("Private")){
            if(check.equals(getResources().getString(R.string.english))) {
                jobType.setText("Private Jobs");
            } else{
                jobType.setText(R.string.non_government_jobs1);
            }
        }

        else if (activity.equals("Freelancing")){
            if(check.equals(getResources().getString(R.string.english))) {
                jobType.setText("Freelancing");
            } else{
                jobType.setText(R.string.freelancing1);
            }
        }

        else {
            if(check.equals(getResources().getString(R.string.english))) {
                jobType.setText("Tenders");
            } else{
                jobType.setText(R.string.tenders1);
            }
        }
        final SearchView mySearchView = view.findViewById(R.id.mySearchView);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        mySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                govAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                },
                3000
        );

        pd = new ProgressDialog(Freelancing.this);

        if (check.equals("Eng")) {
            pd.setMessage("Fetching data");
        } else {
            pd.setMessage("डेटा लाया जा रहा है");
        }

        pd.show();

        try {

            reff3 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("All Jobs").child("Freelancing");
            reff3.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    size = (int) dataSnapshot.getChildrenCount();

                    for (int k = 0; k < size; k++) {

                        String i = Integer.toString(k);
                        reff4 = FirebaseDatabase.getInstance().getReference().child("Jobs Revolution").child("All Jobs").child("Freelancing").child(i);
                        reff4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                job_data d = snapshot.getValue(job_data.class);
                                details.add(d);
                                govAdapter = new jobs_adapter(Freelancing.this, details);
                                freelance.setAdapter(govAdapter);


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                if(check.equals(getResources().getString(R.string.english))){
                                    Toast.makeText(Freelancing.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Freelancing.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if(check.equals(getResources().getString(R.string.english))){
                        Toast.makeText(Freelancing.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Freelancing.this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 3000);

        drawer = findViewById(R.id.draw_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);
        navigationView.setNavigationItemSelectedListener(this);
        t = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(t);
        t.syncState();
        menu2 = navigationView.getMenu();
        Gov = menu2.findItem(R.id.government);
        Non_Gov = menu2.findItem(R.id.non_government);
        Tender = menu2.findItem(R.id.tenders);
        Free_Lancing = menu2.findItem(R.id.free_lancing);
        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        profile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        loadImageFromStorage(path);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Freelancing.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Freelancing.this, Profile.class);
                startActivity(profileIntent);
            }
        });
        uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(Freelancing.this, Profile.class);
                startActivity(profileIntent);
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_name = dataSnapshot.child("Username").getValue().toString();
                phone = dataSnapshot.child("Phone").getValue().toString();
                mStorageReference = FirebaseStorage.getInstance().getReference().child(phone).child("Profile Picture");
                try {
                    final long ONE_MEGABYTE = 1024 * 1024;
                    mStorageReference.getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    DisplayMetrics dm = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    profile.setMinimumHeight(dm.heightPixels);
                                    profile.setMinimumWidth(dm.widthPixels);
                                    profile.setImageBitmap(bm);
                                    path = saveToInternalStorage(bm);
                                    SharedPreferences.Editor editor1 = getSharedPreferences(S,i).edit();
                                    editor1.putString("path", path);
                                    editor1.apply();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                } catch(Exception e){

                }
                uphone.setText(phone);
                uname.setText(decryptUsername(u_name));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Hin")) {
                    Toast.makeText(Freelancing.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Freelancing.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(check.equals("Hin")){
            NavHin();
            toHin();
        } else{
            NavEng();
            toEng();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.government:
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Government");
                editor.apply();
                Intent intent1 = new Intent(Freelancing.this,Government.class);
                startActivity(intent1);
                break;
            case R.id.non_government:
                SharedPreferences.Editor editor1 = getSharedPreferences(J,x).edit();
                editor1.putString("Activity", "Private");
                editor1.apply();
                Intent intent = new Intent(Freelancing.this, Private.class);
                startActivity(intent);
                break;
            case R.id.tenders:
                SharedPreferences.Editor editor2 = getSharedPreferences(J,x).edit();
                editor2.putString("Activity", "Tender");
                editor2.apply();
                Intent intent5 = new Intent(Freelancing.this, Tenders.class);
                startActivity(intent5);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu1 = menu;
        getMenuInflater().inflate(R.menu.option_menu,menu);
        if(check.equals("Hin")){
            optionHin();
        }else {
            optionEng();
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(t.onOptionsItemSelected(menuItem))
            return true;
        switch (menuItem.getItemId()) {
            case R.id.switch1:
                if (English) {
                    toHin();
                    NavHin();
                    optionHin();
                } else {
                    toEng();
                    NavEng();
                    optionEng();
                }
                return true;
            case R.id.logout: {
                Intent intent = new Intent(Freelancing.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;
            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(Freelancing.this, Rating.class);
                startActivity(rateIntent);
                return true;

            case R.id.go_to_profile:
                Intent profileIntent = new Intent(Freelancing.this, Profile.class);
                startActivity(profileIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void toEng(){
        getSupportActionBar().setTitle("Freelancing");
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        getSupportActionBar().setTitle(R.string.freelancing1);
        English = false;
        lang = "Hin";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }
    public void optionHin(){
        setOptionTitle(R.id.switch1, getResources().getString(R.string.switch1));
        setOptionTitle(R.id.rate_us, getResources().getString(R.string.rate1));
        setOptionTitle(R.id.logout, getResources().getString(R.string.logout1));
        setOptionTitle(R.id.contact_us, getResources().getString(R.string.contact_us1));
        setOptionTitle(R.id.go_to_profile, getResources().getString(R.string.go_to_profile1));
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
        setOptionTitle(R.id.go_to_profile, "Go To Profile");
    }
    public void NavHin(){
        Gov.setTitle("                  सरकारी नौकरियों");
        Non_Gov.setTitle("                  प्राइवेट नौकरी");
        Tender.setTitle("                  निविदाएं");
        Free_Lancing.setTitle("                  फ़्रीलांसिंग");
    }
    public void NavEng(){
        Gov.setTitle("                  Government Jobs");
        Non_Gov.setTitle("                  Private Jobs");
        Tender.setTitle("                  Tenders");
        Free_Lancing.setTitle("                  Freelancing");
    }
    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu1.findItem(id);
        item.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer((GravityCompat.START));
        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        if(check.equals("Hin")){
            English = false;
            NavHin();
            toHin();
            try{
                optionHin();
            } catch(Exception e){
            }
        } else{
            English = true;
            NavEng();
            toEng();
            try{
                optionEng();
            } catch(Exception e){

            }
        }
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            profile.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile_picture", Context.MODE_PRIVATE);
        File mypath = new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public StringBuilder decryptUsername(String uname) {
        int pllen;
        StringBuilder sb = new StringBuilder();
        int ciplen = uname.length();
        String temp = Character.toString(uname.charAt(ciplen - 2));
        if (temp.matches("[a-z]+")) {
            pllen = Character.getNumericValue(uname.charAt(ciplen - 1));
            pllen -= 2;
        } else {
            String templen = uname.charAt(ciplen - 2) + Character.toString(uname.charAt(ciplen - 1));
            pllen = Integer.parseInt(templen);
            pllen -= 2;
        }
        String[] separated = uname.split("[a-zA-Z]");
        for (int i = 0; i < pllen; i++) {
            String splitted = separated[i];
            int split = Integer.parseInt(splitted);
            split = split + pllen + (2 * i);
            char pln = (char) split;
            sb.append(pln);
        }
        return sb;
    }

}