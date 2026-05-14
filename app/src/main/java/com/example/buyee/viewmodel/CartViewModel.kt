package com.example.buyee.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.buyee.model.AppUnit.showToast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class CartViewModel : ViewModel() {

    fun addToCart(
        context: Context,
        productId: String,
        onSuccess: () -> Unit = {}
    ) {

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {

            if (it.isSuccessful) {

                val currentCart =
                    it.result.get("cartItems") as? Map<String, Long>
                        ?: emptyMap()

                val currentQuantity = currentCart[productId] ?: 0

                val updatedQuantity = currentQuantity + 1

                val updatedCart =
                    mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {

                            showToast(
                                context,
                                "Item added to the cart"
                            )

                            onSuccess()

                        } else {

                            showToast(
                                context,
                                "Failed adding item to the cart"
                            )
                        }
                    }
            }
        }
    }

    fun removeFromCart(context: Context, productId: String, removeAll: Boolean = false) {

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val currentCart = it.result.get("cartItems") as? Map<String, Long> ?: emptyMap()
                val currentQuantity = currentCart[productId] ?: 0
                val updatedQuantity = currentQuantity - 1

                val updatedCart =
                    if (updatedQuantity <= 0 || removeAll)
                        mapOf("cartItems.$productId" to FieldValue.delete())
                    else
                        mapOf("cartItems.$productId" to updatedQuantity)

                userDoc.update(updatedCart)
                    .addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            showToast(context, "Item remove from the cart")
                        } else {
                            showToast(context, "Failed removing item from the cart")
                        }
                    }
            }
        }
    }

}