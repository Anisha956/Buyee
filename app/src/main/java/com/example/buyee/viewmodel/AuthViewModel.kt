package com.example.buyee.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.buyee.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {

        if (email.isBlank() || password.isBlank()) {
            onResult(false, "Empty fields")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }

    fun signup(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {

        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            onResult(false, "Empty fields")
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                Log.d("FirebaseAuth", "Sending Email: '$email'")

                if (task.isSuccessful) {

                    val userId = task.result?.user?.uid

                    val userModel = UserModel(
                        name = name,
                        email = email,
                        uid = userId!!
                    )

                    firestore.collection("users")
                        .document(userId)
                        .set(userModel)
                        .addOnCompleteListener { dbTask ->

                            if (dbTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, "Something went wrong")
                            }
                        }

                } else {
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }



    fun signInWithGoogle(
        idToken: String,
        onResult: (Boolean, String?) -> Unit
    ) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)


        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = task.result.user
                    val userId = user?.uid ?: return@addOnCompleteListener

                    val userModel = UserModel(
                        name = user.displayName ?: "User",
                        email = user.email ?: "",
                        uid = userId
                    )

                    firestore.collection("users")
                        .document(userId)
                        .set(userModel)
                        .addOnCompleteListener { dbTask ->

                            if (dbTask.isSuccessful) {
                                onResult(true, null)
                            } else {
                                onResult(false, "Firestore save failed")
                            }
                        }

                } else {
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }


    fun forgotPassword(email: String, onResult: (Boolean, String?) -> Unit) {

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(true, "Reset email sent")
                } else {
                    onResult(false, task.exception?.localizedMessage)
                }
            }
    }


}