package com.example.buyee.model

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object Checkout : Screen("checkout")
    object Search : Screen("search")
    object Order : Screen("orders")

    object CategoryProducts : Screen("category-products/{categoryId}") {
        fun createRoute(categoryId: String) = "category-products/$categoryId"
    }

    object ProductDetails : Screen("product-details/{productId}") {
        fun createRoute(productId: String) = "product-details/$productId"
    }
}