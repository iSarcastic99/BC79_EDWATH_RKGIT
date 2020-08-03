package com.example.sih.Atmanirbhar.Atmanirbhar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.cloud.translate.Translate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<serviceCardView> details;
    ArrayList<serviceCardView> fullDetails;
    Translate translate;
    DatabaseReference reff;
    String check;


    public serviceAdapter(ArrayList<serviceCardView> d, Context c) {
        context = c;
        details = d;
        fullDetails = new ArrayList<>(d);
    }

    public serviceAdapter(ClientService c, ArrayList<String> content) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.service_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        try {
            holder.JobName.setText(details.get(position).getJobName());
            holder.Description.setText(details.get(position).getDescription());
            holder.Days.setText(details.get(position).getDays());
            holder.Number.setText(details.get(position).getPhone());

            final String phone = details.get(position).getPhone();
            Toast.makeText(context, phone, Toast.LENGTH_SHORT).show();
//            Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    final AlertDialog alertDialog1 = new AlertDialog.Builder(

                            context).create();

                        alertDialog1.setTitle("Connect");
                        alertDialog1.setMessage("How would you like to communicate?");

                        alertDialog1.setIcon(R.drawable.ic_completed_24);

                    alertDialog1.setButton(Dialog.BUTTON_POSITIVE,"Contact via Chat",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reff = FirebaseDatabase.getInstance().getReference().child("Users");
                            reff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try {
                                        String Username = snapshot.child(phone).child("Name").getValue().toString();
                                        Intent intent = new Intent(context, com.example.sih.chatApp.Chat.class);
                                        intent.putExtra("Phone", phone);
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

                    alertDialog1.setButton(Dialog.BUTTON_NEGATIVE,"Contact via Phone Call",new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" +91 + phone));
                            context.startActivity(callIntent);
                        }
                    });

                    alertDialog1.show();
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

        TextView JobName, Description, Number, Days;
        ImageView Profile_Picture;
        String M, J;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            JobName = itemView.findViewById(R.id.jobname);
            Description = itemView.findViewById(R.id.description);
            Number = itemView.findViewById(R.id.phone);
            Days = itemView.findViewById(R.id.days);
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
            ArrayList<serviceCardView> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullDetails);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (serviceCardView item : fullDetails){
                    try {
                        if (item.getJobName().toLowerCase().contains(filterPattern)){
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


}
