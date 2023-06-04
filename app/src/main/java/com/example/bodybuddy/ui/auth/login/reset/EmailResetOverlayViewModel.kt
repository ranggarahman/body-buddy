package com.example.bodybuddy.ui.auth.login.reset

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bodybuddy.R
import com.example.bodybuddy.ui.auth.validator.AuthFormState
import com.example.bodybuddy.util.isEmailValid
import com.google.firebase.auth.FirebaseAuth

class EmailResetOverlayViewModel : ViewModel() {

    private val _resetForm = MutableLiveData<AuthFormState>()
    val resetFormState: LiveData<AuthFormState> = _resetForm

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    init {
        _resetForm.value = AuthFormState(isDataValid = false)
    }

    fun sendResetEmail(email: String){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _isSuccess.value = task.isSuccessful
            }
    }

    fun resetDataChanged(email : String) {
        if (!isEmailValid(email)) {
            _resetForm.value = AuthFormState(emailError = R.string.invalid_email)
        } else if (email.isBlank()){
            _resetForm.value = AuthFormState(isDataValid = false)
        }
        else {
            _resetForm.value = AuthFormState(isDataValid = true)
        }
    }

}