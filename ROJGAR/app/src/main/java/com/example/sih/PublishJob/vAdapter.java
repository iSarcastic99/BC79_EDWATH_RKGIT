package com.example.sih.PublishJob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;

import java.util.ArrayList;

public class vAdapter extends RecyclerView.Adapter<vAdapter.MyViewHolder> {

    Context context;
    ArrayList<vocationalPost> posts;

    public vAdapter(Context c, ArrayList<vocationalPost> j){
        context = c;
        posts = j;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview1,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(posts.get(position).getJobName());
        holder.description.setText(posts.get(position).getDescription());
        holder.duration.setText(posts.get(position).getDays());
        holder.contact.setText(posts.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, duration, contact;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.desc);
            duration = itemView.findViewById(R.id.time);
            contact = itemView.findViewById(R.id.contact);

        }
    }

}