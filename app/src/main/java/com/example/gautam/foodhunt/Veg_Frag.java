package com.example.gautam.foodhunt;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gautam.foodhunt.Adapter.DataAdapter;
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

/**
 * Created by gautam on 14/12/16.
 */

public class Veg_Frag extends Fragment {
    RecyclerView recyclerview;private DataAdapter dataAdapter;
    private ArrayList<ProductVersion> products;
    ProgressBar progressBar; String cate;
    FrameLayout coordinatorLayout;
    String titlename;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.veg_frag,container,false);
         cate=this.getArguments().getString("cat");
        titlename=this.getArguments().getString("name");
        getActivity().setTitle(titlename);
         initViews(view);
        return view;
    }

    private void initViews(View view) {
        coordinatorLayout=(FrameLayout)view.findViewById(R.id.frame_veg);
        recyclerview= (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutmanager=new GridLayoutManager(getActivity(),2);
        recyclerview.setLayoutManager(layoutmanager);
        progressBar=(ProgressBar)view.findViewById(R.id.progress_Spinner);
        progressBar.setVisibility(View.VISIBLE);
        loadJson();

    }

    private void loadJson() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user =new User();
        user.setCategory(cate);
        ServerRequest serverRequest=new ServerRequest();
        serverRequest.setOperation(Constants.getfromcategory);
        serverRequest.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> call =requestInterface.operation(serverRequest);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                ProductResponse productResponse =response.body();
                products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                dataAdapter=new DataAdapter(products,getActivity());
                recyclerview.setAdapter(dataAdapter);
                Snackbar.make(coordinatorLayout,"Products successfully loaded",Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(coordinatorLayout,"Connection problem",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent =new Intent(getActivity(),MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).show();

            }
        });


    }
}
