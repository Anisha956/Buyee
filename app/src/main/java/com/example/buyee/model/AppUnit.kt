package com.example.buyee.model

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Locale


object AppUnit {

    fun showToast(
        context: Context,
        message: String
    ) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getDiscountPercentage(): Float = 10f

    fun getTaxPercentage(): Float = 13f


    fun toggleFavorite(context: Context, productId: String) {

        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        userDoc.get().addOnSuccessListener { doc ->

            val currentList = doc.get("favorites") as? List<String> ?: emptyList()

            val updatedList = if (currentList.contains(productId)) {
                currentList - productId   // remove
            } else {
                currentList + productId   // add
            }

            userDoc.update("favorites", updatedList)
                .addOnSuccessListener {
                    showToast(context, "Updated favorites")
                }
                .addOnFailureListener {
                    showToast(context, "Failed to update favorites")
                }
        }
    }

    fun formatDate(timestamp: Timestamp): String{
        val sdf = SimpleDateFormat("dd MMM yyyy , hh:mm a", Locale.getDefault())
        return sdf.format(timestamp.toDate().time)
    }
}


