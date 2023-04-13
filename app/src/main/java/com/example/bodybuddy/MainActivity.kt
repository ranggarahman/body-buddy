package com.example.bodybuddy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
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
@Preview(device = "id:pixel_5")
@Composable
fun BodyBuddyApp() {
    var isLoginScreen by remember { mutableStateOf(true) }
    val register = HandleRegister()
    val login = HandleLogin()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isLoginScreen) "Log in" else "HandleRegister")
                }
            )
        },
        content = {

            if (isLoginScreen) {
                login.LoginScreen()
            } else {
                register.RegisterScreen()
            }
        },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_login_24), stringResource(
                        id = R.string.icon_login
                    ) ) },
                    label = {
                        Text("Log in", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center, )
                            },
                    selected = isLoginScreen,
                    onClick = { isLoginScreen = true },
                )
                BottomNavigationItem(
                    icon = { Icon(ImageVector.vectorResource(id = R.drawable.ic_app_registration_24), stringResource(
                        id = R.string.icon_register
                    ) )},
                    label = { Text("HandleRegister", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ) },
                    selected = !isLoginScreen,
                    onClick = { isLoginScreen = false },
                )
            }
        }
    )
}