package in.OrderCruch;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gauti on 02/09/17.
 */

public class Resturantchooser_Activity  extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private GradientBackgroundPainter gradientBackgroundPainter;
    private SharedPreferences pref;
    @BindView(R.id.btn_marufaz)
    AppCompatButton btn_marufaz;

    @BindView(R.id.btn_haji)
    AppCompatButton btn_haji;
    @BindView(R.id.btn_log_out)
    AppCompatButton btn_log_out;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        pref=getSharedPreferences("ABC", Context.MODE_PRIVATE);
        ButterKnife.bind(this);

        btn_haji.setOnClickListener(this);
        btn_marufaz.setOnClickListener(this);
        btn_log_out.setOnClickListener(this);

        initView();

        initButtons();


    }

    private void initButtons() {


    }

    private void initView() {
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_marufaz:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra(Constants.RESTURANT,"products");
                intent.putExtra(Constants.MARUFAZ_SLID,"special_menu");
                intent.putExtra(Constants.MARUFAZ_CAR,"tandoori_nonveg");
                startActivity(intent);
              //  this.finish();
                break;
            case R.id.btn_haji:
                Intent intent2=new Intent(getApplicationContext(),MainActivity.class);
                intent2.putExtra(Constants.MARUFAZ_SLID,"sea_food_soup");
                intent2.putExtra(Constants.MARUFAZ_CAR,"roti_paratha_naan");
                intent2.putExtra(Constants.RESTURANT,"product2");
                startActivity(intent2);
              //  this.finish();
                break;
            case R.id.btn_log_out:
                SharedPreferences   pref=getSharedPreferences("ABC", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean(Constants.IS_LOGGED_IN,false);
                editor.putString(Constants.EMAIL,"");
                editor.putString(Constants.NAME,"");
                editor.putString(Constants.UNIQUE_ID,"");
                editor.apply();
                Intent intent3=new Intent(this,LoginActivity.class);
                startActivity(intent3);
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        MaterialDialog builder=new MaterialDialog.Builder(this)
                .content("Do you want to close the app?")
                .positiveText("YES")
                .negativeText("NO")
                .positiveColor(getResources().getColor(R.color.green))
                .negativeColor(getResources().getColor(R.color.grey))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        finish();
                        System.exit(0);

                    }
                })
                .build();
        builder.show();
    }
}
