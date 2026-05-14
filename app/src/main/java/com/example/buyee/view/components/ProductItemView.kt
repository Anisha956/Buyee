package com.example.buyee.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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

@Composable
fun ProductItemView(
    modifier: Modifier = Modifier,
    product: ProductModel,
    navController: NavController
) {

    val context = LocalContext.current
    val viewmodel: CartViewModel = viewModel()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .clickable {
                navController.navigate(
                    Screen.ProductDetails.createRoute(product.id)
                )
            },
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column {

            // IMAGE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
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
                    contentDescription = product.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                )

                // STAR BADGE
                if (product.star.isNotEmpty()) {

                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.TopStart),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1B5E20)
                        ),
                        shape = RoundedCornerShape(8.dp)
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
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                )
            ) {

                // TITLE
                if (product.title.isNotEmpty()) {

                    Text(
                        text = product.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        maxLines = 2,
                        lineHeight = 18.sp,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        // OLD PRICE
                        if (product.price.isNotEmpty()) {

                            Text(
                                text = "₹${product.price}",
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = TextStyle(
                                    textDecoration =
                                        TextDecoration.LineThrough
                                ),
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(2.dp))
                        }

                        // ACTUAL PRICE
                        if (product.actualPrice.isNotEmpty()) {

                            Text(
                                text = "₹${product.actualPrice}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // ADD BUTTON
                    Button(
                        onClick = {
                            viewmodel.addToCart(
                                context,
                                product.id
                            )
                        },
                        contentPadding = PaddingValues(
                            horizontal = 10.dp,
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
                            modifier = Modifier.size(15.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Add",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}