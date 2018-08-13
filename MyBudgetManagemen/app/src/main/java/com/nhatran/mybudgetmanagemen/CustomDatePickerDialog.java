package com.nhatran.mybudgetmanagemen;

import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class CustomDatePickerDialog {

    Context mContext;
    public OnDateSetListener mListener;


    public interface OnDateSetListener{
        void onSet(Date date);
    }

    public CustomDatePickerDialog(Context context){
        mContext = context;
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        mListener = listener;
    }

    public void createDatePickerDialog(){

        Calendar myCalendar = Calendar.getInstance();

        android.app.DatePickerDialog dpd = new android.app.DatePickerDialog(mContext,
                new android.app.DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar tempCalendar = Calendar.getInstance();

                        tempCalendar.set(Calendar.YEAR, year);
                        tempCalendar.set(Calendar.MONTH, monthOfYear);
                        tempCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (mListener != null){
                            mListener.onSet(tempCalendar.getTime());
                        }

                    }
                }, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DATE));
        dpd.show();
    }


}
