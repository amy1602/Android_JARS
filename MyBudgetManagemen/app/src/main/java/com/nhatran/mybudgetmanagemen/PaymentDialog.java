package com.nhatran.mybudgetmanagemen;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class PaymentDialog {

    Context mContext;
    Date mDate;
    Payment mPayment;
    private OnDialogButtonClickListener mListener;

    public interface OnDialogButtonClickListener{
        void onSaveClick(String moneyAmount);
        void onCancelClick();
        void onUnitSpinnerSelect(String unitString);
        void onDatePickerSelect(Date date);
    }

    public void setOnButtonClickListener(OnDialogButtonClickListener listener){
        mListener = listener;
    }


    public PaymentDialog(Context context){
        mContext = context;
    }

    public PaymentDialog(Context context, Payment payment){
        mContext = context;
        mPayment = payment;
    }


    public void createNewPaymentDialog(){

        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("New payment");
        alert.setMessage("Type money amount");
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout,null);
        alert.setView(dialogView);
        final EditText input = dialogView.findViewById(R.id.edittext_money);
        TextView txtPickDate = dialogView.findViewById(R.id.txt_date);
        final Spinner spinnerUnit = dialogView.findViewById(R.id.spinner_unit);
        Button btnSave = dialogView.findViewById(R.id.btn_save);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,R.array.array_unit, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);
        spinnerUnit.setSelection(0);

        if (mPayment != null){
            input.setText(String.valueOf(mPayment.getMoneyAmount()));
            spinnerUnit.setSelection(adapter.getPosition(mPayment.getPaymentUnit()));
            mDate = mPayment.getPaymentTime();
        }

        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onUnitSpinnerSelect(adapterView.getItemAtPosition(i).toString());
                spinnerUnit.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mListener.onUnitSpinnerSelect(adapterView.getItemAtPosition(0).toString());
                spinnerUnit.setSelection(0);
            }

        });

        final Calendar myCalendar = Calendar.getInstance();

        txtPickDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(alert.getContext());
                datePickerDialog.createDatePickerDialog();
                datePickerDialog.setOnDateSetListener(new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onSet(Date date) {
                        mDate = date;
                        mListener.onDatePickerSelect(mDate);
                    }
                });
            }
        });

        final AlertDialog alertDialog = alert.create();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input.getText().toString().equals("") || input.getText() == null){
                    Toast.makeText(mContext,"Money amount is not empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mDate == null){
                    mListener.onDatePickerSelect(Calendar.getInstance().getTime());
                }
                mListener.onSaveClick(input.getText().toString());

                if (mPayment != null && mPayment.getPaymentTime() == mDate){
                    mListener.onDatePickerSelect(mDate);
                }

                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }



}
