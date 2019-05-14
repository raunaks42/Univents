package com.lassi.univents;

import android.Manifest;
//import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomeScreen extends AppCompatActivity {

    //SharedPreferences pref;
    Intent intent;
    int notDisplayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Intent i = getIntent();
        notDisplayed = i.getIntExtra("notDisplayed",1);
        checkPerm();

//        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        intent = new Intent(this,EventList.class);
        /*if(pref.contains("username")){
            startActivity(intent);
        }*/
    }

    public void Login(View view) {
        Intent i = new Intent(this , UserLogin.class);
        startActivity(i);
    }

    public void SignUp(View view) {
        Intent i = new Intent(this , UserSignup.class);
        startActivity(i);
    }

    public void orgLogin(View view) {
        Intent i = new Intent(this , OrgLogin.class);
        startActivity(i);
    }

    public void checkPerm() {
        //is permission enabled?
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //should show permission reason before asking because earlier denied?
            if (notDisplayed==1 && ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Intent i=new Intent(this,GrantPerm.class);
                startActivity(i);   //permission reason activity
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        69);    //permission request
            }
        }
        else {
            //permission enabled, save qr image
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode==69) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted, save qr
            }
            else {
                notDisplayed=1; //permission not granted, reset to reason not displayed
                checkPerm();    //check permission again
            }
        }
    }
}


