package com.example.buyee.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buyee.R
import com.example.buyee.model.OrderModel
import com.example.buyee.view.components.OrderView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun OrderPage(modifier: Modifier = Modifier) {

    val orderList = remember {
        mutableStateOf<List<OrderModel>>(emptyList())
    }

    val resultList = mutableListOf<OrderModel>()

    LaunchedEffect(Unit) {

        Firebase.firestore.collection("orders")
            .whereEqualTo(
                "userId",
                FirebaseAuth.getInstance().currentUser?.uid!!
            )
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {

                    it.result.documents.forEach { doc ->

                        try {
                            val order = doc.toObject(OrderModel::class.java)

                            if (order != null) {
                                resultList.add(order)
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    orderList.value = resultList
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(
                    horizontal = 18.dp,
                    vertical = 20.dp
                )
        ) {

            Text(
                text = "My Orders",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Track and manage your purchases",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }


        if (orderList.value.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.shopping_bag),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier
                                .padding(24.dp)
                                .size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "No orders yet",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your placed orders will appear here",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 20.dp
                )
            ) {

                items(orderList.value) {

                    OrderView(it)
                }
            }
        }
    }
}