package com.example.bodybuddy.ui.main.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.data.FoodListItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _totalCalories = MutableLiveData<Double>()
    val totalCalories: LiveData<Double> = _totalCalories

    private val _totalProtein = MutableLiveData<Double>()
    val totalProtein: LiveData<Double> = _totalProtein

    private val _totalCarbs = MutableLiveData<Double>()
    val totalCarbs: LiveData<Double> = _totalCarbs

    private val _totalFat = MutableLiveData<Double>()
    val totalFat: LiveData<Double> = _totalFat

    private val _snapshotExits = MutableLiveData<Boolean>()
    val snapshotExist : LiveData<Boolean> = _snapshotExits

    private val user = FirebaseManager.currentUser.currentUser
    private val database = FirebaseManager.database

    private val userId = user?.uid

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun fetchTotalMacronutrients(date: String) {
        val databaseRef = database.getReference("users/$userId/meals/$date")
        viewModelScope.launch {
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var totalCalories = 0.0
                    var totalProtein = 0.0
                    var totalCarbs = 0.0
                    var totalFat = 0.0

                    if (snapshot.exists()){
                        _snapshotExits.value = true
                    }

                    for (mealSnapshot in snapshot.children) {
                        for (foodSnapshot in mealSnapshot.children) {
                            val foodItem = foodSnapshot.getValue(FoodListItem::class.java)
                            foodItem?.let {
                                totalCalories += it.calorie
                                totalProtein += it.protein
                                totalCarbs += it.carbs
                                totalFat += it.fat
                            }
                        }
                    }

                    // Update your LiveData or any other mechanism to store the totals
                    // For example, if you have LiveData properties:
                    _totalCalories.value = totalCalories
                    _totalProtein.value = totalProtein
                    _totalCarbs.value = totalCarbs
                    _totalFat.value = totalFat

                    Log.d(TAG, "CAL : $totalCalories, PROT : $totalProtein, Carbs: $totalCarbs")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(TAG, error.message)
                }
            })
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}