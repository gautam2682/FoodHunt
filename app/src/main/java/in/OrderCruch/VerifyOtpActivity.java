package in.OrderCruch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.afollestad.materialdialogs.MaterialDialog;

import in.OrderCruch.Modal.ServerRequest;
import in.OrderCruch.Modal.ServerResponse;
import in.OrderCruch.Modal.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {
    AppCompatButton btn_otp,btn_resend;
    EditText otp;
    String o,email;
    ProgressBar progressBar;
    MaterialDialog.Builder materialDialog; MaterialDialog dialog;    private GradientBackgroundPainter gradientBackgroundPainter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        btn_otp = (AppCompatButton)findViewById(R.id.btn_otp);
        otp=(EditText)findViewById(R.id.otp);
        progressBar=(ProgressBar)findViewById(R.id.progress_otp);
        btn_resend=(AppCompatButton)findViewById(R.id.btn_resend);
        btn_otp.setOnClickListener(this);
        email=getIntent().getStringExtra("email");
        btn_resend.setOnClickListener(this);

        materialDialog=new MaterialDialog.Builder(this)
                .content(R.string.loading)
                .progress(true, 0);
        dialog=materialDialog.build();

        keyBoard();


        View backgroundImage = findViewById(R.id.root_view);
        final int[] drawables = new int[7];
        drawables[0] = R.drawable.gradient_1;
        drawables[1] = R.drawable.gradients_2;
        drawables[2] = R.drawable.gradients_3;
        drawables[3] = R.drawable.gradient_4;
        drawables[4] = R.drawable.gradient_5;
        drawables[5] = R.drawable.gradient_6;
        drawables[6] = R.drawable.gradient_7;




        gradientBackgroundPainter = new GradientBackgroundPainter(backgroundImage, drawables);
        gradientBackgroundPainter.start();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_otp:
               // progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                o=otp.getText().toString();
                otpProcess();
                break;
            case R.id.btn_resend:
               // progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                resendProcess();
                break;

        }
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
                    Snackbar.make(getCurrentFocus(),resp.getMessage(), Snackbar.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),"Verified ",Toast.LENGTH_SHORT).show();
                   // progressBar.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                    goToLogin();
                }
                else {
                    Toast.makeText(getApplicationContext(),"please enter the valid otp or click on resend ",Toast.LENGTH_SHORT).show();
                  //  progressBar.setVisibility(View.INVISIBLE);
                    dialog.dismiss();
                    View view=getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    onClick(view);
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                dialog.dismiss();
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getCurrentFocus(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
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
              //  progressBar.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                if(resp.getResult().equals(Constants.SUCCESS)) {
                    Toast.makeText(getApplicationContext(),"Click on verify button",Toast.LENGTH_SHORT).show();
                    onClick(getCurrentFocus());
                }
                else {
                    Toast.makeText(getApplicationContext(),"Service unavailable,Try again after some time",Toast.LENGTH_SHORT).show();
                    View view=getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    onClick(view);
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
              //  progressBar.setVisibility(View.INVISIBLE);
                dialog.dismiss();
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getCurrentFocus(), "Connection Problem", Snackbar.LENGTH_LONG).show();


            }
        });
    }


    private void goToLogin(){

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("fragment",1);
        startActivity(intent);
    }

    //to handle click automatically
    private void keyBoard() {
        otp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_DONE){
                    btn_otp.performClick();
                    return  true;
                }
                return false;
            }
        });
    }
}


