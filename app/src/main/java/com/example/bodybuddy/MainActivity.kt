package com.example.bodybuddy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bodybuddy.ui.theme.BodyBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BodyBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BodyBuddyApp()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BodyBuddyApp() {
    var isLoginScreen by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isLoginScreen) "Log in" else "Register")
                }
            )
        },
        content = { LoginScreen()
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icons.Default.Lock },
                    label = { Text("Log in") },
                    selected = isLoginScreen,
                    onClick = { isLoginScreen = true },
                )
                BottomNavigationItem(
                    icon = { Icons.Default.Lock },
                    label = { Text("Register") },
                    selected = !isLoginScreen,
                    onClick = { isLoginScreen = false },
                )
            }
        }
    )
}

@Composable
fun LoginScreen() {
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
            value = "",
            onValueChange = {},
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)
        )
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Log in")
        }
    }
}