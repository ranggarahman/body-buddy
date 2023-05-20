package com.example.bodybuddy.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bodybuddy.data.datasource.AuthDataSource
import com.example.bodybuddy.data.repository.AuthRepository
import com.example.bodybuddy.ui.auth.login.LoginViewModel
import com.example.bodybuddy.ui.auth.register.RegisterViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class AuthViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                authRepository = AuthRepository(
                    dataSource = AuthDataSource()
                )
            ) as T
        }

        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(
                authRepository = AuthRepository(
                    dataSource = AuthDataSource()
                )
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}