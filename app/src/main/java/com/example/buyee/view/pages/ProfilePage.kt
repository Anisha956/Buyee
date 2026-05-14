package com.example.buyee.view.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.buyee.R
import com.example.buyee.model.AppUnit
import com.example.buyee.model.Screen
import com.example.buyee.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

@Composable
fun ProfilePage(modifier: Modifier = Modifier, navController: NavController) {

    val userModel = remember { mutableStateOf(UserModel()) }
    var addressInput by remember { mutableStateOf(userModel.value.address) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = it.result.toObject(UserModel::class.java)
                    if (result != null) {
                        userModel.value = result
                        addressInput = userModel.value.address
                    }
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)

        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //  IMAGE
                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .size(90.dp)
                            .padding(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = userModel.value.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = userModel.value.email,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }


        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // ADDRESS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFFFF9800)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Delivery Address",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp)),
                        value = addressInput,
                        onValueChange = {
                            addressInput = it
                        },
                        placeholder = {
                            Text("Enter your address")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {

                            if (addressInput.isNotEmpty()) {

                                Firebase.firestore.collection("users")
                                    .document(FirebaseAuth.getInstance().currentUser?.uid!!)
                                    .update("address", addressInput)
                                    .addOnCompleteListener {

                                        if (it.isSuccessful) {
                                            AppUnit.showToast(
                                                context,
                                                "Address updated successfully"
                                            )
                                        }
                                    }

                            } else {

                                AppUnit.showToast(
                                    context,
                                    "Address can't be empty"
                                )
                            }
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF8F8F8),
                            unfocusedContainerColor = Color(0xFFF8F8F8),

                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,

                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // OPTIONS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color.Black
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Settings",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Notifications, theme & preferences",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        TextButton(
                            onClick = {
                                //  settings page
                            }
                        ) {
                            Text("Open")
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F1F1))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.policy),
                            contentDescription = null,
                            tint = Color(0xFF1565C0)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Privacy Policy",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Read our terms and privacy policy",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        TextButton(
                            onClick = {
                                //  privacy page
                            }
                        ) {
                            Text("View")
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F1F1))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.support),
                            contentDescription = null,
                            tint = Color(0xFFFF9800)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Help & Support",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "FAQs, contact support & app help",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        TextButton(
                            onClick = {
                                //  support page
                            }
                        ) {
                            Text("Help")
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F1F1))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ExitToApp,
                            contentDescription = null,
                            tint = Color.Red
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "Logout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Sign out from your account",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        TextButton(
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                navController.popBackStack()
                                navController.navigate(Screen.Login.route)

                            }
                        ) {
                            Text(
                                "Exit",
                                color = Color.Red
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Buyee App",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(30.dp))
        }


    }
}