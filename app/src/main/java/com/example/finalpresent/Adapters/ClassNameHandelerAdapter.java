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

import com.example.finalpresent.DataHandeler.ClassName;
import com.example.finalpresent.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ClassNameHandelerAdapter extends RecyclerView.Adapter<ClassNameHandelerAdapter.MyViewHolder> {

    private Context context;
    private List<ClassName> classNameslist;

    DatabaseReference databaseReference;

    private OnItemClickListner listner;

    public ClassNameHandelerAdapter(Context context, List<ClassName> classNameslist) {
        this.context = context;
        this.classNameslist = classNameslist;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.classname_listsample,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ClassName className=classNameslist.get(position);

        holder.nametextview.setText(className.getClassname());
        holder.subjectNameTextview.setText(className.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return classNameslist.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView nametextview,subjectNameTextview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

             nametextview=itemView.findViewById(R.id.textviewid);
             subjectNameTextview=itemView.findViewById(R.id.subjectnameTextviewid);



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
            MenuItem deleteitem=menu.add(Menu.NONE,1,1,"Delete");

            deleteitem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
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
        void onDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListner listener){
            this.listner=listener;

    }

}
