package com.nhatran.mybudgetmanagemen

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

import com.nhatran.mybudgetmanagemen.db.PaymentDBHelper

import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class MainActivity2 : AppCompatActivity() {

    private var rvPaymentList: RecyclerView? = null
    private var mAdapter: PaymentListAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var btnAddPayment: FloatingActionButton? = null
    private var txtUsedAmount: TextView? = null
    private var txtRemain: TextView? = null
    private var txtTotal: EditText? = null
    private var btnSaveTotalAmount: Button? = null
    private var btnEditTotal: ImageButton? = null
    private var txtFromDate: TextView? = null
    private var txtToDate: TextView? = null
    private var txtSuggest: TextView? = null
    private var txtRemainDay: TextView? = null

    private var totalAmount: Long = 3000000
    private var mDatabaseManagement: PaymentDBHelper? = null

    internal val remainDay: Int
        get() = if (fromDate != null) {
            if (fromDate!!.after(Calendar.getInstance().time)) getDaysDifference(fromDate, toDate) else getDaysDifference(Calendar.getInstance().time, toDate)
        } else {
            1
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        mDatabaseManagement = PaymentDBHelper(applicationContext)
        btnAddPayment = findViewById(R.id.btn_add_payment)
        txtFromDate = findViewById(R.id.txt_from_date)
        txtToDate = findViewById(R.id.txt_to_date)
        txtRemainDay = findViewById(R.id.txt_day_remain)
        txtSuggest = findViewById(R.id.txt_suggest)

        layoutManager = LinearLayoutManager(this)
        mAdapter = PaymentListAdapter(this)

        rvPaymentList = findViewById(R.id.rv_payment_list)
        rvPaymentList!!.layoutManager = layoutManager
        rvPaymentList!!.adapter = mAdapter

        txtUsedAmount = findViewById(R.id.txt_used)
        txtRemain = findViewById(R.id.txt_remain)
        txtTotal = findViewById(R.id.edittext_total)
        btnSaveTotalAmount = findViewById(R.id.btn_save_total_amount)
        btnEditTotal = findViewById(R.id.btn_edit_total)

        updateText()

        txtFromDate!!.setOnClickListener {
            val datePickerDialog = CustomDatePickerDialog(this@MainActivity2)
            datePickerDialog.createDatePickerDialog()
            datePickerDialog.setOnDateSetListener { date ->
                txtFromDate!!.text = Payment.convertDateToString(date)
                fromDate = date
                updateText()
            }
        }

        txtToDate!!.setOnClickListener {
            val datePickerDialog = CustomDatePickerDialog(this@MainActivity2)
            datePickerDialog.createDatePickerDialog()
            datePickerDialog.setOnDateSetListener { date ->
                txtToDate!!.text = Payment.convertDateToString(date)
                toDate = date
                updateText()
            }
        }

        btnEditTotal!!.setOnClickListener {
            txtTotal!!.isEnabled = true
            btnSaveTotalAmount!!.visibility = View.VISIBLE
            txtTotal!!.setText(totalAmount.toString())
            txtTotal!!.inputType = InputType.TYPE_CLASS_NUMBER
        }

        btnSaveTotalAmount!!.setOnClickListener {
            totalAmount = Integer.valueOf(txtTotal!!.text.toString()).toLong()
            txtTotal!!.isEnabled = false
            btnSaveTotalAmount!!.visibility = View.GONE
            updateText()
        }

        mAdapter!!.setOnEditClickListener { position ->
            val dialog = PaymentDialog(this@MainActivity2, mAdapter!!.getPayment(position))
            dialog.createNewPaymentDialog()
            val payment = Payment()
            dialog.setOnButtonClickListener(object : PaymentDialog.OnDialogButtonClickListener {
                override fun onSaveClick(moneyAmount: String) {
                    payment.setMoneyAmount(java.lang.Long.valueOf(moneyAmount))
                    mDatabaseManagement!!.deletePayment(mAdapter!!.getPayment(position).getId())
                    tempList.remove(mAdapter!!.getPayment(position))
                    tempList.add(payment)
                    mAdapter!!.editPayment(position, payment)
                    updateText()
                }

                override fun onCancelClick() {

                }

                override fun onUnitSpinnerSelect(unitString: String) {
                    payment.setPaymentUnit(unitString)
                }

                override fun onDatePickerSelect(date: Date) {
                    payment.setPaymentTime(date)
                }
            })
        }

        mAdapter!!.setOnItemLongClickListener { position ->
            val dialog = AlertDialog.Builder(this@MainActivity2)
            dialog.setTitle("Delete confirmation")
            dialog.setMessage("Do you sure delete this payment?")

            dialog.setPositiveButton("Yes") { dialogInterface, i ->
                mDatabaseManagement!!.deletePayment(mAdapter!!.getPayment(position).getId())
                tempList.remove(mAdapter!!.getPayment(position))
                mAdapter!!.removePayment(position)
                updateText()
            }

            dialog.setNegativeButton("No") { dialogInterface, i -> }

            dialog.show()
        }


        btnAddPayment!!.setOnClickListener {
            val dialog = PaymentDialog(this@MainActivity2)
            dialog.createNewPaymentDialog()
            val payment = Payment()
            dialog.setOnButtonClickListener(object : PaymentDialog.OnDialogButtonClickListener {
                override fun onSaveClick(moneyAmount: String) {
                    payment.setMoneyAmount(java.lang.Long.valueOf(moneyAmount))
                    mAdapter!!.addPayment(payment)
                    tempList.add(payment)
                    rvPaymentList!!.scrollToPosition(0)
                    updateText()
                }

                override fun onCancelClick() {

                }

                override fun onUnitSpinnerSelect(unitString: String) {
                    payment.setPaymentUnit(unitString)
                }


                override fun onDatePickerSelect(date: Date) {
                    payment.setPaymentTime(date)
                }
            })
        }

        // initData();
    }

    private fun updateText() {
        txtUsedAmount!!.text = "Used: " + mAdapter!!.totalMoneyAmount.toString() + " VND"
        txtRemain!!.text = "Remain: " + (totalAmount - mAdapter!!.totalMoneyAmount).toString() + " VND"
        txtSuggest!!.text = "Suggest: " + ((totalAmount - mAdapter!!.totalMoneyAmount) / remainDay).toString() + " VND/ day"
        txtTotal!!.setText("Total: " + totalAmount.toString())
        txtRemainDay!!.text = remainDay.toString() + " days"
        if (fromDate != null) {
            txtFromDate!!.text = Payment.convertDateToString(fromDate)
        }
        if (toDate != null) {
            txtToDate!!.text = Payment.convertDateToString(toDate)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            mAdapter!!.setPaymentList(mDatabaseManagement!!.allPayments)
            totalAmount = mDatabaseManagement!!.total
            fromDate = mDatabaseManagement!!.fromDate
            toDate = mDatabaseManagement!!.toDate
            updateText()
            isFirstLoad = false
        }
    }

    private fun saveData() {
        for (i in tempList) {
            mDatabaseManagement!!.addPayment(i)
        }
        mDatabaseManagement!!.deleteTotalAndDate()
        mDatabaseManagement!!.saveTotalAndDate(totalAmount, Payment.convertDateToString(fromDate), Payment.convertDateToString(toDate))
        tempList = ArrayList()
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }

    private fun initData() {
        val payments = ArrayList<Payment>()
        payments.add(Payment(Calendar.getInstance().time, 11000, "VND"))
        payments.add(Payment(Calendar.getInstance().time, 21000, "VND"))
        payments.add(Payment(Calendar.getInstance().time, 31000, "VND"))
        payments.add(Payment(Calendar.getInstance().time, 41000, "VND"))
        mAdapter!!.setPaymentList(payments)
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    companion object {
        internal var tempList: MutableList<Payment> = ArrayList()
        internal var isFirstLoad = true
        internal var fromDate: Date? = null
        internal var toDate: Date? = null

        fun getDaysDifference(fromDate: Date?, toDate: Date?): Int {
            return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) / (1000 * 60 * 60 * 24)).toInt()

        }
    }
}
