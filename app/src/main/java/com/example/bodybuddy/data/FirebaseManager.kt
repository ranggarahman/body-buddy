package com.example.bodybuddy.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseManager {
    val database = FirebaseDatabase.getInstance("https://idyllic-aspect-298005-default-rtdb.asia-southeast1.firebasedatabase.app/").apply {
        setPersistenceEnabled(true)
    }

    val currentUser = FirebaseAuth.getInstance().apply {
        currentUser
    }
}