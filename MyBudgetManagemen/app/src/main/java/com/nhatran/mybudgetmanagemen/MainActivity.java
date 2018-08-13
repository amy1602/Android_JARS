package com.nhatran.mybudgetmanagemen;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhatran.mybudgetmanagemen.db.PaymentDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvPaymentList;
    private PaymentListAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private FloatingActionButton btnAddPayment;
    private TextView txtUsedAmount;
    private TextView txtRemain;
    private EditText txtTotal;
    private Button btnSaveTotalAmount;
    private ImageButton btnEditTotal;
    private TextView txtFromDate;
    private TextView txtToDate;
    private TextView txtSuggest;
    private TextView txtRemainDay;

    private long totalAmount = 3000000;
    private PaymentDBHelper mDatabaseManagement;
    static List<Payment>  tempList =new ArrayList<>();
    static boolean isFirstLoad = true;
    static Date fromDate;
    static Date toDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mDatabaseManagement = new PaymentDBHelper(getApplicationContext());
        btnAddPayment = findViewById(R.id.btn_add_payment);
        txtFromDate = findViewById(R.id.txt_from_date);
        txtToDate = findViewById(R.id.txt_to_date);
        txtRemainDay = findViewById(R.id.txt_day_remain);
        txtSuggest = findViewById(R.id.txt_suggest);

        layoutManager = new LinearLayoutManager(this);
        mAdapter = new PaymentListAdapter(this);

        rvPaymentList = findViewById(R.id.rv_payment_list);
        rvPaymentList.setLayoutManager(layoutManager);
        rvPaymentList.setAdapter(mAdapter);

        txtUsedAmount = findViewById(R.id.txt_used);
        txtRemain = findViewById(R.id.txt_remain);
        txtTotal = findViewById(R.id.edittext_total);
        btnSaveTotalAmount = findViewById(R.id.btn_save_total_amount);
        btnEditTotal = findViewById(R.id.btn_edit_total);

        updateText();

        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(MainActivity.this);
                datePickerDialog.createDatePickerDialog();
                datePickerDialog.setOnDateSetListener(new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onSet(Date date) {
                        txtFromDate.setText(Payment.convertDateToString(date));
                        fromDate = date;
                        updateText();
                    }
                });
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(MainActivity.this);
                datePickerDialog.createDatePickerDialog();
                datePickerDialog.setOnDateSetListener(new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onSet(Date date) {
                        txtToDate.setText(Payment.convertDateToString(date));
                        toDate = date;
                        updateText();
                    }
                });
            }
        });

        btnEditTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTotal.setEnabled(true);
                btnSaveTotalAmount.setVisibility(View.VISIBLE);
                txtTotal.setText(String.valueOf(totalAmount));
                txtTotal.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        });

        btnSaveTotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalAmount = Integer.valueOf(txtTotal.getText().toString());
                txtTotal.setEnabled(false);
                btnSaveTotalAmount.setVisibility(View.GONE);
                updateText();
            }
        });

        mAdapter.setOnEditClickListener(new PaymentListAdapter.OnEditClickListener() {
            @Override
            public void onClick(final int position) {
                PaymentDialog dialog = new PaymentDialog(MainActivity.this, mAdapter.getPayment(position));
                dialog.createNewPaymentDialog();
                final Payment payment = new Payment();
                dialog.setOnButtonClickListener(new PaymentDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onSaveClick(String moneyAmount) {
                        payment.setMoneyAmount(Long.valueOf(moneyAmount));
                        mDatabaseManagement.deletePayment(mAdapter.getPayment(position).getId());
                        tempList.remove(mAdapter.getPayment(position));
                        tempList.add(payment);
                        mAdapter.editPayment(position,payment);
                        updateText();
                    }

                    @Override
                    public void onCancelClick() {

                    }

                    @Override
                    public void onUnitSpinnerSelect(String unitString) {
                        payment.setPaymentUnit(unitString);
                    }

                    @Override
                    public void onDatePickerSelect(Date date) {
                        payment.setPaymentTime(date);
                    }
                });
            }
        });

        mAdapter.setOnItemLongClickListener(new PaymentListAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(final int position) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Delete confirmation");
                dialog.setMessage("Do you sure delete this payment?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseManagement.deletePayment(mAdapter.getPayment(position).getId());
                        tempList.remove(mAdapter.getPayment(position));
                        mAdapter.removePayment(position);
                        updateText();
                    }
                });

                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
            }
        });


        btnAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentDialog dialog = new PaymentDialog(MainActivity.this);
                dialog.createNewPaymentDialog();
                final Payment payment = new Payment();
                dialog.setOnButtonClickListener(new PaymentDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onSaveClick(String moneyAmount) {
                        payment.setMoneyAmount(Long.valueOf(moneyAmount));
                        mAdapter.addPayment(payment);
                        tempList.add(payment);
                        rvPaymentList.scrollToPosition(0);
                        updateText();
                    }

                    @Override
                    public void onCancelClick() {

                    }

                    @Override
                    public void onUnitSpinnerSelect(String unitString) {
                        payment.setPaymentUnit(unitString);
                    }


                    @Override
                    public void onDatePickerSelect(Date date) {
                        payment.setPaymentTime(date);
                    }
                });

            }
        });

       // initData();
    }

    int getRemainDay(){
        if (fromDate != null){
            if (fromDate.after(Calendar.getInstance().getTime())) return getDaysDifference(fromDate, toDate);
            return getDaysDifference(Calendar.getInstance().getTime(), toDate);
        }else {
            return 1;
        }

    }

    private void updateText(){
        txtUsedAmount.setText("Used: "+String.valueOf(mAdapter.getTotalMoneyAmount())+" VND");
        txtRemain.setText("Remain: "+ String.valueOf(totalAmount - mAdapter.getTotalMoneyAmount())+ " VND");
        txtSuggest.setText("Suggest: "+ String.valueOf((totalAmount - mAdapter.getTotalMoneyAmount()) / (getRemainDay()))+" VND/ day");
        txtTotal.setText("Total: "+String.valueOf(totalAmount));
        txtRemainDay.setText(String.valueOf(getRemainDay()) + " days");
        if (fromDate != null){
            txtFromDate.setText(Payment.convertDateToString(fromDate));
        }
        if (toDate != null){
            txtToDate.setText(Payment.convertDateToString(toDate));
        }
    }

    public static int getDaysDifference(Date fromDate,Date toDate)
    {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstLoad){
            mAdapter.setPaymentList(mDatabaseManagement.getAllPayments());
            totalAmount = mDatabaseManagement.getTotal();
            fromDate = mDatabaseManagement.getFromDate();
            toDate = mDatabaseManagement.getToDate();
            updateText();
            isFirstLoad = false;
        }
    }

    private void saveData(){
        for (int i = 0; i < tempList.size() ; i++){
            mDatabaseManagement.addPayment(tempList.get(i));
        }
        mDatabaseManagement.deleteTotalAndDate();
        mDatabaseManagement.saveTotalAndDate(totalAmount, Payment.convertDateToString(fromDate), Payment.convertDateToString(toDate));
        tempList = new ArrayList<>();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    private void initData(){
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(Calendar.getInstance().getTime(),11000,"VND"));
        payments.add(new Payment(Calendar.getInstance().getTime(),21000,"VND"));
        payments.add(new Payment(Calendar.getInstance().getTime(),31000,"VND"));
        payments.add(new Payment(Calendar.getInstance().getTime(),41000,"VND"));
        mAdapter.setPaymentList(payments);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }
}
