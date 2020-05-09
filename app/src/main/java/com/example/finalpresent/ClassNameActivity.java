package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalpresent.Adapters.ClassNameHandelerAdapter;
import com.example.finalpresent.DataHandeler.ClassName;
import com.example.finalpresent.DataHandeler.SubjectName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassNameActivity extends AppCompatActivity{


    private FirebaseAuth mAuth;
    private EditText classnameEdittext,subjectnameEdittext;
    private  Button clsNamesavebutton;
    private FirebaseUser mCurrentUser;
    DatabaseReference databaseReference;
    DatabaseReference mRootref;
    private  ProgressDialog progressDialog;

    private RecyclerView mainRecyclerView;

    private List<ClassName> classnameList;
    ClassNameHandelerAdapter customAdapter;
    String  current_uid;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_name);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainRecyclerView=findViewById(R.id.mainRecyclerviewid);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        classnameList=new ArrayList<>();

        mRootref=FirebaseDatabase.getInstance().getReference();
        customAdapter=new ClassNameHandelerAdapter(ClassNameActivity.this,classnameList);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        customAdapter.setOnItemClickListener(new ClassNameHandelerAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                goother(position);
            }

            @Override
            public void onDelete(int position) {

                ClassName selecteditem=classnameList.get(position);

                key=selecteditem.getKey();

                databaseReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ClassNameActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            sendtoStart();
        }
        else {
            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            current_uid = mCurrentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("Classname");
            databaseReference.keepSynced(true);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    classnameList.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ClassName className = dataSnapshot1.getValue(ClassName.class);
                        className.setKey(dataSnapshot1.getKey());
                        classnameList.add(className);

                    }
                    mainRecyclerView.setAdapter(customAdapter);
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.classname_menu,menu);


        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logoutMenuButtonid) {
            FirebaseAuth.getInstance().signOut();
            sendtoStart();
        }
        else if(item.getItemId()==R.id.classnameaddMenuButtonid){
            showcustomdioloage();
        }
        if(item.getItemId()==android.R.id.home){
            finish();

        }

        return  true;
    }

    public void sendtoStart(){
        Intent intent=new Intent(ClassNameActivity.this,StartActivity.class);
        startActivity(intent);
    }
    public void showcustomdioloage(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ClassNameActivity.this);
        View view=getLayoutInflater().inflate(R.layout.clssname_diolouge,null);
        builder.setView(view);
        subjectnameEdittext=view.findViewById(R.id.alert_subjectnameEdittext);
        classnameEdittext=view.findViewById(R.id.alert_classnameEdittext);
        clsNamesavebutton=view.findViewById(R.id.alert_classnameSaveButtonid);
        final AlertDialog dialog=builder.create();
        dialog.show();
        clsNamesavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }
    private void saveData() {

        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        current_uid=mCurrentUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("Classname");


        final String subjectname=subjectnameEdittext.getText().toString().trim();
        final String clsname=classnameEdittext.getText().toString().trim();
        if(clsname.isEmpty()){
            classnameEdittext.setError("Enter A Class Name Like(Computer 5cmtB2)");
            classnameEdittext.requestFocus();
            return;
        }
        else  if(subjectname.isEmpty()){
            subjectnameEdittext.setError("Enter The Subject Name");
            subjectnameEdittext.requestFocus();return;
        }

        else {
            String key=databaseReference.push().getKey();
            final ClassName className = new ClassName(clsname, subjectname);

            databaseReference.child(key).setValue(className).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    SubjectName subjectName=new SubjectName(subjectname);
                            mRootref.child("Users").child(current_uid).child("SubjectTopic").child(mRootref.push().getKey()).setValue(subjectName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(ClassNameActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                    classnameEdittext.setText("");
                                    subjectnameEdittext.setText("");
                                }
                            });
                }
            });

        }

    }

    public void goother(int position){

        if(position==0){
            changeActivity(position);
        }
        else if(position==1){
            changeActivity(position);
        }
        else if(position==2){
            changeActivity(position);
        }
        else if(position==3){
            changeActivity(position);
        }
        else if(position==4){
            changeActivity(position);
        }
        else if(position==5){
            changeActivity(position);
        }
        else if(position==6){
            changeActivity(position);
        }
        else if(position==7){
            changeActivity(position);
        }
        else if(position==8){
            changeActivity(position);
        }
        else if(position==9){
            changeActivity(position);
        }
        else if(position==10){
            changeActivity(position);
        }
        else if(position==11){
            changeActivity(position);
        }
        else if(position==12){
            changeActivity(position);
        }
        else if(position==13){
            changeActivity(position);
        }
        else if(position==14){
            changeActivity(position);
        }
        else if(position==15){
            changeActivity(position);
        }
        else if(position==16){
            changeActivity(position);
        }
        else if(position==17){
            changeActivity(position);
        }
        else if(position==18){
            changeActivity(position);
        }
        else if(position==19){
            changeActivity(position);
        }
        else if(position==20){
            changeActivity(position);
        }
        else if(position==21){
            changeActivity(position);
        }
        else if(position==22){
            changeActivity(position);
        }
        else if(position==23){
            changeActivity(position);
        }
        else if(position==24){
            changeActivity(position);
        }
        else if(position==25){
            changeActivity(position);
        }
        else if(position==26){
            changeActivity(position);
        }
        else if(position==27){
            changeActivity(position);
        }
        else if(position==28){
            changeActivity(position);
        }
        else if(position==29){
            changeActivity(position);
        }
        else if(position==30){
            changeActivity(position);
        }
        else if(position==31){
            changeActivity(position);
        }
        else if(position==32){
            changeActivity(position);
        }
        if(position==33){
            changeActivity(position);
        }
        else if(position==34){
            changeActivity(position);
        }
        else if(position==35){
            changeActivity(position);
        }
        else if(position==36){
            changeActivity(position);
        }
        else if(position==37){
            changeActivity(position);
        }
        else if(position==38){
            changeActivity(position);
        }
        else if(position==39){
            changeActivity(position);
        }
        else if(position==40){
            changeActivity(position);
        }
        else if(position==41){
            changeActivity(position);
        }
        else if(position==42){
            changeActivity(position);
        }
        else if(position==43){
            changeActivity(position);
        }
        else if(position==44){
            changeActivity(position);
        }
        else if(position==45){
            changeActivity(position);
        }      else if(position==46){
            changeActivity(position);
        }      else if(position==47){
            changeActivity(position);
        }      else if(position==48){
            changeActivity(position);
        }      else if(position==49){
            changeActivity(position);
        }
        else if(position==50){
            changeActivity(position);
        }


    }

    private void changeActivity(int position) {
        ClassName className=classnameList.get(position);

        String clsName=className.getClassname();
        String subjectname=className.getSubjectName();
        Intent intent=new Intent(ClassNameActivity.this,StudentNameShowerActivity.class);
        intent.putExtra("classname",clsName);
        intent.putExtra("key",key);
        intent.putExtra("subjectname",subjectname);
        startActivity(intent);
    }
}
