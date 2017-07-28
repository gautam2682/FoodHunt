package in.OrderCruch;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


import java.util.Calendar;
import java.util.Date;

import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment  extends Fragment implements View.OnClickListener{
    private AppCompatButton btn_login;

    private EditText et_email;
    private TextView tv_register,greeting_login,company_name;
    SharedPreferences pref;
    private EditText et_name;

    MaterialDialog.Builder materialDialog; MaterialDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initViews(view);
        et_email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        et_name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        return view;
    }

    private void initViews(View view){

        pref=getActivity().getSharedPreferences("ABC", Context.MODE_PRIVATE);


        btn_login = (AppCompatButton)view.findViewById(R.id.btn_login);
        et_email = (EditText)view.findViewById(R.id.et_email);
        greeting_login=(TextView)view.findViewById(R.id.greeting_login);
        company_name=(TextView)view.findViewById(R.id.company_name);
        et_name=(EditText)view.findViewById(R.id.et_name);
        btn_login.setOnClickListener(this);

        String greets=getTimeFromAndroid();
        greeting_login.setText(greets);
        materialDialog=new MaterialDialog.Builder(getActivity())
                .content(R.string.loading)
                .widgetColor(Color.RED)
                .progress(true, 0);
        dialog=materialDialog.build();
        Typeface font= Typeface.createFromAsset(getActivity().getAssets(),"greet.ttf");
        greeting_login.setTypeface(font);
        Typeface font2= Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        company_name.setTypeface(font2);
        Typeface font3= Typeface.createFromAsset(getActivity().getAssets(),"Asiago.ttf");



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if(et_name.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please tell us your Name",Toast.LENGTH_SHORT).show();

                }else {
                    dialog.show();
                    loginclicked(v);
                }
                break;
        }

    }

    private void loginclicked(View v) {
        final String email= et_email.getText().toString();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserResInterface requestInterface = retrofit.create(UserResInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setName(et_name.getText().toString());
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.loginoperation);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
//                Toast.makeText(getActivity(),resp.getMessage(),Toast.LENGTH_SHORT).show();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(Constants.EMAIL,email);
                    editor.putString(Constants.NAME,et_name.getText().toString());

                    editor.apply();
                    Toast.makeText(getActivity(), resp.getMessage(), Toast.LENGTH_LONG).show();
                    gotootpfragment();

                }


                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                dialog.dismiss();
                Log.d("TAG","failed");
                Toast.makeText(getActivity(), "Connection Problem", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void gotootpfragment() {
        Fragment f=new OtpFragment();
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_login,f);
        ft.addToBackStack(null);
        ft.commit();


    }


    private String getTimeFromAndroid() {
        String greeting=null;
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);


        if(hours>=0 && hours<=12){
            greeting = "Good Morning";
        } else if(hours>=12 && hours<=16){
            greeting = "Good Afternoon";
        } else if(hours>=16 && hours<=21){
            greeting = "Good Evening";
        } else if(hours>=21 && hours<=24){
            greeting = "Good Night";
        }
        return greeting;
    }

}
