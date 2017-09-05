package in.OrderCruch;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class LoginActivity  extends AppCompatActivity{

    private Toolbar toolbar;
    private GradientBackgroundPainter gradientBackgroundPainter;
    private SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref=getSharedPreferences("ABC", Context.MODE_PRIVATE);

        initFragment();


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



    private void initFragment(){
        if(pref.getBoolean(Constants.IS_LOGGED_IN,false)) {
            Intent intent = new Intent(getApplicationContext(), Resturantchooser_Activity.class);
            startActivity(intent);
        }else {
            goToLogin();
        }

    }
    public void goToLogin()

    {   Fragment fragment;
        fragment = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }



}
