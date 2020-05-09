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


import android.view.View;
import android.widget.Button;
import com.example.finalpresent.Adapters.MainItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    private  RecyclerView mRecyclerview;

    private FirebaseAuth mAuth;



    private  String[] itemvalue={"Take Present","Add Topic"};
    MainItemAdapter adapter;
    private  int[] image={R.drawable.presence2,R.drawable.presentationimage};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        adapter=new MainItemAdapter(this,itemvalue,image);
        mRecyclerview=findViewById(R.id.item_RecyclerViewid);

        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(adapter);



        adapter.setOnItemClickListener(new MainItemAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {
                if(position==0){
                    Intent intent=new Intent(MainActivity.this,ClassNameActivity.class);
                    startActivity(intent);
                } if(position==1){
                    Intent intent=new Intent(MainActivity.this,SubjecctNameActivity.class);
                    startActivity(intent);
                }
            }
        });





    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            sendtoStart();
        }



    }

    @Override
    public void onBackPressed() {
        exitdiolouge();
    }
    public void exitdiolouge(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view=getLayoutInflater().inflate(R.layout.exitcustom_diolouge,null);
        builder.setView(view);

        Button yes,no;
        yes=view.findViewById(R.id.alertExityesbuttonid);
        no=view.findViewById(R.id.alertExitNobuttonid);

        final AlertDialog dialog=builder.create();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    public void sendtoStart(){
        Intent intent=new Intent(MainActivity.this,StartActivity.class);
        startActivity(intent);
    }



}
