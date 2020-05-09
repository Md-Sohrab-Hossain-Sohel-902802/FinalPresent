package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private Toolbar loginToolbar;
    private EditText loginemailEdittext,loginpasswordEdittext;
    private Button loginLoginButton,loginCreateAccountButton;


    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        loginemailEdittext=findViewById(R.id.login_emailEdittextid);
        loginpasswordEdittext=findViewById(R.id.login_passwordEdittext);
        loginLoginButton=findViewById(R.id.login_loginbuttonid);
        loginCreateAccountButton=findViewById(R.id.login_accountcreateButtonid);

        loginLoginButton.setOnClickListener(this);
        loginCreateAccountButton.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_accountcreateButtonid){
            Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.login_loginbuttonid){
            String email=loginemailEdittext.getText().toString();
            String pasword=loginpasswordEdittext.getText().toString();


            if(email.isEmpty()){
                emailblankerror();
            }
            else if(pasword.isEmpty()){
                passwordblankerror();
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                wrongemailerror();
            }
            else if(pasword.length()<6){
                passwordlengtherror();
            }
            else{

                progressDialog.setTitle("Logging In");
                progressDialog.setMessage("Please Wait while we are check your credentials.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                loginuser(email,pasword);
            }

        }
    }


    public void emailblankerror(){
        loginemailEdittext.setError("Enter An Email");
        loginemailEdittext.requestFocus();
        return;
    }
    public void wrongemailerror(){
        loginemailEdittext.setError("Enter A Valid Email");
        loginemailEdittext.requestFocus();
        return;
    }
    public void passwordblankerror(){
        loginpasswordEdittext.setError("Enter your password");
        loginpasswordEdittext.requestFocus();
        return;
    }
    public void passwordlengtherror(){
        loginpasswordEdittext.setError("password should be 6 characters");
        loginpasswordEdittext.requestFocus();
        return;
    }
    private void loginuser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent mainIntent =new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();

                }
                else{
                    progressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Cant Sign in . Check the form .and try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
