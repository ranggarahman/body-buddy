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
    private val _eventList = MutableLiveData<List<Event>?>()
    val eventList : LiveData<List<Event>?> = _eventList

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

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

    companion object {
        private const val TAG = "CalendarViewModel"
    }
}