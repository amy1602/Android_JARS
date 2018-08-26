package com.nhatran.mybudgetmanagemen

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView

import java.util.ArrayList

class PaymentListAdapter2(private val mContext: Context) : RecyclerView.Adapter<PaymentListAdapter2.PaymentViewHolder>() {


    internal var paymentList: MutableList<Payment2> = ArrayList()
    private var mEditListener: OnEditClickListener? = null
    private var mLongClickListener: OnItemLongClickListener? = null

    val totalMoneyAmount: Long
        get() {
            var total: Long = 0
            for (i in paymentList.indices) {
                if (paymentList[i].paymentUnit == "USD") {
                    total += paymentList[i].moneyAmount * 22727
                }
                if (paymentList[i].paymentUnit == "VND") {
                    total += paymentList[i].moneyAmount
                }
                if (paymentList[i].paymentUnit == "EUR") {
                    total += paymentList[i].moneyAmount* 26714
                }
            }

            return total
        }


    interface OnEditClickListener {
        fun onClick(position: Int)
    }

    fun setOnEditClickListener(listener: OnEditClickListener) {
        mEditListener = listener
    }

    interface OnItemLongClickListener {
        fun onLongClick(position: Int)
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mLongClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PaymentViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_budget, viewGroup, false)
        return PaymentViewHolder(itemView)
    }

    override fun onBindViewHolder(paymentViewHolder: PaymentViewHolder, i: Int) {
        val payment = paymentList[i]

        paymentViewHolder.txtTime.text = payment.timeAsString + ": "
        paymentViewHolder.txtMoneyAmount.text = payment.moneyAsString

    }

    fun setPaymentList(payment: List<Payment2>) {
        paymentList.addAll(payment)
        notifyDataSetChanged()
    }

    fun addPayment(payment: Payment2) {
        paymentList.add(payment)
        notifyDataSetChanged()
    }

    fun editPayment(position: Int, payment: Payment2) {
        paymentList[position] = payment
        notifyDataSetChanged()
    }

    fun getPayment(position: Int): Payment2 {
        return paymentList[position]
    }

    fun removePayment(position: Int) {
        paymentList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }

    inner class PaymentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtTime: TextView
        var txtMoneyAmount: TextView
        var btnEdit: ImageButton

        init {

            txtTime = itemView.findViewById(R.id.txt_time)
            txtMoneyAmount = itemView.findViewById(R.id.txt_money_amount)
            btnEdit = itemView.findViewById(R.id.btn_edit)

            btnEdit.setOnClickListener {
                if (mEditListener != null) {
                    mEditListener!!.onClick(adapterPosition)
                }
            }

            itemView.setOnLongClickListener {
                if (mLongClickListener != null) {
                    mLongClickListener!!.onLongClick(adapterPosition)
                }
                true
            }

        }
    }
}
