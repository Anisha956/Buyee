package com.example.buyee.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.buyee.model.ProductModel
import com.example.buyee.model.Screen
import com.example.buyee.viewmodel.CartViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun FavoritePage(modifier: Modifier = Modifier,navController: NavController) {

    val favoriteList = remember {
        mutableStateOf<List<ProductModel>>(emptyList())
    }

    val context = LocalContext.current
    val viewmodel: CartViewModel = viewModel()

    LaunchedEffect(Unit) {

        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get()
            .addOnSuccessListener { userDoc ->

                val favorites =
                    userDoc.get("favorites") as? List<String>
                        ?: emptyList()

                if (favorites.isNotEmpty()) {

                    Firebase.firestore.collection("data")
                        .document("stock")
                        .collection("products")
                        .whereIn("id", favorites)
                        .get()
                        .addOnSuccessListener { result ->

                            favoriteList.value =
                                result.toObjects(ProductModel::class.java)
                        }
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
                text = "My Favorites",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        if (favoriteList.value.isEmpty()) {

            // EMPTY STATE
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(5.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier
                            .padding(26.dp)
                            .size(44.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "No favorites yet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Items you like will appear here",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(
                    start = 10.dp,
                    end = 10.dp,
                    top = 12.dp,
                    bottom = 20.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                items(favoriteList.value) { product ->

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        ),
                        modifier = Modifier.clickable(onClick = {
                            navController.navigate(Screen.ProductDetails.route)
                        })
                    ) {

                        Column {

                            // IMAGE
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFFF8F8F8),
                                                Color(0xFFEDEDED)
                                            )
                                        )
                                    )
                            ) {

                                AsyncImage(
                                    model = product.images.firstOrNull(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(14.dp),
                                    contentScale = ContentScale.Fit
                                )

                                // FAVORITE
                                Card(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .align(Alignment.TopEnd),
                                    shape = CircleShape,
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(18.dp)
                                    )
                                }

                                if (product.star.isNotEmpty()) {
                                    // RATING
                                    Card(
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .align(Alignment.TopStart),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFF1B5E20)
                                        )
                                    ) {

                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 6.dp,
                                                vertical = 3.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = product.star,
                                                color = Color.White,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )


                                            Spacer(modifier = Modifier.width(2.dp))

                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(11.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text = product.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black,
                                    maxLines = 2,
                                    lineHeight = 18.sp,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    verticalAlignment = Alignment.Bottom
                                ) {

                                    Column {

                                        Text(
                                            text = "₹${product.price}",
                                            fontSize = 11.sp,
                                            textDecoration =
                                                TextDecoration.LineThrough,
                                            color = Color.Gray
                                        )

                                        Spacer(
                                            modifier = Modifier.height(2.dp)
                                        )

                                        Text(
                                            text = "₹${product.actualPrice}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.Black
                                        )
                                    }

                                    Spacer(modifier = Modifier.weight(1f))

                                    Button(
                                        onClick = {
                                            viewmodel.addToCart(context, product.id)
                                        },
                                        contentPadding = PaddingValues(
                                            horizontal = 12.dp,
                                            vertical = 0.dp
                                        ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFFFD54F)
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.height(38.dp)
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = "Add",
                                            color = Color.Black,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}