package com.example.bodybuddy.ui.main.ui.food_info

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.data.FoodListItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FoodInfoViewModel : ViewModel() {
    private val database = FirebaseManager.database.getReference("foods")

    private val _foodList = MutableLiveData<List<FoodListItem>>()
    val foodList: LiveData<List<FoodListItem>> = _foodList

    fun retrieveFoodListFromDatabase() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val tempList = ArrayList<FoodListItem>()
                    for (foodSnapshot in dataSnapshot.children) {
                        val foodName = foodSnapshot.key.toString()
                        val foodData = foodSnapshot.getValue(FoodListItem::class.java)
                        if (foodData != null) {
                            val foodItem = FoodListItem(foodName,
                                foodData.calorie,
                                foodData.carbs,
                                foodData.fat,
                                foodData.protein)
                            tempList.add(foodItem)
                        }
                    }

                    Log.d(TAG, "FODLIST = ${_foodList.value}")

                    _foodList.value = tempList
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "FODLIST = err")
            }
        })
    }

    companion object {
        private const val TAG = "FoodInfoViewModel"
    }

}