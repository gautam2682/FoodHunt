package in.OrderCruch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Arrays;

import in.OrderCruch.Modal.ProductResponse;
import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gautam on 20/6/17.
 */

public class RatingFragment extends Fragment {
    Button submit_rating;
    String p_id;
    RatingBar ratingBar;
    ProgressDialog pd;
    SharedPreferences pref;
    LinearLayout ratinglayout;
    ArrayList<User> ratelist;
    private String oldrating;
    private boolean updatecomment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view=inflater.inflate(R.layout.rating_fragment,container,false);
        intiViews(view);
        p_id=getArguments().getString(Constants.PID);
        pref=getActivity().getSharedPreferences("ABC", Context.MODE_PRIVATE);
        loadratingjson();

        submit_ratinglistener();;
        return view;
    }
    public static RatingFragment newInstace(String p_id){
        RatingFragment fragment=new RatingFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.PID,p_id);
        fragment.setArguments(bundle);
        return fragment;

    }


    private void intiViews(View view) {
        pd=new ProgressDialog(getActivity());
        submit_rating=(Button)view.findViewById(R.id.submit_ratings);
        ratingBar=(RatingBar)view.findViewById(R.id.rating_bar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        ratinglayout=(LinearLayout) view.findViewById(R.id.linear_rating);
       // stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
       // ratingBar.setVisibility(View.INVISIBLE);

    }

    private void submit_ratinglistener() {
        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                if(updatecomment){
                    UpdateRateing();

                }else {
                    sendRating();
                }




            }

            private void sendRating() {
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(Constants.base_url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                User user =new User();
                user.setP_id(p_id);
                user.setRating(ratingBar.getRating());
                user.setEmail(pref.getString(Constants.EMAIL,""));
                final ServerRequest request=new ServerRequest();
                request.setOperation(Constants.rateit);

                request.setUser(user);
                RequestInterface requestInterface=retrofit.create(RequestInterface.class);
                Call<ProductResponse> response=requestInterface.operation(request);
                response.enqueue(new Callback<ProductResponse>() {
                    @Override
                    public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                        Toast.makeText(getActivity(),"Thanks for rating this item ",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        CommentFragment fragment;
                        fragment = CommentFragment.newInstace(p_id,true, " ");;
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_control,fragment);

                        ft.commit();

                    }

                    @Override
                    public void onFailure(Call<ProductResponse> call, Throwable t) {
                        pd.dismiss();
                     //   Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });
    }

    private void UpdateRateing() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        User user =new User();
        user.setP_id(p_id);
        user.setRating(ratingBar.getRating());
        user.setOldrating(Float.valueOf(oldrating));
        user.setOrirating(Float.valueOf(oldrating )-ratingBar.getRating());
        user.setEmail(pref.getString(Constants.EMAIL,""));
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.updaterating);
        request.setUser(user);
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Log.d("RAting", String.valueOf(Float.valueOf(oldrating )-ratingBar.getRating()));
        Call<ProductResponse> response=requestInterface.operation(request);
        response.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                Toast.makeText(getActivity(),"Thanks for rating this item ",Toast.LENGTH_SHORT).show();
                pd.dismiss();
                CommentFragment fragment;
                fragment = CommentFragment.newInstace(p_id,true, " ");;
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame_control,fragment);

                ft.commit();

            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                pd.dismiss();
                //   Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void loadratingjson() {
      //  pd.show();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final User user =new User();
        user.setP_id(p_id);
        user.setEmail(pref.getString(Constants.EMAIL," "));
        final ServerRequest request=new ServerRequest();
        request.setOperation(Constants.loadrating);
        request.setUser(user);
        UserResInterface requestInterface=retrofit.create(UserResInterface.class);
        Call<ServerResponse> response=requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                //  pd.dismiss();
                ServerResponse productResponse = response.body();
                if (productResponse == null) {
                    ratinglayout.setVisibility(View.VISIBLE);

                } else {
                    if (productResponse.getUsers() == null) {
                        ratinglayout.setVisibility(View.VISIBLE);
                    } else {


                        ratelist = new ArrayList<User>(Arrays.asList(productResponse.getUsers()));


                            ratingBar.setRating(Float.valueOf(ratelist.get(0).getP_star()));
                            oldrating=ratelist.get(0).getP_star();
                            ratinglayout.setVisibility(View.INVISIBLE);
                        updatecomment=true;
                        //loadcommentresponse(response);
                    }


                }
            }


            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
              //  pd.dismiss();
               // SnackbarFailed();
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

}
