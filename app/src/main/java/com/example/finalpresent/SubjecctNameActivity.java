package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalpresent.Adapters.SubjectnameAdpater;
import com.example.finalpresent.DataHandeler.ClassName;
import com.example.finalpresent.DataHandeler.SubjectName;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubjecctNameActivity extends AppCompatActivity {


    private RecyclerView subjectRecyclerView;
    private SubjectnameAdpater adpater;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private  DatabaseReference mRootref;

    private List<SubjectName> subjectNames=new ArrayList<>();

    private FloatingActionButton floatingActionButton;



    // Alert Variables
    private EditText subjectnameEdittext;
    private Button saveData;

    String current_uid;


    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjecct_name);


        floatingActionButton=findViewById(R.id.floatingActionButtonid);



        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_uid = mCurrentUser.getUid();

        subjectRecyclerView=findViewById(R.id.subjectname_RecyclerViewid);
        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adpater =new SubjectnameAdpater(SubjecctNameActivity.this,subjectNames);

        subjectRecyclerView.setAdapter(adpater);

        mRootref=FirebaseDatabase.getInstance().getReference();

        adpater.setOnItemClickListener(new SubjectnameAdpater.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                SubjectName selcteditem=subjectNames.get(position);
                String selecteditem= selcteditem.getKey();

                sendtoOthers(position,selecteditem);


            }

            @Override
            public void onDelete(int position) {
                    SubjectName selcteditem=subjectNames.get(position);
                   String selecteditem= selcteditem.getKey();
                   mRootref.child("Users").child(current_uid).child("SubjectTopic").child(selecteditem).removeValue();
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            showcustomdioloage();
            }
        });




    }


    @Override
    public void onStart() {
        super.onStart();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("SubjectTopic");
        databaseReference.keepSynced(true);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subjectNames.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        SubjectName subjectName = dataSnapshot1.getValue(SubjectName.class);
                        subjectName.setKey(dataSnapshot1.getKey());
                        subjectNames.add(subjectName);
                        adpater.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    public void showcustomdioloage(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SubjecctNameActivity.this);
        View view=getLayoutInflater().inflate(R.layout.dioulouge_subject,null);
        builder.setView(view);
        subjectnameEdittext=view.findViewById(R.id.subject_subjectnameEdittext);
        saveData=view.findViewById(R.id.subject_subjectsaveButtonid);
        final AlertDialog dialog=builder.create();
        dialog.show();
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addSubjectname();
                    dialog.dismiss();
            }
        });
    }
    private void addSubjectname() {
        String subjectname=subjectnameEdittext.getText().toString();
        if(subjectname.isEmpty()){
            subjectnameEdittext.setError("Enter The Subject Name");
            subjectnameEdittext.requestFocus();
            return;
        }
        else{
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("SubjectTopic");

            SubjectName subjectName=new SubjectName(subjectname);
            databaseReference2.child(databaseReference2.push().getKey()).setValue(subjectName);
        }


    }

    public void sendtoOthers(int position,String key){
        if(position==0){
                changeActivity(key);
        } if(position==1){
                changeActivity(key);
        } if(position==2){
                changeActivity(key);
        } if(position==3){
                changeActivity(key);
        } if(position==4){
                changeActivity(key);
        } if(position==5){
                changeActivity(key);
        } if(position==6){
                changeActivity(key);
        } if(position==7){
                changeActivity(key);
        } if(position==8){
                changeActivity(key);
        } if(position==9){
                changeActivity(key);
        } if(position==10){
                changeActivity(key);
        } if(position==11){
                changeActivity(key);
        } if(position==12){
                changeActivity(key);
        }
    }

    public void changeActivity(String key){
        Intent intent=new Intent(SubjecctNameActivity.this,ChepterListActivity.class);
        intent.putExtra("subjectnamekey",key);
        startActivity(intent);

    }

}

