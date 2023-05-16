package com.example.bodybuddy.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.util.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileInputViewModel: ViewModel() {
    private val currentUser = FirebaseManager.currentUser
    private val database = FirebaseManager.database
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isDataExist = MutableLiveData<Boolean>()
    val isDataExist: LiveData<Boolean> = _isDataExist

    private val newUser = database.child("users").child(currentUser?.uid!!)

    fun checkUserData(){
        newUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isDataExist.value = snapshot.exists()
            }
            override fun onCancelled(error: DatabaseError) {
                _isDataExist.value = false
                Log.e(TAG, "Database error: ${error.message}")
            }
        })
    }

    fun saveUserData(age: Int, weight: Double, height: Double) {
        _isLoading.value = true
        try {
            val userData = hashMapOf(
                "age" to age,
                "weight" to weight,
                "height" to height,
                "meals" to hashMapOf<String, Any>()
            )
            newUser.setValue(userData)

            _isSuccess.value = true
            Log.d(TAG, "Success!")
            _isLoading.value = false
        } catch (e: Exception){
            Log.e(TAG, "FAILED")
            _isLoading.value = false
            _isSuccess.value = false
        }
    }
    companion object{
        private const val TAG = "ProfileInputViewModel"
    }
}