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

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaymentViewHolder>  {


    List<Payment> paymentList = new ArrayList<>();
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

    public PaymentListAdapter(Context context){
        mContext = context;
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_budget,viewGroup,false);
        return new PaymentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder paymentViewHolder, int i) {
        Payment payment = paymentList.get(i);

        paymentViewHolder.txtTime.setText(payment.getTimeAsString() +": ");
        paymentViewHolder.txtMoneyAmount.setText(payment.getMoneyAsString());

    }

    public void setPaymentList(List<Payment> payment){
        paymentList.addAll(payment);
        notifyDataSetChanged();
    }

    public void addPayment(Payment payment){
        paymentList.add(payment);
        notifyDataSetChanged();
    }

    public void editPayment(int position, Payment payment){
        paymentList.set(position, payment);
        notifyDataSetChanged();
    }

    public Payment getPayment(int position){
        return paymentList.get(position);
    }

    public void removePayment(int position){
        paymentList.remove(position);
        notifyItemRemoved(position);
    }

    public long getTotalMoneyAmount(){
        long total = 0;
        for (int i = 0 ; i < paymentList.size() ;i++){
            if (paymentList.get(i).getPaymentUnit().equals("USD")){
                total += paymentList.get(i).getMoneyAmount() * 22727;
            }
            if (paymentList.get(i).getPaymentUnit().equals("VND")){
                total += paymentList.get(i).getMoneyAmount();
            }
            if (paymentList.get(i).getPaymentUnit().equals("EUR")){
                total += paymentList.get(i).getMoneyAmount() * 26714;
            }
        }

        return total;
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    class PaymentViewHolder extends RecyclerView.ViewHolder{

        TextView txtTime;
        TextView txtMoneyAmount;
        ImageButton btnEdit;

        public PaymentViewHolder(final View itemView) {
            super(itemView);

            txtTime = itemView.findViewById(R.id.txt_time);
            txtMoneyAmount = itemView.findViewById(R.id.txt_money_amount);
            btnEdit = itemView.findViewById(R.id.btn_edit);

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
