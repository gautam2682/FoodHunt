package in.OrderCruch;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;



import java.util.ArrayList;
import java.util.Arrays;

import in.OrderCruch.Adapter.DataAdapter;
import in.OrderCruch.Constants;
import in.OrderCruch.Modal.ProductResponse;
import in.OrderCruch.Modal.ProductVersion;
import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Activity_search extends AppCompatActivity {
    RecyclerView recyclerview;
    ProgressBar progressBar;
    String search;
    ArrayList<ProductVersion> products;
    DataAdapter dataAdapter;
    CoordinatorLayout rootview;
    String query;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    //  HandleIntent(getIntent());
       initView();
        Intent intent=getIntent();
        HandleIntent(intent);
    }

    private void initView() {
        rootview=(CoordinatorLayout)findViewById(R.id.activity_search);
        recyclerview=(RecyclerView)findViewById(R.id.recycler_search);
        progressBar=(ProgressBar)findViewById(R.id.progress_Spinner_search);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutmanager=new GridLayoutManager(this,2);
        recyclerview.setLayoutManager(layoutmanager);


    }

    private void dosearch(String query) {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ServerRequest request=new ServerRequest();
        User user =new User();
        user.setQuery(query);
        request.setOperation(Constants.search);
        request.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<ProductResponse> call =requestInterface.operation(request);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                ProductResponse productResponse =response.body();
                products=new ArrayList<ProductVersion>(Arrays.asList(productResponse.getProducts()));
                dataAdapter=new DataAdapter(products,getApplicationContext());
                recyclerview.setAdapter(dataAdapter);

                Snackbar.make(rootview,"Items successfully loaded",Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(rootview,"Connection problem try again",Snackbar.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        HandleIntent(intent);

    }

    private void HandleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle(query);
            dosearch(query);
        }


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
