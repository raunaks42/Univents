package com.lassi.univents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class OrgLogin extends AppCompatActivity {

    EditText orgUserName;
    EditText orgPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_login);

        orgUserName = findViewById(R.id.InputOrgUsername);
        orgPassword = findViewById(R.id.InputOrgPassword);

    }
    public void OrgLoginCheck(View view) {

        if(orgUserName.getText().toString().equals("admin") && orgPassword.getText().toString().equals("admin") ){
            Intent i = new Intent(this, AddEvent.class);
            startActivity(i);
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),"Invalid Credentials",Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
