package com.example.buyee.view.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.buyee.model.ProductModel
import com.example.buyee.view.components.ProductItemView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryProductPage(
    modifier: Modifier = Modifier,
    categoryId: String,
    navController: NavController
) {

    val productsList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }

    LaunchedEffect(categoryId) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .whereEqualTo("category", categoryId.lowercase())
            .get()
            .addOnSuccessListener { result ->
                productsList.value = result.documents.mapNotNull {
                    it.toObject(ProductModel::class.java)
                }
            }
    }

    if (productsList.value.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("No products found")
        }

    } else {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(productsList.value) { product ->
                ProductItemView(
                    product = product,
                    modifier = Modifier.fillMaxWidth(),
                    navController = navController
                )
            }
        }
    }
}