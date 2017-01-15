package com.example.gautam.foodhunt;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gautam.foodhunt.Adapter.PopularAdapter;
import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ProductVersion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gautam on 14/12/16.
 */

public class Main_frag extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    ArrayList<ProductVersion> products;
    RecyclerView recyclerview;
    ProgressBar progressbar;
    PopularAdapter popularAdapter;
    FrameLayout linearlayot;
    MainActivity mainActivity;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout;
    OkHttpClient client;
    static Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, container, false);
        mainActivity = new MainActivity();
        getActivity().setTitle(getTimeFromAndroid());
        initView(view);
        OkCacheit();
        //  loadfragjson(view );

        loadrxjava(view);
        return view;
    }


    private void initView(View v) {
         context=getActivity().getApplicationContext();
        linearLayout = (LinearLayout) v.findViewById(R.id.linearalayout_main);
        linearLayout.setVisibility(View.INVISIBLE);
        linearlayot = (FrameLayout) v.findViewById(R.id.pop_linear);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.color5, R.color.color1, R.color.color2,
                R.color.color3, R.color.color4, R.color.color6);
        //  linearlayot.setBackgroundResource(R.drawable.food);
        progressbar = (ProgressBar) v.findViewById(R.id.progress_Spinner);
        recyclerview = (RecyclerView) v.findViewById(R.id.recycler_popular);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearmanager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearmanager);


    }

    private void loadfragjson(final View view) {
        progressbar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ProductResponse> call = requestInterface.getProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                initView(view);
                progressbar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                ProductResponse productResponse = response.body();
                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                popularAdapter = new PopularAdapter(products, getActivity());
                recyclerview.setAdapter(popularAdapter);
                Snackbar.make(linearlayot, "items successfully loaded", Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressbar.setVisibility(View.INVISIBLE);
                ShowFailsnackbar();


            }
        });

        Call<ProductResponse> call2 = requestInterface.getProducts();
        call2.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                initTopView(view, R.id.recycler_top);
                ProductResponse productResponse = response.body();
                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                popularAdapter = new PopularAdapter(products, getActivity());
                recyclerview.setAdapter(popularAdapter);
                Snackbar.make(linearlayot, "items successfully loaded again", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                ShowFailsnackbar();

            }
        });
        call.clone().enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                initTopView(view, R.id.recycler_trending);
                ProductResponse productResponse = response.body();
                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                popularAdapter = new PopularAdapter(products, getActivity());
                recyclerview.setAdapter(popularAdapter);
                Snackbar.make(linearlayot, "items successfully loaded third call", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                ShowFailsnackbar();

            }
        });

    }

    private void ShowFailsnackbar() {
        Snackbar.make(linearlayot, "Connection problem", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                    }
                }).show();

    }

    private void initTopView(View view, int id) {
        recyclerview = (RecyclerView) view.findViewById(id);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearmanager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearmanager);
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onRefresh() {

        View view = getView();
        loadfragjson(view);
        swipeRefreshLayout.setRefreshing(false);


    }


    private String getTimeFromAndroid() {
        String greeting = null;
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);


        if (hours >= 0 && hours <= 12) {
            greeting = "Good Morning";
        } else if (hours >= 12 && hours <= 16) {
            greeting = "Good Afternoon";
        } else if (hours >= 16 && hours <= 21) {
            greeting = "Good Evening";
        } else if (hours >= 21 && hours <= 24) {
            greeting = "Good Night";
        }
        return greeting;
    }

    private void loadrxjava(final View view) {
        progressbar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RxjavaInterface rxinterface = retrofit.create(RxjavaInterface.class);
        Observable<ProductResponse> observable = rxinterface.getProducts();
        Subscription s1 = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ProductResponse>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), "Rx completed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProductResponse productResponse) {
                        for (int i = 0; i < 3; i++) {
                            if (i == 0) {
                                progressbar.setVisibility(View.INVISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                popularAdapter = new PopularAdapter(products, getActivity());
                                recyclerview.setAdapter(popularAdapter);
                                Snackbar.make(linearlayot, "items successfully loaded", Snackbar.LENGTH_SHORT).show();
                            }
                            if (i == 1) {
                                initTopView(view, R.id.recycler_top);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                popularAdapter = new PopularAdapter(products, getActivity());
                                recyclerview.setAdapter(popularAdapter);
                                Snackbar.make(linearlayot, "items successfully loaded again", Snackbar.LENGTH_SHORT).show();
                            }
                            if (i == 2) {
                                initTopView(view, R.id.recycler_trending);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                popularAdapter = new PopularAdapter(products, getActivity());
                                recyclerview.setAdapter(popularAdapter);
                                Snackbar.make(linearlayot, "items successfully loaded third call", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }


                });
    }

    public void OkCacheit() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(new File(getActivity().getApplication().getCacheDir(),"okhttp"), cacheSize);

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
           client=new OkHttpClient.Builder()
                   .cache(cache)
                   .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                   .build();

    }
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            if (Utils.isNetworkAvailable(context)) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }

        }
    };




}
