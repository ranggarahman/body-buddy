package com.example.bodybuddy.ui.auth.login.reset

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.bodybuddy.databinding.FragmentEmailResetOverlayBinding
import com.example.bodybuddy.util.afterTextChanged

class EmailResetOverlayFragment : DialogFragment() {

    private val emailResetViewModel by viewModels<EmailResetOverlayViewModel>()

    private var _binding : FragmentEmailResetOverlayBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailResetOverlayBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailResetViewModel.isSuccess.observe(viewLifecycleOwner){isSuccess ->

            Log.d(TAG, "RESULT : $isSuccess")

            if (isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Password Reset Email Sent!",
                    Toast.LENGTH_SHORT
                ).show()
                dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Oops, Something Went Wrong :(",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        emailResetViewModel.resetFormState.observe(viewLifecycleOwner, Observer {
            //make sure observer handles once
            val resetState = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.buttonSendResetEmail.isEnabled = resetState.isDataValid

            if (resetState.emailError != null) {
                binding.inputEmailReset.error = getString(resetState.emailError)
            }
        })

        binding.inputEmailReset.afterTextChanged {
            emailResetViewModel.resetDataChanged(
                binding.inputEmailReset.text.toString()
            )
        }

        binding.buttonSendResetEmail.setOnClickListener {
            emailResetViewModel.sendResetEmail(
                binding.inputEmailReset.text.toString()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        // Set the width and height of the dialog fragment to match_parent
        val width = ViewGroup.LayoutParams.WRAP_CONTENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    companion object{
        private const val TAG = "EmailResetOverlayFragment"
    }

}