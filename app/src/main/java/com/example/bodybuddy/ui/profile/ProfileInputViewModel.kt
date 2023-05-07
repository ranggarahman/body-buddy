package com.example.bodybuddy.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.util.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileInputViewModel: ViewModel() {
    private val currentUser = FirebaseManager.currentUser
    private val database = FirebaseManager.database
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun saveUserData(age: Int, weight: Double, height: Double) {
        _isLoading.value = true
        try {
            val newUser = database.child("users").child(currentUser?.uid!!)
            val userData = hashMapOf(
                "age" to age,
                "weight" to weight,
                "height" to height,
                "meals" to hashMapOf<String, Any>()
            )
            newUser.setValue(userData)
            Log.d(TAG, "Success!")
            _isLoading.value = false
        } catch (e: Exception){
            Log.e(TAG, "FAILED")
            _isLoading.value = false
        }
    }
    companion object{
        private const val TAG = "ProfileInputViewModel"
    }
}