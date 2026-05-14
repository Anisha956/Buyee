package com.example.buyee.view.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.buyee.model.UserModel
import com.example.buyee.ui.theme.YellowJC
import com.example.buyee.view.components.CartItemView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun CartPage(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val userModel = remember { mutableStateOf(UserModel()) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid



    if (userId == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Please login to view cart")
        }
        return
    }

    DisposableEffect(userId) {
        val listener = Firebase.firestore.collection("users")
            .document(userId)
            .addSnapshotListener { snapshot, _ ->
                snapshot?.toObject(UserModel::class.java)?.let {
                    userModel.value = it
                }
            }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Text(
            text = "Your Cart",
            style = TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (userModel.value.cartItems.isNotEmpty()) {

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    userModel.value.cartItems.toList(),
                    key = { it.first }
                ) { (productId, qty) ->
                    CartItemView(
                        productId = productId,
                        qty = qty
                    )
                }
            }

            Button(
                onClick = {
                    navController.navigate("checkout")
                },
                colors = ButtonDefaults.buttonColors(containerColor = YellowJC),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Checkout",  color = Color.Black,
                    fontWeight = FontWeight.Bold)
            }

        } else {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No items here", fontSize = 20.sp)
            }
        }
    }
}