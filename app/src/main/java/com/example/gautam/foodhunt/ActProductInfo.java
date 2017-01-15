package com.example.gautam.foodhunt;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import static com.example.gautam.foodhunt.Main_frag.context;

public class ActProductInfo extends AppCompatActivity {
    String p_id;
    TextView A_name,A_description,A_sold; LinearLayout act_product;
    EditText add_comment;
    Button p_cart,btn_post;
    ImageView act_image;
    private ProductVersion product;
    private ArrayList<ProductVersion> products; ArrayList<User> comment,comment2;
    ProgressBar progressBar;
    RatingBar ratingBar;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;Palette palette;
    RecyclerView recyclerview;DataAdapter dataAdapter;CommentAdapter commentAdapter;SearchView searchView;
    MaterialDialog progressbar;OkHttpClient client;Context actcontext;

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
        addComment();


    }


    private void initView() {
         actcontext=getApplicationContext();
        OkCacheit();
        act_product=(LinearLayout)findViewById(R.id.content_act_product_info);
        act_product.setVisibility(View.INVISIBLE);
        act_image=(ImageView)findViewById(R.id.act_image);
         A_name=(TextView)findViewById(R.id.A_name);
         A_description=(TextView)findViewById(R.id.A_description);
         A_sold=(TextView)findViewById(R.id.A_sold);
          p_cart=(Button)findViewById(R.id.btncart);
        ratingBar=(RatingBar)findViewById(R.id.rating_bar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        ratingBar.setVisibility(View.INVISIBLE);
        progressBar=(ProgressBar)findViewById(R.id.progress_Spinner_info);
        progressBar.setVisibility(View.VISIBLE);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator_product_info);
        add_comment=(EditText)findViewById(R.id.addcomment);
        btn_post=(Button)findViewById(R.id.btn_post);

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
                .client(client)
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
                A_sold.setText(products.get(0).getP_sold());
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
                ratingBar.setRating(Float.parseFloat(products.get(0).getP_star()));
                ratingBar.setVisibility(View.VISIBLE);
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
                 user.setEmail("7827789246");
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

    }

    private void loadProductjson() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user =new User();
        user.setP_id(p_id);
        user.setEmail("7827789246");
        final ServerRequest request=new ServerRequest();
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> response=requestInterface.getProducts();
        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                initRecyclerProducts();
                ProductResponse productResponse =response.body();
                products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                dataAdapter=new DataAdapter(products,getApplicationContext());
                recyclerview.setAdapter(dataAdapter);



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
        GridLayoutManager linearmanager=new GridLayoutManager(this,2);
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
        user.setEmail("7827789246");
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.loadcomment);
        request.setUser(user);
        UserResInterface requestInterface=retrofit.create(UserResInterface.class);
        Call<UserResponse> response=requestInterface.operation(request);
        response.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                loadcommentresponse( response);

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                SnackbarFailed();

            }
        });


    }

    private void loadcommentresponse(Response<UserResponse> response) {
        UserResponse productResponse = response.body();
        if (productResponse.getResult().equals("Failure")) {
        } else {
            initRecycler(R.id.comment_recycler);
            comment = new ArrayList<User>(Arrays.asList(productResponse.getUsers()));
            commentAdapter = new CommentAdapter(comment, getApplicationContext(), new CommentAdapter.editcomment() {
                @Override
                public void editcommentlistener(int pos, String comm) {
                    editComment(pos,comm);
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



    private void addComment() {
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add_comment.getText().toString().isEmpty()) {
                    Snackbar.make(coordinatorLayout, "Comment cannot be Empty", Snackbar.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(ActProductInfo.this);
                    progressDialog.setMessage("posting your comment");
                    progressDialog.show();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.base_url)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    User user = new User();
                    user.setP_id(p_id);
                    user.setComment(add_comment.getText().toString());
                    user.setEmail(Constants.email);
                    final ServerRequest request = new ServerRequest();
                    request.setOperation(Constants.addcomment);
                    request.setUser(user);
                    UserResInterface requestInterface = retrofit.create(UserResInterface.class);
                    Call<UserResponse> response = requestInterface.operation(request);
                    response.enqueue(new Callback<UserResponse>() {
                        @Override
                        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                            progressDialog.dismiss();
                            Snackbar.make(coordinatorLayout, "Successfully added ", Snackbar.LENGTH_SHORT).show();
                            add_comment.setText("");
                        loadcommentresponse(response);

                        }

                        @Override
                        public void onFailure(Call<UserResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            //SnackbarFailed();

                        }
                    });

                }
            }
        });
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
            user.setComment(add_comment.getText().toString());
            user.setEmail(Constants.email);
            user.setComment_id(comment.get(pos).getComment_id());
            user.setComment(comm);
            final ServerRequest request = new ServerRequest();
            request.setOperation(Constants.editComment);
            request.setUser(user);
            UserResInterface requestInterface = retrofit.create(UserResInterface.class);
            Call<UserResponse> response = requestInterface.operation(request);
            response.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "Successfully edited ", Snackbar.LENGTH_SHORT).show();
                    loadcommentresponse(response);

                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
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
        user.setComment(add_comment.getText().toString());
        user.setEmail(Constants.email);
        user.setComment_id(comment.get(pos).getComment_id());
        final ServerRequest request = new ServerRequest();
        request.setOperation(Constants.deletecomment);
        request.setUser(user);
        UserResInterface requestInterface = retrofit.create(UserResInterface.class);
        Call<UserResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout, "Successfully deleted ", Snackbar.LENGTH_SHORT).show();
                loadcommentresponse(response);

            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
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

    public void OkCacheit() {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = null;
        
        try {
            cache = new Cache(getApplication().getCacheDir(), 10 * 1024 * 1024);

        }catch (Exception e){
            Log.e("Tag",e.toString());
        }


        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client=new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .build();

    }
    private  final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response originalResponse = chain.proceed(chain.request());
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                Log.d("Network ava","Network ava");
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();

            }else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                Toast.makeText(getApplicationContext(),"Network no",Toast.LENGTH_SHORT).show();
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }

        }
    };




}