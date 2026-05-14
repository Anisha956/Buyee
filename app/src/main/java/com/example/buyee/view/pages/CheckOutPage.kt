package com.example.buyee.view.pages

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.buyee.model.AppUnit
import com.example.buyee.model.ProductModel
import com.example.buyee.model.Screen
import com.example.buyee.model.UserModel
import com.example.buyee.ui.theme.YellowJC
import com.example.buyee.viewmodel.PaymentViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@SuppressLint("DefaultLocale")
@Composable
fun CheckOutPage(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val userModel = remember { mutableStateOf(UserModel()) }
    val productList = remember { mutableStateListOf<ProductModel>() }

    val subTotal = remember { mutableStateOf(0f) }
    val discount = remember { mutableStateOf(0f) }
    val tax = remember { mutableStateOf(0f) }
    val total = remember { mutableStateOf(0f) }

    val context = LocalContext.current
    val activity = context as Activity
    val viewModel: PaymentViewModel = viewModel()

    fun calculateAndAssign() {
        var tempSubTotal = 0f

        productList.forEach { product ->
            val price = product.actualPrice.toFloatOrNull() ?: 0f
            val qty = userModel.value.cartItems[product.id] ?: 0
            tempSubTotal += price * qty
        }

        subTotal.value = tempSubTotal
        discount.value = tempSubTotal * AppUnit.getDiscountPercentage() / 100
        tax.value = tempSubTotal * AppUnit.getTaxPercentage() / 100
        total.value = tempSubTotal - discount.value + tax.value
    }

    LaunchedEffect(Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@LaunchedEffect

        Firebase.firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                doc.toObject(UserModel::class.java)?.let { user ->
                    userModel.value = user

                    Firebase.firestore.collection("data")
                        .document("stock")
                        .collection("products")
                        .whereIn("id", user.cartItems.keys.toList())
                        .get()
                        .addOnSuccessListener { result ->
                            productList.clear()
                            val products = result.toObjects(ProductModel::class.java)
                            productList.addAll(products)
                            calculateAndAssign()
                        }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "Checkout",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 16.dp , bottom = 20.dp)
            )


            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {

                // ADDRESS
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Deliver to", fontSize = 12.sp, color = Color.Gray)
                        Text(userModel.value.name, fontWeight = FontWeight.Bold)
                        Text(userModel.value.address, fontSize = 13.sp, color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Items", fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                // ITEMS
                productList.forEach { product ->
                    val qty = userModel.value.cartItems[product.id] ?: 0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                navController.navigate(
                                    Screen.ProductDetails.createRoute(product.id)
                                )
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            AsyncImage(
                                model = product.images.firstOrNull(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {

                                Text(
                                    product.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(5.dp))

                                Text("₹${product.actualPrice}", fontWeight = FontWeight.Bold)

                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text("x$qty", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BILL
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text("Bill Details", fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = Modifier.height(10.dp))

                        RowCheckOutItems(
                            "Subtotal",
                            "₹${String.format("%.2f", subTotal.value)}"
                        )

                        RowCheckOutItems(
                            "Discount",
                            "-₹${String.format("%.2f", discount.value)}"
                        )

                        RowCheckOutItems(
                            "Tax",
                            "+₹${String.format("%.2f", tax.value)}"
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "To Pay",
                                fontWeight = FontWeight.Bold)
                            Text(
                                "₹${String.format("%.2f", total.value)}",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // BOTTOM BAR
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Button(
                onClick = {
                    viewModel.startPayment(activity, total.value)
                },
                colors = ButtonDefaults.buttonColors(containerColor = YellowJC),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(55.dp)
            ) {
                Text(
                    "Place Order",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}
@Composable
fun RowCheckOutItems(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            fontSize = 16.sp
        )
    }
}