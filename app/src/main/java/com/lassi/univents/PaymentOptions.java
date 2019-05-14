package com.lassi.univents;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaymentOptions extends AppCompatActivity {
    Double p;
    String eID;
    String bID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        p = Double.parseDouble(getIntent().getStringExtra("price"));
        eID = getIntent().getStringExtra("id");
        Log.d("intent", eID);
    }

    public void confirm(View view) {
        Map<String, Object> booking = new HashMap<>();
        SharedPreferences pref = getSharedPreferences("user_details",MODE_PRIVATE);
        booking.put("e_id", eID);
        booking.put("u_id",pref.getString("userId",null));
//        booking.put("u_id","abc");
        db.collection("Bookings")
                .add(booking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        bID=documentReference.getId();
                        Log.d("Booking Uploaded", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), bID, Toast.LENGTH_LONG);
                    }
                });
        Intent i = new Intent(this, ConfirmationPage.class);
        i.putExtra("id", bID);
        i.putExtra("eid", eID);
        startActivity(i);
    }
    public void payPayTm(View view) {
        String url = "https://p-y.tm/5lez-yo";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
//        startActivity(i);
        startActivityForResult(i, 1, null);
    }

    public void payUpi(View view) {
        String url = getUPIString("sarthak7gupta@dbs", "Univents", "", "b1a2b750bc1d08fe5c6ce1d3386f77c52a3ea7cb", "18765432", "Univents", p.toString(), "INR", "");
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        Intent chooser = Intent.createChooser(i, "Pay with...");
        startActivityForResult(chooser, 1, null);
    }
    private String getUPIString(String payeeAddress, String payeeName, String payeeMCC, String trxnID, String trxnRefId,
                                String trxnNote, String payeeAmount, String currencyCode, String refUrl) {
        String UPI = "upi://pay?pa=" + payeeAddress + "&pn=" + payeeName
                + "&mc=" + payeeMCC + "&tid=" + trxnID + "&tr=" + trxnRefId
                + "&tn=" + trxnNote + "&am=" + payeeAmount + "&cu=" + currencyCode
                + "&refUrl=" + refUrl;
        return UPI.replace(" ", "+");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                if (resultCode == -1) {
                    Intent i = new Intent(this, ConfirmationPage.class);
                    i.putExtra("id", bID);
                    startActivity(i);
                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "upiId")
                            .setSmallIcon(R.drawable.whatsapp_icon)
                            .setContentTitle("UPI for Univents failed")
                            .setContentText("PLEASE TRY AGAIN AGAIN")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    createNotificationChannel();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                    notificationManager.notify(986, builder.build());
                    Intent i = new Intent(this, ConfirmationPage.class);
                    i.putExtra("id", bID);
                    startActivity(i);
                }
                PaymentOptions.this.finish();
            } catch (Exception e) {
                Log.e("Error in UPI onActivityResult->", "" + e.getMessage());
            }
        } else if (requestCode == 2) {
            addBooking();
            Intent i = new Intent(this, ConfirmationPage.class);
            i.putExtra("id", bID);
            startActivity(i);
        }

    }

    public String addBooking(){
        Map<String, Object> booking = new HashMap<>();
        SharedPreferences pref = getSharedPreferences("user_details",MODE_PRIVATE);
        booking.put("e_id",eID);
        booking.put("u_id",pref.getString("userId",null));
        db.collection("Bookings")
                .add(booking)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Booking Uploaded", "DocumentSnapshot added with ID: " + documentReference.getId());
                        bID=documentReference.getId();

                    }
                });
        return bID;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "upiId";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("upiId", name, importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
