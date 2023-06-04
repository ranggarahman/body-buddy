package com.example.bodybuddy.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.ui.auth.login.profile.ProfileInputViewModel
import java.util.Date

class AddEventOverlayFragmentViewModel: ViewModel() {
    private val currentUser = FirebaseManager.currentUser.currentUser
    private val database = FirebaseManager.database

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val ref = database.reference
        .child("users")
        .child(currentUser!!.uid)
        .child("meals")

    fun overwriteUserData(
        date: String,
        mealtype: String,
        name: String,
        calorie: Double,
        carbs: Double,
        fat: Double,
        protein: Double
    ) {
        _isLoading.value = true
        try {
            val mealData = hashMapOf<String, Any>(
                "calorie" to calorie,
                "carbs" to carbs,
                "fat" to fat,
                "protein" to protein
            )

            val userData = hashMapOf<String, Any>(
                "/$date/$mealtype/$name" to mealData
            )

            ref.updateChildren(userData) { error, _ ->
                if (error == null) {
                    _isLoading.value = false
                } else {
                    // Handle error
                }
            }
            Log.e(TAG, "SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "FAILED")
            _isLoading.value = false
            _isSuccess.value = false
        }
    }


    fun saveUserData(date: String,
                     mealtype: String,
                     name: String,
                     calorie: Double,
                     carbs: Double,
                     fat: Double,
                     protein: Double
                     ) {
        _isLoading.value = true
        try {
            val mealData = hashMapOf<String, Any>(
                "calorie" to calorie,
                "carbs" to carbs,
                "fat" to fat,
                "protein" to protein
            )

            val mealTypeData = hashMapOf<String, Any>(
                name to mealData
            )

            val userData = hashMapOf<String, Any>(
                date to hashMapOf<String, Any>(
                    mealtype to mealTypeData
                )
            )

            val dateRef = ref.child(date)
            val mealTypeRef = dateRef.child(mealtype)
            val nameRef = mealTypeRef.child(name)

            nameRef.setValue(mealData) { error, _ ->
                if (error == null) {
                    _isLoading.value = false
                } else {
                    // Handle error
                }
            }
            Log.e(TAG, "SUCCESS")
        } catch (e: Exception){
            Log.e(TAG, "FAILED")
            _isLoading.value = false
            _isSuccess.value = false
        }
    }

    companion object{
        private const val TAG = "AddEventOverlayFragmentViewModel"
    }

}