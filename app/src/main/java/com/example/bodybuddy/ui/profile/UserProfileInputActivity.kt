package com.example.bodybuddy.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bodybuddy.databinding.ActivityUserProfileInputBinding
import com.example.bodybuddy.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class UserProfileInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileInputBinding
    private val profileInputViewModel by viewModels<ProfileInputViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        checkData()
        super.onCreate(savedInstanceState)

        profileInputViewModel.isDataExist.observe(this){isDataExists ->
            if (isDataExists){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                binding = ActivityUserProfileInputBinding.inflate(layoutInflater)

                setContentView(binding.root)

                setData()

                setDashboard()
            }
        }
    }

    private fun checkData() {
        profileInputViewModel.checkUserData()
    }

    private fun setDashboard() {
        profileInputViewModel.isSuccess.observe(this){isSuccess ->
            if (isSuccess){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Input Data Gagal.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
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