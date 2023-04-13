package com.example.bodybuddy

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HandleRegister{
    @Composable
    fun RegisterScreen() {

        val nameState = remember { mutableStateOf("") }
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create a Body Buddy account",
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = nameState.value,
                onValueChange = {nameState.value = it},
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {emailState.value = it},
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {passwordState.value = it},
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)
            )
            Button(
                onClick = {
                    val email = emailState.value
                    val password = passwordState.value
                    val username = nameState.value

                    createUserAndSaveData(email, password, username)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Create account")
            }
        }
    }

    private fun createUserAndSaveData(email: String, password: String, username: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let { firebaseUser ->
                        val database =
                            FirebaseDatabase.getInstance("https://idyllic-aspect-298005-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
                        val newUser = database.child("users").child(firebaseUser.uid)
                        val userData = hashMapOf(
                            "email" to email,
                            "username" to username,
                            "meals" to hashMapOf<String, Any>()
                        )
                        newUser.setValue(userData)

                        // Add sample data for user1
                        val sampleData = sampleDataHashMap()
                        newUser.child("meals").setValue(sampleData)

                        firebaseUser.sendEmailVerification()
                            .addOnCompleteListener { emailTask ->
                                if (emailTask.isSuccessful) {
                                    Log.d(TAG, "Email verification sent!")
                                } else {
                                    Log.e(
                                        TAG,
                                        "Failed to send email verification: ${emailTask.exception}"
                                    )
                                }
                            }
                        Log.d(TAG, "Success!")
                    }
                } else {
                    Log.e(TAG, "Failed! : ${task.exception}")
                }
            }
    }

    private fun sampleDataHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "20220412" to hashMapOf<String, Any>(
                "breakfast" to hashMapOf<String, Any>(
                    "eggs" to hashMapOf<String, Any>(
                        "food_id" to "eggs",
                        "quantity" to 2
                    ),
                    "toast" to hashMapOf<String, Any>(
                        "food_id" to "toast",
                        "quantity" to 1
                    )
                ),
                "dinner" to hashMapOf<String, Any>(
                    "salmon" to hashMapOf<String, Any>(
                        "food_id" to "salmon",
                        "quantity" to 1
                    ),
                    "vegetables" to hashMapOf<String, Any>(
                        "food_id" to "vegetables",
                        "quantity" to 1
                    )
                ),
                "lunch" to hashMapOf<String, Any>(
                    "chicken_salad" to hashMapOf<String, Any>(
                        "food_id" to "chicken_salad",
                        "quantity" to 1
                    )
                ),
                "snack" to hashMapOf<String, Any>(
                    "apple" to hashMapOf<String, Any>(
                        "food_id" to "apple",
                        "quantity" to 1
                    )
                ),
                "total_calories" to 1500
            )
        )
    }

    @Preview
    @Composable
    fun PreviewRegisterScreen() {
        RegisterScreen()
    }

    companion object {
        private const val TAG = "RegisterScreen"
    }
}
