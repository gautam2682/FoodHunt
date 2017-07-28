package in.OrderCruch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;

import in.OrderCruch.Adapter.CommentAdapter;
import in.OrderCruch.Adapter.DataAdapter;
import in.OrderCruch.Modal.ProductVersion;
import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gautam on 16/7/17.
 */

public class Reviews_Activity extends AppCompatActivity {
    String p_id;

    private ArrayList<ProductVersion> productsrev=new ArrayList<>(); ArrayList<User> comment,comment2=new ArrayList<>();

    RecyclerView recyclerview;DataAdapter dataAdapter;CommentAdapter commentAdapter;SearchView searchView;

    SharedPreferences pref;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reiviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("All Reviews");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        p_id=getIntent().getStringExtra("DATAINTENT");
        pd=new ProgressDialog(Reviews_Activity.this);
        initView();
        loadCommentjson();


    }

    private void initView() {
        recyclerview=(RecyclerView)findViewById(R.id.comment_recycler);
        pref=getApplicationContext().getSharedPreferences("ABC",Context.MODE_PRIVATE);



    }

    private void loadCommentjson() {
        pd.show();

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
                pd.dismiss();
                loadcommentresponse(response);

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(Reviews_Activity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

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
                     //   findViewById(R.id.frame_control).setVisibility(View.GONE);

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
                Toast.makeText(Reviews_Activity.this,"Successfully edited ",Toast.LENGTH_SHORT).show();
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
            //    Snackbar.make(coordinatorLayout, "Successfully deleted ", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(Reviews_Activity.this,"Successfully deleted ",Toast.LENGTH_SHORT).show();
                loadcommentresponse(response);

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();
                //SnackbarFailed();

            }
        });

    }


}
