package com.example.finalpresent.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalpresent.DataHandeler.ChepterLIst;
import com.example.finalpresent.DataHandeler.ChepterValue;
import com.example.finalpresent.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChapterItemValueadapter extends RecyclerView.Adapter<ChapterItemValueadapter.MyViewHolder> {

    private Context context;
    private List<ChepterValue> chepterValueList;

    DatabaseReference databaseReference;

    private OnItemClickListner listner;

    public ChapterItemValueadapter(Context context, List<ChepterValue> chepterValueList) {
        this.context = context;
        this.chepterValueList = chepterValueList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.chepter_value_item_layoute,viewGroup,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChepterValue chepterValue=chepterValueList.get(position);

        holder.serialTextview.setText(chepterValue.getPartnumber());
        holder.questionTextview.setText(chepterValue.getQuestion());
        holder.answerTextview.setText(chepterValue.getAnswer());
        Picasso.with(context).load(chepterValue.getImage()).into(holder.serial_item_Imageview);

    }

    @Override
    public int getItemCount() {
        return chepterValueList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView  serialTextview;
        TextView  questionTextview;
        TextView  answerTextview;
        ImageView serial_item_Imageview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

             serialTextview=itemView.findViewById(R.id.serialTextviewid);
             questionTextview=itemView.findViewById(R.id.questionTextviewid);
             answerTextview=itemView.findViewById(R.id.answerTextviewid);
             serial_item_Imageview=itemView.findViewById(R.id.chapterValue_item_Imageviewid);

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
