package com.example.bodybuddy.ui.main.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.data.FoodListItem
import com.example.bodybuddy.ui.main.ui.calendar.event.Event
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is calendar Fragment"
    }
    val text: LiveData<String> = _text

    private val _eventList = MutableLiveData<List<Event>?>()
    val eventList : LiveData<List<Event>?> = _eventList

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _foodList = MutableLiveData<List<FoodListItem>>()
    val foodList : LiveData<List<FoodListItem>> = _foodList

    private val user = FirebaseManager.currentUser.currentUser
    private val database = FirebaseManager.database

    private val userId = user?.uid

    fun updateAdapterForDate(date: LocalDate) {

        if (_eventList.value != null){
            _eventList.value = null
        }

        Log.d(TAG, "UPDATE ADAPTER CALLED : date : $date")
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        Log.d(TAG, "DATE FORMATTER: date : $formattedDate")
        if (user != null) {
            val databaseRef = database.getReference("users/$userId/meals/$formattedDate")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val eventList = mutableListOf<Event>()
                    if (snapshot.exists()){
                        Log.d(TAG, "SNAPSHOT EXIST : $snapshot")
                        for (foodSnapshot in snapshot.children){
                            val mealType = foodSnapshot.key.toString()

                            // Check if the event already exists in the list
                            val existingEvent = eventList.find {
                                it.text == mealType && it.date == date
                            }
                            if (existingEvent == null) {
                                eventList.add(Event(mealType, date))
                            }
                        }
                        _isSuccess.value = true
                        _eventList.value = eventList

                    } else {
                        Log.e(TAG, "SNAPSHOT DOESN'T EXIST")
                        _isSuccess.value = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _isSuccess.value = false
                }
            })
        }
    }

    fun retrieveFoodListFromDatabase(mealType: String, date: LocalDate) {
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val databaseRef = database.getReference("users/$userId/meals/$formattedDate/$mealType")

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
        private const val TAG = "CalendarViewModel"
    }
}