package com.example.sih.chatApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.Registration.Login;
import com.example.sih.Jobs.Non_Government;
import com.example.sih.R;
import com.example.sih.Jobs.Tenders;
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

import java.util.ArrayList;

public class User_List extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView uphone, uname, Premium, Days, Title;
    Boolean English = true;
    String lang, M, J, check, S, phone, u_name, path, days, isPremium, activity;
    int j, i, x, count = 0;
    DrawerLayout drawer;
    ImageView profile, crown;
    NavigationView navigationView;
    StorageReference mStorageReference;
    ActionBarDrawerToggle t;
    Menu menu1, menu2;
    MenuItem Gov, Non_Gov, Tender, Free_Lancing, GetPremium, chat, topJobs, publishJob;
    DatabaseReference reff, reff1;
    RecyclerView users;
    ArrayList<usercardview> details;
    User_Adapter userAdapter;
    ProgressDialog pd;
    int size;
    Boolean isRegistered = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getSharedPreferences(S, i);
        phone = preferences.getString("Phone", "");
        path = preferences.getString("path", "");
        isPremium = preferences.getString("isPremium", "No");
        days = preferences.getString("remainingDays", "0");
        SharedPreferences preferences1 = getSharedPreferences(M, j);
        check = preferences1.getString("Lang", "Eng");
        SharedPreferences preferences2 = getSharedPreferences(J,x);
        activity = preferences2.getString("Activity","");
        setContentView(R.layout.activity_user__list);
        users = findViewById(R.id.users);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.chat_action_bar);
        View view =getSupportActionBar().getCustomView();
        Title = view.findViewById(R.id.users);

        users.setLayoutManager(new LinearLayoutManager(this));
        details = new ArrayList<>();

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
                                userAdapter.getFilter().filter(newText);
                                return false;
                            }
                        });
                    }
                },
                3000
        );

        reff = FirebaseDatabase.getInstance().getReference().child("Users");
        pd = new ProgressDialog(User_List.this);

        if (check.equals("Eng")) {
            pd.setMessage("Fetching data");
        } else {
            pd.setMessage("डेटा लाया जा रहा है");
        }

        pd.show();

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(count == 0) {
                    size = (int) dataSnapshot.getChildrenCount();
                    for (final DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        reff1 = FirebaseDatabase.getInstance().getReference().child("Users").child(childDataSnapshot.getKey());
                        reff1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                usercardview d = snapshot.getValue(usercardview.class);
                                if (!(phone.equals(childDataSnapshot.getKey()))) {
                                    details.add(d);
                                    userAdapter = new User_Adapter(details, User_List.this);
                                    users.setAdapter(userAdapter);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                if (check.equals("Eng")) {
                                    Toast.makeText(User_List.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(User_List.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    count++;
                } else {
                    recreate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(check.equals("Eng")) {
                    Toast.makeText(User_List.this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(User_List.this, "कृपया अपने इंटरनेट कनेक्शन की जाँच करें", Toast.LENGTH_SHORT).show();
                }

            }

        });

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
        GetPremium = menu2.findItem(R.id.premium);
        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        profile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        Premium = navigationView.getHeaderView(0).findViewById(R.id.premium);
        chat = menu2.findItem(R.id.chat);
        topJobs = menu2.findItem(R.id.topJobs);
        publishJob = menu2.findItem(R.id.publish);
        Days = navigationView.getHeaderView(0).findViewById(R.id.days);
        crown = navigationView.getHeaderView(0).findViewById(R.id.crownimage);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(User_List.this, ContactsContract.Profile.class);
                startActivity(profileIntent);
            }
        });
        uphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(User_List.this, ContactsContract.Profile.class);
                startActivity(profileIntent);
            }
        });
        uname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(User_List.this, ContactsContract.Profile.class);
                startActivity(profileIntent);
            }
        });
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u_name = dataSnapshot.child("Username").getValue().toString();
                phone = dataSnapshot.child("Phone").getValue().toString();
                if (isPremium.equals("Yes")) {
                    if (check.equals("Hin")) {
                        Premium.setText("प्रीमियम");
                    }
                    Premium.setVisibility(View.VISIBLE);
                    crown.setVisibility(View.VISIBLE);
                    if (days.equals("1")) {
                        if (check.equals("Hin")) {
                            Days.setText(days + " दिन शेष");
                        } else {
                            Days.setText(days + " day remaining");
                        }
                    } else {
                        if (check.equals("Hin")) {
                            Days.setText(days + " दिन शेष");
                        } else {
                            Days.setText(days + " days remaining");
                        }
                    }
                    Days.setVisibility(View.VISIBLE);
                }
                String username = decryptUsername(u_name).toString();
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
                                    // path = saveToInternalStorage(bm);
                                    SharedPreferences.Editor editor1 = getSharedPreferences(S, i).edit();
                                    editor1.putString("path", path);
                                    editor1.apply();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                } catch (Exception e) {

                }
                uphone.setText(phone);
                uname.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (check.equals("Hin")) {
                    Toast.makeText(User_List.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(User_List.this, "There is some error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!check.equals(getResources().getString(R.string.english))) {
            NavHin();
            toHin();
        } else {
            NavEng();
            toEng();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.non_government:
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Private");
                editor.apply();
                Intent intent1 = new Intent(User_List.this, Non_Government.class);
                startActivity(intent1);
                break;
            case R.id.free_lancing:
                SharedPreferences.Editor editor1 = getSharedPreferences(J,x).edit();
                editor1.putString("Activity", "Freelancing");
                editor1.apply();
                Intent intent = new Intent(User_List.this, com.example.sih.Jobs.Free_Lancing.class);
                startActivity(intent);
                break;
            case R.id.tenders:
                SharedPreferences.Editor editor2 = getSharedPreferences(J,x).edit();
                editor2.putString("Activity", "Tender");
                editor2.apply();
                Intent intent5 = new Intent(User_List.this, Tenders.class);
                startActivity(intent5);
                break;
            case R.id.premium:
                Intent intent2 = new Intent(User_List.this, com.example.sih.Profile.Premium.class);
                startActivity(intent2);
                break;
            case R.id.chat:
                Intent intent6 = new Intent(User_List.this, com.example.sih.chatApp.User_List.class);
                startActivity(intent6);
                break;
            case R.id.publish:
                if (!isRegistered) {
                    Intent intent7 = new Intent(User_List.this, com.example.sih.PublishJob.CreateYourJob.class);
                    startActivity(intent7);
                }
                else{
                    Intent intent7 = new Intent(User_List.this, com.example.sih.PublishJob.jobsPublished.class);
                    startActivity(intent7);
                }
                break;
            case R.id.topJobs:
                Intent intent8 = new Intent(User_List.this, com.example.sih.Jobs.topJobsFragment.class);
                startActivity(intent8);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu1 = menu;
        getMenuInflater().inflate(R.menu.option_menu,menu);
        if(!check.equals(getResources().getString(R.string.english))){
            optionHin();
        }
        else{
            optionEng();
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(t.onOptionsItemSelected(menuItem))
            return true;
        switch (menuItem.getItemId()) {
            case R.id.switch1:
                if(check.equals(getResources().getString(R.string.english))) {
                    pd.setMessage("डेटा लाया जा रहा है");
                    SharedPreferences.Editor editor1 = getSharedPreferences(M, j).edit();
                    editor1.putString("Lang", "Hin");
                    editor1.apply();
                } else{
                    pd.setMessage("Fetching data");
                    SharedPreferences.Editor editor1 = getSharedPreferences(M, j).edit();
                    editor1.putString("Lang", "Eng");
                    editor1.apply();
                }
                recreate();
                return true;
            case R.id.logout: {
                Intent intent = new Intent(User_List.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;
            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(User_List.this, Rating.class);
                startActivity(rateIntent);
                return true;
            case R.id.contact_us:
                String recipient = "firstloveyourself1999@gmail.com";
                Intent intent4 = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                intent4.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void toEng(){
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        English = false;
        lang = "Hin";
        Title.setText("उपयोगकर्ता");
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }
    public void optionHin(){
        setOptionTitle(R.id.switch1, getResources().getString(R.string.switch1));
        setOptionTitle(R.id.rate_us, getResources().getString(R.string.rate1));
        setOptionTitle(R.id.logout, getResources().getString(R.string.logout1));
        setOptionTitle(R.id.contact_us, getResources().getString(R.string.contact_us1));
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
    }
    private void setOptionTitle(int id, String title)
    {
        MenuItem item = menu1.findItem(id);
        item.setTitle(title);
    }
    public void NavHin(){
        Gov.setTitle("                  सरकारी नौकरियों");
        Non_Gov.setTitle("                  गैर सरकारी नौकरी");
        Tender.setTitle("                  निविदाएं");
        Free_Lancing.setTitle("                  फ़्रीलांसिंग");
        GetPremium.setTitle("                  प्रीमियम प्राप्त करें");
        Premium.setText("प्रीमियम");
        Days.setText(days + " दिन शेष");
    }
    public void NavEng(){
        Gov.setTitle("                  Government Jobs");
        Non_Gov.setTitle("                  Non-Government Jobs");
        Tender.setTitle("                  Tenders");
        Free_Lancing.setTitle("                  Freelancing");
        GetPremium.setTitle("                  Get Premium");
        Premium.setText("Premium");
        if(days.equals("1")){
            Days.setText(days + " day remaining");
        } else {
            Days.setText(days + "days remaining");
        }
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
