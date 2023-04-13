package com.example.bodybuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class HandleUserInformation {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun InputScreen(onSubmit: (weight: Float, height: Float, age: Int) -> Unit) {
        val weightState = remember { mutableStateOf("") }
        val heightState = remember { mutableStateOf("") }
        val ageState = remember { mutableStateOf("") }
        val hasUserInteracted = remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = weightState.value,
                onValueChange = {
                    weightState.value = it
                    hasUserInteracted.value = true
                },
                label = { Text("Weight (in kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    errorIndicatorColor = Color.Red
                ),
                isError = hasUserInteracted.value && weightState.value.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = heightState.value,
                onValueChange = {
                    heightState.value = it
                    hasUserInteracted.value = true
                },
                label = { Text("Height (in cm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    errorIndicatorColor = Color.Red
                ),
                isError = hasUserInteracted.value && heightState.value.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = ageState.value,
                onValueChange = {
                    ageState.value = it
                    hasUserInteracted.value = true
                },
                label = { Text("Age") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    errorIndicatorColor = Color.Red
                ),
                isError = hasUserInteracted.value && ageState.value.isBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val weight = weightState.value.toFloatOrNull() ?: 0f
                    val height = heightState.value.toFloatOrNull() ?: 0f
                    val age = ageState.value.toIntOrNull() ?: 0
                    onSubmit(weight, height, age)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Submit")
            }
        }
    }
}