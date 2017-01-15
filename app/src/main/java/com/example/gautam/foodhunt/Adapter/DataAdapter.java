package com.example.gautam.foodhunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gautam.foodhunt.ActProductInfo;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<ProductVersion> products;
    Context context;

    public DataAdapter(ArrayList<ProductVersion> products, Context context) {

        this.products = products;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.p_name.setText(products.get(position).getP_name());
        holder.p_info.setText(products.get(position).getP_info());
        holder.p_sold.setText(products.get(position).getP_sold());
        Picasso.with(context).load(products.get(position).getP_image())
                .into(holder.p_image);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView p_name, p_sold, p_info;
        private ImageView p_image;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            p_name = (TextView) itemView.findViewById(R.id.p_name);
            p_info = (TextView) itemView.findViewById(R.id.p_info);
            p_sold = (TextView) itemView.findViewById(R.id.p_sold);
            p_image=(ImageView)itemView.findViewById(R.id.p_image);



        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            // Toast.makeText(view.getContext(),products.get(pos).getP_name(),Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(context,ActProductInfo.class);
            intent.putExtra("DATAINTENT",products.get(pos).getP_id());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        }


    }
}
