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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalpresent.Adapters.MyAdapter;
import com.example.finalpresent.DataHandeler.StudentDetails;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class StudentNameShowerActivity extends AppCompatActivity {

    private FirebaseUser mCurrentUser;

    private Button studentDetailsSaveButton;
    private EditText studentNameEdittext,studentRollEdittext,studentGroupEdittext,studentShiftEdittext;
    private RecyclerView recyclerView;
    private List<StudentDetails> studentNameList;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    String classname,subjectname,key;
    MyAdapter adapter;
    String pastvalue;
    private FirebaseStorage firebaseStorage;

    String current_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_name_shower);

        Intent intent=getIntent();
        classname=intent.getStringExtra("classname");
        subjectname=intent.getStringExtra("subjectname");
        pastvalue=classname.concat(classname+""+subjectname);
        this.setTitle(subjectname);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.studentRecyclerViewid);
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        current_uid=mCurrentUser.getUid();


        firebaseStorage=FirebaseStorage.getInstance();

        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("StudentName").child(classname.concat(subjectname));
        databaseReference.keepSynced(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseStorage=FirebaseStorage.getInstance();


        studentNameList=new ArrayList<>();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        adapter=new MyAdapter(StudentNameShowerActivity.this,studentNameList);


        adapter.setOnitemclickListener(new MyAdapter.OnItemclickListener() {
            @Override
            public void onItemClick(int position) {
                goanotherActivity(position);
            }

            @Override
            public void onDelete(final int position) {

                StudentDetails selecteditem=studentNameList.get(position);

                final String key=selecteditem.getKey();

                databaseReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(StudentNameShowerActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
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

                studentNameList.clear();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    StudentDetails studentDetails=dataSnapshot1.getValue(StudentDetails.class);
                    studentDetails.setKey(dataSnapshot1.getKey());
                    studentNameList.add(studentDetails);
                }
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }






            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


   /*     databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(value)) {
                    //String request_type = dataSnapshot.child(userid).child("request_type").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.student_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.studentnameaddMenuButtonid){
            showcustomdioloage();
        }
        else if(item.getItemId()==android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveData() {

        String studentName=studentNameEdittext.getText().toString().trim();
        String studentRoll=studentRollEdittext.getText().toString().trim();
        String studentGroup=studentGroupEdittext.getText().toString().trim();
        String studentShift=studentShiftEdittext.getText().toString().trim();
        String key=databaseReference.push().getKey();


        if(studentName.isEmpty()){
            studentNameerror();
        }
        else if(studentRoll.isEmpty()){
            studentRollerror();
        }
        else if(studentGroup.isEmpty()){
            studentGrouperror();
        }
        else if(studentShift.isEmpty()){
            studentShifterror();
        }
        else {


            StudentDetails studentDetails = new StudentDetails(studentName, studentRoll, studentGroup, studentShift, subjectname);

            databaseReference.child(key).setValue(studentDetails);
            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();
            studentRollEdittext.setText("");
            studentNameEdittext.setText("");
        }

    }



    public void showcustomdioloage(){
        AlertDialog.Builder builder=new AlertDialog.Builder(StudentNameShowerActivity.this);
        View view=getLayoutInflater().inflate(R.layout.studentname_diolouge,null);
        builder.setView(view);

        studentNameEdittext=view.findViewById(R.id.alert_studentnameEdittext);
        studentRollEdittext=view.findViewById(R.id.alert_studentRollEdittextid);
        studentGroupEdittext=view.findViewById(R.id.alert_studentGroupEdittextid);
        studentShiftEdittext=view.findViewById(R.id.alertstudentShiftEdittextid);
        studentDetailsSaveButton=view.findViewById(R.id.alert_saveButtonid);
        final AlertDialog dialog=builder.create();
        dialog.show();
        studentDetailsSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }


    private void studentNameerror() {
        studentNameEdittext.setError("Pleaser Enter a Student Name");
        studentNameEdittext.requestFocus();
        return;

    }
    private void studentRollerror() {
        studentRollEdittext.setError("Pleaser Enter a Student Roll");
        studentRollEdittext.requestFocus();
        return;

    }
    private void studentGrouperror() {
        studentGroupEdittext.setError("Pleaser Enter a Group");
        studentGroupEdittext.requestFocus();
        return;


    }
    private void studentShifterror() {
        studentShiftEdittext.setError("Pleaser Enter Shift");
        studentShiftEdittext.requestFocus();
        return;

    }
    public void changeActivity(int position){
        StudentDetails studentDetails=studentNameList.get(position);
        String roll=studentDetails.getRoll();
        String name=studentDetails.getName();
        String group=studentDetails.getGroup();
        String shift=studentDetails.getShift();
        String subject=studentDetails.getSubjectname();
       String value2=roll.concat(name).concat(group).concat(shift).concat(subject);
        Intent intent=new Intent(StudentNameShowerActivity.this, StudentPresentShower.class);
        intent.putExtra("value",value2);
        intent.putExtra("roll",roll);
        intent.putExtra("pastvalue",pastvalue);
        intent.putExtra("studentname",name);
        startActivity(intent);

    }

    public void goanotherActivity(int position){
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
        }      else if(position==13){
            changeActivity(position);
        }      else if(position==14){
            changeActivity(position);
        }      else if(position==15){
            changeActivity(position);
        }      else if(position==16){
            changeActivity(position);
        }      else if(position==17){
            changeActivity(position);
        }      else if(position==18){
            changeActivity(position);
        }      else if(position==19){
            changeActivity(position);
        }      else if(position==20){
            changeActivity(position);
        }      else if(position==21){
            changeActivity(position);
        }      else if(position==22){
            changeActivity(position);
        }      else if(position==23){
            changeActivity(position);
        }      else if(position==24){
            changeActivity(position);
        }      else if(position==25){
            changeActivity(position);
        }      else if(position==26){
            changeActivity(position);
        }      else if(position==27){
            changeActivity(position);
        }      else if(position==28){
            changeActivity(position);
        }      else if(position==29){
            changeActivity(position);
        }      else if(position==30){
            changeActivity(position);
        }      else if(position==31){
            changeActivity(position);
        }      else if(position==32){
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
        }      else if(position==50){
            changeActivity(position);
        }      else if(position==51){
            changeActivity(position);
        }      else if(position==52){
            changeActivity(position);
        }      else if(position==53){
            changeActivity(position);
        }      else if(position==54){
            changeActivity(position);
        }      else if(position==55){
            changeActivity(position);
        }      else if(position==56){
            changeActivity(position);
        }      else if(position==57){
            changeActivity(position);
        }      else if(position==58){
            changeActivity(position);
        }      else if(position==59){
            changeActivity(position);
        }      else if(position==60){
            changeActivity(position);
        }      else if(position==61){
            changeActivity(position);
        }      else if(position==62){
            changeActivity(position);
        }      else if(position==63){
            changeActivity(position);
        }      else if(position==64){
            changeActivity(position);
        }      else if(position==65){
            changeActivity(position);
        }           else if(position==66){
            changeActivity(position);
        }      else if(position==67){
            changeActivity(position);
        }      else if(position==68){
            changeActivity(position);
        }      else if(position==69){
            changeActivity(position);
        }      else if(position==70){
            changeActivity(position);
        }      else if(position==71){
            changeActivity(position);
        }      else if(position==72){
            changeActivity(position);
        }      else if(position==73){
            changeActivity(position);
        }      else if(position==74){
            changeActivity(position);
        }      else if(position==75){
            changeActivity(position);
        }      else if(position==76){
            changeActivity(position);
        }      else if(position==77){
            changeActivity(position);
        }      else if(position==78){
            changeActivity(position);
        }      else if(position==79){
            changeActivity(position);
        }      else if(position==80){
            changeActivity(position);
        }      else if(position==81){
            changeActivity(position);
        }      else if(position==82){
            changeActivity(position);
        }      else if(position==83){
            changeActivity(position);
        }      else if(position==84){
            changeActivity(position);
        }      else if(position==85){
            changeActivity(position);
        }      else if(position==86){
            changeActivity(position);
        }

    }

}
