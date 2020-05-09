package com.example.finalpresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class DevloperActivity extends AppCompatActivity {

    private Button showbutton;
    private CircleImageView doyalimageview;
    private TextView textView;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devloper);


        getSupportActionBar().setTitle("Developer Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        showbutton=findViewById(R.id.showbuttonid);
        doyalimageview=findViewById(R.id.doyalcircaleimageview);
        textView=findViewById(R.id.doyaltextview);




    showbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count%2==0){
                    doyalimageview.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
                else{
                    doyalimageview.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
