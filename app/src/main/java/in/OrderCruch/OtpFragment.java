package in.OrderCruch;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gautam on 1/2/17.
 */
public class OtpFragment extends Fragment implements View.OnClickListener {
    AppCompatButton btn_otp,btn_resend;
    EditText otp;
    String o,email;
    ProgressBar progressBar;
    MaterialDialog.Builder materialDialog; MaterialDialog dialog;
    SharedPreferences pref;
    LinearLayout linearlayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otp,container,false);
        initViews(view);
        otp.getBackground().mutate().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        return view;
    }

    private void initViews(View view) {
        pref=getActivity().getSharedPreferences("ABC", Context.MODE_PRIVATE);
        linearlayout=(LinearLayout)view.findViewById(R.id.root_view);

        btn_otp = (AppCompatButton)view.findViewById(R.id.btn_otp);
        otp=(EditText)view.findViewById(R.id.otp);
        otp.requestFocus();
        progressBar=(ProgressBar)view.findViewById(R.id.progress_otp);
        btn_resend=(AppCompatButton)view.findViewById(R.id.btn_resend);
        btn_otp.setOnClickListener(this);
        email=pref.getString(Constants.EMAIL," ");
        btn_resend.setOnClickListener(this);

        materialDialog=new MaterialDialog.Builder(getActivity())
                .content(R.string.loading)
                .progress(true, 0);
        dialog=materialDialog.build();
    }


    public void otpProcess()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserResInterface requestInterface = retrofit.create(UserResInterface.class);

        User user = new User();
        user.setOtp(o.toString());
        user.setEmail(email);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.VERIFYOTP_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                if(resp.getResult().equals(Constants.SUCCESS)) {
                    Snackbar.make(linearlayout,resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getActivity(),"Verified  Successfully ",Toast.LENGTH_SHORT).show();
           ;
                    dialog.dismiss();
                    goToMainActivity();
                }
                else {
                    Toast.makeText(getActivity(),"please enter the valid otp or click on resend ",Toast.LENGTH_SHORT).show();
                    //  progressBar.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                    View view=getView();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    onClick(view);
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                dialog.dismiss();
                Snackbar.make(linearlayout, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
    }

    private void goToMainActivity() {
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    //resend otp
    public void resendProcess()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserResInterface requestInterface = retrofit.create(UserResInterface.class);

        User user = new User();
        user.setEmail(email);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.RESENDOTP_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                dialog.dismiss();
                if(resp.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getActivity(),"Click on verify button",Toast.LENGTH_SHORT).show();
                    onClick(getView());
                }
                else {
                    Toast.makeText(getActivity(),"Service unavailable,Try again after some time",Toast.LENGTH_SHORT).show();
                    View view=getView();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    onClick(view);
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                //  progressBar.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                Snackbar.make(linearlayout, "Connection Problem", Snackbar.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_otp:
                // progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                o = otp.getText().toString();
                otpProcess();
                break;
            case R.id.btn_resend:
                // progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                resendProcess();
                break;
        }


    }
}
