package com.nhatran.mybudgetmanagemen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatran.mybudgetmanagemen.db.PaymentDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements FragmentLifeCycle{

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
    static List<Payment> tempList =new ArrayList<>();
    static boolean isFirstLoad = true;
    static Date fromDate;
    static Date toDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btnAddPayment = view.findViewById(R.id.btn_add_payment);
        txtFromDate = view.findViewById(R.id.txt_from_date);
        txtToDate = view.findViewById(R.id.txt_to_date);
        txtRemainDay = view.findViewById(R.id.txt_day_remain);
        txtSuggest = view.findViewById(R.id.txt_suggest);

        layoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new PaymentListAdapter(this.getContext());

        rvPaymentList = view.findViewById(R.id.rv_payment_list);
        rvPaymentList.setLayoutManager(layoutManager);
        rvPaymentList.setAdapter(mAdapter);

        txtUsedAmount = view.findViewById(R.id.txt_used);
        txtRemain = view.findViewById(R.id.txt_remain);
        txtTotal = view.findViewById(R.id.edittext_total);
        btnSaveTotalAmount = view.findViewById(R.id.btn_save_total_amount);
        btnEditTotal = view.findViewById(R.id.btn_edit_total);
        txtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(HomeFragment.this.getActivity());
                datePickerDialog.createDatePickerDialog();
                datePickerDialog.setOnDateSetListener(new CustomDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onSet(Date date) {
                        txtFromDate.setText(Payment.convertDateToString(date));
                        fromDate = date;
                        mAdapter.setPaymentList(mDatabaseManagement.getPaymentsInPeriodTime(fromDate, toDate));
                        updateText();
                    }
                });
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(HomeFragment.this.getActivity());
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
                PaymentDialog dialog = new PaymentDialog(HomeFragment.this.getActivity(), mAdapter.getPayment(position));
                dialog.createNewPaymentDialog();
                final Payment payment = new Payment();
                dialog.setOnButtonClickListener(new PaymentDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onSaveClick(String moneyAmount) {
                        payment.setMoneyAmount(Long.valueOf(moneyAmount));
                        mDatabaseManagement.deletePayment(mAdapter.getPayment(position).getId());
                        tempList.remove(mAdapter.getPayment(position));
                        tempList.add(payment);
                        mAdapter.editPayment(position, payment);
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


                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeFragment.this.getActivity());
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
                PaymentDialog dialog = new PaymentDialog(HomeFragment.this.getActivity());
                dialog.createNewPaymentDialog();
                final Payment payment = new Payment();
                dialog.setOnButtonClickListener(new PaymentDialog.OnDialogButtonClickListener() {
                    @Override
                    public void onSaveClick(String moneyAmount) {
                        payment.setMoneyAmount(Long.valueOf(moneyAmount));
                        tempList.add(payment);
                        if (payment.getPaymentTime().getTime() >= fromDate.getTime() && payment.getPaymentTime().getTime() <= toDate.getTime()){
                            mAdapter.addPayment(payment);
                            rvPaymentList.scrollToPosition(0);
                            updateText();
                        }
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

        return view;
    }

    int getRemainDay(){
        if (fromDate != null && toDate != null){
            if (fromDate.after(Calendar.getInstance().getTime())) return getDaysDifference(fromDate, toDate);
            return getDaysDifference(Calendar.getInstance().getTime(), toDate);
        }else {
            return 1;
        }
    }

    private void updateText(){
        if (toDate != null && toDate.getTime() < Calendar.getInstance().getTime().getTime()){
            Toast.makeText(getContext(),"Time's over! Set the new one.", Toast.LENGTH_SHORT).show();
            //return;
        }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void saveData(){
        for (int i = 0; i < tempList.size() ; i++){
            mDatabaseManagement.addPayment(tempList.get(i));
        }
        mDatabaseManagement.deleteTotalAndDate();
        mDatabaseManagement.saveTotalAndDate(totalAmount, Payment.convertDateToString(fromDate), Payment.convertDateToString(toDate));
        tempList = new ArrayList<>();
        mDatabaseManagement.addStatistic(new Statistic(toDate, mAdapter.getTotalMoneyAmount(), "VND"));

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
    public void onStop() {
        super.onStop();
        saveData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onPauseFragment() {
        saveData();
    }

    @Override
    public void onResumeFragment() {
        mDatabaseManagement = new PaymentDBHelper(getContext());
        updateText();
        loadDataFromDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDatabaseManagement = new PaymentDBHelper(getContext());
        updateText();
        loadDataFromDB();

    }

    void loadDataFromDB(){
        fromDate = mDatabaseManagement.getFromDate();
        toDate = mDatabaseManagement.getToDate();
        mAdapter.setPaymentList(mDatabaseManagement.getPaymentsInPeriodTime(fromDate, toDate));
        totalAmount = mDatabaseManagement.getTotal();
        updateText();
    }
}
