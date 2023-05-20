package com.example.bodybuddy.ui.auth_compose.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createUserAndSaveData(email: String, password: String, username: String) {
        _isLoading.value = true

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
                                        TAG,
                                        "Failed to send email verification: ${emailTask.exception}"
                                    )
                                }
                            }
                        Log.d(TAG, "Success!")
                    }
                    _isLoading.value = false
                } else {
                    Log.e(TAG, "Failed! : ${task.exception}")
                    _isLoading.value = false
                }
            }
    }

    companion object{
        private const val TAG = "RegisterViewModel"
    }

}