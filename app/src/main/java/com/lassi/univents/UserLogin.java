package com.lassi.univents;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserLogin extends AppCompatActivity {

    EditText uemail, pwd;
    //SharedPreferences pref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        uemail = (EditText)findViewById(R.id.InputUseremail);
        pwd = (EditText)findViewById(R.id.InputPassword);
//        pref = getSharedPreferences("user_details",MODE_PRIVATE);
    }

    public void LoginCheck(View view) {

        final Intent i = new Intent(this, EventList.class);
        final String useremail = uemail.getText().toString();
        final String password = pwd.getText().toString();

        db.collection("Users").whereEqualTo("u_email",useremail).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            User user= documentSnapshot.toObject(User.class);
                            if(password.equals(user.getU_password())) {
                                String username=user.getU_name();
                                String userid=documentSnapshot.getId();
//                                SharedPreferences.Editor editor = pref.edit();
//                                editor.putString("userName",username); //adds username to sharedpref once login is validated
//                                editor.putString("userEmail",useremail);
//                                editor.putString("userId",userid);
//                                editor.commit();
                                startActivity(i);
                                break;
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void forgotPassword(View view) {
        Intent i = new Intent(this, ForgotPass.class);
        startActivity(i);
    }

}
