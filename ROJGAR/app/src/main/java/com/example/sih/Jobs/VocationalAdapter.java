package com.example.sih.Jobs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.cloud.translate.Translate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VocationalAdapter extends RecyclerView.Adapter<VocationalAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<vocationalCardView> details;
    ArrayList<vocationalCardView> fullDetails;
    String check;
    DatabaseReference reff;


    public VocationalAdapter(ArrayList<vocationalCardView> d, Context c) {
        context = c;
        details = d;
        fullDetails = new ArrayList<>(d);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.vocationalcardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {
            holder.JobName.setText(details.get(position).getJobName());
            holder.Description.setText(details.get(position).getDescription());
            holder.NumberOfDays.setText(details.get(position).getDays());
            holder.Phone.setText(details.get(position).getPhone());
            Thread thread = new Thread() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getImage(details.get(position).getPhone(), holder);
                            } catch(Exception e){

                            }

                        }
                    });
                }
            };
            thread.start();
            final String phone = details.get(position).getPhone();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    reff = FirebaseDatabase.getInstance().getReference().child("Users");
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            String[] ph = phone.split(": ");
                            String Username = snapshot.child(ph[1]).child("Name").getValue().toString();
                            Intent intent = new Intent(context, com.example.sih.chatApp.Chat.class);
                            intent.putExtra("Phone", ph[1]);
                            intent.putExtra("Username", Username);
                            view.getContext().startActivity(intent);
                        } catch (Exception e) {
                            if(check.equals("Eng")){
                                Toast.makeText(context, "This user is not present over chat platform", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "यह उपयोगकर्ता चैट प्लेटफॉर्म पर मौजूद नहीं है", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return details ==null ? 0 : details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView JobName, Description, NumberOfDays, Phone;
        ImageView Profile_Picture;
        String M, J;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            JobName = itemView.findViewById(R.id.jobName);
            Description = itemView.findViewById(R.id.jobDescription);
            NumberOfDays = itemView.findViewById(R.id.numberOfDays);
            Phone = itemView.findViewById(R.id.phone);
            Profile_Picture = itemView.findViewById(R.id.profile_picture);
            check = preferences.getString("Lang","Eng");
        }
    }

    @Override
    public Filter getFilter() {
        return detailsFilter;
    }

    private Filter detailsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<vocationalCardView> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullDetails);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (vocationalCardView item : fullDetails){
                    try {
                        if (item.getJobName().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getDescription().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                        else if (item.getPhone().toLowerCase().contains(filterPattern)){
                            filteredList.add(item);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            details.clear();
            details.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public void getImage(String user, final MyViewHolder holder){
        final Bitmap[] bm = new Bitmap[1];
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(user + "/Profile Picture");
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bm[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.Profile_Picture.setImageBitmap(bm[0]);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

}
