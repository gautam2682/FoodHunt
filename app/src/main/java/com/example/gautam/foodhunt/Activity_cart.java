package com.example.gautam.foodhunt;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gautam.foodhunt.Adapter.CartAdapter;
import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.User;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_cart extends AppCompatActivity {
    RecyclerView recyclerview;
    ProgressBar progressBar;
    ArrayList<ProductVersion> products;
    CartAdapter cartAdapter;
    Toolbar ordertoolbar;  Button btnorder;CoordinatorLayout coordinatorLayout;ArrayList<String> idsA,noiA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cart");
        initView();


    }




    private void initView() {
        btnorder=(Button)findViewById(R.id.order_btn);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_cart);
        recyclerview= (RecyclerView) findViewById(R.id.recycler_view_cart);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutmanager=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutmanager);
        progressBar=(ProgressBar)findViewById(R.id.progress_Spinner_cart);
        progressBar.setVisibility(View.VISIBLE);
        loadJson();

    }

    private void loadJson() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user =new User();
        user.setEmail("7827789246");

        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.showcart);
        request.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> call =requestInterface.operation(request);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                idsA=new ArrayList<String>();
                noiA=new ArrayList<String>();
                progressBar.setVisibility(View.INVISIBLE);
                ProductResponse productResponse =response.body();
                products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                for(int i=0;i<products.size();i++){
                    idsA.add(products.get(i).getP_id());
                    noiA.add("1");
                }
                cartAdapter=new CartAdapter(products, getBaseContext(), new CartAdapter.onClickremove() {
                    @Override
                    public void onClickremovelist(int pos) {
                        idsA.remove(pos);
                        noiA.remove(pos);

                    }

                    @Override
                    public void ordercart(ArrayList<String> ids, ArrayList<String> nois) {
                       ordermycart(ids,nois);
                    }

                    @Override
                    public void spinnerchange(int i, String noi) {
                        noiA.add(i,noi);
                    }
                });
                recyclerview.setAdapter(cartAdapter);

                Toast.makeText(getApplicationContext(),"Cart loaded ",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(coordinatorLayout,"Cart Failure",Snackbar.LENGTH_SHORT).show();


            }
        });


    }

    public void ordermycart(final ArrayList<String> ids, final ArrayList<String> nois){
            btnorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog progressDialog=new ProgressDialog(Activity_cart.this);
                    progressDialog.setMessage("Ordering items");
                    progressDialog.show();
                    for (int i = 0; i < idsA.size(); i++) {
                     Log.d("IDS",idsA.get(i));
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Constants.base_url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                        User user = new User();
                        user.setEmail(Constants.email);
                        user.setP_id(idsA.get(i));
                        user.setNoi(noiA.get(i));
                        final ServerRequest request = new ServerRequest();
                        request.setOperation(Constants.ordercart);
                        request.setUser(user);
                        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                        Call<ProductResponse> call = requestInterface.operation(request);
                        call.enqueue(new Callback<ProductResponse>() {
                            @Override
                            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                                progressDialog.dismiss();
                                Snackbar.make(coordinatorLayout, "Order placed ", Snackbar.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(Call<ProductResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Snackbar.make(coordinatorLayout, "Connection problem", Snackbar.LENGTH_LONG).show();

                            }
                        });


                    }
                }
            });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
