package in.OrderCruch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private TextView dob;
    private DatePicker mDatePicker;

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dob=(TextView)getActivity().findViewById(R.id.et_dob);
        dob.setText(i2+"/"+i1+"/"+i);

    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_frag,null);
        Calendar calendar=Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=  new   DatePickerDialog(getActivity(),this,year,month,day);
        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);

        return   new MaterialDialog.Builder(getActivity())
                .customView(v,false)
                .title("Chooose your birthday")
                .titleColor(Color.RED)
                .positiveText("OK")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        int year=mDatePicker.getYear();
                        int month=mDatePicker.getMonth();
                        month=month+1;
                        int day=mDatePicker.getDayOfMonth();
                        dob=(TextView)getActivity().findViewById(R.id.et_dob);
                        dob.setText(day+"/"+month+"/"+year);
                    }
                })
                .positiveColor(Color.RED)
                .build();
    }

}
