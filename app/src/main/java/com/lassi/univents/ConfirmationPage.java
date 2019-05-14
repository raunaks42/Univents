package com.lassi.univents;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class ConfirmationPage extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ImageView qrImage;
    String fileName;
    String eventName;
    Date e_date_d;
    String userEmail="raunaks42@gmail.com";

    String savePath = Environment.getExternalStorageDirectory().getPath() + "/Univents/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    //SharedPreferences pref = getSharedPreferences("user_details",MODE_PRIVATE);
    /*int notifID = new Random().nextInt(6969) + 10;
    int broadcastID = new Random().nextInt(6969) + 10;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_page);
        final TextView name = findViewById(R.id.e_name);
        final ImageView photo = findViewById(R.id.e_photo);
        final TextView date = findViewById(R.id.e_date);
        final TextView location = findViewById(R.id.e_location);
        final TextView price = findViewById(R.id.e_price);
        //userEmail=pref.getString("userEmail",null);

        Intent i = getIntent();
        final String bID = i.getStringExtra("id");
        final String eID = i.getStringExtra("eid");

        try {
//            DocumentReference docRefB = db.collection("Bookings").document(bID);
//            docRefB.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(final DocumentSnapshot documentSnapshotB) {
//                    if (documentSnapshotB.exists()) {
//                        String eID=documentSnapshotB.getString("e_id");
//            eID = i.getStringExtra("eid");
                        DocumentReference docRef =db.collection("Events").document(eID);
                        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()) {
                                    String e_name = name.getText()+" "+documentSnapshot.getString("e_name");
                                    name.setText(e_name);
                                    eventName=documentSnapshot.getString("e_name");
                                    fileName=eventName+" "+bID;
                                    e_date_d = documentSnapshot.getDate("e_date");
                                    final DateFormat df = new SimpleDateFormat("dd MMM, yyyy HH:mm a");
                                    String e_date = date.getText() + " " + df.format(e_date_d);
                                    date.setText(e_date);
                                    String e_location = location.getText() + " " + documentSnapshot.getString("e_location");
                                    location.setText(e_location);
                                    String e_price = price.getText() + " " + documentSnapshot.getDouble("e_price").toString();
                                    price.setText(e_price);
                                    String e_photo = documentSnapshot.getString("e_photo");
                                    Picasso.get()
                                            .load(e_photo)
                                            .error(R.drawable.common_google_signin_btn_text_dark)
                                            .into(photo);

                                    //encodes and sets QR
                                    qrImage =  findViewById(R.id.QR_Image);

//                                    if (bID.length() > 0) {
                                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                        Display display = manager.getDefaultDisplay();
                                        Point point = new Point();
                                        display.getSize(point);
                                        int width = point.x;
                                        int height = point.y;
                                        int smallerDimension = width < height ? width : height;
                                        smallerDimension = smallerDimension * 3 / 4;

                                        qrgEncoder = new QRGEncoder(
                                                "DDXYwLUQvyPaV2prYqYP", null,
                                                QRGContents.Type.TEXT,
                                                smallerDimension);
                                        try {
                                            bitmap = qrgEncoder.encodeAsBitmap();
                                            qrImage.setImageBitmap(bitmap);
                                        } catch (WriterException e) {
                                            Log.v("qr: ", e.toString());
                                        }
//                                    }

                                    boolean save;
                                    String result;
                                    try {
                                        save = QRGSaver.save(savePath, fileName, bitmap, QRGContents.ImageType.IMAGE_JPEG);
                                        result = save ? "Image Saved" : "Image Not Saved";
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //email
                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                GMailSender sender = new GMailSender(
                                                        "univents.noreply@gmail.com",
                                                        "Univents123");

                                                sender.addAttachment(savePath+fileName+".jpg");
                                                sender.sendMail("Booking Confirmed", "You have booked your spot in "+eventName+" on "+df.format(e_date_d)+" successfully. Please find your booking QR Code attached. You will have to get this scanned at the event gates for entry.",
                                                        "univents.noreply@gmail.com",
                                                        userEmail);

                                            } catch (Exception e) {
                                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }).start();

                                    //notification
                                    //showNotif();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                    Intent i= new Intent(getApplicationContext(),NotifHandler.class);
                                    i.putExtra("ename",eventName);
                                    i.putExtra("edate",sdf.format(e_date_d));
                                    startService(i);

                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Event DNE", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "Booking DNE", Toast.LENGTH_LONG).show();
//                    }
//                }
//            });
        } catch (Exception e) {
            Log.d("PAYMENT ISSUE", e.toString());
//            i = new Intent(this, EventList.class);
//            startActivity(i);
        }
    }

    /*public void showNotif() {
        //notification
        Date currDate = Calendar.getInstance().getTime();
        Date alertDate = new Date(e_date_d.getTime() - 7200000);
        long millsec = Math.abs(alertDate.getTime() - currDate.getTime());  //time till 2 hours before event

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "idk")
                .setSmallIcon(android.R.drawable.sym_action_chat)
                .setContentTitle(eventName)
                .setContentText("The event starts in two hours")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();    //builds notif

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notifID);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, broadcastID, notificationIntent, 0); //pending intent to broadcast reciever that will create notif

        long futureInMillis = SystemClock.elapsedRealtime() + millsec;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);  //at set time, pendingintent activated
    }*/

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.idk);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("idk", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
