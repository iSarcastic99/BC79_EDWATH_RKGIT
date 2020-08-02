package com.example.sih.Atmanirbhar.Atmanirbhar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sih.R;
import com.google.cloud.translate.Translate;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<ProductCardView> details;
    ArrayList<ProductCardView> fullDetails;
    Translate translate;
    String check;


    public ProductAdapter(ArrayList<ProductCardView> d, Context c) {
        context = c;
        details = d;
        fullDetails = new ArrayList<>(d);
    }

    public ProductAdapter(ClientService c, ArrayList<String> content) {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.product_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        try {
            holder.ProductName.setText(details.get(position).getProductName());
            holder.Description.setText(details.get(position).getDescription());
            holder.Price.setText(details.get(position).getPrice());
            holder.Number.setText(details.get(position).getPhone());


//            Picasso.get().load(details.get(position).getCompany_logo()).into(holder.company_logo);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(final View view) {
//
//                    Intent intent = new Intent(context, com.example.sih.chatApp.Chat.class);
//                    String phone = details.get(position).getPhone();
//                    String jobName = details.get(position).getJobName();
//                    intent.putExtra("Phone", phone);
//                    intent.putExtra("jobName", jobName);
//                    view.getContext().startActivity(intent);
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return details == null ? 0 : details.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ProductName, Description, Number, Price;
        String M, J;
        int j;
        SharedPreferences preferences = context.getSharedPreferences(M,j);

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            ProductName = itemView.findViewById(R.id.productname);
            Description = itemView.findViewById(R.id.description);
            Number = itemView.findViewById(R.id.phone);
            Price = itemView.findViewById(R.id.price);
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
            ArrayList<ProductCardView> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullDetails);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ProductCardView item : fullDetails){
                    try {
                        if (item.getProductName().toLowerCase().contains(filterPattern)){
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
