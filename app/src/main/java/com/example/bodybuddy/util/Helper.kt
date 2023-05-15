package com.example.bodybuddy.util

import android.util.Log
import com.example.bodybuddy.ui.auth.register.HandleRegister
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Helper {

    private fun createUserAndSaveData(email: String, password: String, username: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let { firebaseUser ->
                        val database =
                            FirebaseDatabase.getInstance("https://idyllic-aspect-298005-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
                        val newUser = database.child("users").child(firebaseUser.uid)
                        val userData = hashMapOf(
                            "email" to email,
                            "username" to username,
                            "meals" to hashMapOf<String, Any>()
                        )
                        newUser.setValue(userData)

                        // Add sample data for user1
                        val sampleData = sampleDataHashMap()
                        newUser.child("meals").setValue(sampleData)

                        firebaseUser.sendEmailVerification()
                            .addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    Log.d("TAG", "Email verification sent!")
                                } else {
                                    Log.e(
                                        "TAG",
                                        "Failed to send email verification: ${emailTask.exception}"
                                    )
                                }
                            }
                        Log.d("TAG", "Success!")
                    }
                } else {
                    Log.e("TAG", "Failed! : ${task.exception}")
                }
            }
    }

    private fun sampleDataHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "20220412" to hashMapOf<String, Any>(
                "breakfast" to hashMapOf<String, Any>(
                    "eggs" to hashMapOf<String, Any>(
                        "food_id" to "eggs",
                        "quantity" to 2
                    ),
                    "toast" to hashMapOf<String, Any>(
                        "food_id" to "toast",
                        "quantity" to 1
                    )
                ),
                "dinner" to hashMapOf<String, Any>(
                    "salmon" to hashMapOf<String, Any>(
                        "food_id" to "salmon",
                        "quantity" to 1
                    ),
                    "vegetables" to hashMapOf<String, Any>(
                        "food_id" to "vegetables",
                        "quantity" to 1
                    )
                ),
                "lunch" to hashMapOf<String, Any>(
                    "chicken_salad" to hashMapOf<String, Any>(
                        "food_id" to "chicken_salad",
                        "quantity" to 1
                    )
                ),
                "snack" to hashMapOf<String, Any>(
                    "apple" to hashMapOf<String, Any>(
                        "food_id" to "apple",
                        "quantity" to 1
                    )
                ),
                "total_calories" to 1500
            )
        )
    }

}