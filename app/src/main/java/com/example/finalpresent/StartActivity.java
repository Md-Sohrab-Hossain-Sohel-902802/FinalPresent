package com.example.finalpresent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private Button loginButton;
    private  Button registrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginButton=findViewById(R.id.start_loginbuttonid);
        registrationButton=findViewById(R.id.startRegButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fintent=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(fintent);
            }
        });
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tintent=new Intent(StartActivity.this,RegistrationActivity.class);
                startActivity(tintent);
            }
        });


    }

}
