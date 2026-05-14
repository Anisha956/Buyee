package com.example.buyee.model

data class OrderItem(

    var productId: String = "",
    var title: String = "",
    var imageUrl: String = "",
    var price: String = "",
    var quantity: Long = 1

)