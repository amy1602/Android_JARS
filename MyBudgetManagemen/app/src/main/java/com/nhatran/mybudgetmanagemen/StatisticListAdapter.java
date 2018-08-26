package com.nhatran.mybudgetmanagemen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatisticListAdapter extends RecyclerView.Adapter<StatisticListAdapter.StatisticViewHolder>  {


    List<Statistic> statisticList = new ArrayList<>();
    private Context mContext;
    private OnEditClickListener mEditListener;
    private OnItemLongClickListener mLongClickListener;


    public interface OnEditClickListener{
        void onClick(int position);
    }

    public void setOnEditClickListener(OnEditClickListener listener){
        mEditListener = listener;
    }

    public interface OnItemLongClickListener{
        void onLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mLongClickListener = listener;
    }

    public StatisticListAdapter(Context context){
        mContext = context;
    }

    @Override
    public StatisticViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_statistic,viewGroup,false);
        return new StatisticViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StatisticViewHolder viewHolder, int i) {
        Statistic statistic = statisticList.get(i);

        viewHolder.txtTime.setText(statistic.getTimeAsStringFromMonth() +": ");
        viewHolder.txtMoneyAmount.setText(statistic.getMoneyAsString());

    }

    public void setStatisticList(List<Statistic> statisticList){
        this.statisticList.addAll(statisticList);
        notifyDataSetChanged();
    }

    public void addStatistic(Statistic statistic){
        statisticList.add(statistic);
        notifyDataSetChanged();
    }

    public void editStatistic(int position, Statistic statistic){
        statisticList.set(position, statistic);
        notifyDataSetChanged();
    }

    public Statistic getStatistic(int position){
        return statisticList.get(position);
    }

    public void removeStatistic(int position){
        statisticList.remove(position);
        notifyItemRemoved(position);
    }

    public long getTotalMoneyAmount(){
        long total = 0;
        for (int i = 0; i < statisticList.size() ; i++){
            if (statisticList.get(i).getPaymentUnit().equals("USD")){
                total += statisticList.get(i).getMoneyAmount() * 22727;
            }
            if (statisticList.get(i).getPaymentUnit().equals("VND")){
                total += statisticList.get(i).getMoneyAmount();
            }
            if (statisticList.get(i).getPaymentUnit().equals("EUR")){
                total += statisticList.get(i).getMoneyAmount() * 26714;
            }
        }

        return total;
    }

    @Override
    public int getItemCount() {
        return statisticList.size();
    }

    class StatisticViewHolder extends RecyclerView.ViewHolder{

        TextView txtTime;
        TextView txtMoneyAmount;
        ImageButton btnEdit;

        public StatisticViewHolder(final View itemView) {
            super(itemView);

            txtTime = itemView.findViewById(R.id.txt_time);
            txtMoneyAmount = itemView.findViewById(R.id.txt_money_amount);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnEdit.setVisibility(View.GONE);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEditListener != null){
                        mEditListener.onClick(getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mLongClickListener != null){
                        mLongClickListener.onLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });

        }
    }
}
