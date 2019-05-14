package com.lassi.univents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserSignedUpConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signed_up_confirmation);
    }


    public void returnToHome(View view) {
        Intent i = new Intent(this , HomeScreen.class);
        startActivity(i);
    }
}

