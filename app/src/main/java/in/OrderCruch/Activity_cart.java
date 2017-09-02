package in.OrderCruch;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Arrays;

import in.OrderCruch.Adapter.CartAdapter;
import in.OrderCruch.Modal.ProductResponse;
import in.OrderCruch.Modal.ProductVersion;
import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Activity_cart extends AppCompatActivity {
    RecyclerView recyclerview;
    ProgressBar progressBar;
    ArrayList<ProductVersion> products;
    CartAdapter cartAdapter;
    Toolbar ordertoolbar;  Button btnorder;CoordinatorLayout coordinatorLayout;ArrayList<String> idsA,noiA;
    SharedPreferences pref;String table_no;
    boolean mordercompleted=false,cordercompleted=false;
    int count=0;
    ProgressDialog progressDialog;
    Float totalprice=0.0f;
    ArrayList<Float> total=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Diet");
        initView();


    }




    private void initView() {
        pref=getApplicationContext().getSharedPreferences("ABC", Context.MODE_PRIVATE);
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
        user.setEmail(pref.getString(Constants.EMAIL," "));

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
                    total.add(products.get(i).getP_sold());

                }
                updatePrice();
                cartAdapter=new CartAdapter(products, getBaseContext(), new CartAdapter.onClickremove() {
                    @Override
                    public void onClickremovelist(int pos) {
                        idsA.remove(pos);
                        noiA.remove(pos);
                        total.remove(pos);
                        totalprice= totalprice-(products.get(pos).getP_sold()* Float.parseFloat(noiA.get(pos)));
                        btnorder.setText("Total:-  Rs."+ String.valueOf(totalprice));

                    }

                    @Override
                    public void ordercart(ArrayList<String> ids, ArrayList<String> nois) {
                   //    ordermycart(ids,nois);
                    }

                    @Override
                    public void spinnerchange(int i, String noi) {
                        noiA.add(i,noi);
                        total.set(i,products.get(i).getP_sold()* Float.parseFloat(noi));
                        totalprice=0.0f;
                        updatePrice();


                    }
                });
                recyclerview.setAdapter(cartAdapter);

                Toast.makeText(getApplicationContext(),"Diet loaded ",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(coordinatorLayout,"Diet Failure",Snackbar.LENGTH_SHORT).show();


            }
        });


    }

    private void updatePrice() {
        for(int i=0;i<total.size();i++) {
            totalprice += total.get(i);
        }
        btnorder.setText("Total:-  Rs."+ String.valueOf(totalprice));

    }

    private void AddTableNo() {
        String[] Tables=new String[]{
                "Table 1",
                "Table 2",
                "Table 3",
                "Table 4",
                "Table 5",
                "Table 6"

        };
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(Tables,0,null)
                .setPositiveButton("Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int selectedpos=((AlertDialog)dialogInterface).getListView().getSelectedItemPosition();

                    }
                })
                .show();
    }

    public void ordermycart(final ArrayList<String> ids, final ArrayList<String> nois){
            btnorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // AlertDialogTable(view);


                }
            });
    }

    private void OrderwithTable(View view, String table_no) {
       progressDialog=new ProgressDialog(Activity_cart.this);
        progressDialog.setMessage("Ordering items");
        progressDialog.show();
        for (int i = 0; i < idsA.size(); i++) {
            Log.d("IDS",idsA.get(i));
            count++;
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            User user = new User();
            user.setEmail(pref.getString(Constants.EMAIL," "));
            user.setP_id(idsA.get(i));
            user.setTable_no(table_no);
            user.setNoi(noiA.get(i));
            final ServerRequest request = new ServerRequest();
            request.setOperation(Constants.ordercart);
            request.setUser(user);
            RxjavaInterface requestInterface = retrofit.create(RxjavaInterface.class);
            Observable<ProductResponse>  observable= requestInterface.operation(request);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Subscriber<ProductResponse>() {
                        @Override
                        public void onCompleted() {
                            checkifcompleted();

                             makebill();

                        }

                        @Override
                        public void onError(Throwable e) {
                            checkiffailed();



                        }

                        @Override
                        public void onNext(ProductResponse productResponse) {

                        }
                    });


        }




    }

    private void checkiffailed() {
        if(count==idsA.size()){
            progressDialog.dismiss();

            Snackbar.make(coordinatorLayout, "Connection problem", Snackbar.LENGTH_LONG).show();

        }

    }

    private void checkifcompleted() {
        if(count==idsA.size()){
            progressDialog.dismiss();
            Snackbar.make(coordinatorLayout, "Order placed ", Snackbar.LENGTH_SHORT).show();
            products.clear();
            cartAdapter.notifyDataSetChanged();
            btnorder.setEnabled(false);

        }
    }

    private void makebill() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user = new User();
        user.setEmail(pref.getString(Constants.EMAIL," "));
        user.setTable_no(table_no);
        final ServerRequest request = new ServerRequest();
        request.setOperation(Constants.makebill);
        request.setUser(user);
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<ProductResponse> call = requestInterface.operation(request);
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
             //   Snackbar.make(coordinatorLayout, "bill is ready ", Snackbar.LENGTH_SHORT).show();
            //    makebill();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
              //  Snackbar.make(coordinatorLayout, "Connection problem", Snackbar.LENGTH_LONG).show();

            }
        });


    }

    private void AlertDialogTable(final View view) {
        MaterialDialog materialDialog=new MaterialDialog.Builder(this)
                .customView(R.layout.prompts_table,false)
                .positiveText("Submit")
                .positiveColor(getResources().getColor(R.color.light_blue500))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText edit=(EditText)dialog.getCustomView().findViewById(R.id.editTextDialogTable);
                        table_no=edit.getText().toString();
                        if(!table_no.isEmpty()) {
                            OrderwithTable(view, table_no);
                        }else {
                            Snackbar.make(coordinatorLayout,"Please enter Table number",Snackbar.LENGTH_LONG).show();
                        }
                    }
                })
                .negativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .neutralColor(getResources().getColor(R.color.colorPrimaryDark))
                .title("Enter Table No")
                .build();
        materialDialog.show();
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
