package com.example.bodybuddy.data.models

/**
 * Data class that captures user information for logged in users retrieved from AuthRepository
 */
data class LoggedInUser(
    val userId: String?,
    val name: String?,
)