package com.example.bodybuddy.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.databinding.ActivityAuth2Binding
import com.example.bodybuddy.ui.auth.login.profile.UserProfileInputActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuth2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseManager.currentUser.currentUser != null) {
            val intent = Intent(this@AuthActivity, UserProfileInputActivity::class.java)
            startActivity(intent)

            finish()
        } else {
            binding = ActivityAuth2Binding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }
}