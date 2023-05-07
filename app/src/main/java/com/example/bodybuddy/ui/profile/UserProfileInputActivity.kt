package com.example.bodybuddy.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bodybuddy.databinding.ActivityUserProfileInputBinding
import com.google.firebase.auth.FirebaseAuth

class UserProfileInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileInputBinding
    private val profileInputViewModel by viewModels<ProfileInputViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileInputBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setData()
    }

    private fun setData() {
        binding.btnSubmitData.setOnClickListener {
            val age = binding.edAge.text.toString().toInt()
            val height = binding.edHeight.text.toString().toDouble()
            val weight = binding.edWeight.text.toString().toDouble()

            profileInputViewModel.saveUserData(age, weight, height)
        }
    }

    companion object{
        private const val TAG = "ProfileInputActivity"
    }
}