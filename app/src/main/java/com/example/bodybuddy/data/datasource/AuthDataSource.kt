package com.example.bodybuddy.data.datasource

import android.util.Log
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.data.models.LoggedInUser
import com.example.bodybuddy.data.models.RegisteredUser
import com.example.bodybuddy.data.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthDataSource {

    private val currentUser = FirebaseManager.currentUser

    fun login(email: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val loggedInUser = LoggedInUser(
                            name = currentUser.currentUser?.displayName.toString(),
                            userId = currentUser.currentUser?.uid
                        )
                        callback(Result.Success(loggedInUser))
                    } else {
                        callback(Result.Error("Error"))
                    }
                }
        } catch (e: Throwable) {
            callback(Result.Error("Error"))
        }
    }

    fun register(username: String, email: String, password: String, callback: (Result<RegisteredUser>) -> Unit) {
        try {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user?.let { firebaseUser ->
                            firebaseUser.updateProfile(profileUpdates)
                                .addOnCompleteListener{ updateName ->
                                    if (updateName.isSuccessful){
                                        Log.d(TAG, "User profile updated.")
                                    } else {
                                        Log.e(TAG, "Failed to update user profile: ${task.exception}")
                                    }
                                }
                            firebaseUser.sendEmailVerification()
                                .addOnCompleteListener { emailTask ->
                                    if (emailTask.isSuccessful) {
                                        Log.d(TAG, "Email verification sent!")
                                    } else {
                                        Log.e(
                                            TAG, "Failed to send email verification: ${emailTask.exception}"
                                        )
                                    }
                                }
                            Log.d(TAG, "Success!")
                        }
                        callback(
                            Result.Success(
                                data = RegisteredUser(
                                    error = false,
                                    message = "Success"
                                )
                            )
                        )
                    } else {
                        Log.e(TAG, "Failed! : ${task.exception}")
                        callback(Result.Error("Eerror"))
                    }
                }
        } catch (e: Throwable) {
            callback(Result.Error("Eerror"))
        }
    }



    fun logout() {
        // TODO: revoke authentication
    }
    
    companion object {
        private const val TAG = "AuthDataSource"
    }

}