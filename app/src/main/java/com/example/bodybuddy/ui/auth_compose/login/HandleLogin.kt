package com.example.bodybuddy.ui.auth_compose.login

import android.content.Intent
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bodybuddy.ui.auth.login.profile.UserProfileInputActivity

class HandleLogin(private val loginViewModel: LoginViewModel) {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun LoginScreen() {

        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val emailError = remember { mutableStateOf<String?>(null) }
        val passwordVisibility = remember { mutableStateOf(false) }

        val hasUserInteracted = remember { mutableStateOf(false) }

        val context = LocalContext.current
        val isLoading by loginViewModel.isLoading.observeAsState(false)
        val isSuccess by loginViewModel.isSuccess.observeAsState(false)
        
        emailError.value?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Log in to Body Buddy",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = {value ->
                        emailState.value = value
                        hasUserInteracted.value = true},
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    isError = hasUserInteracted.value && !isEmailValid(emailState.value),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    ),
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        if (emailState.value.isNotEmpty() && hasUserInteracted.value) {
                            val icon = if (isEmailValid(emailState.value)) {
                                Icons.Filled.CheckCircle
                            } else {
                                Icons.Filled.Warning
                            }
                            Icon(icon, contentDescription = null)
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Email, contentDescription = null )
                    }
                )

                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = {value ->
                        passwordState.value = value
                        hasUserInteracted.value = true},
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    isError = hasUserInteracted.value && !isPasswordValid(passwordState.value),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Blue,
                        unfocusedIndicatorColor = Color.Gray,
                        errorIndicatorColor = Color.Red
                    ),
                    shape = RoundedCornerShape(8.dp),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordVisibility.value = !passwordVisibility.value }
                        ) {
                            val passwordVisibilityIcon =
                                if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                            Icon(
                                imageVector = passwordVisibilityIcon,
                                contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                            )
                        }
                    },
                    leadingIcon ={
                        Icon(Icons.Filled.Lock, contentDescription = null )
                    }
                )
                Button(
                    onClick = {
                        val email = emailState.value
                        val password = passwordState.value

                        loginViewModel.userLogin(email, password)
                        if (isSuccess){
                            val intent = Intent(context, UserProfileInputActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Log in")
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f))
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Processing Login.. Do not exit",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

        }
    }

    private fun isEmailValid(query: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(query).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }

    companion object {
        private const val TAG = "LoginScreen"
    }
}
