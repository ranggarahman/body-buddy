package com.example.bodybuddy.ui.main.ui.calendar.event.eventoverlay

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.ui.main.ui.calendar.CalendarViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventDetailOverlayViewModel: ViewModel() {

    private val _foodList = MutableLiveData<List<FoodListItem>>()
    val foodList : LiveData<List<FoodListItem>> = _foodList

    private val user = FirebaseManager.currentUser.currentUser
    private val database = FirebaseManager.database

    private val userId = user?.uid

    fun retrieveFoodListFromDatabase(mealType: String, date: String) {
        val databaseRef = database.getReference("users/$userId/meals/$date/$mealType")

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val tempList = ArrayList<FoodListItem>()
                    //This is mealtype's child
                    for (foodSnapshot in dataSnapshot.children) {
                        val foodName = foodSnapshot.key.toString()
                        val foodData = foodSnapshot.getValue(FoodListItem::class.java)
                        if (foodData != null) {
                            val foodItem = FoodListItem(
                                foodName,
                                foodData.calories,
                                foodData.carbs,
                                foodData.fats,
                                foodData.protein
                            )
                            tempList.add(foodItem)
                        }
                    }

                    _foodList.value = tempList

                    Log.d(TAG, "FODLIST = $tempList")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "FODLIST = err")
            }
        })
    }

    companion object {
        private const val TAG = "EventDetailOverlayViewModel"
    }

}