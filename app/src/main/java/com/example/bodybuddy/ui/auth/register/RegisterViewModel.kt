package com.example.bodybuddy.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodybuddy.R
import com.example.bodybuddy.data.repository.AuthRepository
import com.example.bodybuddy.ui.auth.validator.AuthFormState
import com.example.bodybuddy.ui.auth.validator.AuthResult
import com.example.bodybuddy.util.Event
import com.example.bodybuddy.data.Result
import com.example.bodybuddy.util.isEmailValid
import com.example.bodybuddy.util.isNameValid
import com.example.bodybuddy.util.isPasswordValid
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerForm = MutableLiveData<AuthFormState>()
    val registerFormState: LiveData<AuthFormState> = _registerForm

    private val _registerResult = MutableLiveData<Event<AuthResult>>()
    val registerResult: LiveData<Event<AuthResult>> = _registerResult

    fun register(name: String, email: String, password: String,
                 callback: ((AuthResult) -> Unit)? = null) {
        viewModelScope.launch {
            authRepository.register(name, email, password) {result->
                if (result is Result.Success) {
                    val successResult =
                        AuthResult(successRegister = RegisteredUserView(message = result.data.message))
                    _registerResult.value = Event(successResult)
                    Log.d(TAG, "register function called : ${registerResult.value}")
                    callback?.invoke(successResult)
                } else if (result is Result.Error) {
                    val errorResult =
                        AuthResult(failedRegister = RegisteredUserView(message = result.exception))
                    _registerResult.value = Event(errorResult)
                    callback?.invoke(errorResult)
                    Log.e(TAG, "register function called : ${registerResult.value}")
                }
            }
        }
    }

    fun registerDataChanged(name: String, email: String, password: String) {
        if (!isNameValid(name)){
            _registerForm.value = AuthFormState(nameError = R.string.invalid_name)
        } else if (!isEmailValid(email)){
            _registerForm.value = AuthFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)){
            _registerForm.value = AuthFormState(passwordError = R.string.invalid_password)
        } else {
            _registerForm.value = AuthFormState(isDataValid = true)
        }
    }

    companion object{
        private const val TAG = "RegisterViewModel"
    }

}