package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.finalpresent.Adapters.ChapterItemValueadapter;
import com.example.finalpresent.DataHandeler.ChepterLIst;
import com.example.finalpresent.DataHandeler.ChepterValue;
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

public class ChepterValueShowerActivity extends AppCompatActivity {

    String chepterKey;
    String subjectkey;
    DatabaseReference getDatabase;
    DatabaseReference databaseReference;

    String currentuser;
    FirebaseUser mFirebaseuser;


    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<ChepterValue> chepterValueList=new ArrayList<>();

    private ChapterItemValueadapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chepter_value_shower);


        chepterKey=getIntent().getStringExtra("chepterKey");
        subjectkey=getIntent().getStringExtra("subjectKey");

        mFirebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        currentuser=mFirebaseuser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuser)
                .child("Presentation")
                .child(subjectkey)
                .child("Data")
                .child(chepterKey);
        floatingActionButton=findViewById(R.id.chapter_value_FloatingActionbar);
        recyclerView=findViewById(R.id.chapter_value_Recyclerviewid);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new ChapterItemValueadapter(this,chepterValueList);
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChepterValueShowerActivity.this, TakeChepterValue.class);
                intent.putExtra("Firstvalue",chepterKey);
                intent.putExtra("subjectValue",subjectkey);
                startActivity(intent);
            }
        });



        adapter.setOnItemClickListener(new ChapterItemValueadapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void onDelete(int position) {

                 ChepterValue selecteditem=chepterValueList.get(position);
                    String keyvalue=selecteditem.getKey();
                     databaseReference.child(keyvalue).removeValue();
                     
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        getDatabase= FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuser)
                .child("Presentation")
                .child(subjectkey)
                .child("Data")
                .child(chepterKey);
        getDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chepterValueList.clear();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ChepterValue chepterValue = dataSnapshot1.getValue(ChepterValue.class);
                    chepterValue.setKey(dataSnapshot1.getKey());
                    chepterValueList.add(chepterValue);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.chepter_menuitem,menu);

        return  super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.add_chepterValuemenuitemid){
            Intent intent=new Intent(ChepterValueShowerActivity.this, TakeChepterValue.class);
            intent.putExtra("Firstvalue",chepterKey);
            intent.putExtra("subjectValue",subjectkey);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}
