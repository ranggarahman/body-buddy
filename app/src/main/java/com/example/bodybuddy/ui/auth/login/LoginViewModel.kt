package com.example.bodybuddy.ui.auth.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.ui.profile.UserProfileInputActivity
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun userLogin(email: String, password: String){
        _isLoading.value = true
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Success!")
                    _isLoading.value = false
                    _isSuccess.value = true
                } else {
                    Log.e(TAG, "Failed! : ${task.exception}")
                    _isLoading.value = false
                    _isSuccess.value = false
                }
            }
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }
}