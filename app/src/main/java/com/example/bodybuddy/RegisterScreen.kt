package com.example.bodybuddy

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen() {
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
            value = "",
            onValueChange = {},
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Email address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 16.dp)
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
            Text(text = "Create account")
        }
    }
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen()
}
