package in.OrderCruch;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


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

public class LoginFragment extends Fragment implements View.OnClickListener{
    private AppCompatButton btn_login;

    private EditText et_email,et_password;
    private TextView tv_register,greeting_login,company_name;
    private TextView tv_reset;
   // private ProgressBar progress;
    private SharedPreferences pref;
    MaterialDialog.Builder materialDialog; MaterialDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        pref=getActivity().getSharedPreferences("ABC", Context.MODE_PRIVATE);

        btn_login = (AppCompatButton)view.findViewById(R.id.btn_login);
        tv_register = (TextView)view.findViewById(R.id.tv_register);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
       // progress = (ProgressBar)view.findViewById(R.id.progress);
        tv_reset = (TextView)view.findViewById(R.id.tv_reset);
        greeting_login=(TextView)view.findViewById(R.id.greeting_login);
        company_name=(TextView)view.findViewById(R.id.company_name);
        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
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
        tv_register.setTypeface(font3);
        tv_reset.setTypeface(font3);
        keyBoard();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_register:
                goToRegister();
                break;

            case R.id.btn_login:

                    String email = et_email.getText().toString();
                    String password = et_password.getText().toString();

                    if (!email.isEmpty() && !password.isEmpty()) {
                        //  progress.setVisibility(View.VISIBLE);
                        dialog.show();
                        loginProcess(email, password);

                    } else {

                        Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                    }
                break;
            case R.id.tv_reset:
                goToResetPassword();
                break;
        }
    }



    private void loginProcess(String email,String password){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserResInterface requestInterface = retrofit.create(UserResInterface.class);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
             //   Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if(resp.getResult().equals(Constants.SUCCESS)){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.putString(Constants.EMAIL,resp.getUser().getEmail());
                    editor.putString(Constants.NAME,resp.getUser().getName());
                    editor.putString(Constants.UNIQUE_ID,resp.getUser().getUnique_id());
                    editor.putString(Constants.WALLET,resp.getUser().getMoney());
                    editor.putString(Constants.DOB,resp.getUser().getDob());
                    editor.putString(Constants.PLACE,resp.getUser().getPlace());


                    editor.apply();
                    goToProfile();

                }
              //  progress.setVisibility(View.INVISIBLE);
               dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                //progress.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), "Connection Problem", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void goToRegister(){

        Fragment register = new RegisterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
      //  ft.setCustomAnimations(R.animator.slide_in_up,R.animator.slide_up_out);
        ft.replace(R.id.fragment_frame,register);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void goToResetPassword(){

        Fragment reset = new ResetPasswordFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
     //   ft.setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        ft.replace(R.id.fragment_frame,reset);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void goToProfile(){

        Intent intent=new Intent(getActivity(),Resturantchooser_Activity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    //time of day

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
        } else if(hours>=16 && hours<=24){
            greeting = "Good Evening";
        }
        return greeting;
    }


    //to handle click automatically
    private void keyBoard() {
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    btn_login.performClick();
                    return  true;
                }
                return false;
            }
        });
    }

}
