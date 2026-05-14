package com.example.buyee.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.buyee.model.AppUnit
import com.example.buyee.model.GlobalNavigation
import com.example.buyee.model.ProductModel
import com.example.buyee.model.Screen
import com.example.buyee.ui.theme.YellowJC
import com.example.buyee.viewmodel.CartViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
@Composable
fun ProductDetailsPage(
    modifier: Modifier = Modifier,
    productId: String
) {

    var product by remember { mutableStateOf(ProductModel()) }
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }
    val viewmodel: CartViewModel = viewModel()

    LaunchedEffect(productId) {
        Firebase.firestore.collection("data")
            .document("stock")
            .collection("products")
            .document(productId)
            .get()
            .addOnSuccessListener {
                product = it.toObject(ProductModel::class.java)
                    ?: ProductModel()
            }
    }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val list =
                    it.get("favorites") as? List<String>
                        ?: emptyList()

                isFavorite = list.contains(productId)
            }
    }

    if (product.title.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            // TITLE
            Row(
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Card(
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F8F8)
                    )
                ) {

                    IconButton(
                        onClick = {
                            AppUnit.toggleFavorite(
                                context,
                                productId
                            )

                            isFavorite = !isFavorite
                        }
                    ) {

                        Icon(
                            contentDescription = "Favorite",
                            tint = if (isFavorite)
                                Color.Red
                            else
                                Color.Gray,
                            imageVector = if (isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder
                        )
                    }
                }
            }

            // RATING
            if (
                product.star.isNotEmpty() ||
                product.rating.isNotEmpty()
            ) {

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (product.star.isNotEmpty()) {

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1B5E20)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {

                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp
                                ),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text = product.star,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )

                                Spacer(
                                    modifier = Modifier.width(3.dp)
                                )

                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    if (product.rating.isNotEmpty()) {

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "(${product.rating} ratings)",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // IMAGES
            val images = product.images

            if (images.isNotEmpty()) {

                val pageState = rememberPagerState {
                    images.size
                }

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                    ) {

                        HorizontalPager(
                            state = pageState
                        ) { index ->

                            AsyncImage(
                                model = images[index],
                                contentDescription =
                                    product.title,
                                contentScale =
                                    ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {

                        DotsIndicator(
                            dotCount = images.size,
                            type = ShiftIndicatorType(
                                DotGraphic(
                                    color = Color.Black,
                                    size = 6.dp
                                )
                            ),
                            pagerState = pageState
                        )
                    }
                }
            }

            // PRICE
            if (
                product.price.isNotEmpty() ||
                product.actualPrice.isNotEmpty()
            ) {

                Spacer(modifier = Modifier.height(22.dp))

                Column {

                    if (product.price.isNotEmpty()) {

                        Text(
                            text = "₹${product.price}",
                            fontSize = 14.sp,
                            style = TextStyle(
                                textDecoration =
                                    TextDecoration.LineThrough
                            ),
                            color = Color.Gray,
                            maxLines = 1
                        )

                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )
                    }

                    if (product.actualPrice.isNotEmpty()) {

                        Text(
                            text = "₹${product.actualPrice}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider(
                    color = Color(0xFFEAEAEA)
                )
            }

            // DESCRIPTION
            if (product.description.isNotEmpty()) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Product Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    color = Color.DarkGray
                )
            }


            if (product.otherDetails.isNotEmpty()) {

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Specifications",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(14.dp))

                product.otherDetails.forEach { (key, value) ->

                    if (
                        key.isNotEmpty() &&
                        value.isNotEmpty()
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = key,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Spacer(
                                modifier = Modifier.width(20.dp)
                            )

                            Text(
                                text = value,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFF1F1F1)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))


            Button(
                onClick = {
                    viewmodel.addToCart(
                        context,
                        productId
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = YellowJC
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                Text(
                    text = "Add to Cart",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedButton(
                onClick = {

                    viewmodel.addToCart(
                        context = context,
                        productId = productId,
                        onSuccess = {

                            GlobalNavigation.navController
                                .navigate(
                                    Screen.Checkout.route
                                )
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {

                Text(
                    text = "Buy Now",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}