package com.example.sih.Profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sih.Jobs.Government;
import com.example.sih.Jobs.Non_Government;
import com.example.sih.Jobs.StudyResources;
import com.example.sih.MainActivity;
import com.example.sih.R;
import com.example.sih.Registration.Login;
import com.example.sih.chatApp.ContactUs;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Random;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/** Activity through which users can view their profile and can make edit in it
 * Both database reading and writing functionality are working here
 */

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    EditText ETUsername, ETName;
    Button BTUsername, BTName, BTPassword, BTCertificates, BTDelete;
    DatabaseReference reff;
    TextView uname, uphone, Premium, Days;
    ConstraintLayout Layout;
    ImageView camera, profile, drawerProfile, fullProfile, premiumProfile, crown;
    Boolean English = true, isFull = false;
    Firebase firebase;
    StorageReference mStorageReference;
    String user_name, name, phone, S, M, J, check, lang, user_phone, isPremium, days;
    int i, j, x;
    final static int PICK_IMAGE_REQUEST = 2342;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle t;
    Boolean isRegistered = false;
    Menu menu1, menu2;
    MenuItem Gov, Non_Gov, Tender, Free_Lancing, Resources, GetPremium, chat, topJobs, publishJob, Jobs, Features, Connection, Top_Jobs, Publish;
    ProgressDialog pd;
    String username;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Non-Government Jobs");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        path = preferences.getString("path","");
        isPremium = preferences.getString("isPremium", "No");
        days = preferences.getString("remainingDays", "0");
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        setContentView(R.layout.activity_profile);
        Layout = findViewById(R.id.layout);
        ETUsername = findViewById(R.id.usernameET);
        ETName = findViewById(R.id.nameET);
        camera = findViewById(R.id.camera);
        profile = findViewById(R.id.profile_image);
        premiumProfile = findViewById(R.id.profile_image_premium);
        fullProfile = findViewById(R.id.profile_image_full);
        BTUsername = findViewById(R.id.usernameBT);
        BTName = findViewById(R.id.nameBT);
        BTPassword = findViewById(R.id.password);
        BTCertificates = findViewById(R.id.certificate);
        BTDelete = findViewById(R.id.delete);
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
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
        chat = menu2.findItem(R.id.chat);
        topJobs = menu2.findItem(R.id.topJobs);
        publishJob = menu2.findItem(R.id.publish);
        Jobs = menu2.findItem(R.id.title1);
        Features = menu2.findItem(R.id.title2);
        Top_Jobs = menu2.findItem(R.id.topJobs);
        Connection = menu2.findItem(R.id.chat);
        Publish = menu2.findItem(R.id.publish);
        Resources = menu2.findItem(R.id.resources);
        uname = navigationView.getHeaderView(0).findViewById(R.id.name_of_user);
        uphone = navigationView.getHeaderView(0).findViewById(R.id.phone_of_user);
        drawerProfile = navigationView.getHeaderView(0).findViewById(R.id.image_of_user);
        Premium = navigationView.getHeaderView(0).findViewById(R.id.premium);
        Days = navigationView.getHeaderView(0).findViewById(R.id.days);
        crown = navigationView.getHeaderView(0).findViewById(R.id.crownimage);

        if(!check.equals(getResources().getString(R.string.english))){
            NavHin();
            toHin();
        } else{
            NavEng();
            toEng();
        }

        if(isPremium.equals("Yes")){
            profile.setVisibility(View.INVISIBLE);
            premiumProfile.setVisibility(View.VISIBLE);
        }
        pd = new ProgressDialog(Profile.this);
        if(!check.equals(getResources().getString(R.string.english)) || !English){
            pd.setMessage("छवि अपडेट हो रही है...");
        } else {
            pd.setMessage("Updating Image...");
        }
        firebase = new Firebase("https://smart-e60d6.firebaseio.com/Users");
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    user_name = dataSnapshot.child("Username").getValue().toString();
                    name = dataSnapshot.child("Name").getValue().toString();
                    user_phone = dataSnapshot.child("Phone").getValue().toString();
                    username  = decryptUsername(user_name).toString();
                    mStorageReference = FirebaseStorage.getInstance().getReference().child(user_phone).child("Profile Picture");
                    ETUsername.setText(username);
                    ETName.setText(name);
                    if (isPremium.equals("Yes")) {
                        if (!check.equals(getResources().getString(R.string.english))) {
                            Premium.setText("प्रीमियम");
                        }
                        Premium.setVisibility(View.VISIBLE);
                        crown.setVisibility(View.VISIBLE);
                        if (days.equals("1")) {
                            if (!check.equals(getResources().getString(R.string.english))) {
                                Days.setText(days + " दिन शेष");
                            } else {
                                Days.setText(days + " day remaining");
                            }
                        } else {
                            if (!check.equals(getResources().getString(R.string.english))) {
                                Days.setText(days + " दिन शेष");
                            } else {
                                Days.setText(days + " days remaining");
                            }
                        }
                        Days.setVisibility(View.VISIBLE);
                    }
                } catch(Exception e){

                }

                try {
                    final long ONE_MEGABYTE = 1024 * 1024;
                    mStorageReference.getBytes(ONE_MEGABYTE)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    DisplayMetrics dm = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                                    if(isPremium.equals("Yes")){
                                        premiumProfile.setMinimumHeight(dm.heightPixels);
                                        premiumProfile.setMinimumWidth(dm.widthPixels);
                                        premiumProfile.setImageBitmap(bm);
                                    } else {
                                        profile.setMinimumHeight(dm.heightPixels);
                                        profile.setMinimumWidth(dm.widthPixels);
                                        profile.setImageBitmap(bm);
                                    }
                                    fullProfile.setImageBitmap(bm);
                                    drawerProfile.setMinimumHeight(dm.heightPixels);
                                    drawerProfile.setMinimumWidth(dm.widthPixels);
                                    drawerProfile.setImageBitmap(bm);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch(Exception e){

                }
               uphone.setText(user_phone);
               uname.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if(!check.equals(getResources().getString(R.string.english)) || !English) {
                    Toast.makeText(Profile.this, getResources().getString(R.string.error1), Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(Profile.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        reff = FirebaseDatabase.getInstance().getReference().child("Users").child("Company Representative Details").child(phone);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{

                    String post = dataSnapshot.child("Post").getValue().toString();
                    isRegistered = true;
                } catch (Exception e){
                    isRegistered = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this, "Something went wrong",Toast.LENGTH_LONG).show();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              fullProfile.setVisibility(View.VISIBLE);
              Layout.setVisibility(View.INVISIBLE);
              isFull = true;
            }
        });

        premiumProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullProfile.setVisibility(View.VISIBLE);
                Layout.setVisibility(View.INVISIBLE);
                isFull = true;
            }
        });

        BTUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encryptedUsername = encryptUsername(ETUsername.getText().toString().trim()).toString();
                    firebase.child(phone).child("Username").setValue(encryptedUsername);
                    if(!check.equals(getResources().getString(R.string.english)) || !English){
                        Toast.makeText(Profile.this, getResources().getString(R.string.username_updated1), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Username Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        BTName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    firebase.child(phone).child("Name").setValue(ETName.getText().toString().trim());
                    if(!check.equals(getResources().getString(R.string.english)) || !English){
                        Toast.makeText(Profile.this, getResources().getString(R.string.name_updated1), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Name Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        BTPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Change_Password.class);
                startActivity(intent);
            }
        });
        BTCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Update_Certificates.class);
                startActivity(intent);
            }
        });
        BTDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_Acc del=new Delete_Acc(Profile.this);
                del.show();
            }
        });
        fullProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullProfile.setVisibility(View.GONE);
                Layout.setVisibility(View.VISIBLE);
                isFull = false;
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.non_government:
                SharedPreferences.Editor editor = getSharedPreferences(J,x).edit();
                editor.putString("Activity", "Private");
                editor.apply();
                Intent intent1 = new Intent(Profile.this, Non_Government.class);
                startActivity(intent1);
                break;
            case R.id.free_lancing:
                SharedPreferences.Editor editor1 = getSharedPreferences(J,x).edit();
                editor1.putString("Activity", "Freelancing");
                editor1.apply();
                Intent intent = new Intent(Profile.this, com.example.sih.Jobs.Free_Lancing.class);
                startActivity(intent);
                break;
            case R.id.government:
                SharedPreferences.Editor editor2 = getSharedPreferences(J,x).edit();
                editor2.putString("Activity", "Government");
                editor2.apply();
                Intent intent5 = new Intent(Profile.this, Government.class);
                startActivity(intent5);
                break;
            case R.id.resources:
                Intent intent3 = new Intent(Profile.this, StudyResources.class);
                startActivity(intent3);
            case R.id.premium:
                Intent intent2 = new Intent(Profile.this, com.example.sih.Profile.Premium.class);
                startActivity(intent2);
                break;
            case R.id.chat:
                Intent intent6 = new Intent(Profile.this, com.example.sih.chatApp.User_List.class);
                startActivity(intent6);
                break;
            case R.id.publish:
                if (!isRegistered) {
                    Intent intent7 = new Intent(Profile.this, com.example.sih.PublishJob.CreateYourJob.class);
                    startActivity(intent7);
                }
                else{
                    Intent intent7 = new Intent(Profile.this, com.example.sih.PublishJob.jobsPublished.class);
                    startActivity(intent7);
                }
                break;
            case R.id.topJobs:
                Intent intent8 = new Intent(Profile.this, com.example.sih.Jobs.topJobsFragment.class);
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
                if(English){
                    toHin();
                    NavHin();
                    optionHin();
                    English = false;
                }else{
                    toEng();
                    NavEng();
                    optionEng();
                    English = true;
                }
                return true;
            case R.id.logout: {
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                SharedPreferences.Editor editor = getSharedPreferences(S, i).edit();
                editor.putString("Status", "Null");
                editor.apply();
                finishAffinity();
                break;

            }
            case R.id.rate_us:
                Intent rateIntent = new Intent(Profile.this, Rating.class);
                startActivity(rateIntent);
                return true;

            case R.id.contact_us:
                Intent intent = new Intent(Profile.this, ContactUs.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
    public void toEng(){
        BTUsername.setText("Change Username");
        BTPassword.setText("Change Password");
        BTName.setText("Change Name");
        BTCertificates.setText("Update Qualification Certificates");
        BTDelete.setText("Delete Your Account");
        getSupportActionBar().setTitle("Profile");
        English = true;
        lang = "Eng";
        SharedPreferences.Editor editor1 = getSharedPreferences(M,j).edit();
        editor1.putString("Lang",lang);
        editor1.apply();
    }

    public void toHin(){
        BTUsername.setText(R.string.change_username1);
        BTPassword.setText(R.string.change_password1);
        BTName.setText(R.string.change_name1);
        BTCertificates.setText(R.string.update_qualification_certificates1);
        BTDelete.setText(R.string.delete_your_account1);
        getSupportActionBar().setTitle(R.string.profile1);
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
    }
    public void optionEng(){
        setOptionTitle(R.id.switch1, "Change Language");
        setOptionTitle(R.id.rate_us, "Rate Us");
        setOptionTitle(R.id.logout, "Logout");
        setOptionTitle(R.id.contact_us, "Contact Us");
    }
    public void NavHin(){
        Gov.setTitle("                  सरकारी नौकरियों");
        Non_Gov.setTitle("                  गैर सरकारी नौकरी");
        Tender.setTitle("                  निविदाएं");
        Free_Lancing.setTitle("                  फ़्रीलांसिंग");
        GetPremium.setTitle("                  प्रीमियम प्राप्त करें");
        Publish.setTitle("                  अपनी नौकरी प्रकाशित करें");
        Top_Jobs.setTitle("                  शीर्ष नौकरियां");
        Connection.setTitle("                  अपने कनेक्शन बनाएँ");
        Premium.setText("प्रीमियम");
        Days.setText(days + " दिन शेष");
        Jobs.setTitle("           नौकरी क्षेत्र");
        Features.setTitle("           अधिक सुविधाएं");
    }
    public void NavEng(){
        Gov.setTitle("                  Government Jobs");
        Non_Gov.setTitle("                  Non-Government Jobs");
        Tender.setTitle("                  Tenders");
        Free_Lancing.setTitle("                  Freelancing");
        GetPremium.setTitle("                  Get Premium");
        Publish.setTitle("                  Publish Your Job");
        Top_Jobs.setTitle("                  Top Jobs");
        Connection.setTitle("                  Build Your Connections");
        Premium.setText("Premium");
        if(days.equals("1")){
            Days.setText(days + " day remaining");
        } else {
            Days.setText(days + "days remaining");
        }
        Jobs.setTitle("           Job Sectors");
        Features.setTitle("           More Features");
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
        } else if(isFull){
            fullProfile.setVisibility(View.GONE);
            Layout.setVisibility(View.VISIBLE);
            isFull = false;
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        if(!check.equals(getResources().getString(R.string.english))){
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

    private void checkPermission() {

        //Checking Storage read permission for fetching internal documents
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if(!check.equals(getResources().getString(R.string.english)) || !English){
            startActivityForResult(Intent.createChooser(intent, "छवि का चयन करें"), PICK_IMAGE_REQUEST);
        } else {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap img = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if (img != null) {
                    pd.show();
                    profile.setImageBitmap(img);
                    fullProfile.setImageBitmap(img);
                    drawerProfile.setImageBitmap(img);
                    uploadFile(data.getData());
                    SharedPreferences.Editor editor1 = getSharedPreferences(S,i).edit();
                    editor1.putString("path", path);
                    editor1.apply();
                } else{

                }
            } catch (Exception e) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(this, "This format is not supported", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "यह प्रारूप समर्थित नहीं है", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void uploadFile(Uri data) {
            mStorageReference.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   pd.dismiss();
                   if(!check.equals(getResources().getString(R.string.english))) {
                       Toast.makeText(Profile.this, "अद्यतन सफलतापूर्ण हो गया", Toast.LENGTH_SHORT).show();
                   } else{
                       Toast.makeText(Profile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                   }
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });
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
    public StringBuilder encryptUsername(String uname) {
        StringBuilder stringBuilder = new StringBuilder();
        Random r = new Random();
        int len = uname.length();
        for (int i = 0; i < len; i++) {
            char a = uname.charAt(i);
            char c = (char) (r.nextInt(26) + 'a');
            stringBuilder.append((a - len) - (2 * i));
            stringBuilder.append(c);
        }
        String strlen = Integer.toString(len + 2);
        stringBuilder.append(strlen);
        return stringBuilder;
    }
}
