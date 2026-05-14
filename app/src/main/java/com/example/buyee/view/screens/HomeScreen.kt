package com.example.buyee.view.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.buyee.R
import com.example.buyee.model.UserModel
import com.example.buyee.view.pages.CartPage
import com.example.buyee.view.pages.FavoritePage
import com.example.buyee.view.pages.HomePage
import com.example.buyee.view.pages.OrderPage
import com.example.buyee.view.pages.ProfilePage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val navItemList = listOf(
        NavItem(label = "Home", icon = R.drawable.home),
        NavItem(label = "Favourite", icon = R.drawable.favorite),
        NavItem(label = "Cart", icon = R.drawable.shopping_cart),
        NavItem(label = "My Order", icon = R.drawable.shopping_bag),
        NavItem(label = "Profile", icon = R.drawable.profile)
    )

    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val userModel = remember { mutableStateOf(UserModel()) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    DisposableEffect(userId) {

        if (userId != null) {

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

        } else {
            onDispose { }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->

                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {

                            if (navItem.label == "Cart") {

                                BadgedBox(
                                    badge = {

                                        val cartCount = userModel.value.cartItems.values.sum()

                                        if (cartCount > 0) {
                                            Badge {
                                                Text(cartCount.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = navItem.icon),
                                        contentDescription = navItem.label
                                    )
                                }

                            } else {

                                Icon(
                                    painter = painterResource(id = navItem.icon),
                                    contentDescription = navItem.label
                                )
                            }
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->

        ContentScreen(
            modifier = modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            navController = navController
        )
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController
) {
    when (selectedIndex) {
        0 -> HomePage(modifier, navController)
        1 -> FavoritePage(modifier,navController)
        2 -> CartPage(modifier, navController)
        3 -> OrderPage(modifier)
        4 -> ProfilePage(modifier,navController)
    }
}

data class NavItem(
    val label: String,

    @DrawableRes
    val icon: Int
)