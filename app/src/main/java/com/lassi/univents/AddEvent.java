package com.lassi.univents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddEvent extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText eventName;
    EditText eventLocation;
    EditText eventdd;
    EditText eventmm;
    EditText eventyy;
    EditText eventhrs;
    EditText eventmins;
    EditText eventType;
    EditText eventDesc;
    EditText eventImgUrl;
    EditText eventTicketPrice;
    EditText eventorgno;

    String eventDate;
    Date date1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventName = findViewById(R.id.EventName);
        eventLocation = findViewById(R.id.EventLocation);
        eventdd = findViewById(R.id.date);
        eventmm = findViewById(R.id.mm);
        eventyy = findViewById(R.id.yy);
        eventhrs = findViewById(R.id.hrs);
        eventmins = findViewById(R.id.mins);
        eventType = findViewById(R.id.type);
        eventImgUrl = findViewById(R.id.image);
        eventDesc = findViewById(R.id.desc);
        eventTicketPrice = findViewById(R.id.price);
        eventorgno=findViewById(R.id.orgno);

        //To clear all the editText values on reaching this activity on back button press
        eventName.getText().clear();
        eventLocation.getText().clear();
        eventdd.getText().clear();
        eventmm.getText().clear();
        eventyy.getText().clear();
        eventhrs.getText().clear();
        eventmins.getText().clear();
        eventType.getText().clear();
        eventImgUrl.getText().clear();
        eventDesc.getText().clear();
        eventTicketPrice.getText().clear();
        eventorgno.getText().clear();
    }

    public void addEve(View view) {


        eventDate = eventdd.getText().toString() + '/' + eventmm.getText().toString() + '/' + eventyy.getText().toString() + ' ' + eventhrs.getText().toString() + ':' + eventmins.getText().toString() + ':' + '0'+'0';
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String, Object> event = new HashMap<>();

        event.put("e_name", eventName.getText().toString());
        event.put("e_date", date1);
        event.put("e_description", eventDesc.getText().toString());
        event.put("e_location", eventLocation.getText().toString());
        event.put("e_photo", eventImgUrl.getText().toString());
        event.put("e_type", eventType.getText().toString());
        event.put("e_price", Integer.parseInt(eventTicketPrice.getText().toString()));
        event.put("e_orgno",eventorgno.getText().toString());

        db.collection("Events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Event Uploaded", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                });

        Intent i = new Intent(this , EventAddedConfirmation.class);
        startActivity(i);

    }

}
