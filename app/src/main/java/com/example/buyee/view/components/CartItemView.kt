package com.example.buyee.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import coil.compose.AsyncImage
import com.example.buyee.model.GlobalNavigation.navController
import com.example.buyee.model.ProductModel
import com.example.buyee.model.Screen
import com.example.buyee.viewmodel.CartViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CartItemView(
    modifier: Modifier = Modifier,
    productId: String,
    qty: Long
) {

    var product by remember { mutableStateOf(ProductModel()) }
    val context = LocalContext.current
    val viewModel : CartViewModel = viewModel()


    LaunchedEffect(productId) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener {
                product = it.toObject(ProductModel::class.java) ?: ProductModel()
            }
    }

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
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // IMAGE
            AsyncImage(
                model = product.images.firstOrNull(),
                contentDescription = null,
                modifier = Modifier
                    .size(75.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // TITLE
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // PRICE
                Text(
                    text = "₹${product.actualPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // QUANTITY
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(
                        onClick = {
                            viewModel.removeFromCart(context, productId)
                        },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("-")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = qty.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    TextButton(
                        onClick = {
                            viewModel.addToCart(context, productId)
                        },
                        modifier = Modifier.size(36.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("+")
                    }
                }
            }

            // DELETE BUTTON
            IconButton(
                onClick = {
                    viewModel.removeFromCart(context, productId, true)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove"
                )
            }
        }
    }
}