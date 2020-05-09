package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalpresent.Adapters.ChapterItemValueadapter;
import com.example.finalpresent.DataHandeler.ChepterValue;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TakeChepterValue extends AppCompatActivity {

    private EditText chepter_PartNumberEdittext;
    private  EditText chepter_partDiscriptionEdittext;
    private Button cheptervalue_SaveButton;
    private ImageView chepter_Imageview;
    private EditText chepterAnswerEdittext;

    private Uri imageUri=null;

    private static  final  int IMAGE_REQUEST=1;


    String chapterValuekey;
    String subjectValuekey;
    String currentuser;
    FirebaseUser mFirebaseuser;
    StorageReference storageReference;

     DatabaseReference mDatabaseRefrence;


     private ProgressDialog progressDialog;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_chepter_value);




        chapterValuekey=getIntent().getStringExtra("Firstvalue");
        subjectValuekey=getIntent().getStringExtra("subjectValue");
        mFirebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        currentuser=mFirebaseuser.getUid();
        storageReference= FirebaseStorage.getInstance().getReference();


        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Saving Data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait");


        Toast.makeText(this, chapterValuekey+"\nSubject key"+subjectValuekey , Toast.LENGTH_LONG).show();

        chepter_PartNumberEdittext=findViewById(R.id.takechepter_partEdittextid);
        chepter_partDiscriptionEdittext=findViewById(R.id.takechepter_questionEdittextid);
        cheptervalue_SaveButton=findViewById(R.id.takechepterValueSaveButtonid);
        chepter_Imageview=findViewById(R.id.takechepter_ImageViewid);
        chepterAnswerEdittext=findViewById(R.id.takechepter_AnswerEdittextid);


        chepter_Imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();
            }
        });







        cheptervalue_SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveValue();
            }
        });



    }

    private void openFilechooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data.getData()!=null){
                imageUri=data.getData();
            Picasso.with(this).load(imageUri).into(chepter_Imageview);
        }



    }




    public String getFileExtension(Uri imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }


    private void saveValue() {
        mDatabaseRefrence= FirebaseDatabase.getInstance().getReference("Users");
       final String chepterid=chepter_PartNumberEdittext.getText().toString();
      final   String chepterQuestion=chepter_partDiscriptionEdittext.getText().toString();
      final   String chepterAnswer=chepterAnswerEdittext.getText().toString();

        if(chepterid.isEmpty() || chepterQuestion.isEmpty() || chepterAnswer.isEmpty()){
            Toast.makeText(this, "Add all Value", Toast.LENGTH_SHORT).show();
        }
        else {
                try {
                    progressDialog.show();
                    String extension2 = getFileExtension(imageUri);
                    StorageReference filePath = storageReference.child("ChapterValueImage").child(mDatabaseRefrence.push().getKey() + chepterid + extension2);
                    filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloaduri = urlTask.getResult();

                                ChepterValue chepterValue = new ChepterValue(chepterid, chepterQuestion, chepterAnswer, downloaduri.toString());
                                mDatabaseRefrence.child(currentuser)
                                        .child("Presentation")
                                        .child(subjectValuekey)
                                        .child("Data")
                                        .child(chapterValuekey)
                                        .child(mDatabaseRefrence.push().getKey())
                                        .setValue(chepterValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(TakeChepterValue.this, ChepterValueShowerActivity.class);
                                        Toast.makeText(TakeChepterValue.this, "Success", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        startActivity(intent);
                                    }
                                });

                            }
                        }
                    });
                }catch (Exception e){
                    progressDialog.dismiss();
                    Toast.makeText(this, "Add An Image", Toast.LENGTH_SHORT).show();
                }

                    }

        }

    }

