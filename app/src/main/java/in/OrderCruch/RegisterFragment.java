package in.OrderCruch;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;


import com.afollestad.materialdialogs.MaterialDialog;

import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterFragment extends Fragment implements View.OnClickListener {
    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name,et_dob,et_place;
    private TextView tv_login;
    private ProgressBar progress;
    private String email;
    MaterialDialog.Builder materialDialog; MaterialDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        btn_register = (AppCompatButton)view.findViewById(R.id.btn_register);
        tv_login = (TextView)view.findViewById(R.id.tv_login);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        et_dob = (EditText)view.findViewById(R.id.et_dob);
        et_place = (EditText)view.findViewById(R.id.et_place);
        et_dob.setOnClickListener(this);

        progress = (ProgressBar)view.findViewById(R.id.progress);
        TextView textregister=(TextView)view.findViewById(R.id.txtregister);

        Typeface font2= Typeface.createFromAsset(getActivity().getAssets(),"billabong.ttf");
        textregister.setTypeface(font2);
        Typeface font3= Typeface.createFromAsset(getActivity().getAssets(),"Asiago.ttf");
        tv_login.setTypeface(font3);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        materialDialog=new MaterialDialog.Builder(getActivity())
                .content(R.string.loading)
                .widgetColor(Color.RED)
                .progress(true, 0);
        dialog=materialDialog.build();

        keyBoard();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_login:
                goToLogin();
                break;
            case R.id.et_dob:
                DatePickerFragment datePickerFragment=new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(),"Date Picker");
                break;

            case R.id.btn_register:

                String name = et_name.getText().toString();
                email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String dob=et_dob.getText().toString();
                String place=et_place.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                      dialog.show();
                //    progress.setVisibility(View.VISIBLE);
                    registerProcess(name,email,password,dob,place);

                } else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }

    }

    private void registerProcess(String name, String email,String password,String dob,String place){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserResInterface requestInterface = retrofit.create(UserResInterface.class);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setDob(dob);
        user.setPlace(place);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
//                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                //progress.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                gotoVerifyOtp();
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

    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
      //  ft.setCustomAnimations(R.animator.slide_in_down,R.animator.slide_out_down);
        ft.replace(R.id.fragment_frame,login);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void gotoVerifyOtp(){

        Intent intent=new Intent(getActivity().getBaseContext(),VerifyOtpActivity.class);
        intent.putExtra("email",email);
        getActivity().startActivity(intent);
    }

    //to handle click automatically
    private void keyBoard() {
        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    btn_register.performClick();
                    return  true;
                }
                return false;
            }
        });
    }



}
