package com.example.bodybuddy.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bodybuddy.data.FirebaseManager
import com.example.bodybuddy.databinding.ActivityAuthBinding
import com.example.bodybuddy.databinding.FragmentLoadingOverlayBinding
import com.example.bodybuddy.ui.LoadingFragmentOverlay
import com.example.bodybuddy.ui.auth.login.LoginFragment.Companion.LOGIN_RESULT_OK
import com.example.bodybuddy.ui.auth.login.LoginFragment.Companion.LOGIN_RESULT_ONGOING
import com.example.bodybuddy.ui.auth.login.profile.UserProfileInputActivity
import com.example.bodybuddy.ui.auth.register.RegisterFragment.Companion.REGISTER_RESULT_OK
import com.example.bodybuddy.ui.auth.register.RegisterFragment.Companion.REGISTER_RESULT_ONGOING
import com.example.bodybuddy.ui.auth.validator.ResultListener

class AuthActivity : AppCompatActivity(), ResultListener {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseManager.currentUser.currentUser != null) {
            val intent = Intent(this@AuthActivity, UserProfileInputActivity::class.java)
            startActivity(intent)

            finish()
        } else {
            binding = ActivityAuthBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }

    }

    override fun onResult(resultCode: Int) {
        setResult(resultCode)

//        when(resultCode){
//            LOGIN_RESULT_OK, REGISTER_RESULT_OK -> {
//                dialog.dismiss()
//            }
//        }

    }
}