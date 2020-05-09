package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalpresent.Adapters.Chepteradapter;
import com.example.finalpresent.DataHandeler.ChepterLIst;
import com.example.finalpresent.DataHandeler.SubjectName;
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

public class ChepterListActivity extends AppCompatActivity {

    String subjectkey;
    private FloatingActionButton floatingActionButton;
    
    
    
    //diolouge
    private EditText chepternameEdittext;
    private Button chepterSaveButton;

    String currentUser;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootref;
    private DatabaseReference getDataDatabase;

    List<ChepterLIst> chepterLIsts=new ArrayList<>();

    Chepteradapter chepteradapter;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chepter_list);

        subjectkey=getIntent().getStringExtra("subjectnamekey");




        floatingActionButton=findViewById(R.id.cepter_FloatingActionBar);
        recyclerView=findViewById(R.id.chepter_Recyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


         chepteradapter=new Chepteradapter(ChepterListActivity.this,chepterLIsts);
        recyclerView.setAdapter(chepteradapter);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = mCurrentUser.getUid();
        mRootref= FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("Presentation").child(subjectkey);
        getDataDatabase=FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("Presentation").child(subjectkey).child("Chepter");
       getDataDatabase.keepSynced(true);

       chepteradapter.setOnItemClickListener(new Chepteradapter.OnItemClickListner() {
           @Override
           public void OnItemClick(int position) {
               ChepterLIst selecteditem=chepterLIsts.get(position);
               String key=selecteditem.getKey();
               sendtoOthers(position,key);

           }

           @Override
           public void onDelete(int position) {

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
    protected void onStart() {
        super.onStart();

        getDataDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chepterLIsts.clear();

                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        ChepterLIst chepterLIst = dataSnapshot1.getValue(ChepterLIst.class);
                        chepterLIst.setKey(dataSnapshot1.getKey());
                        chepterLIsts.add(chepterLIst);
                        chepteradapter.notifyDataSetChanged();

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void showcustomdioloage(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ChepterListActivity.this);
        View view=getLayoutInflater().inflate(R.layout.chepterlist_custom_diolouge,null);
        builder.setView(view);
        chepternameEdittext=view.findViewById(R.id.cheptername_Edittext);
        chepterSaveButton=view.findViewById(R.id.chepter_SaveButtonid);
        final AlertDialog dialog=builder.create();
        dialog.show();
        chepterSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChepterName();
                dialog.dismiss();
            }
        });
    }

    private void addChepterName() {

          String cheptername=  chepternameEdittext.getText().toString();
            if(cheptername.isEmpty()){
                chepternameEdittext.setError("Enter A Chapter Name(Like Chapter-1)");
                chepternameEdittext.requestFocus();
                return;
            }
            else{
                mRootref.child("Chepter").child(mRootref.push().getKey()).child("Lesson").setValue(cheptername);
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
        Intent intent=new Intent(ChepterListActivity.this,ChepterValueShowerActivity.class);
        intent.putExtra("chepterKey",key);
        intent.putExtra("subjectKey",subjectkey);
        startActivity(intent);

    }

}
