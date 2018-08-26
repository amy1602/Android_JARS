package com.nhatran.mybudgetmanagemen

import java.text.SimpleDateFormat
import java.util.Date

class Payment2 {
    var paymentTime: Date? = null
    var moneyAmount: Long = 0
    var paymentUnit: String?="";
    var id = -1

    val moneyAsString: String
        get() = moneyAmount.toString() + " " + paymentUnit

    val timeAsString: String
        get() = convertDateToString(paymentTime)

    constructor(paymentTime: Date, moneyAmount: Long, paymentUnit: String) {
        this.paymentTime = paymentTime
        this.moneyAmount = moneyAmount
        this.paymentUnit = paymentUnit
    }

    constructor(id: Int, paymentTime: Date, moneyAmount: Long, paymentUnit: String) {
        this.paymentTime = paymentTime
        this.moneyAmount = moneyAmount
        this.paymentUnit = paymentUnit
        this.id = id
    }

    constructor() {}

    companion object {

        fun convertDateToString(time: Date?): String {

            if (time != null) {
                val mdformat = SimpleDateFormat("dd/MM/yyyy")
                return mdformat.format(time)
            }
            return ""
        }
    }
}
