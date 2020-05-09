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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText regusernameEdittext,reguseremailEdittext,regUserpasswordEdittext;
    private Button regCreateAccountButtonid,regLoginButtonid;
    private Toolbar registrationToolbar;

    private ProgressDialog regprogressDialog;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regusernameEdittext=findViewById(R.id.reg_usernameEdittextid);
        reguseremailEdittext=findViewById(R.id.reg_emailEdittextid);
        regUserpasswordEdittext=findViewById(R.id.reg_passwordEdittext);

        regCreateAccountButtonid=findViewById(R.id.reg_createaccountButtonid);
        regLoginButtonid=findViewById(R.id.reg_loginbuttonid);

        regprogressDialog=new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        regCreateAccountButtonid.setOnClickListener(this);
        regLoginButtonid.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
            if(v.getId()==R.id.reg_loginbuttonid){
                Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
            else if(v.getId()==R.id.reg_createaccountButtonid){
                String name=regusernameEdittext.getText().toString();
                String email=reguseremailEdittext.getText().toString();
                String password=regUserpasswordEdittext.getText().toString();
              if(name.isEmpty()){
                    regusernameEdittext.setError("A Name Must Be needed");
                    regusernameEdittext.requestFocus();
                    return;
                }
                else  if(email.isEmpty()){
                    emailblankerror();
                }
                else if(password.isEmpty()){
                    passwordblankerror();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    wrongemailerror();
                }
                else if(password.length()<6){
                    passwordlengtherror();
                }

                else {
                    regprogressDialog.setTitle("Registering User");
                    regprogressDialog.setMessage("Please wait while we create your account");
                    regprogressDialog.setCanceledOnTouchOutside(false);
                    regprogressDialog.show();
                    register_user(name, email, password);
                }
            }
    };

    public void emailblankerror(){
        reguseremailEdittext.setError("Enter An Email");
        reguseremailEdittext.requestFocus();
        return;
    }
    public void wrongemailerror(){
        reguseremailEdittext.setError("Enter A Valid Email");
        reguseremailEdittext.requestFocus();
        return;
    }
    public void passwordblankerror(){
        regUserpasswordEdittext.setError("Enter your password");
        regUserpasswordEdittext.requestFocus();
        return;
    }
    public void passwordlengtherror(){
        regUserpasswordEdittext.setError("password should be 6 characters");
        regUserpasswordEdittext.requestFocus();
        return;
    }
    private void register_user(final String displayname, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser current_user=FirebaseAuth.getInstance().getCurrentUser();
                    String uid=current_user.getUid();
                    mDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("UserDetails");
                    HashMap<String,String> usermap=new HashMap<>();
                    usermap.put("name",displayname);
                    usermap.put("email",email);
                    usermap.put("password",password);
                    mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                regprogressDialog.dismiss();
                                Intent mainIntent=new Intent(RegistrationActivity.this,MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }
                        }
                    });





                }
                else{
                    regprogressDialog.hide();
                    Toast.makeText(RegistrationActivity.this, "Can not sign in please check the box and try again" +
                            ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}






