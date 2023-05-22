package com.example.bodybuddy.ui.auth_compose

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.bodybuddy.R
import com.example.bodybuddy.ui.auth_compose.login.HandleLogin
import com.example.bodybuddy.ui.auth_compose.login.LoginViewModel
import com.example.bodybuddy.ui.auth_compose.register.HandleRegister
import com.example.bodybuddy.ui.auth_compose.register.RegisterViewModel
import com.example.bodybuddy.ui.auth.login.profile.UserProfileInputActivity
import com.example.bodybuddy.ui.theme.BodyBuddyTheme
import com.example.bodybuddy.data.FirebaseManager

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val currentUser = FirebaseAuth.getInstance().currentUser
        if (FirebaseManager.currentUser.currentUser != null) {
            val intent = Intent(this@AuthActivity, UserProfileInputActivity::class.java)
            startActivity(intent)
            finish()
//            return
        } else {
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
    companion object {
        private const val TAG = "AuthActivity"
    }

}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(device = "id:pixel_5")
@Composable
fun BodyBuddyApp() {
    var isLoginScreen by remember { mutableStateOf(true) }
    val register = HandleRegister(registerViewModel = RegisterViewModel())
    val login = HandleLogin(loginViewModel = LoginViewModel())

//    val scaffoldColors = rememberScaffoldColors(
//        primary = colorResource(id = R.color.blue_air_force), // change to the primary color you want
//        surface = Color.White // change to the surface color you want
//    )

    Scaffold(
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
                    label = { Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ) },
                    selected = !isLoginScreen,
                    onClick = { isLoginScreen = false },
                )
            }
        }
    )
}