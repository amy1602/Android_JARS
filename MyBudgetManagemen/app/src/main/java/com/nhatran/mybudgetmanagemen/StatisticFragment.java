package com.nhatran.mybudgetmanagemen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhatran.mybudgetmanagemen.db.PaymentDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticFragment extends Fragment implements FragmentLifeCycle{

    private RecyclerView rvStatisticList;
    private StatisticListAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private PaymentDBHelper mDatabaseManagement;
    static boolean isFirstLoadStatistic = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        layoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new StatisticListAdapter(this.getContext());

        rvStatisticList = view.findViewById(R.id.rv_statistic);
        rvStatisticList.setLayoutManager(layoutManager);
        rvStatisticList.setAdapter(mAdapter);

        //initData();
        mAdapter.setOnItemLongClickListener(new StatisticListAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(final int position) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(StatisticFragment.this.getActivity());
                dialog.setTitle("Delete confirmation");
                dialog.setMessage("Do you sure delete this payment?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabaseManagement.deleteStatistic(mAdapter.getStatistic(position).getId());
                        mAdapter.removeStatistic(position);
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

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabaseManagement = new PaymentDBHelper(getContext());
    }

    private void initData(){
        List<Statistic> statistics = new ArrayList<>();
        statistics.add(new Statistic(Calendar.getInstance().getTime(),11000,"VND"));
        statistics.add(new Statistic(Calendar.getInstance().getTime(),21000,"VND"));
        statistics.add(new Statistic(Calendar.getInstance().getTime(),31000,"VND"));
        statistics.add(new Statistic(Calendar.getInstance().getTime(),41000,"VND"));
        mAdapter.setStatisticList(statistics);
    }


    @Override
    public void onPauseFragment() {

    }

    @Override
    public void onResumeFragment() {
        loadDataFromDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromDB();

    }

    void loadDataFromDB(){
        if (mAdapter.getItemCount() == 0 )
            mAdapter.setStatisticList(mDatabaseManagement.getAlStatistic());
    }
}
