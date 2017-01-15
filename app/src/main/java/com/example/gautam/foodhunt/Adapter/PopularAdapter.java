package com.example.gautam.foodhunt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gautam.foodhunt.ActProductInfo;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gautam on 17/12/16.
 */

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    private ArrayList<ProductVersion> products;
    Context context;

    public PopularAdapter(ArrayList<ProductVersion> products, Context context) {

        this.products = products;
        this.context = context;
    }


    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_row, parent, false);
       // View rowview=LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);
        return new PopularAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.p_name.setText(products.get(position).getP_name());
        Picasso.with(context).load(products.get(position).getP_image()).resize(250,200).into(holder.p_image);


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
            p_name = (TextView) itemView.findViewById(R.id.popular_name);
            p_image=(ImageView)itemView.findViewById(R.id.popular_image);



        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            // Toast.makeText(view.getContext(),products.get(pos).getP_name(),Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(context,ActProductInfo.class);
            intent.putExtra("DATAINTENT",products.get(pos).getP_id());
            context.startActivity(intent);


        }


    }


}
