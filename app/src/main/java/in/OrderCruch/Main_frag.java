package in.OrderCruch;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.synnapps.carouselview.CarouselView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


import in.OrderCruch.Adapter.Carousel_Adapter;
import in.OrderCruch.Adapter.DataAdapter;
import in.OrderCruch.Adapter.PopularAdapter;
import in.OrderCruch.Modal.ProductResponse;
import in.OrderCruch.Modal.ProductVersion;
import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.User;
import okhttp3.OkHttpClient;
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
    Carousel_Adapter carousel_adapter;
    FrameLayout linearlayot;
    MainActivity mainActivity;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout linearLayout;CoordinatorLayout coordtinator;
    OkHttpClient client;
    static Context context;
    CarouselView carouselView;
    int NUMBER_OF_PAGES = 5;
    int[] sampleImages = {R.drawable.burger, R.drawable.fruit};
    String[] sampleTitles = {"Orange", "Grapes", "Strawberry", "Cherry", "Apricot"};

    TextView textmarufaz,textordercrunch,textdwat;
    String resturant;
    String slider;
    String popular;

    String[] sampleNetworkImageURLs = {
            "http://www.buildupcareer.com/gauti/Hunt/Burger.jpg",
            "http://www.buildupcareer.com/gauti/Hunt/pizza.jpg",
            "http://www.buildupcareer.com/gauti/Hunt/Food.jpg",
            "http://www.buildupcareer.com/gauti/Hunt/Food.jpg",
            "http://www.buildupcareer.com/gauti/Hunt/Burger.jpg"

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, container, false);
        mainActivity = new MainActivity();
        getActivity().setTitle(getTimeFromAndroid());
        initView(view);
        resturant=getArguments().getString(Constants.RESTURANT);
        slider=getArguments().getString(Constants.MARUFAZ_SLID);
        popular=getArguments().getString(Constants.MARUFAZ_CAR);
        //loadrxjava(view);
        loadretrofit(view);
     //   loadcarousel(view);
      //  initializerecyclerads(view);
        return view;
    }

    private void loadretrofit(final View view) {

        //FIRST CALL
        progressbar.setVisibility(View.VISIBLE);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final User user =new User();
        user.setResturant(resturant);
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.gettoprated);
        request.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> call =requestInterface.operation(request);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
               // progressbar.setVisibility(View.INVISIBLE);
                initTopView(view, R.id.recycler_popular);
                progressbar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                ProductResponse productResponse =response.body();
                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                popularAdapter = new PopularAdapter(products, getActivity(), new PopularAdapter.Onclicklistnerpopular() {
                    @Override
                    public void Onclikpos() {
                    }
                });
                recyclerview.setAdapter(popularAdapter);
                //SECOND CALL


                final User user =new User();
                user.setResturant(resturant);
                user.setCategory(popular);
                final ServerRequest request=new ServerRequest();
                request.setOperation(Constants.getspecial);
                request.setUser(user);
                RequestInterface requestInterface=retrofit.create(RequestInterface.class);
                call = requestInterface.operation(request);
                call.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        // progressbar.setVisibility(View.INVISIBLE);
                        initTopView(view, R.id.recycler_top);
                        progressbar.setVisibility(View.INVISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        ProductResponse productResponse = response.body();
                        products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                        popularAdapter = new PopularAdapter(products, getActivity(), new PopularAdapter.Onclicklistnerpopular() {
                            @Override
                            public void Onclikpos() {

                            }
                        });
                        recyclerview.setAdapter(popularAdapter);

                        //THIRD CALL
                        thirdcall(view);
                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        progressbar.setVisibility(View.INVISIBLE);
                        Snackbar.make(coordtinator,"Connection problem",Snackbar.LENGTH_INDEFINITE).show();

                    }
                });





            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressbar.setVisibility(View.INVISIBLE);
                Snackbar.make(coordtinator,"Connection problem",Snackbar.LENGTH_INDEFINITE).show();

            }
        });





    }

    private void thirdcall(final View view) {
        progressbar.setVisibility(View.VISIBLE);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final User user =new User();
        user.setCategory(popular);
        user.setResturant(resturant);
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.getcar);
        request.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> call =requestInterface.operation(request);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, final Response<ProductResponse> response) {
                // progressbar.setVisibility(View.INVISIBLE);
                initTopView(view, R.id.recycler_carousel);
                progressbar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                ProductResponse productResponse =response.body();
                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                carousel_adapter = new Carousel_Adapter(products, getActivity(), new Carousel_Adapter.OnClicklisteners() {
                    @Override
                    public void onPosClicked(int pos) {
                        Intent intent =new Intent(context,ActProductInfo.class);
                        intent.putExtra(Constants.RESTURANT,resturant);
                        intent.putExtra("DATAINTENT",products.get(pos).getP_id());
                        context.startActivity(intent);

                    }
                });
                recyclerview.setAdapter(carousel_adapter);






            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressbar.setVisibility(View.INVISIBLE);
                Snackbar.make(coordtinator,"Connection problem",Snackbar.LENGTH_INDEFINITE).show();

            }
        });


    }




    private void initView(View v) {
        context = getActivity().getApplicationContext();
        linearLayout = (LinearLayout) v.findViewById(R.id.linearalayout_main);
        linearLayout.setVisibility(View.INVISIBLE);
      //  linearlayot = (FrameLayout) v.findViewById(R.id.pop_linear);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.color5, R.color.color1, R.color.color2,
                R.color.color3, R.color.color4, R.color.color6);
        //  linearlayot.setBackgroundResource(R.drawable.food);
        progressbar = (ProgressBar) v.findViewById(R.id.progress_Spinner);
        recyclerview = (RecyclerView) v.findViewById(R.id.recycler_popular);
        textdwat=(TextView)v.findViewById(R.id.textdawat);
        textordercrunch=(TextView)v.findViewById(R.id.textordercrunch);
        coordtinator=(CoordinatorLayout)v.findViewById(R.id.pop_linear);
        textmarufaz=(TextView)v.findViewById(R.id.textmarufaz);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "Handlee-Regular.ttf");
        Typeface face2 = Typeface.createFromAsset(getActivity().getAssets(),
                "JosefinSans-Regular.ttf");

        textdwat.setTypeface(face);
        textordercrunch.setTypeface(face2);
        textmarufaz.setTypeface(face2);

        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearmanager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearmanager);


    }

 /*   private void loadcarousel(View v) {

        carouselView = (CarouselView) v.findViewById(R.id.carouselView);
        carouselView.setPageCount(NUMBER_OF_PAGES);
        carouselView.setImageListener(imageListener);


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {

            // imageView.setImageResource(sampleImages[position]);
            Picasso.with(getActivity()).load(sampleNetworkImageURLs[position]).fit().centerCrop().into(imageView);

        }
    };
    */



    private void initTopView(View view, int id) {
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        recyclerview = (RecyclerView) view.findViewById(id);
        recyclerview.setHasFixedSize(true);
        snapHelper.attachToRecyclerView(recyclerview);
        LinearLayoutManager linearmanager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearmanager);
    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public void onRefresh() {

        View view = getView();
        swipeRefreshLayout.setRefreshing(false);


    }


    private String getTimeFromAndroid() {
        String greeting = null;
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);


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
        final User user =new User();
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.gettoprated);
        request.setUser(user);
        RxjavaInterface rxinterface = retrofit.create(RxjavaInterface.class);
        Observable<ProductResponse> observable = rxinterface.operation(request);
        Subscription s1 = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<ProductResponse>() {
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(getActivity(), "Rx completed", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ProductResponse productResponse) {
                        for (int i = 0; i < 4; i++) {
                            if (i == 0) {
                                initTopView(view, R.id.recycler_popular);
                                progressbar.setVisibility(View.INVISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                popularAdapter = new PopularAdapter(products, getActivity(), new PopularAdapter.Onclicklistnerpopular() {
                                    @Override
                                    public void Onclikpos() {

                                    }
                                });
                                recyclerview.setAdapter(popularAdapter);


                            //    Snackbar.make(linearlayot, "items successfully loaded", Snackbar.LENGTH_SHORT).show();
                            }
                            if (i == 1) {
                                initTopView(view, R.id.recycler_top);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                popularAdapter = new PopularAdapter(products, getActivity(), new PopularAdapter.Onclicklistnerpopular() {
                                    @Override
                                    public void Onclikpos() {

                                    }
                                });
                                recyclerview.setAdapter(popularAdapter);
                             //   Snackbar.make(linearlayot, "items successfully loaded again", Snackbar.LENGTH_SHORT).show();
                            }
                            if (i == 2) {
                                initTopView(view, R.id.recycler_trending);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                popularAdapter = new PopularAdapter(products, getActivity(), new PopularAdapter.Onclicklistnerpopular() {
                                    @Override
                                    public void Onclikpos() {

                                    }
                                });
                                recyclerview.setAdapter(popularAdapter);
                               // Snackbar.make(linearlayot, "items successfully loaded third call", Snackbar.LENGTH_SHORT).show();
                            }
                            if(i==3){
                                initTopView(view, R.id.recycler_carousel);
                                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                                carousel_adapter = new Carousel_Adapter(products, getActivity(), new Carousel_Adapter.OnClicklisteners() {
                                    @Override
                                    public void onPosClicked(int pos) {
                                        Intent intent =new Intent(context,ActProductInfo.class);
                                        intent.putExtra(Constants.RESTURANT,resturant);
                                        intent.putExtra("DATAINTENT",products.get(pos).getP_id());
                                        context.startActivity(intent);

                                    }
                                });
                                recyclerview.setAdapter(carousel_adapter);

                            }
                        }
                    }


                });
    }
    /*private void loadcarouseljava(final View view) {
        progressbar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface rxinterface = retrofit.create(RequestInterface.class);
         Call<ProductResponse> productResponseCall=rxinterface.getProducts();
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();

                progressbar.setVisibility(View.INVISIBLE);
                ProductResponse productResponse =response.body();
                products = new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                sampleNetworkImageURLs=new ArrayList<String>();
                sampleNetworkImageURLs.add( products.get(0).getP_image());
                sampleNetworkImageURLs.add(products.get(1).getP_image());
                sampleNetworkImageURLs.add( products.get(0).getP_image());
                sampleNetworkImageURLs.add(products.get(1).getP_image());
                sampleNetworkImageURLs.add( products.get(0).getP_image());
                Log.d("Tdthfh",products.get(1).getP_image());
                Toast.makeText(getActivity(),products.get(0).getP_image(),Toast.LENGTH_SHORT).show();
              //  loadcarousel(view);

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {


            }
        });
        */




}