package com.example.buyee.model

import com.google.firebase.Timestamp

data class OrderModel(
    var id: String = "",
    var date: Timestamp = Timestamp.now(),
    var userId: String = "",
    var status: String = "",
    var address: String = "",
    var items: List<OrderItem> = emptyList()
)
