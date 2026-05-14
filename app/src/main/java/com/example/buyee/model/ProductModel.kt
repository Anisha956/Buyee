package com.example.buyee.model

data class ProductModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val actualPrice: String = "",
    val rating: String = "",
    val star: String = "",
    val images: List<String> = emptyList(),
    val otherDetails: Map<String, String> = mapOf()
)
