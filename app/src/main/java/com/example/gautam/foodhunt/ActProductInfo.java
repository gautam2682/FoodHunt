package com.example.gautam.foodhunt;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gautam.foodhunt.Adapter.CommentAdapter;
import com.example.gautam.foodhunt.Adapter.DataAdapter;
import com.example.gautam.foodhunt.Adapter.PopularAdapter;
import com.example.gautam.foodhunt.Modal.ProductResponse;
import com.example.gautam.foodhunt.Modal.ProductVersion;
import com.example.gautam.foodhunt.Modal.ServerRequest;
import com.example.gautam.foodhunt.Modal.ServerResponse;
import com.example.gautam.foodhunt.Modal.User;
import com.example.gautam.foodhunt.Modal.UserResponse;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActProductInfo extends AppCompatActivity {
    String p_id;
    TextView A_name,A_description,A_sold; LinearLayout act_product;
    EditText add_comment;
    Button p_cart;
     public  Button  btn_post;
    ImageView act_image;
    private ProductVersion product;
    private ArrayList<ProductVersion> products=new ArrayList<>(); ArrayList<User> comment,comment2=new ArrayList<>();
    ProgressBar progressBar;

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;Palette palette;
    RecyclerView recyclerview;DataAdapter dataAdapter;CommentAdapter commentAdapter;SearchView searchView;
    MaterialDialog progressbar;OkHttpClient client;Context actcontext;
    PopularAdapter popularAdapter;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_product_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        p_id=getIntent().getStringExtra("DATAINTENT");
        Button gotoorderbtn=(Button)findViewById(R.id.btngorder);
        gotoorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Activity_cart.class);
                startActivity(intent);
            }
        });


        initView();
        loadJson();
        loadCommentjson();
        loadProductjson();
        loadsubmitcomment();


    }

    private void loadsubmitcomment() {
        RatingFragment fragment;
        fragment = RatingFragment.newInstace(p_id);;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_control,fragment);
        ft.commit();
    }


    private void initView() {
        pref=getApplicationContext().getSharedPreferences("ABC",Context.MODE_PRIVATE);
         actcontext=getApplicationContext();
        act_product=(LinearLayout)findViewById(R.id.content_act_product_info);
        act_product.setVisibility(View.INVISIBLE);
        act_image=(ImageView)findViewById(R.id.act_image);
         A_name=(TextView)findViewById(R.id.troops_text);
         A_description=(TextView)findViewById(R.id.A_description);
         A_sold=(TextView)findViewById(R.id.A_sold);
          p_cart=(Button)findViewById(R.id.btncart);


        progressBar=(ProgressBar)findViewById(R.id.progress_Spinner_info);
        progressBar.setVisibility(View.VISIBLE);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_product_info);


        initCollapsingtoolbar();
    }

    private void initCollapsingtoolbar() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
      collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        getSupportActionBar().setTitle("Add to cart");



    }


    private void loadJson() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user =new User();
        user.setP_id(p_id);
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.getproductdetail);
        request.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> response=requestInterface.operation(request);
        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                act_product.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                ProductResponse productResponse =response.body();

               products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                A_name.setText(products.get(0).getP_name());
                A_description.setText(products.get(0).getP_info());
                A_sold.setText(getString(R.string.Rs)+" " +products.get(0).getP_sold());
                Picasso.with(getApplicationContext()).load(products.get(0).getP_image())
                        .resize(360,300)
                        .into(act_image, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) act_image.getDrawable()).getBitmap();
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            public void onGenerated(Palette palette) {
                                applyPalette(palette);
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });
               // ratingBar.setRating(Float.parseFloat(products.get(0).getP_star()));
               // ratingBar.setVisibility(View.VISIBLE);
                collapsingToolbarLayout.setTitle(products.get(0).getP_name());

                plceCart();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                SnackbarFailed();


            }
        });


    }

    private void plceCart() {

        p_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog=new ProgressDialog(ActProductInfo.this);
                progressDialog.setMessage(products.get(0).getP_name() + " adding to your cart");
                progressDialog.show();

                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(Constants.base_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                User user =new User();
                user.setP_id(p_id);
                 user.setEmail(pref.getString(Constants.EMAIL,""));
                user.setNoi("1");
                final ServerRequest request=new ServerRequest();
                request.setOperation(Constants.addtocart);
                request.setUser(user);
                RequestInterface requestInterface=retrofit.create(RequestInterface.class);
                Call<ProductResponse> response=requestInterface.operation(request);
                response.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        progressDialog.dismiss();
                        Snackbar.make(coordinatorLayout,"Successfully added to cart",Snackbar.LENGTH_INDEFINITE)
                                .setAction("CART", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getApplicationContext(),Activity_cart.class);
                                        startActivity(intent);
                                    }
                                }).show();

                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        SnackbarFailed();

                    }
                });
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
    private void applyPalette(Palette palette) {
       // int mutedColor = palette.getMutedColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        int mutedColor=palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
        int mutedDarkColor = palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark));
        int vibrantColor = palette.getVibrantColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        collapsingToolbarLayout.setContentScrimColor(vibrantColor);
        collapsingToolbarLayout.setStatusBarScrimColor(mutedDarkColor);
        A_name.setBackgroundColor(vibrantColor);
        A_name.getBackground().setAlpha(150);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(mutedDarkColor);
        }

    }

    private void loadProductjson() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user =new User();
        user.setP_id(p_id);
        user.setEmail(pref.getString(Constants.EMAIL," "));
        final ServerRequest request=new ServerRequest();
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> response=requestInterface.getProducts();
        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                initRecyclerProducts();
                ProductResponse productResponse =response.body();
                products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
             //   dataAdapter=new DataAdapter(products,getApplicationContext());
                popularAdapter=new PopularAdapter(products,getApplicationContext());
                recyclerview.setAdapter(popularAdapter);



            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                SnackbarFailed();

            }
        });
    }

    private void initRecyclerProducts() {
        recyclerview=(RecyclerView)findViewById(R.id.product_recycler);
        recyclerview.setHasFixedSize(true);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(recyclerview);
       // GridLayoutManager linearmanager=new GridLayoutManager(this,2);
        LinearLayoutManager linearmanager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(linearmanager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);

    }

    private void loadCommentjson() {

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final User user =new User();
        user.setP_id(p_id);
        user.setEmail(pref.getString(Constants.EMAIL," "));
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.loadcomment);
        request.setUser(user);
        UserResInterface requestInterface=retrofit.create(UserResInterface.class);
        Call<ServerResponse> response=requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                loadcommentresponse(response);

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                SnackbarFailed();

            }
        });


    }

    private void loadcommentresponse(Response<ServerResponse> response) {
        ServerResponse productResponse = response.body();

        if (productResponse.getResult()==null) {
        } else {
            if (productResponse.getResult().equals("Failure")) {

            } else {
                initRecycler(R.id.comment_recycler);
                comment = new ArrayList<User>(Arrays.asList(productResponse.getUsers()));
                for (int i = 0; i < comment.size(); i++) {
                    if (comment.get(i).getEmail().equals(pref.getString(Constants.EMAIL, " "))) {

                        //btn_post.setEnabled(false);
                  /*  CommentFragment fragment;
                    fragment = CommentFragment.newInstace(p_id,false,comment.get(i).getComment());;
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_control,fragment);
                    ft.commit();
                  */
                        findViewById(R.id.frame_control).setVisibility(View.GONE);

                    }
                }
                commentAdapter = new CommentAdapter(comment, getApplicationContext(), new CommentAdapter.editcomment() {
                    @Override
                    public void editcommentlistener(int pos, String comm) {
                        editComment(pos, comm);
                    }

                    @Override
                    public void deletecommentlistener(int pos) {
                        deleteComment(pos);

                    }
                });
                recyclerview.setAdapter(commentAdapter);
                //  Snackbar.make(coordinatorLayout, "Comment successfully loaded" + p_id, Snackbar.LENGTH_SHORT).show();

            }
        }
    }

    private void initRecycler(int id) {
        recyclerview=(RecyclerView)findViewById(id);
        recyclerview.setHasFixedSize(true);
        LinearLayoutManager linearmanager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerview.setLayoutManager(linearmanager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);
    }
   public void SnackbarFailed(){
       Snackbar.make(coordinatorLayout,"Check connection",Snackbar.LENGTH_INDEFINITE)
               .setAction("RETRY", new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent= new Intent(getApplicationContext(),ActProductInfo.class);
                       startActivity(intent);
                       finish();
                   }
               })
               .show();

    }





    public void editComment(int pos,String comm){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("posting your comment");
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            User user = new User();
            user.setP_id(p_id);
            user.setComment(comm);
            user.setEmail(pref.getString(Constants.EMAIL," "));
            user.setComment_id(comment.get(pos).getComment_id());
            user.setComment(comm);
            final ServerRequest request = new ServerRequest();
            request.setOperation(Constants.editComment);
            request.setUser(user);
            UserResInterface requestInterface = retrofit.create(UserResInterface.class);
            Call<ServerResponse> response = requestInterface.operation(request);
            response.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "Successfully edited ", Snackbar.LENGTH_SHORT).show();
                    loadcommentresponse(response);

                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    //SnackbarFailed();

                }
            });

    }
    public void deleteComment(int pos){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting your comment");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user = new User();
        user.setP_id(p_id);
        user.setComment("");
        user.setEmail(pref.getString(Constants.EMAIL," "));
        user.setComment_id(comment.get(pos).getComment_id());
        final ServerRequest request = new ServerRequest();
        request.setOperation(Constants.deletecomment);
        request.setUser(user);
        UserResInterface requestInterface = retrofit.create(UserResInterface.class);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout, "Successfully deleted ", Snackbar.LENGTH_SHORT).show();
                loadcommentresponse(response);

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();
                //SnackbarFailed();

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchitem=menu.findItem(R.id.action_search);
        searchView=(SearchView) MenuItemCompat.getActionView(searchitem);
        //  searchView.setIconifiedByDefault(false);;
        searchView.setSubmitButtonEnabled(true);
        searchView.setQuery("", false);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }






}
