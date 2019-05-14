package com.lassi.univents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GrantPerm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grant_perm);

    }
    public void goBack(View view) {

        Intent i=new Intent(this,HomeScreen.class);
        i.putExtra("notDisplayed",0);
        startActivity(i);
    }
}
