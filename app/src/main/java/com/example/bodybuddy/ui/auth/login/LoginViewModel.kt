package com.example.bodybuddy.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bodybuddy.R
import com.example.bodybuddy.data.repository.AuthRepository
import com.example.bodybuddy.ui.auth.validator.AuthFormState
import com.example.bodybuddy.ui.auth.validator.AuthResult
import com.example.bodybuddy.util.Event
import kotlinx.coroutines.launch
import com.example.bodybuddy.data.Result
import com.example.bodybuddy.util.isEmailValid

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<AuthFormState>()
    val loginFormState: LiveData<AuthFormState> = _loginForm

    private val _loginResult = MutableLiveData<Event<AuthResult>>()
    val loginResult: LiveData<Event<AuthResult>> = _loginResult

    fun login(username: String, password: String,
              callback: ((AuthResult) -> Unit)? = null) {
        viewModelScope.launch {
            authRepository.login(username, password) { result ->
                if (result is Result.Success) {
                    val loggedInUser = result.data
                    val successResult = AuthResult(
                        successLogin = LoggedInUserView(
                            message = loggedInUser.name.toString()
                        )
                    )

                    _loginResult.value = Event(successResult)
                    callback?.invoke(successResult)
                } else if (result is Result.Error) {
                    val errorResult = AuthResult(
                        failedLogin = LoggedInUserView(
                            message = result.exception,
                        )
                    )
                    _loginResult.value = Event(errorResult)
                    callback?.invoke(errorResult)
                }
            }
        }
    }


    fun loginDataChanged(email : String, password: String) {
        if (!isEmailValid(email)) {
            _loginForm.value = AuthFormState(emailError = R.string.invalid_email)
        } else {
            _loginForm.value = AuthFormState(isDataValid = true)
        }
    }
}