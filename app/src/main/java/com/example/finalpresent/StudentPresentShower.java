package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalpresent.Adapters.CustomAdapter;
import com.example.finalpresent.DataHandeler.PresentDetails;
import com.example.finalpresent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class StudentPresentShower extends AppCompatActivity {
    private List<PresentDetails> presentList;
    DatabaseReference databaseReference;
    String rollnumber;
    CustomAdapter adapter;
    private TextView studentdetailsTextview;
    String studentname;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    private FirebaseStorage firebaseStorage;
    private FirebaseUser mCurrentUser;

    private EditText attendenceEdittext;
    private Button updateAttendanceButton;

  private DatabaseReference updatedatabaseRefrence;
    String   current_uid;

    int presentCount=0;
    int absentCount=0;
    String roll;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_present_shower);

        Intent intent=getIntent();
        rollnumber=intent.getStringExtra("value");
        studentname=intent.getStringExtra("studentname");
        roll=intent.getStringExtra("roll");

        this.setTitle(studentname);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        studentdetailsTextview=findViewById(R.id.studentdetailsshowerTextviewid);

        recyclerView=findViewById(R.id.attendenceRecyclerViewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();

        current_uid=mCurrentUser.getUid();
     databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("StudentPresence").child(rollnumber);
        databaseReference.keepSynced(true);
        firebaseStorage=FirebaseStorage.getInstance();
        presentList=new ArrayList<>();


     progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        adapter=new CustomAdapter(StudentPresentShower.this,presentList);

        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListner() {
            @Override
            public void OnItemClick(int position) {

            }

            @Override
            public void onUpdate(final int position) {



                final AlertDialog.Builder builder=new AlertDialog.Builder(StudentPresentShower.this);
                View view=getLayoutInflater().inflate(R.layout.update_presence_diolouge,null);
                builder.setView(view);
                attendenceEdittext=view.findViewById(R.id.updateAttendenceEdittextid);
                updateAttendanceButton=view.findViewById(R.id.update_attendenceButtonid);
                final AlertDialog dialog=builder.create();
                dialog.show();
                updateAttendanceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateAttendence(position);
                        dialog.dismiss();
                    }
                });

            }

            @Override
            public void onDelete(int position) {


                PresentDetails selecteditem=presentList.get(position);
                String key=selecteditem.getKey();

                databaseReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(StudentPresentShower.this, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

  @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                presentCount=0;
                absentCount=0;

                presentList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                 PresentDetails presentDetails=dataSnapshot1.getValue(PresentDetails.class);
                    presentDetails.setKey(dataSnapshot1.getKey());
                    String value=dataSnapshot1.child("attendence").getValue().toString();
                    if(value.equals("Present")){
                        presentCount++;

                    }else{
                            absentCount++;
                    }
                   // studentdetailsTextview.setText(roll+"\t\t               Total Class : "+(presentList.size()+1)+"\nPresent : "+presentCount+"\t \t \t         Absent : "+absentCount);
                     studentdetailsTextview.setText(roll+"\t\t                     Present : "+presentCount+"\nTotal Class : "+(presentList.size()+1)+"\t \t \t         Absent : "+absentCount);
                    presentList.add(presentDetails);

                }

                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudentPresentShower.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        super.onStart();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.devlopermenu,menu);


        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

           if(item.getItemId()==android.R.id.home){
           finish();

        }
           else if(item.getItemId()==R.id.devlopermenubuttonid){
               Intent devloperIntent=new Intent(StudentPresentShower.this,DevloperActivity.class);
               startActivity(devloperIntent);
           }
        return super.onOptionsItemSelected(item);
    }



   public void updateAttendence(int position){

       PresentDetails selecteditem=presentList.get(position);
       String key=selecteditem.getKey();

       String attendence=attendenceEdittext.getText().toString();
       updatedatabaseRefrence= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("StudentPresence").child(rollnumber).child(key);

       updatedatabaseRefrence.child("attendence").setValue(attendence).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(StudentPresentShower.this, "Changes Successfully", Toast.LENGTH_SHORT).show();

                    }
           }
       });
    }
}
