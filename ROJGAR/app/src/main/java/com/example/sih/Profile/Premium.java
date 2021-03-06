package com.example.sih.Profile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.sih.MainActivity;
import com.example.sih.R;
import com.firebase.client.Firebase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Premium extends AppCompatActivity {

    Button PremiumButton;
    final int UPI_PAYMENT = 0;
    String check, M, phone, S, formattedDate, isPremium;
    int i, j;
    TextView Title, Title2, Description, Benefits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences1 = getSharedPreferences(M,j);
        check = preferences1.getString("Lang","Eng");
        SharedPreferences preferences = getSharedPreferences(S,i);
        phone = preferences.getString("Phone","");
        isPremium = preferences.getString("isPremium", "No");
        setContentView(R.layout.activity_testing);

        Title = findViewById(R.id.title);
        Title2 = findViewById(R.id.title2);
        Description = findViewById(R.id.description);
        Benefits = findViewById(R.id.benefits);
        PremiumButton = findViewById(R.id.premiumButton);

        if(!check.equals(getResources().getString(R.string.english))){
            Description.setText(R.string.benefits1);
        }

        if(isPremium.equals("Yes")){
            if(check.equals(getResources().getString(R.string.english))){
                Title.setText("Congratulations!");
                Title2.setText("You are already a premium member");
                PremiumButton.setText("Go to dashboard");
                Benefits.setText("Benefits of Rojgar Premium");
            } else{
                Title.setText(R.string.congrats);
                Title2.setText(R.string.premium_member);
                PremiumButton.setText("डैशबोर्ड पर जाएं");
                Benefits.setText(R.string.benefits_of_premium1);
            }
        } else {
            if(!check.equals(getResources().getString(R.string.english))){
                Title.setText("Rojgar Premium प्राप्त करें");
                Title2.setText("₹96 / महीने पर");
                PremiumButton.setText("Premium प्राप्त करें");
                Benefits.setText(R.string.benefits_of_premium1);
            } else{

            }
        }
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        formattedDate = df.format(c);
        PremiumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPremium.equals("Yes")){
                    Intent intent = new Intent(Premium.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Payment("Rojgar Premium", "rojgar@apl", "Monthly Premium", "96.00");
                }
            }
        });
    }

    public void Payment(  String name,String upiId, String note, String amount) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay using");
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            if(check.equals(getResources().getString(R.string.english))) {
                Toast.makeText(this, "Please download an UPI app to continue", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(this, "जारी रखने के लिए एक UPI एप्लिकेशन डाउनलोड करें", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user")));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String response = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + response);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(response);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Premium.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    }
                }
                else {
                    if(check.equals(getResources().getString(R.string.english))) {
                        paymentCancel = "Payment cancelled by user.";
                    } else {
                        paymentCancel = "उपयोगकर्ता द्वारा भुगतान रद्द किया गया।";
                    }
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Firebase reference = new Firebase("https://smart-e60d6.firebaseio.com/Users");
                reference.child(phone).child("Premium Date").setValue(formattedDate);
                SharedPreferences.Editor editor = getSharedPreferences(S,i).edit();
                editor.putString("isPremium", "Yes");
                editor.apply();
                Intent intent = new Intent(Premium.this, MainActivity.class);
                startActivity(intent);
                if(!check.equals(getResources().getString(R.string.english))){
                    sendNotification(getResources().getString(R.string.congrats), getResources().getString(R.string.premium_member));
                    Toast.makeText(Premium.this, getResources().getString(R.string.congrats_premium), Toast.LENGTH_LONG).show();
                }else {
                    sendNotification("Congratulations!", "You are now a premium member");
                    Toast.makeText(Premium.this, "Congratulations!, You are now a premium member", Toast.LENGTH_LONG).show();
                }
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(Premium.this, "Payment canceled by user", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "उपयोगकर्ता द्वारा भुगतान रद्द किया गया", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                if(check.equals(getResources().getString(R.string.english))) {
                    Toast.makeText(Premium.this, "Transaction failed, please try again", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "प्रयास विफल रहा, कृपया पुन: प्रयास करें", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if(check.equals(getResources().getString(R.string.english))) {
                Toast.makeText(Premium.this, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_internet1), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void sendNotification(String title, String message) {
        Intent intent = new Intent(this, Premium.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String CHANNEL_ID = "Account";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Account Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(1, notificationBuilder.build());
    }

}
