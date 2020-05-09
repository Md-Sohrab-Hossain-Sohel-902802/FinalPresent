package com.example.finalpresent.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalpresent.DataHandeler.PresentDetails;
import com.example.finalpresent.DataHandeler.StudentDetails;
import com.example.finalpresent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    DatabaseReference databaseReference;
    private Context context;
    private List<StudentDetails> studentDetailsList;
    private static OnItemclickListener listener;
    private FirebaseUser mCurrentUser;

    public MyAdapter(Context context, List<StudentDetails> studentDetailsList) {
        this.context = context;
        this.studentDetailsList = studentDetailsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.studentname_samplelayoute,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        StudentDetails studentDetails=studentDetailsList.get(position);
        holder.nameTextview.setText(studentDetails.getName());
        holder.rollTextview.setText(studentDetails.getRoll());
        holder.absentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(position,"Absent");
            }
        });
        holder.presentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(position,"Present");
            }
        });

    }

    @Override
    public int getItemCount() {
        return studentDetailsList.size();
    }
/*
    public void filterList(ArrayList<StudentDetails> filteredList){
            studentDetailsList=filteredList;
            notifyDataSetChanged();
    }*/

    public static class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView nameTextview,rollTextview;
        Button presentButton,absentButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextview=itemView.findViewById(R.id.nameTextviewid);
            rollTextview=itemView.findViewById(R.id.rollTextview);
            presentButton=itemView.findViewById(R.id.presentButtonid);
            absentButton=itemView.findViewById(R.id.absentbuttonid);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Chose an action");
            MenuItem deleteitem=menu.add(Menu.NONE,1,1,"Delete");

            deleteitem.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(listener!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:

                            listener.onDelete(position);
                                    return  true;
                    }
                }
            }


            return false;
        }
    }
    public interface  OnItemclickListener{
        void onItemClick(int position);


        void onDelete(int position);


    }
    public void setOnitemclickListener(OnItemclickListener listener){
        this.listener=listener;

    }
    private void saveData(int position,String attendence) {



        String roll,name,group,shift;
        StudentDetails studentDetails=studentDetailsList.get(position);
        roll=studentDetails.getRoll();
        name=studentDetails.getName();
        group=studentDetails.getGroup();
        shift=studentDetails.getShift();
        String subjectname=studentDetails.getSubjectname();
        String value=roll.concat(name).concat(group).concat(shift).concat(subjectname);
         mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        String   current_uid=mCurrentUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("StudentPresence").child(value);
         String key=databaseReference.push().getKey();


        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        Date d = new Date();
        String date = (String) DateFormat.format("MMMM d, yyyy ", d.getTime());
        PresentDetails presentDetails=new PresentDetails(roll,currentTime,date,attendence);
        databaseReference.child(key).setValue(presentDetails);
        Toast.makeText(context, roll+" Is : "+attendence, Toast.LENGTH_SHORT).show();
    }





}
