package com.example.bodybuddy.ui.auth.validator

/**
 * Data validation state of the auth form.
 */
data class AuthFormState(
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val nameError: Int? = null,
    val isDataValid: Boolean = false
)