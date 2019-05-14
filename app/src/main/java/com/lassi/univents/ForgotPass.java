package com.lassi.univents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ForgotPass extends AppCompatActivity {

    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Random RANDOM = new Random();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    public void resetPw(View view) {

        EditText uemail=findViewById(R.id.InputUseremail);
        final String newpw=randomString(10);
        final String email=uemail.getText().toString();

        final Map<String, Object> user = new HashMap<>();
        user.put("u_password", newpw);

        db.collection("Users").whereEqualTo("u_email",email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                String uid = documentSnapshot.getId();
                                DocumentReference docRef = db.collection("Users").document(uid);
                                docRef.set(user, SetOptions.merge());
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender(
                                                    "univents.noreply@gmail.com",
                                                    "Univents123");

                                            sender.sendMail("Password Reset", "Your Univents account's password has been reset. Your new login details are:\n" + email + '\n' + newpw,
                                                    "univents.noreply@gmail.com",
                                                    email);

                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).start();
                                break;
                            } else {
                                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


}
