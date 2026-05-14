package com.example.buyee.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.buyee.model.CategoryModel
import com.example.buyee.model.Screen
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun CategoryView(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    var categoryList by remember {
        mutableStateOf<List<CategoryModel>>(emptyList())
    }


    LaunchedEffect(Unit) {

        Firebase.firestore.collection("data")
            .document("stock")
            .collection("categories")
            .get()
            .addOnSuccessListener { result ->

                val list = result.documents.mapNotNull {
                    it.toObject(CategoryModel::class.java)
                }

                categoryList = list
            }
    }

    Column(modifier = modifier) {

        // SECTION TITLE
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 6.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Shop by Category",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        if (categoryList.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }

            return
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                horizontal = 14.dp,
                vertical = 6.dp
            )
        ) {

            items(categoryList) { item ->

                CategoryItem(
                    category = item,
                    navController = navController
                )
            }
        }
    }
}
@Composable
fun CategoryItem(
    category: CategoryModel,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable {
                navController.navigate(
                    Screen.CategoryProducts.createRoute(category.id)
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(62.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = category.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            maxLines = 1
        )
    }


}