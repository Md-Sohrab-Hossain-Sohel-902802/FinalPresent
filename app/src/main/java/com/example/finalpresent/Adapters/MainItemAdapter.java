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

import com.example.finalpresent.R;

public class MainItemAdapter  extends RecyclerView.Adapter<MainItemAdapter.MyViewHolder> {
    private Context context;
    private String[] itemString;
    private int[] image;
    private  OnItemClickListner listner;




    public MainItemAdapter(Context context, String[] itemString, int[] image) {
        this.context = context;
        this.itemString = itemString;
        this.image = image;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_item_layout, parent, false);


        return new MainItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(itemString[position]);
        holder.imageView.setImageResource(image[position]);

    }

    @Override
    public int getItemCount() {
        return itemString.length;
    }



    public class MyViewHolder  extends RecyclerView.ViewHolder  implements View.OnClickListener {

        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.main_item_MainTextviewid);
            imageView = itemView.findViewById(R.id.imageviewid);

            itemView.setOnClickListener(this);

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

    }

    public interface  OnItemClickListner{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(MainItemAdapter.OnItemClickListner listener){
        this.listner=listener;

    }

}
