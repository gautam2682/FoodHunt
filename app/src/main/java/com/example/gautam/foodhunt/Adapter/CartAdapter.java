package com.example.gautam.foodhunt.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gautam.foodhunt.Constants;
import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.User;
import com.example.gautam.foodhunt.R;
import com.example.gautam.foodhunt.RequestInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

    public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
        private ArrayList<ProductVersion> products;
        Context context;
        onClickremove listener; ArrayList<String> ids,nois;

        public CartAdapter(ArrayList<ProductVersion> products, Context context,onClickremove listener) {

            this.products = products;
            this.context = context;
            this.listener=listener;
        }
    public interface onClickremove {
        public void onClickremovelist(int pos);

        public void ordercart(ArrayList<String> ids, ArrayList<String> nois);

        void spinnerchange(int adapterView, String noi);
    }

        @Override
        public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
            return new CartAdapter.ViewHolder(view);
        }
    @Override
        public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
            holder.p_name.setText(products.get(position).getP_name());
            holder.p_info.setText(products.get(position).getP_info());
            holder.p_sold.setText(products.get(position).getP_sold());
            Picasso.with(context).load(products.get(position).getP_image()).into(holder.p_image);
            holder.spinner.setSelection(0);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener {
            private TextView p_name, p_sold, p_info;
            Button p_remove;
            private ImageView p_image;Spinner spinner;  ArrayAdapter<Integer> adapter;


            public ViewHolder(View itemView) {
                super(itemView);

                p_name = (TextView) itemView.findViewById(R.id.p_name);
                p_info = (TextView) itemView.findViewById(R.id.p_info);
                p_sold = (TextView) itemView.findViewById(R.id.p_sold);
                p_image=(ImageView)itemView.findViewById(R.id.p_image);
                spinner=(Spinner)itemView.findViewById(R.id.spinner_pickercart);
                Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
                adapter = new ArrayAdapter<Integer>(itemView.getContext(),android.R.layout.simple_spinner_item, items);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(this);
                p_remove=(Button)itemView.findViewById(R.id.btn_remove_cart);
                itemView.setOnClickListener(this);
                p_remove.setOnClickListener(this);
                ids=new ArrayList<String>();
                nois = new ArrayList<String>();
                listener.ordercart(ids ,nois);

            }

            @Override
            public void onClick(View view) {
                final int pos = getAdapterPosition();

                if(view.getId()==p_remove.getId()){
                        loadremovejson(pos,view);
                }


            }
            private void loadremovejson(int pos, final View view) {
                SharedPreferences pref=context.getSharedPreferences("ABC",Context.MODE_PRIVATE);
                listener.onClickremovelist(pos);
                final Context context=view.getContext();
                final ProgressDialog pd = new ProgressDialog(context);
                pd.show();
                pd.setMessage(products.get(pos).getP_name()+ " removing from cart");
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.base_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                User user =new User();
                user.setEmail(pref.getString(Constants.EMAIL," "));
                user.setP_id(products.get(pos).getP_id());
                final ServerRequest request=new ServerRequest();
                request.setOperation(Constants.removecart);
                request.setUser(user);
                RequestInterface requestInterface=retrofit.create(RequestInterface.class);
                Call<ProductResponse> call =requestInterface.operation(request);
                call.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        pd.dismiss();
                        ProductResponse productResponse =response.body();
                        products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                        notifyDataSetChanged();


                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                         pd.dismiss();
                        // Snackbar.make(getCurrentFocus(),"Connection problem",Snackbar.LENGTH_LONG).show();
                        Toast.makeText(context,"Not able to remove ",Toast.LENGTH_SHORT).show();


                    }
                });

            }

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String noi =adapterView.getItemAtPosition(i).toString();
                listener.spinnerchange(getAdapterPosition(),noi);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }
}
