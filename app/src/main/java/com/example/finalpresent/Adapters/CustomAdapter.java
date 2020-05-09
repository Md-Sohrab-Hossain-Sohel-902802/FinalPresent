package com.example.finalpresent.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalpresent.DataHandeler.PresentDetails;
import com.example.finalpresent.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    private List<PresentDetails> presentDetailsList;
    DatabaseReference databaseReference;

    private OnItemClickListner listner;

    public CustomAdapter(Context context, List<PresentDetails> presentDetailsList) {
        this.context = context;
        this.presentDetailsList = presentDetailsList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.attendence_samplelayoute,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PresentDetails presentDetails=presentDetailsList.get(position);

        holder.timeTextview.setText(presentDetails.getCurrenttime());
        holder.dateTextview.setText(presentDetails.getCurrentDate());
        holder.attendenceTextview.setText(presentDetails.getAttendence());


    }

    @Override
    public int getItemCount() {
        return presentDetailsList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView attendenceTextview,timeTextview,dateTextview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            dateTextview=itemView.findViewById(R.id.dateTextviewid);
            timeTextview=itemView.findViewById(R.id.timeTextviewid);
            attendenceTextview=itemView.findViewById(R.id.attendenceTextviewid);



            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
                if(listner!=null){
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listner.OnItemClick(position);
                    }
                }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Chose an action");
            MenuItem doAnyTask=menu.add(Menu.NONE,1,1,"Update");
            MenuItem deleteitem=menu.add(Menu.NONE,2,2,"Delete");

            doAnyTask.setOnMenuItemClickListener(this);
            deleteitem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            listner.onUpdate(position);
                            return true;

                        case 2:
                            listner.onDelete(position);
                            return  true;
                    }
                }
            }

            return false;
        }
    }

    public interface  OnItemClickListner{
        void OnItemClick(int position);
        void onUpdate(int position);
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListner listener){
            this.listner=listener;

    }

}
