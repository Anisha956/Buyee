package com.example.buyee.viewmodel

import androidx.lifecycle.ViewModel
import com.example.buyee.model.OrderItem
import com.example.buyee.model.OrderModel
import com.example.buyee.model.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import java.util.UUID

class OrderViewModel: ViewModel() {

    fun clearCartAndAddToOrders() {

        val userId = FirebaseAuth.getInstance().currentUser?.uid!!
        val userDoc = Firebase.firestore.collection("users").document(userId)

        userDoc.get().addOnSuccessListener { snapshot ->

            val cartItems =
                snapshot.get("cartItems") as? Map<String, Long> ?: emptyMap()

            Firebase.firestore.collection("data")
                .document("stock")
                .collection("products")
                .get()
                .addOnSuccessListener { result ->

                    val products = result.documents.mapNotNull {
                        it.toObject(ProductModel::class.java)
                    }.associateBy { it.id }

                    val orderItems = cartItems.mapNotNull { (id, qty) ->

                        val p = products[id] ?: return@mapNotNull null

                        OrderItem(
                            productId = id,
                            title = p.title,
                            imageUrl = p.images.firstOrNull() ?: "",
                            price = p.actualPrice,
                            quantity = qty
                        )
                    }

                    val order = OrderModel(
                        id = "ORD_" + UUID.randomUUID()
                            .toString()
                            .replace("-", "")
                            .take(10)
                            .uppercase(),
                        date = Timestamp.now(),
                        userId = userId,
                        status = "Ordered",
                        address = snapshot.getString("address") ?: "",
                        items = orderItems
                    )

                    Firebase.firestore.collection("orders")
                        .document(order.id)
                        .set(order)
                        .addOnSuccessListener {
                            userDoc.update("cartItems", FieldValue.delete())
                        }
                }
        }
    }
}