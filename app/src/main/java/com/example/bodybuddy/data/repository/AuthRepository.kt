package com.example.bodybuddy.data.repository

import com.example.bodybuddy.data.datasource.AuthDataSource
import com.example.bodybuddy.data.models.LoggedInUser
import com.example.bodybuddy.data.models.RegisteredUser
import com.example.bodybuddy.data.Result

class AuthRepository(val dataSource: AuthDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String, callback: (Result<LoggedInUser>) -> Unit) {
        dataSource.login(username, password) { result ->
            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }
            callback(result)
        }
    }


    fun register(name: String, email: String, password: String, callback:(Result<RegisteredUser>) -> Unit) {
        //Call data Source
        dataSource.register(name, email, password, callback)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}