package com.lassi.univents;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventPage extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Double p;
    String eID;
    String orgno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        final TextView name = findViewById(R.id.e_name);
        final ImageView photo = findViewById(R.id.e_photo);
        final TextView date = findViewById(R.id.e_date);
        final TextView location = findViewById(R.id.e_location);
        final TextView price = findViewById(R.id.e_price);
        final TextView desc = findViewById(R.id.e_desc);
        final String TAG = "EVENT: ";
        Intent i = getIntent();
        String id = i.getStringExtra("eventId");
        eID = id;
        DocumentReference docRef = db.collection("Events").document(id);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String e_name = documentSnapshot.getString("e_name");
                    name.setText(e_name);
                    Date e_date_d = documentSnapshot.getDate("e_date");
                    DateFormat df = new SimpleDateFormat("dd MMM, yyyy HH:mm a");
                    String e_date = date.getText() + " " + df.format(e_date_d);
                    date.setText(e_date);
                    String e_location = location.getText() + " " + documentSnapshot.getString("e_location");
                    location.setText(e_location);
                    String e_price = price.getText() + " " + documentSnapshot.getDouble("e_price").toString();
                    price.setText(e_price);
                    orgno=documentSnapshot.getString("e_orgno");
                    p = documentSnapshot.getDouble("e_price");
                    String e_desc = documentSnapshot.getString("e_description");
                    desc.setText(e_desc);
                    String e_photo = documentSnapshot.getString("e_photo");
                    Picasso.get()
                            .load(e_photo)
                            .error(R.drawable.common_google_signin_btn_text_dark)
                            .into(photo);
                } else {
                    Toast.makeText(getApplicationContext(), "DNE", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void whatsapp(View view) {
        String url = "https://api.whatsapp.com/send?phone="+orgno;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void payOpt(View view) {
        Intent i = new Intent(this, PaymentOptions.class);
        i.putExtra("price", p.toString());
        i.putExtra("id", eID);
        startActivity(i);
    }
}
