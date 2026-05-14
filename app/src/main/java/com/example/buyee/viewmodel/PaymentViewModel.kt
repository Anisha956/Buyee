package com.example.buyee.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.razorpay.Checkout
import org.json.JSONObject


class PaymentViewModel : ViewModel() {

    fun startPayment(
        activity: Activity,
        amount: Float
    ) {

        val co = Checkout()
        co.setKeyID("rzp_test_SlC2TYcA56mLL9")

        try {

            val options = JSONObject()

            options.put("name", "Easy Shop")
            options.put("description", "Total amount")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")

            options.put("amount", (amount * 100).toInt())

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "Neha@example.com")
            prefill.put("contact", "+919643910241")

            options.put("prefill", prefill)

            co.open(activity, options)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}