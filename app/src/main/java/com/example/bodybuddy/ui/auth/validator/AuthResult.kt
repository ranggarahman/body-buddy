package com.example.bodybuddy.ui.auth.validator

import com.example.bodybuddy.ui.auth.login.LoggedInUserView
import com.example.bodybuddy.ui.auth.register.RegisteredUserView

/**
 * Authentication result : success (user details) or error message.
 */
data class AuthResult(
    val successLogin: LoggedInUserView? = null,
    val successRegister: RegisteredUserView? = null,
    val failedLogin: LoggedInUserView? = null,
    val failedRegister: RegisteredUserView? = null
)