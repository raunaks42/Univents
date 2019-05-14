package com.lassi.univents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserSignup extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText userName;
    EditText userEmail;
    EditText userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        userName = findViewById(R.id.InputName);
        userEmail = findViewById(R.id.InputUseremail);   //should be renamed to InputEmail in xml and here
        userPassword = findViewById(R.id.InputPassword);
    }

    public void SignupCheck(View view) {

        /*Map<String, Object> user = new HashMap<>();
        user.put("u_name", userName.getText().toString());
        user.put("u_email", userEmail.getText().toString());
        user.put("u_password", userPassword.getText().toString());*/

        final Intent i=new Intent(this,UserSignedUpConfirmation.class);
        final User user=new User(userName.getText().toString(),userEmail.getText().toString(),userPassword.getText().toString());

        db.collection("Users").whereEqualTo("u_email",user.getU_email()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            db.collection("Users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("sign up complete", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(getApplicationContext(), "User added successfully", Toast.LENGTH_LONG).show();

                                            new Thread(new Runnable() {
                                                public void run() {
                                                    try {
                                                        GMailSender sender = new GMailSender(
                                                                "univents.noreply@gmail.com",
                                                                "Univents123");

                                                        sender.sendMail("Univents Account Created", "Your Univents account has been created and linked to this email. Your details are:\n"+user.getU_name()+'\n'+user.getU_email()+'\n'+user.getU_password()+"/nYou may use you email and password to log into the app",
                                                                "univents.noreply@gmail.com",
                                                                user.getU_email());

                                                    } catch (Exception e) {
                                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }).start();

                                            startActivity(i);
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        /*Intent i = new Intent(this , MainActivity.class);       //replace MainActivity.class with the target activity on signup
        startActivity(i);*/

    }
}