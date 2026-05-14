package com.example.buyee.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.buyee.model.ProductModel
import com.example.buyee.model.Screen
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun SearchPage(navController: NavHostController) {

    var searchText by remember { mutableStateOf("") }
    val allProducts = remember { mutableStateListOf<ProductModel>() }


    LaunchedEffect(Unit) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .get()
            .addOnSuccessListener {
                allProducts.clear()
                allProducts.addAll(it.toObjects(ProductModel::class.java))
            }
    }

    val suggestions = remember(searchText, allProducts) {
        if (searchText.isEmpty()) emptyList()
        else allProducts.filter {
            it.title.contains(searchText, ignoreCase = true)
        }.take(6)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Spacer(modifier = Modifier.height(16.dp))
        // SEARCH BAR
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 14.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF757575)
                )

                Spacer(modifier = Modifier.width(10.dp))

                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text(
                            "Search products...",
                            color = Color(0xFF9E9E9E)
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,

                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,

                        cursorColor = Color.Black,

                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                if (searchText.isNotEmpty()) {

                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                searchText = ""
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        //SUGGESTION LIST
        if (searchText.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {

                items(suggestions) { product ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clickable {
                                navController.navigate(
                                    Screen.ProductDetails.createRoute(product.id)
                                )
                            },
                        shape = RoundedCornerShape(14.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 16.dp
                                )
                        ) {

                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = product.title,
                                fontSize = 15.sp,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }


        if (searchText.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Start typing to search products",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }
    }
}