package com.example.buyee.model

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buyee.view.pages.CategoryProductPage
import com.example.buyee.view.pages.CheckOutPage
import com.example.buyee.view.pages.OrderPage
import com.example.buyee.view.pages.ProductDetailsPage
import com.example.buyee.view.pages.SearchPage
import com.example.buyee.view.screens.AuthScreen
import com.example.buyee.view.screens.HomeScreen
import com.example.buyee.view.screens.LoginScreen
import com.example.buyee.view.screens.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {

    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser != null
    val startDestination = if (isLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Auth.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.Auth.route) {
            AuthScreen(modifier, navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(modifier, navController)
        }

        composable(Screen.Signup.route) {
            SignupScreen(modifier, navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(modifier, navController)
        }

        composable(Screen.Checkout.route) {
            CheckOutPage(modifier, navController)
        }

        composable(Screen.Search.route) {
            SearchPage(navController)
        }

        composable(Screen.Order.route) {
            OrderPage()
        }

        composable(Screen.CategoryProducts.route) { backStackEntry ->
            val categoryId =
                backStackEntry.arguments?.getString("categoryId") ?: ""

            CategoryProductPage(modifier, categoryId,navController)
        }

        composable(Screen.ProductDetails.route) { backStackEntry ->
            val productId =
                backStackEntry.arguments?.getString("productId") ?: ""

            ProductDetailsPage(modifier, productId)
        }
    }
}

object GlobalNavigation{
    @SuppressLint("StaticFieldLeak")
    lateinit var navController: NavController
}