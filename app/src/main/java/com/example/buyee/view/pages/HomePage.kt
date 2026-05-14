package com.example.buyee.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.buyee.model.ProductModel
import com.example.buyee.view.components.BannerView
import com.example.buyee.view.components.CategoryView
import com.example.buyee.view.components.HeaderView
import com.example.buyee.view.components.ProductItemView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController) {

    val productsList = remember { mutableStateOf<List<ProductModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .get()
            .addOnSuccessListener { result ->
                productsList.value = result.documents.mapNotNull {
                    it.toObject(ProductModel::class.java)
                }
            }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {

        item {
            HeaderView(
                Modifier.fillMaxWidth(),
                onSearchClick = { navController.navigate("search") }
            )
        }

        item {
            BannerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(14.dp)) }

        item {
            CategoryView(
                Modifier.padding(horizontal = 12.dp),
                navController
            )
        }

        item { Spacer(modifier = Modifier.height(18.dp)) }

        item {
            Text(
                text = "Popular Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }

        item { Spacer(modifier = Modifier.height(10.dp)) }

        item {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .heightIn(max = 2000.dp)
                    .padding(horizontal = 12.dp),
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
}