package com.example.buyee

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.buyee.model.AppNavigation
import com.example.buyee.model.AppUnit
import com.example.buyee.model.GlobalNavigation
import com.example.buyee.model.Screen
import com.example.buyee.ui.theme.BuyeeTheme
import com.example.buyee.viewmodel.OrderViewModel
import com.razorpay.PaymentResultListener

class MainActivity : ComponentActivity(), PaymentResultListener {

    private val viewmodel: OrderViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BuyeeTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) { innerPadding ->
                    AppNavigation(Modifier.padding(innerPadding))
                }
            }
        }
    }

    override fun onPaymentSuccess(paymentId: String?) {

        viewmodel.clearCartAndAddToOrders()

        AlertDialog.Builder(this)
            .setTitle("Payment Successful")
            .setMessage("Thank you! Your order has been placed successfully.")
            .setPositiveButton("OK") { _, _ ->
                val navController = GlobalNavigation.navController
                navController.popBackStack()
                Screen.Home.route
            }
            .setCancelable(false)
            .show()
    }

    override fun onPaymentError(code: Int, response: String?) {

        AppUnit.showToast(this, "Payment Failed")
    }
}